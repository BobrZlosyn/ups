package client;
import game.static_classes.GlobalVariables;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;
import java.net.*;


public class TcpClient{

    private Socket s;
    private BufferedReader reader;
    private PrintWriter writer;
    private int port;
    private String host;
    private SimpleBooleanProperty isConnected;

    public TcpClient ( String host, int port ) {
        this.host = host;
        this.port = port;
        isConnected = new SimpleBooleanProperty(false);
    }


    public Socket getS() {
        return s;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean open() {

        // create a socket to communicate to the specified host and port
        try {
            s = new Socket( host, port);

        }
        catch (IOException e) {
            System.out.println("Connection to " + host + ":" + port + " refused");
            return false;
         }
        catch (IllegalArgumentException e){
            System.out.println("Illegal port - not in allowed range 0 - 65535");
            return false;
        }
        catch (NullPointerException e){
            System.out.println("Hostname not supplied");
            return false;
        }

        try {
            // create streams for reading and writing
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

            // tell the user that we've connected
            System.out.println("Connected to " + s.getInetAddress() +
                    ":" + s.getPort());
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public SimpleBooleanProperty isConnectedProperty(){
        return isConnected;
    }

    public void updateIsConnected(){
        if (GlobalVariables.isNotEmpty(s)) {
            isConnected.set(!s.isClosed());
        }else {
            isConnected.set(false);
        }

    }

    public void close(  ) {
        try {
            if(!GlobalVariables.isEmpty(reader)){
                reader.close();
            }

            if(!GlobalVariables.isEmpty(writer)){
                writer.close();
            }
        }
        // pouze java 1.7 funkcni
        //catch (IOException | NullPointerException e) {
        catch (Exception e) {
            System.err.println("Close error");
        }
        //always be sure to close the socket
        finally {
            try {
                if (s != null){
                    s.close();
                }
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void putMessage( TcpMessage msg) {
        try {
    	    writer.println(msg.getMessage());
            writer.flush();
            System.out.println(msg.getMessage());
            msg.clearMessage();
        }
        catch (Exception e) {
            System.err.println("Write error");
        }
    }

    public String getMessage(  ){
        String line = "";

        // read the response (a line) from the server
        try {

            do{
                Thread.sleep(100);
            } while(GlobalVariables.isNotEmpty(reader) && (line = reader.readLine()).isEmpty());

            return line;
        }
        catch (IOException e) {
            System.err.println("Read error");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isConnected.set(false);
        return line;
    }

    @Override
    protected void finalize() { }
}
