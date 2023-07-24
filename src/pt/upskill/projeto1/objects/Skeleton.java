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
    public void setPosition(Position position, List<ImageTile> tileList) {
        boolean move = true;

        for (ImageTile tile : tileList) {
            if (position.getX() == tile.getPosition().getX() && position.getY() == tile.getPosition().getY()) {
                if (Objects.equals(tile.getName(), "Wall")) {
                    move = !move;
                }
            }
        }

        if (move) {
            this.position = position;
        }
    }

}
