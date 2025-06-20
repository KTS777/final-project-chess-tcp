package controller;

import model.Piece;
import model.Square;
import model.pieces.King;
import view.Board;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class CheckmateDetector {
    private final Board board;
    private final King whiteKing;
    private final King blackKing;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private final List<Square> movableSquares;
    private final Map<Square, List<Piece>> whiteThreatMap;
    private final Map<Square, List<Piece>> blackThreatMap;
    private final MoveService moveService = new MoveService();

    public CheckmateDetector(Board board,
                             LinkedList<Piece> whitePieces,
                             LinkedList<Piece> blackPieces,
                             King whiteKing,
                             King blackKing) {
        this.board = board;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;

        movableSquares = new LinkedList<>();
        whiteThreatMap = new HashMap<>();
        blackThreatMap = new HashMap<>();
        initializeThreatMap();
        update();
    }

    public void update() {
        whiteThreatMap.values().forEach(List::clear);
        blackThreatMap.values().forEach(List::clear);
        updateThreatMap(whitePieces, whiteThreatMap);
        updateThreatMap(blackPieces, blackThreatMap);
    }

    public boolean blackInCheck() {
        update();
        Square pos = blackKing.getPosition();
        System.out.println("[CHECK] Black King at: " + pos);

        List<Piece> threats = whiteThreatMap.getOrDefault(pos, List.of());
        System.out.println("[CHECK] Threats to black king: " + threats.size());
        for (Piece p : threats) {
            System.out.println("    ↳ " + p.getClass().getSimpleName() +
                    " at " + p.getPosition().getXNum() + "," + p.getPosition().getYNum());
        }

        return !threats.isEmpty();
    }


    public boolean whiteInCheck() {
        update();
        return isInCheck(whiteKing, blackThreatMap);
    }

    public boolean blackCheckMated() {
        return isCheckmate(blackKing, whiteThreatMap, blackThreatMap);
    }

    public boolean whiteCheckMated() {
        return isCheckmate(whiteKing, blackThreatMap, whiteThreatMap);
    }

    public List<Square> getAllowableSquares(boolean isWhiteTurn) {
        update();
        movableSquares.clear();

        List<Piece> pieces = isWhiteTurn ? whitePieces : blackPieces;
        for (Piece p : pieces) {
            if (p.getPosition() == null) continue;
            for (Square target : p.getLegalMoves(board)) {
                if (testMove(p, target)) {
                    movableSquares.add(board.getSquare(target.getXNum(), target.getYNum()));
                }
            }
        }

        return movableSquares;
    }

    public boolean testMove(Piece piece, Square targetSquare) {
        Square original = piece.getPosition();
        Piece captured = targetSquare.getOccupyingPiece();
        Square target = board.getSquare(targetSquare.getXNum(), targetSquare.getYNum());

        board.getSquare(original.getXNum(), original.getYNum()).removePiece();
        target.setOccupyingPiece(piece);
        piece.setPosition(target);

        update();

        System.out.println("[TEST] Simulating move: " + piece.getClass().getSimpleName() +
                " (" + (piece.isWhite() ? "White" : "Black") + ") → " + target.getXNum() + "," + target.getYNum());

        boolean safe;
        if (piece instanceof King) {
            safe = getOpponentThreatMap(piece.getColor())
                    .getOrDefault(target, List.of())
                    .isEmpty();
        } else {
            safe = !(piece.getColor() == GameController.WHITE ? whiteInCheck() : blackInCheck());
        }

        if (!safe) {
            System.out.println("[BLOCKED] Move blocked because " +
                    (piece.getColor() == GameController.WHITE ? "White" : "Black") +
                    " king would be in check.");
        } else {
            System.out.println("[ALLOWED] Move allowed for " +
                    piece.getClass().getSimpleName() + " to " +
                    target.getXNum() + "," + target.getYNum());
        }

        target.setOccupyingPiece(captured);
        board.getSquare(original.getXNum(), original.getYNum()).setOccupyingPiece(piece);
        piece.setPosition(original);
        update();

        return safe;
    }



    private Map<Square, List<Piece>> getOpponentThreatMap(int color) {
        return color == GameController.WHITE ? blackThreatMap : whiteThreatMap;
    }

    private void initializeThreatMap() {
        for (Square[] row : board.getSquareArray()) {
            for (Square sq : row) {
                whiteThreatMap.put(board.getSquare(sq.getXNum(), sq.getYNum()), new LinkedList<>());
                blackThreatMap.put(board.getSquare(sq.getXNum(), sq.getYNum()), new LinkedList<>());
            }
        }
    }

    private void updateThreatMap(List<Piece> pieces, Map<Square, List<Piece>> map) {
        for (Piece p : pieces) {
            Square pos = p.getPosition();
            if (pos == null) {
                System.out.println("[SKIP] " + p.getClass().getSimpleName() + " has no position.");
                continue;
            }

            List<Square> moves = p.getLegalMoves(board);
            System.out.println("[DEBUG] " + p.getClass().getSimpleName() + " (" +
                    (p.isWhite() ? "White" : "Black") + ") threatens " +
                    moves.size() + " squares.");

            for (Square sq : moves) {
                Square key = board.getSquare(sq.getXNum(), sq.getYNum());
                if (!map.containsKey(key)) {
                    System.out.println("[WARN] Threat map missing square: " + key.getXNum() + "," + key.getYNum());
                    continue;
                }
                map.get(key).add(p);
            }
        }
    }


    private boolean isInCheck(King king, Map<Square, List<Piece>> opponents) {
        Square kingPos = king.getPosition();

        if (kingPos == null) {
            System.out.println("[ERROR] King has null position.");
            return false;
        }

        Square boardSquare = board.getSquare(kingPos.getXNum(), kingPos.getYNum());
        List<Piece> threats = opponents.getOrDefault(boardSquare, List.of());

        if (!threats.isEmpty()) {
            System.out.println("[CHECK] " + (king.isWhite() ? "White" : "Black") + " King is in check by:");
            for (Piece attacker : threats) {
                System.out.println("    - " + attacker.getClass().getSimpleName() +
                        " at " + attacker.getPosition().getXNum() + "," +
                        attacker.getPosition().getYNum());
            }
        }

        return !threats.isEmpty();
    }


    private boolean isCheckmate(King king,
                                Map<Square, List<Piece>> opponents,
                                Map<Square, List<Piece>> allies) {
        if (!isInCheck(king, opponents)) return false;
        return getAllowableSquares(king.getColor() == GameController.WHITE).isEmpty();
    }
}
