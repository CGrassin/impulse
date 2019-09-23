package fr.charleslabs.impulse.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import fr.charleslabs.impulse.physics.PhysicsVector;

/**
 * This class is a simple plot. It displays
 * any 3D data, described by a PhysicsVector.
 * 
 * @author Charles Grassin
 *
 */
public class PhysicsGraph extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int padding = 10;
	private static final int xLabelPadding = 10;
	private static final int yLabelPadding = 45;
	static final Color lineColorX = new Color(44, 102, 230, 255);
	static final Color lineColorY = new Color(209, 28, 230, 255);
	static final Color lineColorZ = new Color(10, 102, 34, 255);
	static final Color backgroundColor = new Color(255, 255, 255, 240);
	static final Color gridColor = new Color(200, 200, 200, 200);
	static final Color fontColor = new Color(200, 200, 200, 200);
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	private static final int divisionSpacing = 20;
	private List<PhysicsVector> matrix;
	private Double ySpacing = 1.0d;
	
	/**
	 * Constructs an empty graph.
	 * 
	 * @param ySpacing The x interval between each
	 * vector (usually in seconds or milliseconds).
	 */
	public PhysicsGraph(final Double ySpacing) {
		this.matrix = new ArrayList<PhysicsVector>();
		this.ySpacing = ySpacing;
	}
	
	/**
	 * Constructs a graph with a pre-filled
	 * 3D list to display.
	 * 
	 * @param matrix The matrix list to display
	 * in the graph.
	 * @param ySpacing The x interval between each
	 * vector (usually in seconds or milliseconds).
	 */
	public PhysicsGraph(final List<PhysicsVector> matrix,final  Double ySpacing) {
		this.matrix = matrix;
		this.ySpacing = ySpacing;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Enables the anti-aliasing
		final Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Compute UI constants
		final double maxScore = getMaxValue();
		final double minscore = getMinValue();
		int numberYDivisions = ((getHeight() - padding * 2 - xLabelPadding) / divisionSpacing);
		if (numberYDivisions % 2 == 1)
			numberYDivisions--;
		if (numberYDivisions <= 0)
			numberYDivisions = 1;
		double xScale = ((double) getWidth() - (2 * padding) - yLabelPadding)
				/ (matrix.size() - 1);
		double yScale = ((double) getHeight() - 2 * padding - xLabelPadding)
				/ (maxScore - minscore);

		// Get the list of points
		List<Point> graphPointsX = new ArrayList<Point>();
		List<Point> graphPointsY = new ArrayList<Point>();
		List<Point> graphPointsZ = new ArrayList<Point>();
		for (int i = 0; i < matrix.size(); i++) {
			graphPointsX.add(new Point(
					(int) (i * xScale + padding + yLabelPadding),
					(int) ((maxScore - matrix.get(i).x) * yScale + padding)));
			graphPointsY.add(new Point(
					(int) (i * xScale + padding + yLabelPadding),
					(int) ((maxScore - matrix.get(i).y) * yScale + padding)));
			graphPointsZ.add(new Point(
					(int) (i * xScale + padding + yLabelPadding),
					(int) ((maxScore - matrix.get(i).z) * yScale + padding)));
		}

		// Draw background
		g2.setColor(backgroundColor);
		g2.fillRect(padding + yLabelPadding, padding, getWidth()
				- (2 * padding) - yLabelPadding, getHeight() - 2 * padding
				- xLabelPadding);

		// create hatch marks and grid lines for y axis.
		for (int i = 0; i < numberYDivisions + 1; i++) {
			int x0 = padding + yLabelPadding;
			int y0 = getHeight()
					- ((i * (getHeight() - padding * 2 - xLabelPadding))
							/ numberYDivisions + padding + xLabelPadding);
			if (matrix.size() > 0) {
				g2.setColor(gridColor);
				g2.drawLine(padding + yLabelPadding + 1, y0, getWidth()
						- padding - 1, y0);
				g2.setColor(fontColor);
				String yLabel = ((int) ((minscore + (maxScore - minscore)
						* ((i * 1.0) / numberYDivisions)) * 100))
						/ 100.0 + "";
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				g2.drawString(yLabel, x0 - labelWidth - 5,
						y0 + (metrics.getHeight() / 2) - 3);
			}
			g2.drawLine(x0, y0, x0, y0);
		}

		// and for x axis
		if (matrix.size() > 1) {
			for (int i = 0; i < matrix.size(); i++) {
				int x0 = i * (getWidth() - padding * 2 - yLabelPadding)
						/ (matrix.size() - 1) + padding + yLabelPadding - 1;
				int y0 = getHeight() - padding - xLabelPadding;
				if ((i % ((int) ((matrix.size() / 10.0)) + 1)) == 0) {
					g2.setColor(gridColor);
					g2.drawLine(x0, getHeight() - padding - xLabelPadding - 1,
							x0, padding);
					g2.setColor(fontColor);
					String xLabel = String.format("%.0f", i * ySpacing);
					FontMetrics metrics = g2.getFontMetrics();
					g2.drawString(xLabel, x0 - metrics.stringWidth(xLabel) / 2,
							y0 + metrics.getHeight() + 3);
					g2.drawLine(x0, y0, x0, y0);
				}
			}
		}

		// Draw the plots (x,y and z)
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < graphPointsX.size() - 1; i++) {
			g2.setColor(lineColorX);
			g2.drawLine(graphPointsX.get(i).x, graphPointsX.get(i).y,
					graphPointsX.get(i + 1).x, graphPointsX.get(i + 1).y);
			g2.setColor(lineColorY);
			g2.drawLine(graphPointsY.get(i).x, graphPointsY.get(i).y,
					graphPointsY.get(i + 1).x, graphPointsY.get(i + 1).y);
			g2.setColor(lineColorZ);
			g2.drawLine(graphPointsZ.get(i).x, graphPointsZ.get(i).y,
					graphPointsZ.get(i + 1).x, graphPointsZ.get(i + 1).y);
		}
	}

	/**
	 * Looks for the smallest value is the 
	 * vectors.
	 * @return The smallest value in the list
	 * of vectors. It can either be a x, y or
	 * z value.
	 */
	private double getMinValue() {
		double minValue = Double.MAX_VALUE;
		for (PhysicsVector score : matrix) {
			minValue = Math.min(minValue, score.x);
			minValue = Math.min(minValue, score.y);
			minValue = Math.min(minValue, score.z);
		}
		return minValue;
	}
	
	/**
	 * Looks for the largest value is the 
	 * vectors.
	 * @return The largest value in the list
	 * of vectors. It can either be a x, y or
	 * z value.
	 */
	private double getMaxValue() {
		double maxValue = Double.MIN_VALUE;
		for (PhysicsVector score : matrix) {
			maxValue = Math.max(maxValue, score.x);
			maxValue = Math.max(maxValue, score.y);
			maxValue = Math.max(maxValue, score.z);
		}
		return maxValue;
	}
	
	/**
	 * Clears all the data of the plot.
	 */
	public void clear() {
		this.matrix.clear();
	}

	/**
	 * Getter for the list of vectors.
	 * @return The list of vector displayed
	 * by the graph.
	 */
	public List<PhysicsVector> getMatrix() {
		return matrix;
	}

	/**
	 * Adds a vector to the plot.
	 * @param vector A vector to plot, with
	 * the coordinates (ySpacing*nbPoints,x,y,z)
	 */
	public void add(final PhysicsVector vector) {
		matrix.add(vector);
	}
}