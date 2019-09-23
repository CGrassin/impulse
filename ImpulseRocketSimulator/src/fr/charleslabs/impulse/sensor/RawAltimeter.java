package fr.charleslabs.impulse.sensor;

import fr.charleslabs.impulse.rocket.Rocket;

public class RawAltimeter extends Sensor {
	public RawAltimeter() {
		super(Sensor.SensorTypeEnum.ALTIMETER, "Raw Altimeter");
	}

	@Override
	public String measure(Rocket rocket) {
		return rocket.getLinearMotion().position.z+"";
	}
}
