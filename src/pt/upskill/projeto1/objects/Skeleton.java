package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.rogue.utils.Vector2D;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Skeleton extends Enemy {
    private Position position;
    private int power;

    public Skeleton(Position position, int power) {
        this.position = position;
        this.power = power;
    }

    @Override
    public String getName() {
        return "Skeleton";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Boolean isPossibleToWalk() {
        return true;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
