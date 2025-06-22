package controller;

import model.Clock;
import view.Board;
import view.GameWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessTimer {
    private final Timer timer;


    public ChessTimer(Board board,
                      Clock whiteClock,
                      Clock blackClock,
                      JLabel whiteLabel,
                      JLabel blackLabel,
                      JFrame parent,
                      String whiteName,
                      String blackName,
                      int hh,
                      int mm,
                      int ss) {

        this.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean turn = board.getTurn();

                if (turn) {
                    whiteClock.decr();
                    whiteLabel.setText(whiteClock.getTime());
                    if (whiteClock.outOfTime()) {
                        handleTimeout(blackName + " wins by time!", blackName, whiteName);
                    }
                } else {
                    blackClock.decr();
                    blackLabel.setText(blackClock.getTime());
                    if (blackClock.outOfTime()) {
                        handleTimeout(whiteName + " wins by time!", whiteName, blackName);
                    }
                }
            }

            private void handleTimeout(String message, String winner, String opponent) {
                timer.stop();
                int choice = JOptionPane.showConfirmDialog(
                        parent,
                        message + " Play a new game?\nChoosing \"No\" quits the game.",
                        winner + " wins!",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    new GameWindow(opponent, winner, hh, mm, ss, null);
                }
                parent.dispose();
            }
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}