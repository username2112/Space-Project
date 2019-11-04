import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
ArrayList<PrintWriter> clientOutputStreams;

public class ClientHandler implements Runnable {
	BufferedReader reader;
	Socket sock;
	
	public ClientHandler(Socket clientSocket) {
		try {
			sock = clientSocket;
			InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(isReader);
		} catch (Exception ex) {ex.printStackTrace();}
	}

	@Override
	public void run() {
		String data;
			try {
		while ((data = reader.readLine()) != null) {
			sendData(data);
		}
			} catch (Exception ex) {ex.printStackTrace();}
	}
	}

public static void main(String[] args) {
	new Server().go();
}

public void go() {
	clientOutputStreams = new ArrayList<PrintWriter>();
	try {
		ServerSocket serverSock = new ServerSocket(5000);
		
		while (true) {
			Socket clientSocket = serverSock.accept();
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			clientOutputStreams.add(writer);
			
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			System.out.println("Got a connection");
		}
	} catch (Exception ex) {ex.printStackTrace();}
}

public void sendData(String data) {
	Iterator<PrintWriter> it = clientOutputStreams.iterator();
	while (it.hasNext()) {
		try {
			PrintWriter writer = it.next();
			writer.println(data);
			writer.flush();
		} catch (Exception ex) {ex.printStackTrace();}
	}
}
}