package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.List;

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

    public void setPosition(Position position) {
        /*
        boolean move = true;

        for (ImageTile tile : tileList) {
            if (position.getX() == tile.getPosition().getX() && position.getY() == tile.getPosition().getY()) {
                if (tile instanceof Wall) {
                    move = false;
                    break;
                }
            }
        }

        if (move) {
            this.position = position;
        }

         */

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
