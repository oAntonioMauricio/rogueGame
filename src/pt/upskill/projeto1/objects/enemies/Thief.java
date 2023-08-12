package pt.upskill.projeto1.objects.enemies;

import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.rogue.utils.Vector2D;

import java.util.IllegalFormatCodePointException;
import java.util.concurrent.ThreadLocalRandom;

public class Thief extends Enemy {
    private Position position;
    private int initialHealth = 50;
    private int health = 50;
    private int points = 75;

    public Thief(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Thief";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public double getPower() {
        return this.power * 2;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getInitialHealth() {
        return this.initialHealth;
    }

    @Override
    public void setHealth(int newHealth) {
        this.health = newHealth;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }


    // specific thief movement
    @Override
    public Vector2D moveToHero(Hero hero) {
        System.out.println("Thief mov");

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
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 1 + 1);
                    Vector2D directionToMove;

                    // randomize directionToMove
                    if (randomNum == 1) {// right
                        directionToMove = Direction.RIGHT.asVector().plus(Direction.UP.asVector());
                    } else {
                        directionToMove = Direction.RIGHT.asVector().plus(Direction.DOWN.asVector());
                    }

                    return directionToMove;
                } else {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 1 + 1);
                    Vector2D directionToMove;

                    // randomize directionToMove
                    if (randomNum == 1) {// right
                        directionToMove = Direction.LEFT.asVector().plus(Direction.UP.asVector());
                    } else {
                        directionToMove = Direction.LEFT.asVector().plus(Direction.DOWN.asVector());
                    }

                    return directionToMove;
                }
            }

            // if enemy on the same x-axis
            else if (xEnemy == xHero) {
                if (yEnemy < yHero) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 1 + 1);
                    Vector2D directionToMove;

                    // randomize directionToMove
                    if (randomNum == 1) {// right
                        directionToMove = Direction.LEFT.asVector().plus(Direction.DOWN.asVector());
                    } else {
                        directionToMove = Direction.RIGHT.asVector().plus(Direction.DOWN.asVector());
                    }

                    return directionToMove;
                } else {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 1 + 1);
                    Vector2D directionToMove;

                    // randomize directionToMove
                    if (randomNum == 1) {// right
                        directionToMove = Direction.LEFT.asVector().plus(Direction.UP.asVector());
                    } else {
                        directionToMove = Direction.RIGHT.asVector().plus(Direction.UP.asVector());
                    }

                    return directionToMove;
                }
            }

            // if enemy not on x-axis or y-axis
            else {
                if (yEnemy < yHero) {
                    if (xEnemy < xHero) {
                        return Direction.DOWN.asVector().plus(Direction.RIGHT.asVector());
                    } else {
                        return Direction.DOWN.asVector().plus(Direction.LEFT.asVector());
                    }
                } else {
                    if (xEnemy < xHero) {
                        return Direction.UP.asVector().plus(Direction.RIGHT.asVector());
                    } else {
                        return Direction.UP.asVector().plus(Direction.LEFT.asVector());
                    }
                }
            }
        }

        // move random if not close
        return randomPosition();
    }

    @Override
    public Vector2D randomPosition() {

        int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
        Vector2D directionToMove;

        // randomize directionToMove
        switch (randomNum) {
            case 1:
                // right
                directionToMove = Direction.RIGHT.asVector().plus(Direction.UP.asVector());
                break;
            case 2:
                directionToMove = Direction.RIGHT.asVector().plus(Direction.DOWN.asVector());
                break;
            case 3:
                directionToMove = Direction.LEFT.asVector().plus(Direction.UP.asVector());
                break;
            default:
                directionToMove = Direction.LEFT.asVector().plus(Direction.DOWN.asVector());
        }

        return directionToMove;
    }
}
