package network;

import java.io.*;
import java.net.Socket;

public class ChessClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private final String host;
    private final int port;

    private String role; // "WHITE" or "BLACK"


    public ChessClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true); // enable auto-flush
        System.out.println("[CLIENT] Connected to server at " + host + ":" + port);

        role = in.readLine();
        System.out.println("[CLIENT] Assigned role: " + role);
    }

    public void sendMessage(String message) {
        out.println(message);
        System.out.println("[CLIENT] Sent: " + message);
    }

    public String receiveMessage() {
        try {
            String response = in.readLine();
            System.out.println("[CLIENT] Received: " + response);
            return response;
        } catch (IOException e) {
            System.err.println("[CLIENT] Failed to receive message: " + e.getMessage());
            return null;
        }
    }

    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("[CLIENT] Disconnected from server.");
        } catch (IOException e) {
            System.err.println("[CLIENT] Failed to close resources: " + e.getMessage());
        }
    }

    public String getRole() {
        return role;
    }

}
