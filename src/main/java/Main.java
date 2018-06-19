import jssc.SerialPort;
import jssc.SerialPortException;

public class Main {

    public static void main(String[] args){
        final String PORT_NAME = "COM3";
        SerialPort serialPort = new SerialPort(PORT_NAME);

        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(new SerialEventListener(serialPort));

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
