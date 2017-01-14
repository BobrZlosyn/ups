package client;

import game.static_classes.GlobalVariables;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.util.Duration;
import sun.awt.windows.ThemeReader;

public class TcpApplication {

    private TcpClient client;
    private TcpMessage message;
    private Task readTask, connectTask, actionTask;
    private boolean potvrzeni;
    private Timeline checkSending;
    public TcpApplication(){
        String server = GlobalVariables.serverIPAdress.getValue();
        int port = Integer.parseInt(GlobalVariables.serverPort.getValue());
        message = new TcpMessage();
        client = new TcpClient( server , port );
        //checkConnectionLive(); inicializace potvrzovani pripojeni

        GlobalVariables.serverIPAdress.addListener((observable, oldValue, newValue) -> {
            setUpNewConnection(newValue, Integer.parseInt(GlobalVariables.serverPort.getValue()));
        });

        GlobalVariables.serverPort.addListener((observable, oldValue, newValue) -> {
            setUpNewConnection(GlobalVariables.serverIPAdress.getValue(), Integer.parseInt(newValue));
        });
    }

    private void setUpNewConnection(String server, int port){
        endConnection();
        closeConnection();
        client.updateIsConnected();
        client.setPort(port);
        client.setHost(server);
        connectThread();
    }

    public SimpleBooleanProperty isConnectedProperty() {
        return client.isConnectedProperty();
    }

    public boolean isConnected() {
        return client.isConnectedProperty().get();
    }

    public void endConnection(){
        sendMessageToServer(TcpMessage.QUIT, "quit", TcpMessage.ERROR);
        closeConnection();
    }

    private boolean sendConnectionMessage(){
        return client.open();
    }


    public boolean sendMessageToServer(String typeOfMessage, String dataToSend, String expectedResponse){

        if(!isConnected()){
            return false;
        }

        if(!message.hasId()
                && !typeOfMessage.equals(TcpMessage.CONNECTION)
                && !typeOfMessage.equals(TcpMessage.QUIT)
                && !typeOfMessage.equals(TcpMessage.CHECK_CONNECTION)){
            return false;
        }

        message.setMessage(typeOfMessage, dataToSend);
        client.putMessage(message);

        if (!expectedResponse.equals(TcpMessage.NONE)) {
            GlobalVariables.expectedMsg = expectedResponse;
        }
        return true;
    }

    private void doAction() throws InterruptedException{
        //pridat cekani na acknowledge

        if(message.getMessage().isEmpty()){
            return;
        }

        String type = message.getType();
        String data = message.getData();
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


            /*
            akce pro reconnect nepotrebuje se
            case TcpMessage.WAITING_FOR_RECONNECTION: {
                if (!GlobalVariables.gameIsFinished.get()){
                    GlobalVariables.reconnection.set(true);
                }
            }break;


            case TcpMessage.RECONNECTION_BACK: {
                if (!GlobalVariables.gameIsFinished.get()){
                    GlobalVariables.reconnection.set(false);
                }
            }break;
            */
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
                GlobalVariables.enemyLost.set(data.equals(message.getId()));
                break;
            }

            case TcpMessage.ERROR: break;
        }

        GlobalVariables.receivedMsg = type;
    }

    /**
     * task pro cteni zprav ze serveru
     */
    private void readThread(){
        if(GlobalVariables.isNotEmpty(readTask) && !isConnected() || GlobalVariables.APLICATION_EXIT){
            return;
        }


        readTask = new Task<Void>() {
            @Override public Void call() throws InterruptedException {
                while(true) {

                    if (GlobalVariables.APLICATION_EXIT) {
                        closeConnection();
                        break;
                    }

                    if(isConnected()){
                        message.decodeMessage(client.getMessage());
                        if(message.isEmpty()){
                            break;
                        }
                        System.out.println("read "+message.getMessage());
                        doAction();

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
            closeActionThread();
            connectThread();
            actionTask = null;
            readTask = null;
        });

        new Thread(readTask).start();
    }

    /**
     * task pro vytvoreni spojeni se serverem
     */
    public void connectThread(){
        if(GlobalVariables.isNotEmpty(connectTask) || isConnected() || GlobalVariables.APLICATION_EXIT){
            return;
        }

        //checkConnectionStop(); stopnuti zpravy pro zjisteni pripojeni - nepotrebne
        connectTask = new Task<Boolean>() {
            @Override public Boolean call() {
                while(true) {



                    if (GlobalVariables.APLICATION_EXIT) {
                        closeConnection();
                        break;
                    }
                    if(sendConnectionMessage()){
                        break;
                    }


                    try {
                        if(!isConnected()){
                            Thread.sleep(1000 + (int)(Math.random() *1000));
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                return true;
            }
        };

        connectTask.setOnSucceeded(event -> {
            client.updateIsConnected();
            readThread();
            //checkConnectionStart(); - overovani pripojeni - nepotrebne
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

    public void closeActionThread(){
        if(GlobalVariables.isEmpty(actionTask)){
            return;
        }

        actionTask.cancel();
        actionTask = null;
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
        client.updateIsConnected();
        message.removeID();
        connectThread();

    }

    public TcpClient getClient(){
        return client;
    }

    /* posilani zpravy pro overeni ze jsme stale pripojeni - nepotrebne zatim
    private void checkConnectionLive() {
        checkSending = new Timeline(new KeyFrame(Duration.seconds(10), event -> checkConnectionFunction()));
        checkSending.setCycleCount(Animation.INDEFINITE);
        potvrzeni = true;
    }

    public void checkConnectionStart(){
        if(GlobalVariables.isNotEmpty(checkSending)){
            checkSending.playFromStart();
            potvrzeni = true;
        }

    }

    private void checkConnectionFunction() {
        sendMessageToServer(TcpMessage.CHECK_CONNECTION, "is server there?", TcpMessage.NONE);
        if(GlobalVariables.APLICATION_EXIT){
            checkConnectionStop();
        }

    }

    public void checkConnectionStop() {
        if(GlobalVariables.isNotEmpty(checkSending)){
            checkSending.stop();
        }
    }
    */
    public TcpMessage getMessage() {
        return message;
    }
}
