package pt.upskill.projeto1.objects.hero;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.game.Room;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Wall;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hero implements ImageTile, Serializable {
    // properties 🔽
    private Position position;
    private int power = 25;
    private int health = 100;

    public Hero() {
        this.position = new Position(8, 8);
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

    public void move(Position nextPosition) {
        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();

        boolean move = true;

        for (ImageTile tile : gameSingleton.getTiles()) {
            if (nextPosition.getX() == tile.getPosition().getX() && nextPosition.getY() == tile.getPosition().getY()) {
                if (tile instanceof Wall) {
                    move = false;
                    break;
                }
            }
        }

        if (move) {
            setPosition(nextPosition);
        }
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getPower() {
        return power;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int newHealth) {
        if (newHealth > 100) {
            this.health = 100;
        } else {
            this.health = newHealth;
        }
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void fight(Enemy enemyToFight) {
        // singletons 🔽
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();
        List<Room> roomList = gameSingleton.getRoomList();

        // get room index // this is here because it's PRIMITIVE
        int roomIndex = gameSingleton.getRoomIndex();

        // values before fight
        int enemyHP = enemyToFight.getHealth();

        while (enemyToFight.getHealth() > 0 && getHealth() > 0) {
            // deal dmg
            enemyToFight.setHealth(enemyToFight.getHealth() - getPower());
            System.out.println("You dealt " + getPower() + " damage. Enemy HP left: " + enemyToFight.getHealth());

            if (enemyToFight.getHealth() > 0) {
                System.out.println("Enemy attack!");
                setHealth(getHealth() - enemyToFight.getPower());
                System.out.println(enemyToFight.getName() + " attacked you. You lost: " + enemyToFight.getPower() + " health.");
            }
        }

        if (enemyToFight.getHealth() <= 0) {
            gui.setStatus("You killed " + enemyToFight.getName() + ". The enemy had " + enemyHP + " HP at the start and " + enemyToFight.getPower() + " power.");
            // delete after fight
            enemyToFight.death();
        } else {
            // remove hero from the game
            setPosition(new Position(-1,-1));
            tiles.remove(this);
            gui.removeImage(this);
            gui.setStatus("You died in the fight." + " The enemy had " + enemyHP + " HP at the start and " + enemyToFight.getPower() + " power.");
        }
    }

}
