package fr.charleslabs.impulse.ui;

/**
 * This abstract class contains all the 
 * ressources of the Impulse Simulator.
 * 
 * @author Charles Grassin
 *
 */
public abstract class R {
	// General & tabs
	public static final String windowTitle = "Impulse - Gimbaled Thrust Rocket Simulator",
			runningWindowTitle = " [Running]",
			angularTabTitle = "Angular Motion",
			angularTabTooltip = "Angular motion graphs (acceleration, speed and angle)",
			linearTabTitle = "Linear Motion",
			linearTabTooltip = "Linear motion graphs (acceleration, speed and position)",
			view3DTabTitle = "3D View",
			view3DTabTooltip = "3D view of the rocket simulation",
			settingsTabTitle = "Rocket Settings",
			settingsTabTooltip = "Rocket configuration (motor, size, mass, ...)",
			helpTabTitle = "Help",
			helpTabTooltip = "Quick start and information about Impulse.";
	// Side bar
	public static final String kpTooltip = "Proportionnal PID coefficient.",
			kiTooltip = "Integral PID coefficient.",
			kdTooltip = "Derivative PID coefficient.",
			frequencyTooltip = "PID call frequency (Hz).",
			setPIDBtnTitle = "Set PID",
			setPIDBtnTooltip = "Use a PID with the selected Kp, Ki and Kd as the rocket controller.",
			addOffsetBtnTitle = "Add",
			addOffsetBtnTooltip = "Add user-defined offset to the rocket.",
			launchBtnTitle = "Launch",
			stopBtnTitle = "Stop",
			launchBtnTooltip = "Launch or stop the rocket simulation.",
			serialConnectionErrorDialogTitles = "Serial port error",
			serialConnectionErrorDialogMessage = "Could not connect to serial port: ",
			serialBtnTitle = "Connect...",
			serialStopBtnTitle = "Close port",
			serialBtnTooltip = "Connect to a rocket controller board through a serial link.";
	// Rocket Creator menu:
	public static final String basicConfigurationTitle = "Basic configuration",
			massLabel = "Total mass",
			massTooltip = "Mass of the rocket, including the motor",
			lengthLabel = "Length",
			lengthTooltip = "Length of the rocket, from base to tip",
			comCenteredLabel = "Rocket center of mass (CoM) is centered?",
			comCenteredTooltip = "CAD software (eg: SolidWorks) can be used to compute a more precise location of the CoM.",
			comLabel = "    CoM height",
			comTooltip = "The center of mass (CoM) height must be > 0 and < rocket length.",
			diameterLabel = "Body diameter", 
			diameterTooltip = "NOT YET USED",
			motorLabel = "Motor",
			motorTooltip = "The motor determines the thrust curve of the rocket",
			gimbalConfigurationTitle = "Gimbal configuration",
			gimbalSpeedLabel = "Gimbal angular speed",
			gimbalSpeedTooltip = "The maximum angular displacement per second",
			gimbalLimitLabel = "Gimbal max. angle",
			gimbalLimitTooltip = "Maximum angle the gimbal can reach",
			rocketCreateDialogTitle = "Rocket created",
			rocketCreateDialogMessage = "Rocket created",
			rocketCreateErrorDialogTitle = "Rocket not created",
			rocketCreateErrorDialogMessage = "Rocket not created:\n";
}
