package client.controller;



import client.view.Board;
import shared.model.Piece;
import shared.model.Square;
import shared.model.pieces.Bishop;
import shared.model.pieces.King;
import shared.model.pieces.Queen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Component of the Chess game that detects check mates in the game.
 * 
 * @author Jussi Lundstedt
 *
 */
public class CheckmateDetector {
    private final Board board;
    private final King whiteKing;
    private final King blackKing;
    private final LinkedList<Piece> whitePieces;
    private final LinkedList<Piece> blackPieces;
    private final List<Square> movableSquares;
    private final List<Square> allSquares;
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

        allSquares = new LinkedList<>();
        movableSquares = new LinkedList<>();
        whiteThreatMap = new HashMap<>();
        blackThreatMap = new HashMap<>();

        initializeThreatMap();
        update();
    }

    public void update() {
        whiteThreatMap.values().forEach(List::clear);
        blackThreatMap.values().forEach(List::clear);
        movableSquares.clear();

        updateThreatMap(whitePieces, whiteThreatMap);
        updateThreatMap(blackPieces, blackThreatMap);
    }

    public boolean blackInCheck() {
        update();
        return isInCheck(blackKing, whiteThreatMap);
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
        movableSquares.clear();

        if (isWhiteTurn) {
            if (isInCheck(whiteKing, blackThreatMap)) {
                isCheckmate(whiteKing, blackThreatMap, whiteThreatMap);
            }
        } else {
            if (isInCheck(blackKing, whiteThreatMap)) {
                isCheckmate(blackKing, whiteThreatMap, blackThreatMap);
            }
        }

        return movableSquares;
    }


    public boolean testMove(Piece piece, Square targetSquare) {
        Square originalSquare = piece.getPosition();
        Piece capturedPiece = targetSquare.getOccupyingPiece();
        moveService.applyMove(piece, targetSquare, board);
        update();

        boolean isSafeMove = true;
        if (piece.getColor() == 0 && isInCheck(blackKing, whiteThreatMap)) {
            isSafeMove = false;
        } else if (piece.getColor() == 1 && isInCheck(whiteKing, blackThreatMap)) {
            isSafeMove = false;
        }


        moveService.applyMove(piece, originalSquare, board);
        if (capturedPiece != null) {
            targetSquare.setOccupyingPiece(capturedPiece);

        }

        update();

        return isSafeMove;
    }

    private void initializeThreatMap() {
        Square[][] squaresArray = board.getSquareArray();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                allSquares.add(squaresArray[y][x]);
                whiteThreatMap.put(squaresArray[y][x], new LinkedList<Piece>());
                blackThreatMap.put(squaresArray[y][x], new LinkedList<Piece>());
            }
        }
    }

    private void updateThreatMap(List<Piece> pieces, Map<Square, List<Piece>> threatMap) {
        pieces.removeIf(p -> p.getPosition() == null);

        for (Piece p : pieces) {
            if (!(p instanceof King)) {
                List<Square> legalMoves = p.getLegalMoves(board);
                for (Square sq : legalMoves) {
                    threatMap.get(sq).add(p);
                }
            }
        }
    }

    private boolean isInCheck(King king, Map<Square, List<Piece>> opponentThreats) {
        Square kingSquare = king.getPosition();
        boolean inCheck = !opponentThreats.getOrDefault(kingSquare, List.of()).isEmpty();
        if (!inCheck) {
            movableSquares.addAll(allSquares);
        }
        return inCheck;
    }

    private boolean isCheckmate(King king, Map<Square, List<Piece>> opponentThreats, Map<Square, List<Piece>> allyMoves) {
        if (!isInCheck(king, opponentThreats)) return false;

        if (canEvade(opponentThreats, king)) return false;

        List<Piece> threats = opponentThreats.get(king.getPosition());
        if (canCapture(allyMoves, threats, king)) return false;

        if (canBlock(threats, allyMoves, king)) return false;

        return true;
    }

    private boolean canEvade(Map<Square, List<Piece>> opponentThreats, King king) {
        List<Square> legalMoves = king.getLegalMoves(board);
        boolean canEvade = false;

        for (Square move : legalMoves) {
            if (testMove(king, move) && opponentThreats.get(move).isEmpty()) {
                movableSquares.add(move);
                canEvade = true;
            }
        }

        return canEvade;
    }

    private boolean canCapture(Map<Square, List<Piece>> allies, List<Piece> threats, King king) {
        if (threats.size() != 1) return false;

        Square threatSquare = threats.get(0).getPosition();
        boolean capture = false;


        if (king.getLegalMoves(board).contains(threatSquare) && testMove(king, threatSquare)) {
            movableSquares.add(threatSquare);
            capture = true;
        }

        for (Piece ally : new LinkedList<>(allies.getOrDefault(threatSquare, List.of()))) {
            if (testMove(ally, threatSquare)) {
                movableSquares.add(threatSquare);
                capture = true;
            }
        }

        return capture;
    }

    public boolean whiteStalemated() {

        return !whiteInCheck() && whiteKing.getLegalMoves(board).isEmpty();
    }

    public boolean blackStalemated() {
        return !blackInCheck() && blackKing.getLegalMoves(board).isEmpty();
    }


    private boolean canBlock(List<Piece> threats, Map<Square, List<Piece>> allies, King king) {
        if (threats.size() != 1) return false;

        Square threatSquare = threats.get(0).getPosition();
        Square kingSquare = king.getPosition();

        boolean blocked = false;

        blocked |= canBlockVerticalOrHorizontal(kingSquare, threatSquare, allies);
        blocked |= canBlockDiagonally(kingSquare, threatSquare, threats.get(0), allies);

        return blocked;
    }

    private boolean canBlockVerticalOrHorizontal(Square king, Square threat, Map<Square, List<Piece>> allies) {
        boolean blocked = false;
        Square[][] boardArray = board.getSquareArray();

        int kx = king.getXNum(), ky = king.getYNum();
        int tx = threat.getXNum(), ty = threat.getYNum();

        if (kx == tx) {
            int min = Math.min(ky, ty), max = Math.max(ky, ty);
            for (int y = min + 1; y < max; y++) {
                Square sq = boardArray[y][kx];
                blocked |= tryBlockSquare(sq, allies);
            }
        }

        if (ky == ty) {
            int min = Math.min(kx, tx), max = Math.max(kx, tx);
            for (int x = min + 1; x < max; x++) {
                Square sq = boardArray[ky][x];
                blocked |= tryBlockSquare(sq, allies);
            }
        }

        return blocked;
    }

    private boolean canBlockDiagonally(Square king, Square threat, Piece threatPiece, Map<Square, List<Piece>> allies) {
        if (!(threatPiece instanceof Queen) && !(threatPiece instanceof Bishop)) return false;

        boolean blocked = false;
        Square[][] boardArray = board.getSquareArray();

        int kx = king.getXNum(), ky = king.getYNum();
        int tx = threat.getXNum(), ty = threat.getYNum();

        int dx = Integer.compare(tx, kx);
        int dy = Integer.compare(ty, ky);

        int x = kx + dx, y = ky + dy;
        while (x != tx && y != ty) {
            Square sq = boardArray[y][x];
            blocked |= tryBlockSquare(sq, allies);
            x += dx;
            y += dy;
        }

        return blocked;
    }

    private boolean tryBlockSquare(Square square, Map<Square, List<Piece>> allies) {
        boolean blockPossible = false;
        for (Piece ally : new LinkedList<>(allies.getOrDefault(square, List.of()))) {
            if (testMove(ally, square)) {
                movableSquares.add(square);
                blockPossible = true;
            }
        }
        return blockPossible;
    }

}