package fr.charleslabs.impulse.sensor;

import fr.charleslabs.impulse.rocket.Rocket;

/**
 * WORK IN PROGRESS<br>
 * This class represent a sensor, to 
 * simulate the behavior of real sensor,
 * for the serial device.
 * It generates an output based on the type
 * of sensor.
 * 
 * @author Charles Grassin
 * 
 */
public abstract class Sensor {
	/** Enum with the different sensor types available. */
	static public enum SensorTypeEnum {
		ACCELEROMETER, ALTIMETER, GYROSCOPE
	};
	/** The type of the sensor (see SensorTypeEnum). */
	final protected SensorTypeEnum type;
	/**	The name of the sensor. */
	final protected String name;
	
	/**
	 * Build a sensor with a given
	 * type and name.
	 * @param type The type of the sensor (see SensorTypeEnum).
	 * @param name The name of the sensor.
	 */
	protected Sensor(final SensorTypeEnum type, final String name){
		this.type = type;
		this.name = name;
	}
	
	/** Returns the measurement performed by the sensor. */
	public abstract String measure(final Rocket rocket);

	/** Returns the type of the sensor (see SensorTypeEnum).*/
	public SensorTypeEnum getType() {
		return type;
	}

	/** Returns the name of the sensor. */
	public String getName() {
		return name;
	} 
}
