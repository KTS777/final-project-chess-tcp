package client.view;



import client.network.ChessClient;
import shared.model.Square;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BoardMouseHandler implements MouseListener, MouseMotionListener {

    private final Board board;
    private final ChessClient client;
    private Square fromSquare;
    private Square toSquare;



    public BoardMouseHandler(Board board, ChessClient client) {
        this.board = board;
        this.client = client;
    }

    private String getAlgebraic(Square square) {
        char file = (char) ('a' + square.getXNum());
        int rank = 8 - square.getYNum();
        return "" + file + rank;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (!board.getGameController().isMyTurn()) return;

        board.setDragCoordinates(e.getX(), e.getY());
        fromSquare = (Square) board.getComponentAt(new Point(e.getX(), e.getY()));
        board.getGameController().selectPiece(fromSquare);
        board.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        toSquare = (Square) board.getComponentAt(new Point(e.getX(), e.getY()));
        boolean moveHappened = board.getGameController().handlePieceDrop(toSquare);

        if (moveHappened && fromSquare != null && toSquare != null) {
            String move = getAlgebraic(fromSquare) + getAlgebraic(toSquare);  // e.g., e2e4
            client.sendMessage(move);
        }


        if (board.getGameController().isGameOver()) {
            board.removeMouseListener(this);
            board.removeMouseMotionListener(this);
            board.getGameWindow().checkmateOccurred(board.getGameController().getWinningColor());
        }

        board.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        board.setDragCoordinates(e.getX() - 24, e.getY() - 24);
        board.repaint();
    }


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
