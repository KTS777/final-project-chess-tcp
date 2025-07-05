package view;

import controller.CheckmateDetector;
import controller.GameController;
import model.Square;
import model.pieces.King;
import network.ChessClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class StartMenu implements Runnable {

    private static final String BLACK_PAWN_IMG = "bp.png";
    private static final String WHITE_PAWN_IMG = "wp.png";
    private static final String[] HOURS = {"0", "1", "2", "3"};
    private static final int MAX_TIME_UNIT = 60;

    private final ChessClient client;

    public StartMenu(ChessClient client) {
        this.client = client;
    }

    public void run() {
        final JFrame startWindow = new JFrame("Chess");
        startWindow.setLocation(300, 100);
        startWindow.setResizable(false);
        startWindow.setSize(260, 240);

        Box components = Box.createVerticalBox();
        startWindow.add(components);

        addTitlePanel(components);

        final JTextField blackInput = new JTextField("Black", 10);
        components.add(createPlayerPanel("Black", BLACK_PAWN_IMG, blackInput), BorderLayout.EAST);

        final JTextField whiteInput = new JTextField("White", 10);
        JPanel whitePanel = createPlayerPanel("White", WHITE_PAWN_IMG, whiteInput);
        components.add(whitePanel);

        ImageIcon icon = (ImageIcon) ((JLabel) whitePanel.getComponent(0)).getIcon();
        if (icon != null) startWindow.setIconImage(icon.getImage());

        final JComboBox<String> hours = createTimeComboBox(HOURS);
        final JComboBox<String> minutes = createTimeComboBox(generateTimeUnits());
        final JComboBox<String> seconds = createTimeComboBox(generateTimeUnits());
        components.add(setupTimerSettings(hours, minutes, seconds));

        components.add(createControlButtons(startWindow, blackInput, whiteInput, hours, minutes, seconds));
        components.add(Box.createGlue());

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);
    }


    private void handleStartClick(JTextField blackInput,
                                  JTextField whiteInput,
                                  JComboBox<String> hours,
                                  JComboBox<String> minutes,
                                  JComboBox<String> seconds,
                                  JFrame startWindow) {

        String blackName = blackInput.getText();
        String whiteName = whiteInput.getText();
        int hh = Integer.parseInt((String) hours.getSelectedItem());
        int mm = Integer.parseInt((String) minutes.getSelectedItem());
        int ss = Integer.parseInt((String) seconds.getSelectedItem());

        boolean isWhite = client.getRole().equals("WHITE");

        // Swap player names based on actual role
        String player1 = isWhite ? whiteName : blackName;
        String player2 = isWhite ? blackName : whiteName;

        // Create board
        Board board = new Board(client);

        King[] kings = board.setupStandardPosition();
        King whiteKing = kings[0];
        King blackKing = kings[1];

        CheckmateDetector detector = new CheckmateDetector(board,
                new LinkedList<>(board.getWhitePieces()),
                new LinkedList<>(board.getBlackPieces()),
                whiteKing, blackKing);

        GameController controller = new GameController(detector, board, isWhite);

        board.setGameController(controller);
        GameWindow gameWindow = new GameWindow(board, player1, player2, hh, mm, ss, client);
        board.setGameWindow(gameWindow);

        // Attach listeners
        BoardMouseHandler handler = new BoardMouseHandler(board, client);
        board.addMouseListener(handler);
        board.addMouseMotionListener(handler);

        // Start receiver thread
        new Thread(() -> {
            while (true) {
                String move = client.receiveMessage();
                if (move == null || move.length() != 4) continue;

                char fx = move.charAt(0);
                char fy = move.charAt(1);
                char tx = move.charAt(2);
                char ty = move.charAt(3);

                Square from = board.getSquare(fx - 'a', 8 - Character.getNumericValue(fy));
                Square to   = board.getSquare(tx - 'a', 8 - Character.getNumericValue(ty));

                SwingUtilities.invokeLater(() -> {
                    controller.setCurrentPiece(from.getOccupyingPiece());
                    controller.handlePieceDrop(to);
                    board.repaint();
                });
            }
        }).start();

        startWindow.dispose();
    }



    private void addTitlePanel(Box container) {
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Chess");
        titlePanel.add(titleLabel);
        container.add(titlePanel);
    }


    private void showInstructionsDialog(JFrame parent) {
        JOptionPane.showMessageDialog(parent,
                "To begin a new game, input player names\n" +
                        "next to the pieces. Set the clocks and\n" +
                        "click \"Start\". Setting the timer to all\n" +
                        "zeroes begins a new untimed game.",
                "How to play",
                JOptionPane.PLAIN_MESSAGE);
    }


    private JPanel createPlayerPanel(String labelText, String imagePath, JTextField nameField) {
        JPanel panel = new JPanel();
        JLabel pieceIcon = createPieceLabel(imagePath);
        panel.add(pieceIcon);
        panel.add(nameField);
        return panel;
    }


    private Box setupTimerSettings(JComboBox<String> hours,
                                   JComboBox<String> minutes,
                                   JComboBox<String> seconds) {

        Box timerSettings = Box.createHorizontalBox();

        hours.setMaximumSize(hours.getPreferredSize());
        minutes.setMaximumSize(minutes.getPreferredSize());
        seconds.setMaximumSize(minutes.getPreferredSize());

        timerSettings.add(hours);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(seconds);
        timerSettings.add(Box.createHorizontalStrut(10));
        timerSettings.add(minutes);
        timerSettings.add(Box.createVerticalGlue());

        return timerSettings;
    }

    private JComboBox<String> createTimeComboBox(String[] values) {
        JComboBox<String> comboBox = new JComboBox<>(values);
        comboBox.setMaximumSize(comboBox.getPreferredSize());
        return comboBox;
    }

    private String[] generateTimeUnits() {
        String[] units = new String[MAX_TIME_UNIT];
        for (int i = 0; i < MAX_TIME_UNIT; i++) {
            units[i] = String.format("%02d", i);
        }
        return units;
    }

    private JLabel createPieceLabel(String imagePath) {
        JLabel label = new JLabel();
        try {
            Image img = ImageIO.read(getClass().getResource(imagePath));
            label.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Required game file " + imagePath + " missing (" + e.getMessage() + ")");
        }
        return label;
    }

    private Box createControlButtons(JFrame startWindow,
                                     JTextField blackInput,
                                     JTextField whiteInput,
                                     JComboBox<String> hours,
                                     JComboBox<String> minutes,
                                     JComboBox<String> seconds) {
        Box buttons = Box.createHorizontalBox();

        JButton quit = new JButton("Quit");
        quit.addActionListener(e -> startWindow.dispose());

        JButton instr = new JButton("Instructions");
        instr.addActionListener(e -> showInstructionsDialog(startWindow));

        JButton start = new JButton("Start");
        start.addActionListener(e -> handleStartClick(
                blackInput, whiteInput, hours, minutes, seconds, startWindow));

        buttons.add(start);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(instr);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(quit);

        return buttons;
    }



}
