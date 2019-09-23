package fr.charleslabs.impulse.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.charleslabs.impulse.physics.PhysicsMatrix;
import fr.charleslabs.impulse.physics.PhysicsVector;

/**
 * A panel with 3 graphs, that plots a
 * PhysicsMatrix (acceleration, speed &
 * position) in 3D (x, y and z).
 * Can be used for both linear motion
 * and angular motion visualization.
 * 
 * @author Charles Grassin
 */
public class GraphsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	final private PhysicsGraph accelerationGraph, speedGraph, positionGraph;

	/**
	 * Constructs a GraphsPanel that displays 3
	 * graphs.
	 * @param timeInterval The interval between 
	 * each point.
	 * @param unit The Y axis unit.
	 */
	public GraphsPanel(final double timeInterval, final String unit) {
		super(new BorderLayout());
		accelerationGraph = new PhysicsGraph(1000.0d / timeInterval);
		speedGraph = new PhysicsGraph(1000.0d / timeInterval);
		positionGraph = new PhysicsGraph(1000.0d / timeInterval);

		// Center layout
		final JPanel graphPane = new JPanel(new GridLayout(3, 1));
		
		final JPanel accelerationPanel = new JPanel(new BorderLayout());
		accelerationPanel.add(new JLabel("Acceleration (in " + unit + "/s^2)"), BorderLayout.NORTH);
		accelerationPanel.add(accelerationGraph, BorderLayout.CENTER);
		graphPane.add(accelerationPanel);

		final JPanel speedPanel = new JPanel(new BorderLayout());
		speedPanel.add(new JLabel("Speed (in " + unit + "/s)"), BorderLayout.NORTH);
		speedPanel.add(speedGraph, BorderLayout.CENTER);
		graphPane.add(speedPanel);

		final JPanel positionPanel = new JPanel(new BorderLayout());
		positionPanel.add(new JLabel("Position (in " + unit + ")"), BorderLayout.NORTH);
		positionPanel.add(positionGraph, BorderLayout.CENTER);
		graphPane.add(positionPanel);

		// Caption
		final JLabel xLabel = new JLabel(" X ");
		xLabel.setForeground(PhysicsGraph.lineColorX);
		final JLabel yLabel = new JLabel(" Y ");
		yLabel.setForeground(PhysicsGraph.lineColorY);
		final JLabel zLabel = new JLabel(" Z ");
		zLabel.setForeground(PhysicsGraph.lineColorZ);
		final JPanel legend = new JPanel(new FlowLayout());
		legend.add(new JLabel("Caption: "));
		legend.add(xLabel);
		legend.add(yLabel);
		legend.add(zLabel);
		
		// 
		this.add(graphPane, BorderLayout.CENTER);
		this.add(legend, BorderLayout.SOUTH);
	}

	/**
	 * Adds a point on the 3 graphs.
	 * @param matrix The PhysicsMatrix corresponding
	 * to the current time.
	 */
	public void addPoint(final PhysicsMatrix matrix) {
		accelerationGraph.add(new PhysicsVector(matrix.acceleration));
		speedGraph.add(new PhysicsVector(matrix.speed));
		positionGraph.add(new PhysicsVector(matrix.position));
	}

	/**
	 * Clears all the data of the plots.
	 */
	public void clear() {
		accelerationGraph.clear();
		speedGraph.clear();
		positionGraph.clear();
	}

}
