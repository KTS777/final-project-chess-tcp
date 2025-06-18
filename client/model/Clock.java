package model;

public class Clock {
    private int hh;
    private int mm;
    private int ss;

    public Clock(int hh, int mm, int ss) {
        this.hh = hh;
        this.mm = mm;
        this.ss = ss;
    }

    public boolean outOfTime() {
        return hh == 0 && mm == 0 && ss == 0;
    }

    public void decr() {
        if (mm == 0 && ss == 0) {
            ss = 59;
            mm = 59;
            hh--;
        } else if (ss == 0) {
            ss = 59;
            mm--;
        } else {
            ss--;
        }
    }

    public String getTime() {
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    @Override
    public String toString() {
        return getTime();
    }
}
