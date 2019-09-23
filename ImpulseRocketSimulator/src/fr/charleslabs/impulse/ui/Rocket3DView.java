package fr.charleslabs.impulse.ui;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * A 3D panel based of JavaFx, to display
 * a 3D view of the rocket's current attitude.
 * 
 * @author Charles Grassin
 */
public class Rocket3DView extends JFXPanel {
	// Constants
	private static final long serialVersionUID = 1L;
	private static final Color backgroundColor = new Color(.95, .95, .95, 1);
	private static final Color rocketColor = new Color(.841, .413, .35, 1);
	private static final double defaultCameraDistance = -50,
			minCameraDistance = -50,maxCameraDistance = -500;
	private static final double rocketDiameter=1, rocketHeight=10;

	// Dynamic 3D variables
	private Shape3D rocketFx, ground;
	private PerspectiveCamera camera;

	/**
	 * Prepare the 3D view (threaded).
	 */
	public Rocket3DView() {
		super();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				init();
			}
		});
	}
	
	/** 
	 * Checks whether the JVM is compatible with
	 * the 3D view. This should be called prior
	 * to the constructor.
	 * @return true if the JVM is compatible with
	 * this 3D panel.
	 */
	public static boolean isCompatible() {
		return Platform.isSupported(ConditionalFeature.SCENE3D);
	}
	
	/**
	 * Build the 3D visualization of the rocket.
	 */
	private void init() {
		Group root = new Group();

		rocketFx = new Cylinder(rocketDiameter, rocketHeight);
		PhongMaterial material1 = new PhongMaterial();
		material1.setDiffuseColor(rocketColor);
		rocketFx.setMaterial(material1);
		root.getChildren().add(rocketFx);

		ground = new Box(100, 1, 100);
		ground.getTransforms().add(new Translate(0, rocketHeight/2, 0));
		root.getChildren().add(ground);

		Scene scene = new Scene(root, -1, -1, true,
				SceneAntialiasing.BALANCED);
		scene.setFill(backgroundColor);
		camera = new PerspectiveCamera(true);
		camera.setNearClip(0.1);
		camera.setFarClip(1000.0);
		camera.setTranslateZ(defaultCameraDistance);
		scene.setCamera(camera);
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				double newDistance = camera.getTranslateZ() + event.getDeltaY();

				if (newDistance > minCameraDistance)
					newDistance = minCameraDistance;
				else if (newDistance < maxCameraDistance)
					newDistance = maxCameraDistance;

				camera.setTranslateZ(newDistance);
			}
		});

		this.setScene(scene);
	}

	/**
	 * Updates the attitude and position of the rocket
	 * in the 3D view.
	 * @param height The current rocket altitude relative
	 * to ground.
	 * @param xRot The current rotation around the x axis.
	 * @param yRot The current rotation around the y axis.
	 */ 
	public void setPostion(double height, double xRot, double yRot) {
		ground.getTransforms().clear();
		ground.getTransforms().add(new Translate(0, height + 5, 0));
		rocketFx.getTransforms().clear();
		rocketFx.getTransforms().add(new Rotate(xRot, Rotate.X_AXIS));
		rocketFx.getTransforms().add(new Rotate(yRot, Rotate.Z_AXIS));
		this.invalidate();
	}
}
