package client.view;


import client.controller.GameController;
import client.network.ChessClient;
import shared.model.Piece;
import shared.model.PieceFactory;
import shared.model.Square;
import shared.model.pieces.King;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Board extends JPanel {

    private final Square[][] board;
    private GameWindow gameWindow;

    private static final int BOARD_SIZE = 8;

    private final LinkedList<Piece> blackPieces;
    private final LinkedList<Piece> whitePieces;

    private int dragX;
    private int dragY;

    private GameController gameController;
    private final ChessClient client;


    private final BoardRenderer renderer = new BoardRenderer();

    public Board(ChessClient client) {
        this.gameWindow = null;
        this.client = client;
        board = new Square[BOARD_SIZE][BOARD_SIZE];
        blackPieces = new LinkedList<>();
        whitePieces = new LinkedList<>();

        setLayout(new GridLayout(8, 8, 0, 0));
        registerInputListeners();

        initializeBoardSquares();
        configureBoardSize();
    }

    public Square getSquare(int x, int y) {
        return board[y][x];
    }

    public void setupEmptyBoard() {
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                int color = (x + y) % 2;
                board[y][x] = new Square(color, x, y);
                this.add(board[y][x]);
            }
        }
    }

    public King[] setupStandardPosition() {
        return PieceFactory.createStandardSetup(board, whitePieces, blackPieces);
    }


    private void registerInputListeners() {
        BoardMouseHandler handler = new BoardMouseHandler(this, client);
        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
    }

    private void configureBoardSize() {
        Dimension size = new Dimension(400, 400);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        this.setSize(size);
    }


    private void initializeBoardSquares() {
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                int color = (x + y + 1) % 2;
                board[y][x] = new Square(color, x, y);
                this.add(board[y][x]);
            }
        }
    }


    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return gameController.isWhiteTurn(); // Now returns boolean ✅
    }

    public void setCurrPiece(Piece p) {
        gameController.setCurrentPiece(p);
    }

    public Piece getCurrPiece() {
        return gameController.getCurrentPiece();
    }


    public GameController getGameController() {
        return gameController;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void setDragCoordinates(int x, int y) {
        this.dragX = x;
        this.dragY = y;
    }

    @Override
    public void paintComponent(Graphics g) {
        renderer.render(g, board, getCurrPiece(), getTurn(), dragX, dragY);
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setGameWindow(GameWindow window) {
        this.gameWindow = window;
    }


}
