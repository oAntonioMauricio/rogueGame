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
        int xEnemy = getPosition().getX();
        int yEnemy = getPosition().getY();
        int xHero = hero.getPosition().getX();
        int yHero = hero.getPosition().getY();

        int subtractX = xEnemy - xHero;
        int subtractY = yEnemy - yHero;

        if (subtractX <= 2 && subtractX >= -2 && subtractY <= 2 && subtractY >= -2) {
            System.out.println("close!");

            if (yEnemy == yHero && xEnemy == xHero) {
                return new Vector2D(0, 0);
            }
            // if enemy on the same y-axis
            if (yEnemy == yHero) {
                if (xEnemy < xHero) {
                    return Direction.RIGHT.asVector();
                } else {
                    return Direction.LEFT.asVector();
                }
            }
            // if enemy on the same x-axis
            else if (xEnemy == xHero) {
                if (yEnemy < yHero) {
                    return Direction.DOWN.asVector();
                } else {
                    return Direction.UP.asVector();
                }
            }
            // if enemy not on x-axis or y-axis
            else {
                if (xEnemy < xHero) {
                    return Direction.RIGHT.asVector();
                } else {
                    return Direction.LEFT.asVector();
                }
            }
        }

        // move random if not close
        return randomPosition();
    }

    ;

}
