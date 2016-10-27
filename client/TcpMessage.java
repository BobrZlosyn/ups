package client;

public class TcpMessage {

    private String  message;
    private int     bytes;
    private final char START_MESSAGE = '<';
    private final char END_MESSAGE = '>';
    private String id;
    public static final String SEPARATOR = ";";

    /**
     *      MESSAGE TYPES
     */
    public static final String CONNECTION = "C";
    public static final String GAME_START = "G";
    public static final String END_WAITING = "S";
    public static final String IDENTITY = "I";
    public static final String QUIT = "Q";
    public static final String ATTACK = "A";
    public static final String WAITING = "W";
    public static final String LOST = "L";



    public TcpMessage( ) {
        message = "";
        bytes = 0;
        id = "0";
    }

    public String getMessage() {
        return message;
    }

    public int getBytes() {
        return bytes;
    }

    public String getId() {
        return id;
    }

    public boolean hasId(){
        return !id.equals("0");
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String msgType, String msg) {
        StringBuilder msgBuild = new StringBuilder();
        msgBuild.append(START_MESSAGE);

        msgBuild.append(msgType);
        msgBuild.append(SEPARATOR);
        msgBuild.append(id);
        msgBuild.append(SEPARATOR);
        msgBuild.append(msg);

        msgBuild.append(END_MESSAGE);

        message = msgBuild.toString();
        bytes = msg.length();
    }

    public void decodeMessage(String msg) {
        msg = msg.trim();

        if (msg.length() < 2
                || msg.charAt(0) != START_MESSAGE
                || msg.charAt(msg.length() - 1) != END_MESSAGE) {
            message = "";
            bytes = 0;
            return;
        }

        message = msg.substring(1, msg.length() - 1);
        bytes = message.length();
    }

    public void clearMessage(){
        message = "";
        bytes = 0;
    }

    public boolean isWantedMessage(String typeOfMessage){
         if(message.isEmpty()){
            return false;
        }

        return typeOfMessage.equals("" +message.charAt(0));
    }
}
