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
        boolean result = client.open();
        System.out.println("ahoj "+ result);
        if(result){
            message.setMessage(message.QUIT,"quit");
            client.putMessage(message);
            message.decodeMessage(client.getMessage());
            if(message.getBytes() != 0){
                message.setId("0");
            }
        }

        client.close();
    }

    private boolean sendConnectionMessage(){
        client = new TcpClient( server, port );
        boolean result = client.open();
        client.close();
        return result;
    }

    /**
     * when user surrender in the game
     * @return
     */
    public boolean sendLostData(){
        boolean result = client.open();
        if(result){
            message.setMessage(message.LOST, "vzdavam se");
            client.putMessage( message );
            message.decodeMessage(client.getMessage());
        }
        client.close();
        return true;
    }

    /**
     * when user do a attack
     * @param attackMsg
     * @return
     */
    public boolean sendAttackData(String attackMsg){
        boolean result = client.open();
        if(result){
            message.setMessage(message.ATTACK, attackMsg);
            client.putMessage( message );
            message.decodeMessage(client.getMessage());
        }
        client.close();
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

        boolean result = client.open();
        System.out.println(result);
        if(result && !message.hasId()){
            message.setMessage(message.CONNECTION, shipInfo);
            client.putMessage( message );
            message.decodeMessage(client.getMessage());
            result = retrieveID(message.getMessage());
        }

        System.out.println(result);
        if(result){
            message.setMessage(message.GAME_START, "start the game please");
            client.putMessage( message );
            message.decodeMessage(client.getMessage());
            result = runTheGame(message.getMessage());
        }

        System.out.println(result);
        client.close();
        return result;
    }

    /**
     * method for method purpose
     */
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
