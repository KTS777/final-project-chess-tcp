package view;

import model.Square;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BoardMouseHandler implements MouseListener, MouseMotionListener {

    private final Board board;

    public BoardMouseHandler(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        board.setDragCoordinates(e.getX(), e.getY());

        Square sq = (Square) board.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {
            if (!board.getGameController().isCorrectPlayerTurn(sq.getOccupyingPiece())) {
                System.out.println("It's not " + (sq.getOccupyingPiece().isWhite() ? "White" : "Black") + "'s turn.");
                return; // Ignore click on wrong color piece
            }
        }

        board.getGameController().selectPiece(sq);
        board.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Square sq = (Square) board.getComponentAt(new Point(e.getX(), e.getY()));
        boolean moveHappened = board.getGameController().handlePieceDrop(sq);

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
