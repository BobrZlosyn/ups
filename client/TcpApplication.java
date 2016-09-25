package client;

public class TcpApplication
{

    public static void main(String[] args) {

        TcpClient client = new TcpClient( "localhost", 1234 );
        TcpMessage msgSend = new TcpMessage("Hello server");

        client.putMessage( msgSend );
        TcpMessage msgRecv = client.getMessage( );
        System.out.println(msgRecv.getMessage());

        client.close( );
    }
}
