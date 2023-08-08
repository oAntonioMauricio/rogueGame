package pt.upskill.projeto1.leaderboard;

import java.io.Serializable;
import java.util.Arrays;

public class LeaderBoard implements Serializable {

    private Score[] leaderboard;

    public LeaderBoard() {
        this.leaderboard = new Score[]{
                new Score("Rute", 5),
                new Score("António", 4),
                new Score("Francisca", 3),
                new Score("Floribela", 2),
                new Score("Fred", 1)};
    }

    public Score[] getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Score[] leaderboard) {
        this.leaderboard = leaderboard;
    }

    @Override
    public String toString() {
        String text = "";

        for (Score score : leaderboard) {
            text = text + score.toString() + System.getProperty("line.separator");
        }

        return text;
    }
}
