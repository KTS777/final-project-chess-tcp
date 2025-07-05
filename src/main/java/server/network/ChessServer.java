package server.network;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ChessServer {

    private static final int PORT = 5000;
    private final ExecutorService pool = Executors.newFixedThreadPool(2); // For 2 players

    private Socket player1Socket;
    private Socket player2Socket;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Waiting for Player 1...");
            player1Socket = serverSocket.accept();
            System.out.println("[SERVER] Player 1 connected: " + player1Socket.getInetAddress());

            System.out.println("[SERVER] Waiting for Player 2...");
            player2Socket = serverSocket.accept();
            System.out.println("[SERVER] Player 2 connected: " + player2Socket.getInetAddress());

            //Send roles
            PrintWriter out1 = new PrintWriter(player1Socket.getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(player2Socket.getOutputStream(), true);
            out1.println("WHITE");
            out2.println("BLACK");


            // Start handler threads
            pool.execute(new ClientHandler(player1Socket, player2Socket, "Player 1"));
            pool.execute(new ClientHandler(player2Socket, player1Socket, "Player 2"));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket inSocket;
        private final Socket outSocket;
        private final String name;

        public ClientHandler(Socket inSocket, Socket outSocket, String name) {
            this.inSocket = inSocket;
            this.outSocket = outSocket;
            this.name = name;
        }

        @Override
        public void run() {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outSocket.getOutputStream()))
            ) {
                writer.write("[SERVER] " + name + " has connected.\n");
                writer.flush();

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("[" + name + "] " + message);
                    writer.write(message + "\n");
                    writer.flush();
                }

            } catch (IOException e) {
                System.err.println("[SERVER] Connection lost: " + name);
            }
        }
    }
}
