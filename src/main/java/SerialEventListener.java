import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialEventListener implements SerialPortEventListener {


    private SerialPort port;
    private StringBuilder buffer;
    private static final String PACKET_END = "~";
    private static final String READY_TO_RECIEVE = "r";
    private static final String PROGRAM_END = ";";
    private static Integer RECIEVED_COUNTER = 0;


    public SerialEventListener(SerialPort port) {
        this.port = port;
        this.buffer = new StringBuilder();
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        recieveMessage(event);
    }
    
    
    private void printFullMessage() throws SerialPortException {
        buffer.deleteCharAt(buffer.indexOf(PACKET_END));
        System.out.println("Message no " + RECIEVED_COUNTER + ": " + buffer.toString());
        flushBuffer();
        setReadyToRecieve();
        RECIEVED_COUNTER++;
    }
    
    private void flushBuffer(){
        buffer.delete(0, buffer.length());
    }
    
    private void setReadyToRecieve() throws SerialPortException {
        port.writeString(READY_TO_RECIEVE);
//        System.out.println("JAVA program ready to recieve.");
    }

    private void closeConnection(){
        try {
            port.writeString(";");
            port.closePort();
            System.out.println("THE END OF CONNECTION");
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }


    private void recieveMessage(SerialPortEvent event){
        if(RECIEVED_COUNTER >= 10){
            closeConnection();
        }
        else if(event.isRXCHAR() && event.getEventValue() > 0){
            try{
                String recieved = port.readString(event.getEventValue());

                if(READY_TO_RECIEVE.equals(recieved)){
                    setReadyToRecieve();
                }
                else if(recieved != null) {
                    buffer.append(recieved);
                    if(recieved.contains(PACKET_END)){
                        printFullMessage();
                    }
                }
            }
            catch(SerialPortException ex) {
                System.err.println("Wystąpił błąd przy odczytywaniu danych z portu USB: " + ex);
            }
        }
    }
}
