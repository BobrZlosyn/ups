package client;

import game.static_classes.GlobalVariables;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

public class TcpApplication
{

    private int userID;
    private TcpClient client;
    private TcpMessage message;
    private int port, maxTryings;
    private String server;
    private Timeline setupConnection;
    private SimpleBooleanProperty isConnected;
    private boolean isWaiting;

    public TcpApplication(){
        server = "localhost";
        port = 1234;
        maxTryings = 5;
        isConnected = new SimpleBooleanProperty(false);
        message = new TcpMessage();
        isWaiting = false;
    }

    public int getUserID() {
        return userID;
    }

    public SimpleBooleanProperty isConnectedProperty() {
        return isConnected;
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public void setupConnection(){
        if(sendConnectionMessage()){
            isConnected.setValue(true);
            return;
        }else{
            isConnected.setValue(false);
        }

        setupConnection = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            if(sendConnectionMessage()){
                setupConnection.stop();
                setupConnection = null;
                isConnected.setValue(true);
            }else{
                isConnected.set(false);
            }
        }));
        setupConnection.setCycleCount(Animation.INDEFINITE);
        setupConnection.playFromStart();
    }

    public void stopSetupConnection(){
        if(!GlobalVariables.isEmpty(setupConnection)){
            setupConnection.stop();
            setupConnection = null;
        }
    }

    public void endConnection(){
        boolean result = true;
        if(result){
            message.setMessage(message.QUIT,"quit");
            client.putMessage(message);
            message.decodeMessage(client.getMessage());
            if(message.getBytes() != 0){
                message.setId("0");
            }
        }

        closeConnection();
    }

    private boolean sendConnectionMessage(){
        client = new TcpClient( server, port );
        return client.open();
    }

    /**
     * when user surrender in the game
     * @return
     */
    public boolean sendLostData(){
            message.setMessage(message.LOST, "vzdavam se");
            client.putMessage( message );
            message.decodeMessage(client.getMessage());

        return true;
    }

    /**
     * when user do a attack
     * @param attackMsg
     * @return
     */
    public boolean sendAttackData(String attackMsg){
            message.setMessage(message.ATTACK, attackMsg);
            client.putMessage( message );
            message.decodeMessage(client.getMessage());
        return true;
    }

    /**********************************************
     ***************  START OF GAME  **************
     **********************************************/

    /**
     *
     * @param shipInfo
     * @return
     */
    public boolean prepareGame(String shipInfo){

        boolean result =  client.isConnected();
        System.out.println(result);
        if(result && !message.hasId()){
            message.setMessage(message.CONNECTION, shipInfo);
            client.putMessage( message );
            message.decodeMessage(client.getMessage());
            result = retrieveID(message.getMessage());
        }

        if(result){
            message.setMessage(message.GAME_START, "start the game please");
            client.putMessage( message );
            while (true){
                message.decodeMessage(client.getMessage());
                if(message.isWantedMessage(message.END_WAITING)){
                    System.out.println("ahojkz");
                    break;
                }
            }
        }
        return true;
    }

    public void closeConnection(){
        if(!GlobalVariables.isEmpty(client)){
            client.close();
        }
    }

    private boolean retrieveID(String msg){

        String [] parts = msg.split(message.SEPARATOR);
        if(parts.length == 2 && parts[0].equals(message.IDENTITY)){
            message.setId(parts[1]);
            return  true;
        }

        return false;
    }

    private boolean runTheGame(String msg){

        if(message.WAITING.equals(msg.charAt(0))){
            isWaiting = true;
            return false;
        }

        isWaiting = false;
        return true;
    }

    public String getMessage(){
        return message.getMessage().substring(2);
    }


}
