package client;

import game.exportImportDataHandlers.WriteSettings;
import game.static_classes.GlobalVariables;

public class TcpMessage {

    private String  message;
    private int     bytes;
    private final char START_MESSAGE = '<';
    private final char END_MESSAGE = '>';
    public static final String SEPARATOR = ";";
    private String expectedType;

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
    public static final String ERROR = "E";
    public static final String RESULT = "R";
    public static final String ACKNOLEDGE = "P";
    public static final String ORDER = "O";
    public static final String EQUIPMENT_STATUS = "M";
    public static final String DESTROY_CONNECTION = "D";


    public TcpMessage( ) {
        message = "";
        bytes = 0;
        expectedType = "";
    }

    public void setExpectedType(String expectedType) {
        this.expectedType = expectedType;
    }

    public String getExpectedType() {
        return expectedType;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty(){
        return message.isEmpty();
    }

    public int getBytes() {
        return bytes;
    }

    public String getId() {
        return GlobalVariables.playerID;
    }

    public boolean hasId(){
        return !GlobalVariables.playerID.equals("0");
    }

    public void setId(String id) {
        GlobalVariables.playerID = id;
        WriteSettings.writeSettings();
    }

    public void setMessage(String msgType, String msg) {
        StringBuilder msgBuild = new StringBuilder();
        msgBuild.append(START_MESSAGE);

        msgBuild.append(msgType);
        msgBuild.append(SEPARATOR);
        msgBuild.append(GlobalVariables.playerID);
        msgBuild.append(SEPARATOR);
        msgBuild.append(msg);

        msgBuild.append(END_MESSAGE);

        message = msgBuild.toString();
        bytes = msg.length();
    }

    public void decodeMessage(String msg) {
        if(GlobalVariables.isEmpty(msg)){
            message = "";
            bytes = 0;
            return;
        }

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

    public String getData(){
        if(message.length() < 3){
            return  "";
        }
        return message.substring(2);
    }

    public void removeID(){
        GlobalVariables.playerID = "0";
        WriteSettings.writeSettings();
    }

    public String getType(){
        if(message.isEmpty()){
            return  "E";
        }
        return "" +message.charAt(0);
    }
}
