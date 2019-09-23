package fr.charleslabs.impulse.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import fr.charleslabs.impulse.gimbal.GimbalServo;
import fr.charleslabs.impulse.motor.MotorDataBase;
import fr.charleslabs.impulse.physics.PhysicsEngine;
import fr.charleslabs.impulse.rocket.Rocket;

/**
 * A panel to create a Rocket, to
 * set every field.
 * 
 * @author Charles Grassin
 */
public class RocketCreatorPanel extends JPanel implements ItemListener {
	private static final long serialVersionUID = 1L;
	public static final double defaultMass = 0.2, defaultLength = 0.3,
			defaultCOMHeight = defaultLength / 2, defaultGimbalSpeed = 400,
			defaultGimbalMaxAngle = 20, defaultDiameter = 0.05;

	protected JButton applyBtn;
	private JComboBox<String> motorList;
	private JSpinner massSpinner, lengthSpinner, comSpinner,
			gimbalSpeedSpinner, gimbalMaxAngleSpinner, diameterSpinner;
	private JCheckBox isCoMCenteredCB;
	private JPanel comPanel;

	public RocketCreatorPanel(final ActionListener parent) {
		super(new BorderLayout());
		JPanel panel = new JPanel(new GridBagLayout());
		JScrollPane options = new JScrollPane(panel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Init. swing components
		motorList = new JComboBox<String>(MotorDataBase.getInstance().getMotorList());
		massSpinner = new JSpinner(new SpinnerNumberModel(defaultMass, 0.001d,
				30, 0.01d));
		lengthSpinner = new JSpinner(new SpinnerNumberModel(defaultLength,
				0.001d, 90, 0.01d));
		comSpinner = new JSpinner(new SpinnerNumberModel(defaultCOMHeight,
				0.001d, 90, 0.01d));
		gimbalSpeedSpinner = new JSpinner(new SpinnerNumberModel(
				defaultGimbalSpeed, 0, 10000, 10));
		gimbalMaxAngleSpinner = new JSpinner(new SpinnerNumberModel(
				defaultGimbalMaxAngle, 0, 90, 1));
		diameterSpinner = new JSpinner(new SpinnerNumberModel(defaultDiameter,
				0.001, 20, 1));
		isCoMCenteredCB = new JCheckBox("", true);
		isCoMCenteredCB.addItemListener(this);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(3, 3, 3, 3);

		panel.add(categoryTitle(R.basicConfigurationTitle), gbc);
		panel.add(
				fieldWithLabel(R.massLabel, massSpinner, "kg",
						R.massTooltip), gbc);
		panel.add(
				fieldWithLabel(R.lengthLabel, lengthSpinner, "m",
						R.lengthTooltip), gbc);
		panel.add(
				fieldWithLabel(R.comCenteredLabel, isCoMCenteredCB,
						null, R.comCenteredTooltip), gbc);
		comPanel = fieldWithLabel(R.comLabel, comSpinner, "m",
				R.comTooltip);
		panel.add(comPanel, gbc);
		panel.add(
				fieldWithLabel(R.diameterLabel, diameterSpinner, "m",
						R.diameterTooltip), gbc);
		panel.add(
				fieldWithLabel(R.motorLabel, motorList, null,
						R.motorTooltip), gbc);
		panel.add(categoryTitle(R.gimbalConfigurationTitle), gbc);
		panel.add(
				fieldWithLabel(R.gimbalLimitLabel,
						gimbalMaxAngleSpinner, "deg",
						R.gimbalLimitTooltip), gbc);
		panel.add(
				fieldWithLabel(R.gimbalSpeedLabel, gimbalSpeedSpinner,
						"deg/s", R.gimbalSpeedTooltip), gbc);

		//panel.add(
		//		categoryTitle("Sensor simulation (for serial control only) [WIP]"),
		//		gbc);

		applyBtn = new JButton("Apply");
		applyBtn.addActionListener(parent);

		this.itemStateChanged(null);
		this.add(options, BorderLayout.CENTER);
		this.add(applyBtn, BorderLayout.SOUTH);
	}

	/**
	 * Constructs a rocket from the inputs.
	 * @return A Rocket object.
	 * @throws Exception if the Rocket object
	 * could not be created (parameter issue).
	 */
	private Rocket makeRocket() throws Exception {
		final double mass = (Double) massSpinner.getValue();
		final double height = (Double) lengthSpinner.getValue();
		final double comHeight = isCoMCenteredCB.isSelected() ? height / 2
				: (Double) comSpinner.getValue();
		final double maxGimbalAngle = (Double) gimbalMaxAngleSpinner.getValue();
		final double speedGimbal = (Double) gimbalSpeedSpinner.getValue();
		
		return new Rocket(MotorDataBase.getInstance().getMotor((String) this.motorList
				.getSelectedItem()), 
				new GimbalServo(maxGimbalAngle,speedGimbal), mass, height,
				comHeight);
	}
	
	/**
	 * Sets the rocket of the physical engine based on
	 * configuration of this panel. Displays either a
	 * dialog if the boolean isDialogPrinted is true.
	 * 
	 * @param parent The calling JFrame.
	 * @param isDialogPrinted Selects whether a dialog will
	 * be displayed. It can either be confirmation, or 
	 * error.
	 */
	protected void setRocket(final JFrame parent, final boolean isDialogPrinted) {
		try {
			PhysicsEngine.getInstance().setRocket(this.makeRocket());
			if (isDialogPrinted)
				JOptionPane.showMessageDialog(parent,
						R.rocketCreateDialogMessage,
						R.rocketCreateDialogTitle,
						JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			if (isDialogPrinted)
				JOptionPane.showMessageDialog(
						parent,
						R.rocketCreateErrorDialogMessage
								+ e.getMessage(),
						R.rocketCreateErrorDialogTitle,
						JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * In case the checkbox is pressed, display/hide
	 * the com text field.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		comPanel.setVisible(!isCoMCenteredCB.isSelected());
	}
	
	private static JPanel fieldWithLabel(final String label, final JComponent field,
			final String unit, final String toolTip) {
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(field);
		if (label != null)
			panel.add(new JLabel(label + " "), BorderLayout.WEST);
		if (unit != null)
			panel.add(new JLabel(" " + unit), BorderLayout.EAST);
		if (toolTip != null)
			field.setToolTipText(toolTip);
		return panel;
	}

	public static JLabel categoryTitle(final String label) {
		JLabel panel = new JLabel(label);
		panel.setForeground(new Color(191, 91, 42));
		return panel;
	}
}