package view;

import controller.CheckmateDetector;
import controller.GameController;
import model.Piece;
import model.PieceFactory;
import model.Square;
import model.pieces.King;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Board extends JPanel {

    private final Square[][] board;
    private final GameWindow gameWindow;

    private static final int BOARD_SIZE = 8;

    private final LinkedList<Piece> blackPieces;
    private final LinkedList<Piece> whitePieces;

    private int dragX;
    private int dragY;

    private GameController gameController;

    private final BoardRenderer renderer = new BoardRenderer();

    public Board(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        board = new Square[BOARD_SIZE][BOARD_SIZE];
        blackPieces = new LinkedList<>();
        whitePieces = new LinkedList<>();

        setLayout(new GridLayout(8, 8, 0, 0));
        registerInputListeners();

        initializeBoardSquares();
        initializePieces();
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

    private void registerInputListeners() {
        BoardMouseHandler handler = new BoardMouseHandler(this);
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
                int color = (x + y) % 2;
                board[y][x] = new Square(color, x, y);
                this.add(board[y][x]);
            }
        }
    }

    private void initializePieces() {
        King[] kings = PieceFactory.createStandardSetup(board, whitePieces, blackPieces);
        CheckmateDetector cmd = new CheckmateDetector(this, whitePieces, blackPieces, kings[0], kings[1]);
        gameController = new GameController(cmd, this);
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return gameController.isWhiteTurn();
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
}
