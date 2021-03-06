package prvaprimer05.prva;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class UserServerThread extends Thread {

	public UserServerThread(Socket sock, ArrayList<String> users) {
		this.sock = sock;
		this.users = users;
		try {
			// inicijalizuj ulazni stream
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));

			// inicijalizuj izlazni stream
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream())), true);
			// pokreni thread
			start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		String response = "";
		try {
			// procitaj zahtev
			String request = in.readLine();

			// dodaj u listu
			response = clientResponse(request);

			// posalji odgovor
			out.println(response);

			// zatvori konekciju
			in.close();
			out.close();
			sock.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Generisanje odgovora za klijenta
	 */
	private String clientResponse(String request) {
	
		if(request.startsWith("ADD")) {
			String[] delovi = request.split(" "); 
			if(delovi.length != 2) {
				return "BAD REQUEST!!!";
			}
			String newUser = delovi[1];
			users.add(newUser);
			return "ALL OK\nEND";
		} else if (request.equals("LIST")) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < users.size(); i++) {
				String user = users.get(i);
				builder.append(user);
				builder.append(",");
			}
			builder.append("\nEND");
			return builder.toString();
		} else {
			return "BAD REQUEST!!!\nEND";
			
		}
		
		
		
	
	}

	private Socket sock;
	private ArrayList<String> users;
	private BufferedReader in;
	private PrintWriter out;
}
