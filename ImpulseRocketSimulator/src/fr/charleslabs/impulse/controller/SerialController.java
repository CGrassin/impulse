package fr.charleslabs.impulse.controller;

import java.nio.charset.StandardCharsets;

import javafx.application.Platform;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * A RocketController that uses serial port to send and receive information
 * about the rocket to a hardware rocket controller board (eg: an Arduino).<br>
 * This RocketController is asynchronous and event-driven, i.e. it triggers when
 * there is serial communication engaged.
 * 
 * @author Charles Grassin
 */
public class SerialController extends RocketController implements SerialPortEventListener {

	private SerialPort serialPort;
	private static final String gyroscopeCommand = "G", accelerometerCommand = "A", altimeterCommand = "B",
			gimbalXCommand = "GX", gimbalYCommand = "GY";
	private StringBuilder serialMsgBuffer = new StringBuilder();

	/**
	 * Connects the SerialController to a serial port, and immediately starts to
	 * handle serial communication events.
	 * 
	 * @param serialPortName
	 *            The name of the serial port to connect to. Use getPortList() to
	 *            view the available ports.
	 * @throws SerialPortException
	 *             If the connection fails this exception is thrown.
	 */
	public SerialController(final String serialPortName) throws SerialPortException {
		super(0.00001d); // Frequency is very close to 0
		serialPort = new SerialPort(serialPortName);
		serialPort.openPort();

		serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);

		serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

		serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
	}

	@Override
	protected void call(double timeSinceIgnition) {
		// Nothing to do here! (even-driven)
	}

	/**
	 * Read the available serial ports on the machine and return them.
	 * 
	 * @return An array of String containing the names of the available serial
	 *         ports. It might be empty if no port is currently available.
	 */
	static public String[] getPortList() {
		return SerialPortList.getPortNames();
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR() && event.getEventValue() > 0) {
			try {
				readLine(serialPort.readBytes());
			} catch (SerialPortException ignored) {
			}
		}
	}

	@Override
	public void close() {
		super.close();
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		super.init();
		/*
		 * try { serialPort.writeString("LAUNCH\n"); } catch (SerialPortException e) { }
		 */
	}

	/** Return true if the controller is currently connected to a serial device. */
	public boolean isConnected() {
		return serialPort.isOpened();
	}

	/**
	 * Reads the serial buffer, cuts it at newlines (\n), and execute commands.
	 * 
	 * @param buffer
	 *            The serial buffer.
	 */
	private void readLine(final byte buffer[]) {
		for (byte b : buffer) {
			// On new line symbol, try to execute command
			if ((b == '\r' || b == '\n') && serialMsgBuffer.length() > 0) {
				final String toProcess = serialMsgBuffer.toString();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println(toProcess);
						// Execute command and send response (if required)
						final byte[] response = performCommand(toProcess);
						if (response != null)
							try {
								serialPort.writeBytes(response);
							} catch (SerialPortException ignored) {
							}
					}
				});
				serialMsgBuffer.setLength(0);
			} else {
				// else, just add the char to the buffer
				serialMsgBuffer.append((char) b);
			}
		}
	}

	/**
	 * This function is called every time a serial communication is received. It
	 * does the action queried by the command and returns a String which is the
	 * response to send back to the serial device.
	 * 
	 * @param serialValue
	 *            The value receive on the serial port.
	 * @return The command to send back to the serial device. It can be "null" if
	 *         there is no answer to provide.
	 */
	public byte[] performCommand(final String serialValue) {
		if (rocket == null)
			return null;

		// Extract the command and argument
		final String values[] = serialValue.split(" ");
		final String command, argument;
		if (values.length == 1) {
			command = values[0];
			argument = null;
		} else if (values.length == 2) {
			command = values[0];
			argument = values[1];
		} else
			return null;

		// React to the command
		switch (command) {
		case gyroscopeCommand:
			return (gyroscopeCommand+" "+rocket.getAngularMotion().speed.x + " " + rocket.getAngularMotion().speed.y + " "
					+ rocket.getAngularMotion().speed.z + "\n").getBytes(StandardCharsets.UTF_8);
		case accelerometerCommand:
			return (accelerometerCommand+" "+rocket.getLinearMotion().acceleration.x + " " + rocket.getLinearMotion().acceleration.y + " "
					+ rocket.getLinearMotion().acceleration.z + "\n").getBytes(StandardCharsets.UTF_8);
		case altimeterCommand:
			return (altimeterCommand+" "+rocket.getLinearMotion().position.z + "\n").getBytes(StandardCharsets.UTF_8);
		case gimbalXCommand:
			try {
				final double value = Double.parseDouble(argument);
				rocket.getGimbal().setGimbalAngleX(value);
			} catch (Exception e) {
			}
			break;
		case gimbalYCommand:
			try {
				double value = Double.parseDouble(argument);
				rocket.getGimbal().setGimbalAngleY(value);
			} catch (Exception e) {
			}
			break;
		}
		return null;
	}

}
