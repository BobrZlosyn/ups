package client;
import game.static_classes.GlobalVariables;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;
import java.net.*;


public class TcpClient implements NetworkInterface{

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

    @Override
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
        isConnected.set(!s.isClosed());
    }

    @Override
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

    @Override
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

    @Override
    public String getMessage(  ){
        String line = "";

        // read the response (a line) from the server
        try {
            if(reader == null){
                return line;
            }

            do{
                Thread.sleep(100);
            } while(!GlobalVariables.isEmpty(reader) && (line = reader.readLine()).isEmpty());
             return line;
        }
        catch (IOException e) {
            System.err.println("Read error");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return line;
    }

    @Override
    protected void finalize() { }
}
