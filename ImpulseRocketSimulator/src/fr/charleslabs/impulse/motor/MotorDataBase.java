package fr.charleslabs.impulse.motor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public final class MotorDataBase {
	static public final String dbPath = "/res/motors.json";
	private HashMap<String, RocketMotor> motorList = loadMotors();
	private MotorDataBase() {
		motorList = loadMotors();
	}
    private static MotorDataBase INSTANCE = new MotorDataBase();
    public static MotorDataBase getInstance()
    {   return INSTANCE;
    }
    
	public RocketMotor getMotor(String motorName) {
		if (motorList.containsKey(motorName))
			return motorList.get(motorName);
		else
			return null;
	}

	public String[] getMotorList() {
		return motorList.keySet()
				.toArray(new String[motorList.keySet().size()]);
	}

	public boolean isEmpty() {
		return motorList.isEmpty();
	}

	private String loadFromFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResource(path).openStream()));

		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
			line = br.readLine();
		}
		br.close();
		return sb.toString();
	}

	private double[] toDoubleArray(JsonArray object) {
		double returnValue[] = new double[object.size()];
		for (int i = 0; i < object.size(); i++) {
			returnValue[i] = object.get(i).asDouble();
		}
		return returnValue;
	}

	private HashMap<String, RocketMotor> loadMotors() {
		final HashMap<String, RocketMotor> array = new HashMap<String, RocketMotor>();
		String json;
		try {
			json = loadFromFile(dbPath);
		} catch (IOException e) {
			array.put("Default", new SolidFuelMotor(3, 5));
			return array;
		}

		final JsonArray motors = Json.parse(json).asArray();
		for (JsonValue motor : motors) {
			final String name = ((JsonObject) motor).get("name").asString();
			final String type = ((JsonObject) motor).get("type").asString();

			if (type.equals("SolidFuelMotor")) {
				array.put(name,
						new SolidFuelMotor(toDoubleArray(((JsonObject) motor)
								.get("thrust").asArray()), ((JsonObject) motor)
								.get("burnTime").asDouble()));
			}
		}
		return array;
	}
}
