package client.controller;


import client.view.Board;
import shared.model.Piece;
import shared.model.Square;
import shared.model.pieces.King;
import shared.model.pieces.Pawn;
import shared.model.pieces.Queen;
import shared.model.pieces.Rook;

public class MoveService {

    public boolean applyMove(Piece piece, Square destination, Board board) {
        Square origin = piece.getPosition();

        if (piece instanceof King king && !king.wasMoved()) {
            int startX = origin.getXNum();
            int destX = destination.getXNum();
            int y = origin.getYNum();

            if (Math.abs(destX - startX) == 2) {
                boolean kingSide = destX > startX;
                int rookX = kingSide ? 7 : 0;
                int newRookX = kingSide ? destX - 1 : destX + 1;

                Rook rook = (Rook) board.getSquare(rookX, y).getOccupyingPiece();
                Square rookOrigin = board.getSquare(rookX, y);
                Square rookDestination = board.getSquare(newRookX, y);

                rookOrigin.removePiece();
                rookDestination.setOccupyingPiece(rook);
                rook.setPosition(rookDestination);
                rook.setWasMoved(true);
            }
        }

        Piece target = destination.getOccupyingPiece();
        if (target != null) {
            if (target.getColor() == piece.getColor()) {
                return false;
            } else {
                capturePiece(destination, piece, board);
            }
        }

        origin.removePiece();
        destination.setOccupyingPiece(piece);
        piece.setPosition(destination);

        if (piece instanceof Pawn pawn) {
            pawn.setWasMoved(true);

            int y = destination.getYNum();
            if ((pawn.getColor() == 0 && y == 7) || (pawn.getColor() == 1 && y == 0)) {
                Piece promoted = new Queen(pawn.isWhite(), destination,
                        pawn.isWhite() ? "wq.png" : "bq.png");
                destination.setOccupyingPiece(promoted);
                if (pawn.getColor() == 0) {
                    board.getBlackPieces().remove(pawn);
                    board.getBlackPieces().add(promoted);
                } else {
                    board.getWhitePieces().remove(pawn);
                    board.getWhitePieces().add(promoted);
                }
            }
        }

        piece.setWasMoved(true);
        return true;
    }


    public void capturePiece(Square target, Piece attacker, Board board) {
        Piece defender = target.getOccupyingPiece();
        if (defender == null) return;

        if (defender.getColor() == 0) {
            board.getBlackPieces().remove(defender);
        } else {
            board.getWhitePieces().remove(defender);
        }

        target.setOccupyingPiece(attacker);
    }
}
