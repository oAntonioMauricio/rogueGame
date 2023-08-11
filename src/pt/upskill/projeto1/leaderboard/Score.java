package pt.upskill.projeto1.leaderboard;

import java.awt.desktop.PrintFilesEvent;
import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable {
    private String playerName;
    private int playerScore;

    public Score(String playerName, int playerScore) {
        this.playerName = playerName;
        this.playerScore = playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    @Override
    public String toString() {
        return "" + playerName + " - " + playerScore;
    }

    @Override
    public int compareTo(Score o) {
        return Integer.compare(o.getPlayerScore(), getPlayerScore());
    }
}
