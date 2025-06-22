package view;

import controller.ChessTimer;
import model.Clock;
import network.ChessClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class GameWindow {
    private JFrame gameWindow;
    public Clock blackClock;
    public Clock whiteClock;
    private ChessTimer chessTimer;
    private Board board;
    private ChessClient client;




    public GameWindow(String blackName, String whiteName, int hh,
                      int mm, int ss, ChessClient client) {
        this.client = client;
        blackClock = new Clock(hh, ss, mm);
        whiteClock = new Clock(hh, ss, mm);

        initializeFrame();
        addGameComponents(blackName, whiteName, hh, mm, ss);
        gameWindow.setVisible(true);
    }

    private void initializeFrame() {
        gameWindow = new JFrame("Chess");

        try {
            Image whiteImg = ImageIO.read(getClass().getResource("wp.png"));
            gameWindow.setIconImage(whiteImg);
        } catch (Exception e) {
            System.out.println("view.Game file wp.png not found");
        }

        gameWindow.setLocation(100, 100);
        gameWindow.setLayout(new BorderLayout(20, 20));
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addGameComponents(String blackName, String whiteName, int hh, int mm, int ss) {
        this.board = new Board(this, client);

        JPanel gameData = createGameDataPanel(blackName, whiteName, hh, mm, ss);
        gameWindow.add(gameData, BorderLayout.NORTH);

        gameWindow.add(board, BorderLayout.CENTER);
        gameWindow.add(createControlButtonsPanel(), BorderLayout.SOUTH);

        gameWindow.setSize(gameWindow.getPreferredSize());
        gameWindow.setResizable(false);
        gameWindow.pack();
    }


    private JPanel createGameDataPanel(String bn, String wn, int hh, int mm, int ss) {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel wName = createCenteredLabel(wn);
        JLabel bName = createCenteredLabel(bn);

        JLabel wTime = createCenteredLabel("");
        JLabel bTime = createCenteredLabel("");

        panel.add(wName);
        panel.add(bName);
        panel.add(wTime);
        panel.add(bTime);

        if (hh == 0 && mm == 0 && ss == 0) {
            wTime.setText("Untimed game");
            bTime.setText("Untimed game");
        } else {
            chessTimer = new ChessTimer(board, whiteClock, blackClock, wTime, bTime, gameWindow, wn, bn, hh, mm, ss);
            chessTimer.start();
        }

        return panel;
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        return label;
    }


    private JPanel createControlButtonsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));

        JButton howToPlayBtn = new JButton("How to play");
        howToPlayBtn.addActionListener(createInstructionsListener());

        JButton newGameBtn = new JButton("New game");
        newGameBtn.addActionListener(createNewGameListener());

        JButton quitBtn = new JButton("Quit");
        quitBtn.addActionListener(createQuitListener());

        panel.add(howToPlayBtn);
        panel.add(newGameBtn);
        panel.add(quitBtn);

        panel.setPreferredSize(panel.getMinimumSize());
        return panel;
    }

    private ActionListener createInstructionsListener() {
        return e -> JOptionPane.showMessageDialog(
                gameWindow,
                "Move the chess pieces on the board by clicking\n"
                        + "and dragging. The game will watch out for illegal\n"
                        + "moves. You can win either by your opponent running\n"
                        + "out of time or by checkmating your opponent.\n\n"
                        + "Good luck, hope you enjoy the game!",
                "How to play",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private ActionListener createNewGameListener() {
        return e -> {
            int result = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Are you sure you want to begin a new game?",
                    "Confirm new game",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(new StartMenu(client));
                gameWindow.dispose();
            }
        };
    }

    private ActionListener createQuitListener() {
        return e -> {
            int result = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Are you sure you want to quit?",
                    "Confirm quit",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                if (chessTimer != null) chessTimer.stop();
                gameWindow.dispose();
            }
        };
    }


    public void checkmateOccurred(int winningColor) {
        if (chessTimer != null) chessTimer.stop();

        String winner = (winningColor == 0) ? "White" : "Black";

        int choice = JOptionPane.showConfirmDialog(
                gameWindow,
                winner + " wins by checkmate! Set up a new game?\n" +
                        "Choosing \"No\" lets you look at the final situation.",
                winner + " wins!",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(new StartMenu(client));
            gameWindow.dispose();
        }
    }

}
