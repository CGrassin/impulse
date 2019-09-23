package fr.charleslabs.impulse.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import fr.charleslabs.impulse.controller.PIDRocketController;
import fr.charleslabs.impulse.controller.SerialController;
import fr.charleslabs.impulse.physics.PhysicsEngine;
import fr.charleslabs.impulse.rocket.Rocket;
import jssc.SerialPortException;

/**
 * Side bar of the UI of Impulse. It
 * contains the controls that can be 
 * accessed from any tab.
 * 
 * @author Charles Grassin
 *
 */
public class SidebarControlsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JSpinner xAxisUserTorque, yAxisUserTorque, pSpinner, iSpinner,
			dSpinner, frequencySpinner;
	protected JButton startStopBtn, addTorqueBtn, serialConnectBtn,
			pidApplyBtn;
	private static final double defaultKP=4,defaultKI=0, defaultKD=1;

	private boolean isSerialConnected = false;
	
	/**
	 * Constructs the sidebar's layout.
	 * @param parent The caller of the side
	 * bar, which must handle several actions:
	 * startStopBtn, addTorqueBtn, serialConnectBtn
	 * and pidApplyBtn.
	 */
	public SidebarControlsPanel(final ActionListener parent) {
		super(new BorderLayout());
		JPanel controls = new JPanel(new GridBagLayout());
		
		// Layout setup
		controls.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(3, 0, 3, 0);

		// UI elements creation
		xAxisUserTorque = new JSpinner(
				new SpinnerNumberModel(10.0d, -90, 90, 1));
		yAxisUserTorque = new JSpinner(new SpinnerNumberModel(-10.0d, -90, 90,
				1));
		pSpinner = new JSpinner(new SpinnerNumberModel(defaultKP, 0.0d, 300, 0.05d));
		iSpinner = new JSpinner(new SpinnerNumberModel(defaultKI, 0.0d, 300, 0.05d));
		dSpinner = new JSpinner(new SpinnerNumberModel(defaultKD, 0.0d, 300, 0.05d));
		frequencySpinner = new JSpinner(
				new SpinnerNumberModel(30, 0.0d, 300, 1));
		pSpinner.setToolTipText(R.kpTooltip);
		iSpinner.setToolTipText(R.kiTooltip);
		dSpinner.setToolTipText(R.kdTooltip);
		frequencySpinner.setToolTipText(R.frequencyTooltip);

		addTorqueBtn = new JButton(R.addOffsetBtnTitle);
		addTorqueBtn.addActionListener(parent);
		addTorqueBtn.setToolTipText(R.addOffsetBtnTooltip);

		startStopBtn = new JButton(R.launchBtnTitle);
		startStopBtn.addActionListener(parent);
		startStopBtn.setToolTipText(R.launchBtnTooltip);

		serialConnectBtn = new JButton(R.serialBtnTitle);
		serialConnectBtn.addActionListener(parent);
		serialConnectBtn.setToolTipText(R.serialBtnTooltip);

		pidApplyBtn = new JButton(R.setPIDBtnTitle);
		pidApplyBtn.addActionListener(parent);
		pidApplyBtn.setToolTipText(R.setPIDBtnTooltip);
		
		// Generate 
		controls.add(RocketCreatorPanel.categoryTitle("Rocket controller"), gbc);
		controls.add(fieldWithLabel(pSpinner, "Kp"), gbc);
		controls.add(fieldWithLabel(iSpinner, "Ki"), gbc);
		controls.add(fieldWithLabel(dSpinner, "Kd"), gbc);
		controls.add(fieldWithLabel(frequencySpinner, "Hz"), gbc);
		controls.add(pidApplyBtn, gbc);
		JLabel orLabel = new JLabel("or");
		orLabel.setHorizontalAlignment(JLabel.CENTER);
		controls.add(orLabel, gbc);

		controls.add(serialConnectBtn, gbc);
		controls.add(RocketCreatorPanel.categoryTitle("Add force"), gbc);
		controls.add(fieldWithLabel(xAxisUserTorque, "x"), gbc);
		controls.add(fieldWithLabel(yAxisUserTorque, "y"), gbc);
		controls.add(addTorqueBtn, gbc);

		this.add(controls, BorderLayout.CENTER);
		this.add(startStopBtn, BorderLayout.SOUTH);
		this.updateInterface(false);
	}
	
	/**
	 * Updates the sidebar in case of run status
	 * change.
	 * @param running True if the simulator is running.
	 */
	protected void updateInterface(final boolean running) {
		if(running) {
			startStopBtn.setText(R.stopBtnTitle);
			addTorqueBtn.setEnabled(true);
		}else {
			startStopBtn.setText(R.launchBtnTitle);
			addTorqueBtn.setEnabled(false);
		}
	}
	
	/**
	 * Creates a PID controller for the rocket, based 
	 * on the values in the fields.
	 * 
	 * @return A PID controller, form the fields.
	 */
	private PIDRocketController createPID() {
		return new PIDRocketController((Double) this.frequencySpinner.getValue(),
				(Double) this.pSpinner.getValue(),
				(Double) this.iSpinner.getValue(),
				(Double) this.dSpinner.getValue());
	}
	
	/**
	 * Sets the PID 
	 */
	protected void setPID(){
		if(isSerialConnected) {
			isSerialConnected = false;
			serialConnectBtn.setText(R.serialBtnTitle);
		}
		PhysicsEngine.getInstance().setController(this.createPID());
	}
	
	/**
	 * 
	 * @param caller
	 * @return
	 */
	private static String serialPortSelectionDialog(final JFrame caller) {
		String[] serialPortList = SerialController.getPortList();
		if (serialPortList.length == 0) {
			JOptionPane.showMessageDialog(caller, "No serial port found.",
					"Warning", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		String s = (String) JOptionPane.showInputDialog(caller,
				"Select serial port:\n", "Serial port selection",
				JOptionPane.PLAIN_MESSAGE, null, serialPortList,
				serialPortList[0]);
		return s;
	}
	
	protected void serialConnect(final JFrame caller) {
		if(isSerialConnected) {
			this.setPID();
			isSerialConnected = false;
			serialConnectBtn.setText(R.serialBtnTitle);
			return;
		}
		
		String serialPort = SidebarControlsPanel
				.serialPortSelectionDialog(caller);
		if (serialPort != null)
			try {
				PhysicsEngine.getInstance().setController(
						new SerialController(serialPort));
				isSerialConnected = true;
				serialConnectBtn.setText(R.serialStopBtnTitle);
				
			} catch (SerialPortException e) {
				JOptionPane.showMessageDialog(
						caller,
						R.serialConnectionErrorDialogMessage
								+ e.getMessage(),
						R.serialConnectionErrorDialogTitles,
						JOptionPane.ERROR_MESSAGE);
			}
		
	}

	private static JPanel fieldWithLabel(final Component field, final String label) {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel lbl = new JLabel(label + " ");
		lbl.setFont(new Font("monospaced", Font.PLAIN, 12));
		panel.add(lbl, BorderLayout.WEST);
		panel.add(field, BorderLayout.CENTER);
		return panel;
	}

	public void addTorque() {
		Rocket rocket = PhysicsEngine.getInstance().getRocket();
		if(rocket != null){
			rocket.getAngularMotion().position.x += (Double) this.xAxisUserTorque.getValue();
			rocket.getAngularMotion().position.y += (Double) this.yAxisUserTorque.getValue();
		}
	}
}
