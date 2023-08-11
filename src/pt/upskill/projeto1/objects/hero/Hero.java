package pt.upskill.projeto1.objects.hero;

import pt.upskill.projeto1.game.FireBallThread;
import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.game.Room;
import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.props.Floor;
import pt.upskill.projeto1.objects.props.Wall;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.statusbar.Black;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Hero implements ImageTile, Serializable {
    // properties 🔽
    private Position position;
    private Position previousPosition;
    private ImageTile onTopOf;
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

    public Position getPreviousPosition() {
        return previousPosition;
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

    public void setPreviousPosition() {
        this.previousPosition = getPosition();
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

    public ImageTile getOnTopOf() {
        return onTopOf;
    }

    public void setOnTopOf(ImageTile onTopOf) {
        this.onTopOf = onTopOf;
    }

    public void checkWhereHeroIs() {
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();
        // get room index // this is here because it's PRIMITIVE
        int roomIndex = gameSingleton.getRoomIndex();

        ImageTile interaction = null;

        for (ImageTile tile : tiles) {
            if (Objects.equals(tile.getName(), "Hero") || Objects.equals(tile.getName(), "Floor")) {
                continue;
            }

            if (getPosition().getX() == tile.getPosition().getX() && getPosition().getY() == tile.getPosition().getY()) {
                System.out.println("hero is on top of: " + tile.getName());
                interaction = tile;
            }
        }

        if (interaction == null) {
            setOnTopOf(new Floor(new Position(0, 0)));
        } else {
            setOnTopOf(interaction);
        }

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
        int initialEnemyHP = enemyToFight.getHealth();

        // deal dmg
        enemyToFight.setHealth(enemyToFight.getHealth() - getPower());
        System.out.println("You dealt " + getPower() + " damage." + enemyToFight.getName() + " HP left: " + enemyToFight.getHealth());
        gui.setStatus("You dealt " + getPower() + " damage. Enemy HP left: " + enemyToFight.getHealth());

        // recoil
        move(getPreviousPosition());

        /*
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

         */

        if (enemyToFight.getHealth() <= 0) {
            // win fight
            // kill enemy after fight
            enemyToFight.death(initialEnemyHP);
        }

        /*
        else {
            // remove hero from the game
            setPosition(new Position(-1, -1));
            tiles.remove(this);
            gui.removeImage(this);
            gui.setStatus("You died in the fight." + " The enemy had " + initialEnemyHP + " HP at the start and " + enemyToFight.getPower() + " power.");
        }

         */
    }

    public void moveAwayFromTheDoor() {
        // move 1 step away from the door
        if (getPosition().getY() == 9) {
            move(getPosition().plus(Objects.requireNonNull(Direction.UP.asVector())));
        } else if (getPosition().getY() == 0) {
            move(getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector())));
        } else if (getPosition().getX() == 0) {
            move(getPosition().plus(Objects.requireNonNull(Direction.RIGHT.asVector())));
        } else if (getPosition().getX() == 9) {
            move(getPosition().plus(Objects.requireNonNull(Direction.LEFT.asVector())));
        }
    }

    public void sendFireball(Direction direction) {
        // get singleton
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        Hero hero = gameSingleton.getHero();
        StatusBar statusBar = gameSingleton.getStatusBar();

        ImageTile[] fireballs = statusBar.getFireballsArray();

        for (int i = 0; i < fireballs.length; i++) {
            if (fireballs[i] instanceof Fire) {
                gui.setStatus("Sending fireball!");

                ((Fire) fireballs[i]).setPosition(hero.getPosition());
                FireBallThread fireball = new FireBallThread(direction, (FireTile) fireballs[i]);
                gui.addImage(fireballs[i]);
                fireball.start();

                fireballs[i] = new Black(new Position(i, 0));
                break;
            }
        }

        // update ui at the end
        statusBar.updateStatus();
    }

    public void death() {
        // singletons 🔽
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        // remove hero from the game
        setPosition(new Position(-1, -1));
        tiles.remove(this);
        gui.removeImage(this);
        gui.setStatus("You died.");
    }

    public void loadHero(Hero savedHero) {
        this.position = savedHero.getPosition();
        this.health = savedHero.getHealth();
        this.power = savedHero.getPower();
    }

}
