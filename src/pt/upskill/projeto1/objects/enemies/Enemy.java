package pt.upskill.projeto1.objects.enemies;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.game.Room;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.objects.props.Wall;
import pt.upskill.projeto1.objects.items.Item;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.rogue.utils.Vector2D;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Enemy implements ImageTile, Serializable {

    // properties 🔽
    public abstract int getPower();

    public abstract int getHealth();

    public abstract void setHealth(int newHealth);

    public abstract void setPosition(Position position);

    public void move(Position nextPosition) {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        boolean move = true;

        for (ImageTile tile : tiles) {
            if (nextPosition.getX() == tile.getPosition().getX() && nextPosition.getY() == tile.getPosition().getY()) {
                if (tile instanceof Wall || tile instanceof Door || tile instanceof Enemy || tile instanceof Item) {
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

    public void fight(Hero hero) {
        // singletons 🔽
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        // get room index // this is here because it's PRIMITIVE
        int roomIndex = gameSingleton.getRoomIndex();

        int enemyHP = getHealth();

        while (getHealth() > 0 && hero.getHealth() > 0) {
            // deal dmg
            hero.setHealth(hero.getHealth() - getPower());
            System.out.println("Enemy dealt " + getPower() + " damage.");

            if (hero.getHealth() > 0) {
                System.out.println("Hero attack!");
                setHealth(getHealth() - hero.getPower());
                System.out.println("Hero attack and dealt " + hero.getPower() + " damage. Enemy HP left: " + getHealth());
            }
        }

        if (getHealth() <= 0) {
            // hero wins fight
            death();
        } else {
            // remove hero from the game
            gui.setStatus("You died in the fight." + " The enemy had " + enemyHP + " HP at the start and " + getPower() + " power.");
            hero.setPosition(new Position(-1, -1));
            tiles.remove(hero);
            gui.removeImage(hero);
        }


    }

    public void death() {
        // singletons 🔽
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();
        List<Room> roomList = gameSingleton.getRoomList();

        // get room index // this is here because it's PRIMITIVE
        int roomIndex = gameSingleton.getRoomIndex();

        // receive points
        int points = 0;

        switch (getName()) {
            case "Skeleton" -> points = 50;
            case "Bat" -> points = 25;
            case "BadGuy" -> points = 75;
            default -> {
            }
        }

        gameSingleton.setScore(gameSingleton.getScore() + points);

        gui.setStatus("Killed " + getName() + " with " + getHealth() + " HP at the start and " + getPower() + " power. You won " + points + " points.");

        // remove enemy
        roomList.get(roomIndex).getEnemyList().remove(this);
        tiles.remove(this);
        gui.removeImage(this);
    }

}
