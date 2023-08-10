package pt.upskill.projeto1.leaderboard;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class LeaderBoard implements Serializable {

    private Score[] leaderboard;

    public LeaderBoard() {
        this.leaderboard = new Score[]{
                new Score("No record", 0),
                new Score("No record", 0),
                new Score("No record", 0),
                new Score("No record", 0),
                new Score("No record", 0)};
    }

    public Score[] getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Score[] leaderboard) {
        this.leaderboard = leaderboard;
    }

    public void reset() {
        try {
            FileOutputStream fileOut = new FileOutputStream("scores/scores.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new LeaderBoard());
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Erro a salvar o leaderboard!");
        }
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
