package pt.upskill.projeto1.objects.hero;

import pt.upskill.projeto1.game.AttackThread;
import pt.upskill.projeto1.game.FireBallThread;
import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.props.StatueNormal;
import pt.upskill.projeto1.objects.props.StatueNormalCity;
import pt.upskill.projeto1.objects.props.Wall;
import pt.upskill.projeto1.objects.props.WallCity;
import pt.upskill.projeto1.objects.props.attack.Attack;
import pt.upskill.projeto1.objects.statusbar.Black;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.objects.statusbar.StatusBar;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;
import java.util.*;

public class Hero implements ImageTile, Serializable {

    // 🟩 Attributes
    private Position position;
    private Position previousPosition;
    private int power = 25;
    // hero health can drop from 12.5 * n
    private double health = 100.0;

    // 🟩 Constructor
    public Hero() {
        this.position = new Position(8, 8);
    }

    // 🟩 Methods
    public void move(Position nextPosition) {
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        boolean move = true;

        // block  hero move in certain spots
        for (ImageTile tile : tiles) {
            if (nextPosition.isItSamePosition(tile.getPosition())) {
                if (tile instanceof Wall || tile instanceof WallCity || tile instanceof StatueNormal || tile instanceof StatueNormalCity) {
                    move = false;
                    break;
                }
            }
        }

        if (move) {
            setPosition(nextPosition);
        }

    }

    public ArrayList<ImageTile> checkWhereHeroIs() {
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        // create interactions list
        ArrayList<ImageTile> interactions = new ArrayList<>();

        for (ImageTile tile : tiles) {
            if (Objects.equals(tile.getName(), "Hero") || Objects.equals(tile.getName(), "Floor") || Objects.equals(tile.getName(), "FloorCity")) {
                continue;
            }

            if (getPosition().getX() == tile.getPosition().getX() && getPosition().getY() == tile.getPosition().getY()) {
                interactions.add(tile);
            }
        }

        // reverse to deal with enemies first
        Collections.reverse(interactions);

        // check interactions on console
        for (ImageTile inter : interactions) {
            System.out.println("hero is on top of: " + inter.getName());
        }

        return interactions;

    }

    public void fight(Enemy enemyToFight) {
        // singletons 🔽
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

        // deal dmg
        enemyToFight.setHealth(enemyToFight.getHealth() - getPower());
        System.out.println("You dealt " + getPower() + " damage." + enemyToFight.getName() + " HP left: " + enemyToFight.getHealth());
        gui.setStatus("You dealt " + getPower() + " damage. Enemy HP left: " + enemyToFight.getHealth());

        // animation
        Direction direction = Direction.UP;
        Attack attack = new Attack(enemyToFight.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector())));
        AttackThread animationAttack = new AttackThread(direction, attack);
        gui.addImage(attack);
        animationAttack.start();

        // recoil
        move(getPreviousPosition());


        // win fight
        if (enemyToFight.getHealth() <= 0) {
            // kill enemy after fight
            enemyToFight.death(enemyToFight.getInitialHealth());
        }
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

    // 🟩 Getters
    @Override
    public String getName() {
        return "Hero";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public int getPower() {
        return power;
    }

    public double getHealth() {
        return health;
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    // 🟩 Setters
    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPreviousPosition() {
        this.previousPosition = getPosition();
    }

    public void setHealth(double newHealth) {
        this.health = Math.min(newHealth, 100);
    }

    public void setPower(int power) {
        this.power = power;
    }
}
