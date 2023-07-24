package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.rogue.utils.Vector2D;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Enemy implements ImageTile {

    public abstract void setPosition(Position position, List<ImageTile> tileList);

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

    public Vector2D moveToHero(Hero hero) {
        int xSkeleton = getPosition().getX();
        int ySkeleton = getPosition().getY();
        int xHero = hero.getPosition().getX();
        int yHero = hero.getPosition().getY();

        int subtractX = xSkeleton - xHero;
        int subtrackY = ySkeleton - yHero;

        if (subtractX <= 2 && subtractX >= -2 && subtrackY <= 2 && subtrackY >= -2) {
            if (ySkeleton == yHero && xSkeleton == xHero) {
                return new Vector2D(0, 0);
            }
            System.out.println("close!");
            // if enemy on the same y-axis
            if (ySkeleton == yHero) {
                if (xSkeleton < xHero) {
                    return Direction.RIGHT.asVector();
                } else {
                    return Direction.LEFT.asVector();
                }
            }
            // if enemy on the same x-axis
            else if (xSkeleton == xHero) {
                if (ySkeleton < yHero) {
                    return Direction.DOWN.asVector();
                } else {
                    return Direction.UP.asVector();
                }
            }
        }

        // move random if not close
        return randomPosition();
    }

    ;

}
