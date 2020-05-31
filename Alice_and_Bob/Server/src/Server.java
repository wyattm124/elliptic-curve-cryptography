package ecc;

import java.util.Scanner;
import java.net.*;
import java.io.*;


// NOTE : that the Server is BOB in the cryptographic scenario

public class Server {
	private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    public void start() throws IOException {
        serverSocket = new ServerSocket(5432);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
		// server setup
		Server server=new Server();
		try {
			server.start();
			System.out.println("Server found connection !!!");
		} catch (IOException e) {
			System.out.println("error starting server");
			try {
				server.stop();
			} catch (IOException ePrime) {
				System.out.println("Error stopping server");
			}
		}


		// tty setup
		Scanner tty = new Scanner(System.in);

		// strings for messages
		String alicesMessage;
		String bobsMessage;
		int flag = 1;

		ECC E22 = new ECC(new Pair(2,2), new Pair(5, 1), 19);

		System.out.println("Bob waiting for Alice");
		//userMessage = tty.nextLine(); // read line from stdin
		int i = 0;
		while((alicesMessage = server.in.readLine()) != null) {
			if(flag == 1) {
				System.out.println("Alice's bG : " + alicesMessage);
				E22.genKey(alicesMessage);
			} else {
				System.out.println("Alice's message not decoded is : " + alicesMessage);
				System.out.println("Alice's message decoded is : " + E22.Decode(alicesMessage));
			}
			if("END".equals(alicesMessage)) {
				server.out.println("Alice terminated the connection");
				System.out.println("Alice terminated the connection");
				break;
			} else {
				if (flag == 1) {
					// handshake
					System.out.println("Sending Bob's aG : " + (E22.aG).toString());
					server.out.println(E22.PointToString((E22.aG).toString()));
				} else {
					if ((bobsMessage = tty.nextLine()) != null) {
						System.out.println("Going to encode : " + bobsMessage);
						server.out.println(E22.Encode(bobsMessage));
						System.out.println("Sent : " + E22.Encode(bobsMessage)); // DEBUG : Delet later
					} else {
						System.out.println("Bob terminated Connection");
					}
				}
			}
			flag = 0;
			System.out.println(" *** ");
			System.out.println(" ");
		}
		// stop the server
		try {
			System.out.println("Closing Connection");
			server.stop();
		} catch (IOException e) {
			System.out.println("Error stopping server");
		}
    }
}
