package client;

public interface NetworkInterface {
	boolean open();
	void close();
	void putMessage(TcpMessage msg);
	TcpMessage getMessage();
}
