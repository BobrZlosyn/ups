package client;

public class TcpApplication
{

    public static String sendMessageToServer(){
        TcpClient client = new TcpClient( "localhost", 1234 );
        TcpMessage msgSend = new TcpMessage("connect");
        client.putMessage( msgSend );
        TcpMessage msgRecv = client.getMessage();
        String msg = msgRecv.getMessage();

        client.close();


        /*if(msg.trim().equals("success")){
            setConnectionStatusLabel("Pøipojeno");
        }*/

        return msg;
    }
}
