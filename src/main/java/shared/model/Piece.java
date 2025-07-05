package shared.model;



import client.view.Board;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class Piece {
    protected boolean isWhite;
    protected boolean wasMoved = false;

    private Square currentSquare;
    private BufferedImage img;
    private static final int BOARD_SIZE = 8;
    private int color;


    public Piece(boolean isWhite, Square initSq, String imgPath) {
        this.isWhite = isWhite;
        this.currentSquare = initSq;
        if (imgPath != null) {
            this.img = loadImage(imgPath);
        }
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Piece[][] board) {
        return false; // or throw new UnsupportedOperationException();
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean wasMoved() {
        return wasMoved;
    }

    public void setWasMoved(boolean moved) {
        this.wasMoved = moved;
    }

    public Square getPosition() {
        return currentSquare;
    }

    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }

    public Image getImage() {
        return img;
    }

    public int getColor() {
        return isWhite ? 1 : 0;
    }


    public void draw(Graphics g) {
        if (currentSquare != null && img != null) {
            int x = currentSquare.getX();
            int y = currentSquare.getY();
            g.drawImage(this.img, x, y, null);
        }
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResource(path));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load image: " + path + " (" + e.getMessage() + ")");
            return null;
        }
    }



    public int[] getLinearOccupations(Square[][] board, int x, int y) {
        return new int[] {
                findLastYAbove(board, x, y),
                findLastYBelow(board, x, y),
                findLastXLeft(board, x, y),
                findLastXRight(board, x, y)
        };
    }

    private int findLastYAbove(Square[][] board, int x, int y) {
        for (int i = y - 1; i >= 0; i--) {
            if (board[i][x].isOccupied()) {
                return board[i][x].getOccupyingPiece().getColor() != this.color ? i : i + 1;
            }
        }
        return 0;
    }

    private int findLastYBelow(Square[][] board, int x, int y) {
        for (int i = y + 1; i < BOARD_SIZE; i++) {
            if (board[i][x].isOccupied()) {
                return board[i][x].getOccupyingPiece().getColor() != this.color ? i : i - 1;
            }
        }
        return BOARD_SIZE - 1;
    }

    private int findLastXLeft(Square[][] board, int x, int y) {
        for (int i = x - 1; i >= 0; i--) {
            if (board[y][i].isOccupied()) {
                return board[y][i].getOccupyingPiece().getColor() != this.color ? i : i + 1;
            }
        }
        return 0;
    }

    private int findLastXRight(Square[][] board, int x, int y) {
        for (int i = x + 1; i < BOARD_SIZE; i++) {
            if (board[y][i].isOccupied()) {
                return board[y][i].getOccupyingPiece().getColor() != this.color ? i : i - 1;
            }
        }
        return BOARD_SIZE - 1;
    }


    public List<Square> getDiagonalOccupations(Square[][] board, int x, int y) {
        List<Square> diagonals = new LinkedList<>();

        // NW
        diagonals.addAll(scanDiagonal(board, x, y, -1, -1));
        // SW
        diagonals.addAll(scanDiagonal(board, x, y, -1, 1));
        // SE
        diagonals.addAll(scanDiagonal(board, x, y, 1, 1));
        // NE
        diagonals.addAll(scanDiagonal(board, x, y, 1, -1));

        return diagonals;
    }

    private List<Square> scanDiagonal(Square[][] board, int startX, int startY, int dx, int dy) {
        List<Square> result = new LinkedList<>();

        int x = startX + dx;
        int y = startY + dy;

        while (x >= 0 && y >= 0 && x < BOARD_SIZE && y < BOARD_SIZE) {
            Square sq = board[y][x];
            if (sq.isOccupied()) {
                if (sq.getOccupyingPiece().getColor() != this.color) {
                    result.add(sq);
                }
                break;
            } else {
                result.add(sq);
            }
            x += dx;
            y += dy;
        }

        return result;
    }

    public String getPGNCode() {
        return switch (this.getClass().getSimpleName()) {
            case "Knight" -> "N";
            case "Bishop" -> "B";
            case "Rook" -> "R";
            case "Queen" -> "Q";
            case "King" -> "K";
            case "Pawn" -> ""; // no code for pawn
            default -> "?";
        };
    }


    public abstract List<Square> getLegalMoves(Board b);

    public abstract String getSymbol();
}