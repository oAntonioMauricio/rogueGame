package pt.upskill.projeto1.objects.enemies;

import pt.upskill.projeto1.game.AttackThread;
import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.game.Room;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.objects.props.Trap;
import pt.upskill.projeto1.objects.props.Wall;
import pt.upskill.projeto1.objects.props.attack.EnemyAttack;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.rogue.utils.Vector2D;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Enemy implements ImageTile, Serializable {
    // 🟩 Attributes
    private Position previousPosition;
    protected double power = 12.5;

    // 🟩 Methods
    public void move(Position nextPosition) {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        boolean move = true;

        for (ImageTile tile : tiles) {
            if (nextPosition.getX() == tile.getPosition().getX() && nextPosition.getY() == tile.getPosition().getY()) {
                if (tile instanceof Wall || tile instanceof Door || tile instanceof Enemy || tile instanceof Trap) {
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
        Direction directionToMove = switch (randomNum) {
            case 1 -> Direction.RIGHT;
            case 2 -> Direction.DOWN;
            case 3 -> Direction.LEFT;
            default -> Direction.UP;
        };

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

    public void fight(Hero hero) {
        // singletons 🔽
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

        // deal dmg
        hero.setHealth(hero.getHealth() - getPower());
        System.out.println("Enemy dealt " + getPower() + " damage.");
        gui.setStatus(getName() + " dealt " + getPower() + " damage.");

        // animation
        Direction direction = Direction.UP;
        EnemyAttack attack = new EnemyAttack(hero.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector())));
        AttackThread animationAttack = new AttackThread(direction, attack);
        gui.addImage(attack);
        animationAttack.start();

        // recoil
        move(getPreviousPosition());

        if (hero.getHealth() <= 0) {
            // remove hero from the game
            hero.death();
        }
    }

    public void death(int initialEnemyHP) {
        // singletons 🔽
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();
        List<Room> roomList = gameSingleton.getRoomList();
        // get room index // this is here because it's PRIMITIVE
        int roomIndex = gameSingleton.getRoomIndex();

        // receive points
        gameSingleton.setScore(gameSingleton.getScore() + getPoints());

        // remove enemy
        roomList.get(roomIndex).getEnemyList().remove(this);
        tiles.remove(this);
        gui.removeImage(this);

        // message
        gui.setStatus("Killed " + getName() + " with " + initialEnemyHP + " HP at the start and " + getPower() + " power. You won " + getPoints() + " points.");
    }

    // 🟩 Getters
    public abstract int getHealth();

    public abstract int getInitialHealth();

    // implement getPower by multiplying the protected power attribute in this abstract class
    public abstract double getPower();

    public abstract int getPoints();

    public Position getPreviousPosition() {
        return previousPosition;
    }

    // 🟩 Setters
    public abstract void setHealth(int newHealth);

    public abstract void setPosition(Position position);

    public void setPreviousPosition() {
        this.previousPosition = getPosition();
    }

}
