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
    private int port, maxTryings;
    private String server;
    private Timeline setupConnection;
    private SimpleBooleanProperty isConnected;


    public TcpApplication(){
        server = "localhost";
        port = 1234;
        maxTryings = 5;
        isConnected = new SimpleBooleanProperty(false);
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
        }

        setupConnection = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            if(sendConnectionMessage()){
                setupConnection.stop();
                setupConnection = null;
                isConnected.setValue(true);
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
        client = new TcpClient( server, port );
        boolean result = client.open();

        if(result){
            TcpMessage msgSend = new TcpMessage("<Q;quit>");
            client.putMessage( msgSend );
            TcpMessage msgRecv = client.getMessage();
            String msg = msgRecv.getMessage();
        }

        client.close();
    }

    private boolean sendConnectionMessage(){
        client = new TcpClient( server, port );
        boolean result = client.open();
        client.close();
        return result;
    }

    public boolean prepareGame(String shipInfo){

        client = new TcpClient( server, port );
        boolean result = client.open();
        if(result){
            TcpMessage msgSend = new TcpMessage("<C;"+ shipInfo + ">");
            client.putMessage( msgSend );
            TcpMessage msgRecv = client.getMessage();
            retrieveID(msgRecv.getMessage());
        }

        client.close();
        return true;
    }

    private void retrieveID(String msg){
        if(GlobalVariables.isEmpty(msg)){
            return;
        }

        String [] parts = msg.split(";");
        if(parts[0].equals("ID")){
            try {
                userID = Integer.parseInt(parts[1]);
            }catch (Exception e){}
        }
    }



}
