package client;

import game.static_classes.GlobalVariables;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

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
    private Task readTask, connectTask;

    public TcpApplication(){
        server = "localhost";
        port = 1234;
        maxTryings = 5;
        isConnected = new SimpleBooleanProperty(false);
        message = new TcpMessage();
        isWaiting = false;
        client = new TcpClient( server, port );
    }

    public int getUserID() {
        return userID;
    }

    public SimpleBooleanProperty isConnectedProperty() {
        return client.isConnectedProperty();
    }

    public boolean isConnected() {
        return client.isConnectedProperty().get();
    }

    public void endConnection(){
        sendMessageToServer(TcpMessage.QUIT, "quit", "E");
        closeConnection();
    }

    private boolean sendConnectionMessage(){
        return client.open();
    }


    public boolean sendMessageToServer(String typeOfMessage, String dataToSend, String expectedResponse){

        if(!isConnected()){
            return false;
        }

        if(message.getId().equals("0") && !typeOfMessage.equals(TcpMessage.CONNECTION)){
            return false;
        }

        message.setMessage(typeOfMessage, dataToSend);
        client.putMessage(message);
        GlobalVariables.expectedMsg = expectedResponse;

        return true;
    }

    public boolean listenForMessage( String expectedResponse){
        GlobalVariables.expectedMsg = expectedResponse;

        // cekani na odpoved
        try {
            while (message.isEmpty()){
                //nastaveni timeoutu
                Thread.sleep(100);
            }
            return doAction();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return false;
    }

    private boolean doAction() throws InterruptedException{
        //pridat cekani na acknowledge
        String type, data;
        while(true){

            type = message.getType();
            data = message.getData();
            message.clearMessage();

            switch (type){

                case TcpMessage.ATTACK:{
                    GlobalVariables.attackDefinition.set(data);
                    break;
                }

                case TcpMessage.WAITING:{
                    break;
                }

                case TcpMessage.END_WAITING:{
                    GlobalVariables.enemyshipDefinition = data ;
                    break;
                }

                case TcpMessage.IDENTITY:{
                    message.setId(data);
                    break;
                }

                case TcpMessage.EQUIPMENT_STATUS:{
                    GlobalVariables.equipmentStatus.set(data);
                    break;
                }

                case TcpMessage.ORDER:{
                    Thread.sleep(100);
                    GlobalVariables.startingID = data;
                    if(data.equals(message.getId())){
                        GlobalVariables.isPlayingNow.set(true);
                    }else{
                        GlobalVariables.isPlayingNow.set(false);
                    }

                    break;
                }

                case TcpMessage.GAME_START: break;

                case TcpMessage.RESULT:{

                    if(data.equals(message.getId())){
                        GlobalVariables.enemyLost.set(true);
                    }

                    break;
                }
                case TcpMessage.ACKNOLEDGE: break;

                case TcpMessage.ERROR: break;

                default: {
                    return false;
                }
            }

            if(type.equals(GlobalVariables.expectedMsg) && !GlobalVariables.expectedMsg.equals(TcpMessage.WAITING)) {

                break;
            }

            Thread.sleep(100);
        }

        return true;
    }

    /**
     * task pro cteni zprav ze serveru
     */
    private void readThread(){
        if(!GlobalVariables.isEmpty(readTask) && !isConnected()){
            return;
        }

        readTask = new Task<Void>() {
            @Override public Void call() {
                while(true) {
                    if (isCancelled()) {
                        closeConnection();
                        break;
                    }

                    if(isConnected()){
                        message.decodeMessage(client.getMessage());
                        if(message.getMessage().isEmpty()){
                            break;
                        }
                        System.out.println("read "+message.getMessage());
                    }else{
                        break;
                    }
                }
                return null;
            }
        };

        readTask.setOnSucceeded(event -> {
            client.close();
            client.updateIsConnected();
            connectThread();
            readTask = null;
        });

        new Thread(readTask).start();
    }

    /**
     * task pro vytvoreni spojeni se serverem
     */
    public void connectThread(){


        if(!GlobalVariables.isEmpty(connectTask) || isConnected()){
            return;
        }

        connectTask = new Task<Boolean>() {
            @Override public Boolean call() {
                while(true) {
                    if (isCancelled()) {
                        closeConnection();
                        break;
                    }

                    if(sendConnectionMessage()){
                        message.setId("0");
                        break;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                        break;
                    }
                }
                return true;
            }
        };

        connectTask.setOnSucceeded(event -> {
            client.updateIsConnected();
            readThread();
            connectTask = null;
        });

        new Thread(connectTask).start();
    }

    public void closeReadThread(){
        if(GlobalVariables.isEmpty(readTask)){
            return;
        }

        readTask.cancel();
        readTask = null;
    }

    public void closeConnectThread(){
        if(GlobalVariables.isEmpty(connectTask)){
            return;
        }

        connectTask.cancel();
        connectTask = null;
    }
    /**********************************************
     ***************  START OF GAME  **************
     **********************************************/


    public void closeConnection(){
        if(!GlobalVariables.isEmpty(client)){
            client.close();
        }

        message.setId("0");
    }

}
