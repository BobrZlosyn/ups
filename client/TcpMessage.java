package client;


import game.static_classes.GlobalVariables;

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
    public static final String IDENTITY = "I";
    public static final String QUIT = "Q";
    public static final String ATTACK = "A";



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
        System.out.println(message);
        bytes = msg.length();
    }

    public void decodeMessage(String msg) {
        if (GlobalVariables.isEmpty(msg)
                || msg.charAt(0) != START_MESSAGE
                || msg.charAt(msg.length() - 1) != END_MESSAGE) {
            message = "";
            bytes = 0;
            return;
        }

        message = msg.substring(1, msg.length() - 1);
        bytes = message.length();
    }
}
