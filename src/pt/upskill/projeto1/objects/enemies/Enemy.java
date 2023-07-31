package pt.upskill.projeto1.objects.enemies;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.objects.Wall;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.rogue.utils.Vector2D;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Enemy implements ImageTile {

    private String test = "Hey;";

    public abstract int getPower();

    public abstract void setPosition(Position position);

    public void move(Position nextPosition) {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        boolean move = true;

        for (ImageTile tile : tiles) {
            if (nextPosition.getX() == tile.getPosition().getX() && nextPosition.getY() == tile.getPosition().getY()) {
                if (tile instanceof Wall || tile instanceof Door || tile instanceof Enemy) {
                    move = !move;
                }
            }
        }

        if (move) {
            setPosition(nextPosition);
        }
    }

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
                if (xHero > xEnemy) {
                    return Direction.RIGHT.asVector();
                } else {
                    return Direction.LEFT.asVector();
                }
            }
            // if enemy on the same x-axis
            else if (xEnemy == xHero) {
                if (yHero < yEnemy) {
                    return Direction.UP.asVector();
                } else {
                    return Direction.DOWN.asVector();
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

}
