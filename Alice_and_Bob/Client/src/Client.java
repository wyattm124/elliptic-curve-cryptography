package ecc;

import java.util.Scanner;
import java.net.*;
import java.io.*;

// NOTE : cryptographically the client is Alice

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;



    public void startConnection(String ip, int port) throws UnknownHostException, IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void stopConnection() {
		try {
        	in.close();
        	out.close();
        	clientSocket.close();
		} catch (Exception e) {
			System.out.println("Error on close of connection");
    	}
	}

	public static void main(String[] args) throws IOException {
		// Client setup
		Client client = new Client();
		Scanner tty = new Scanner(System.in);
		String alicesMessage;
		String bobsMessage;
    int flag = 1;

    ECC E22 = new ECC(new Pair(2,2), new Pair(5, 1), 19);


		System.out.println("Where is Bob?");
		alicesMessage = tty.nextLine();
		try {
		client.startConnection(alicesMessage, 5432);
	} catch (Exception e) {
		System.out.println("Something went wrong connecting to Bob");
		client.stopConnection();
	}
		System.out.println("Connected to the Bob");

		while((alicesMessage = tty.nextLine()) != null) {
      		// check if we are doing the handshake !!!
      		if(flag == 1) {
				System.out.println("aG for Alice : " + (E22.aG).toString());
        		client.out.println(E22.PointToString((E22.aG).toString())); // aG
      		} else {
				System.out.println("Going to encode  : " + alicesMessage);
        		client.out.println(E22.Encode(alicesMessage));
				System.out.println("Sent : " + E22.Encode(alicesMessage)); // DEBUG : Delet later
      		}
      		// check if we are terminatong connection
			if("END".equals(alicesMessage)) {
				System.out.println("Alice terminated the connection");
				break;
			} else {
        		// check bobs response, did he terminate the connection ???
				if ((bobsMessage = client.in.readLine()) != null) {
          			// check if we are doing the handshake
          			if(flag == 1) {
						System.out.println("Bob's bG : " + bobsMessage);
            			E22.genKey(bobsMessage);
          			} else {
            			System.out.println("Bob's message not decoded is : " + bobsMessage);
            			System.out.println("Bob's message decoded is : " + E22.Decode(bobsMessage));
          			}
				} else {
					System.out.println("Bob terminated the connection");
					break;
				}
        		flag = 0;
				System.out.println(" *** ");
				System.out.println(" ");
			}
		}
		client.stopConnection();
    }
}
