import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerChat {
	ServerSocket listener = null;
	Socket client = null;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	int port = 18888;
	String msg = "";

	void listen() {
		try {
			listener = new ServerSocket(port);
			System.out.println("Waiting for connection");
			// app is blocked by accept() call until
			// a client connection
			client = listener.accept();
			System.out.println("Client connected " + client.getInetAddress().getHostName());
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			in = new ObjectInputStream(client.getInputStream());
			// start dialog
			do {
				try {
					msg = (String) in.readObject();
					// echoed received message
					System.out.println("client> " + msg);
					DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date d = new Date();
					sendMessage("Message received;" + df.format(d));
				} catch (ClassNotFoundException ex) {
					Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
				}
			} while (!msg.equals("exit"));
		} catch (IOException ex) {
			Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				if (listener != null)
					listener.close();
			} catch (IOException ex) {
				Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ex) {
			Logger.getLogger(ServerChat.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
