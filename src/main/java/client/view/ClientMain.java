package client.view;



import client.network.ChessClient;

import javax.swing.*;

public class ClientMain implements Runnable {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    @Override
    public void run() {
        try {
            // 🧠 Connect client to server
            ChessClient client = new ChessClient(SERVER_HOST, SERVER_PORT);
            client.connect();

            // 🪄 Launch StartMenu, optionally pass client if needed
            SwingUtilities.invokeLater(() -> new StartMenu(client).run());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect to server: " + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ClientMain());
    }
}
