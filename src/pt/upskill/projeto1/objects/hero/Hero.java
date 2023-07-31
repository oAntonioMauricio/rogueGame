package pt.upskill.projeto1.objects.hero;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Wall;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Hero implements ImageTile {

    private Position position;
    private int power;
    private int health = 100;

    public Hero() {
        this.position = new Position(8, 8);
        this.power = 100;
    }

    @Override
    public String getName() {
        return "Hero";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Boolean isPossibleToWalk() {
        return true;
    }


    public void move(Position nextPosition) {
        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();

        boolean move = true;

        for (ImageTile tile : gameSingleton.getTiles()) {
            if (nextPosition.getX() == tile.getPosition().getX() && nextPosition.getY() == tile.getPosition().getY()) {
                if (tile instanceof Wall) {
                    move = false;
                    break;
                }
            }
        }

        if (move) {
            setPosition(nextPosition);
        }
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getPower() {
        return power;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > 100) {
            this.health = 100;
        } else {
            this.health = health;
        }
    }

    public void setPower(int power) {
        this.power = power;
    }

}
