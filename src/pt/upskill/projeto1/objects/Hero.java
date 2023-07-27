package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.game.Room;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.items.Item;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.List;
import java.util.Objects;

public class Hero implements ImageTile {

    private Position position;
    private int power;
    private int health = 100;

    public Hero(Position position, int power) {
        this.position = position;
        this.power = power;
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
        this.health = health;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
