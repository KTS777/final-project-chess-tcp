package server;

import server.network.ChessServer;

public class ServerMain {
    public static void main(String[] args) {
        ChessServer server = new ChessServer();
        server.start();
    }
}
