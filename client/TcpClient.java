package client;
import game.static_classes.GlobalVariables;
import javafx.application.Platform;

import java.io.*;
import java.net.*;


public class TcpClient implements NetworkInterface{

    private Socket s;
    private BufferedReader reader;
    private PrintWriter writer;
    private int port;
    private String host;


    public TcpClient ( String host, int port ) {
        this.host = host;
        this.port = port;
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
            return false;
        }

        return true;

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
                if (s != null) s.close();
            }
            catch (IOException e) { }
        }
    }

    public void putMessage( TcpMessage msg) {
        try {
    	    writer.println(msg.getMessage());
            writer.flush();
        }
        catch (Exception e) {
            System.err.println("Write error");
        }
    }

    public String getMessage(  ) {
        String line = "";

        // read the response (a line) from the server
        try {
            if(reader != null){
                line = reader.readLine();
            }

            // write the line to console
            return line;
        }
        catch (IOException e) {
            System.err.println("Read error");
        }
        return line;
    }
    protected void finalize() { }
}
