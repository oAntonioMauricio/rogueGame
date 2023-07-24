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

    @Override
    public Vector2D randomPosition() {

        int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        Direction directionToMove;

        // randomize directionToMove
        switch (randomNum) {
            case 1:
                directionToMove = Direction.RIGHT;
                break;
            case 2:
                directionToMove = Direction.DOWN;
                break;
            case 3:
                directionToMove = Direction.LEFT;
                break;
            default:
                directionToMove = Direction.UP;
        }

        return directionToMove.asVector();
    }

    @Override
    public Vector2D moveToHero(Hero hero) {

        int xSkeleton = this.position.getX();
        int ySkeleton = this.position.getY();

        int xHero = hero.getPosition().getX();
        int yHero = hero.getPosition().getY();

        int subtractX = xSkeleton - xHero;
        int subtrackY = ySkeleton - yHero;

        if (subtractX == 0 && subtrackY == 0) {
            return new Vector2D(0, 0);
        }

        if (subtractX <= 2 && subtractX >= -2 && subtrackY <= 2 && subtrackY >= -2) {
            System.out.println("close");
        }

        // move random if not close
        return randomPosition();
    }
}
