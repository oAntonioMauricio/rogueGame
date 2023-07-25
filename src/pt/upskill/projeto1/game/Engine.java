package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.*;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Engine {

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init();
    }

    // TODO: colocar um boolean na wall para verificar se pode andar
    // TODO: https://wumbo.net/formulas/distance-between-two-points-2d/
    // test git!

    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
    private List<ImageTile> statusBar = new ArrayList<>();
    private List<ImageTile> tiles = new ArrayList<>();
    private Hero hero = new Hero(new Position(1, 3), 100);
    private List<Enemy> enemyList = new ArrayList<>();

    public void init() {
        gui.setEngine(this);

        // add status bar
        addStatusBackground();
        addStatusInitial();

        // add floor and walls
        addFloorAndWallsOnTiles();
        readFile("rooms/room0.txt");

        // add hero
        tiles.add(this.hero);

        // add the enemies
        enemyList.add(new Skeleton(new Position(1, 1), 30));
        tiles.addAll(enemyList);

        gui.newImages(this.tiles);
        gui.go();

        gui.setStatus("O jogo começou!");

        while (true) {
            gui.update();
        }
    }

    public void readFile(String fileName) {
        try {
            Scanner fileScanner = new Scanner(new File(fileName));

            while (fileScanner.hasNext()) {
                String linha = fileScanner.nextLine();
                if (linha.contains("#")) {
                    continue;
                }

                String[] chars = linha.split("");

                for (int i = 0, x = 0, y = 0; i < 10; i++) {
                    switch (chars[i]) {
                        case "W":
                            tiles.add(new Wall(new Position(x, y)));
                        case "0":
                            tiles.add(new Wall(new Position(x, y)));
                        case "S":
                            tiles.add(new Skeleton(new Position(x, y), 20));
                        case "H":
                            System.out.println("hammer here");
                        default:// code block
                    }
                    x++;
                    if (x == 9) {
                        x = 0;
                        y++;
                    }
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public void notify(int keyPressed) {
        if (keyPressed == KeyEvent.VK_DOWN) {
            System.out.println("User pressed down key!");

            hero.setPosition(hero.getPosition().plus(Direction.DOWN.asVector()), this.tiles);
            turn();
        }
        if (keyPressed == KeyEvent.VK_UP) {
            System.out.println("User pressed up key!");

            hero.setPosition(hero.getPosition().plus(Direction.UP.asVector()), this.tiles);
            turn();
        }
        if (keyPressed == KeyEvent.VK_LEFT) {
            System.out.println("User pressed left key!");

            hero.setPosition(hero.getPosition().plus(Direction.LEFT.asVector()), this.tiles);
            turn();
        }
        if (keyPressed == KeyEvent.VK_RIGHT) {
            System.out.println("User pressed right key!");

            hero.setPosition(hero.getPosition().plus(Direction.RIGHT.asVector()), this.tiles);
            turn();
        }
    }

    public void turn() {
        checkIfHeroOnEnemy(false);

        moveEnemy();
        checkIfHeroOnEnemy(true);

        updateHerohealth();
    }

    public void addStatusBackground() {
        System.out.println("building status background");
        for (int i = 0; i < 10; i++) {
            gui.addStatusImage(new Black(new Position(i, 0)));
        }
    }

    public void addStatusInitial() {
        // fireballs
        for (int i = 0; i < 3; i++) {
            gui.addStatusImage(new Fire(new Position(i, 0)));
        }

        // health
        for (int i = 3; i < 7; i++) {
            gui.addStatusImage(new Green(new Position(i, 0)));
        }
    }

    public void addFloorAndWallsOnTiles() {
        System.out.println("building walls");
        // draw floor
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tiles.add(new Floor(new Position(i, j)));
            }
        }

        // draw walls horizontal
        /*
        for (int i = 0; i < 10; i++) {
            tiles.add(new Wall(new Position(i, 0)));
            tiles.add(new Wall(new Position(i, 9)));
        }

        // draw walls vertical
        for (int i = 0; i < 10; i++) {
            tiles.add(new Wall(new Position(0, i)));
            tiles.add(new Wall(new Position(9, i)));
        }

         */
    }

    public void moveEnemy() {
        // move enemy in random direction
        for (Enemy enemy : enemyList) {
            enemy.setNewPosition(enemy.getPosition().plus(enemy.moveToHero(this.hero)), this.tiles);
        }
    }

    public void checkIfHeroOnEnemy(boolean reverseHeroAndEnemy) {
        boolean removeEnemy = false;
        Enemy toRemove = null;

        if (!reverseHeroAndEnemy) {
            for (Enemy enemy : enemyList) {
                if (this.hero.getPosition().isItSamePosition(enemy.getPosition())) {
                    System.out.println("HIT ON ENEMY!");
                    if (this.hero.getPower() >= enemy.getPower()) {
                        gui.setStatus("You destroyed: " + enemy.getName());
                        removeEnemy = true;
                        toRemove = enemy;
                    }
                }
            }
        }

        if (reverseHeroAndEnemy) {
            for (Enemy enemy : enemyList) {
                if (this.hero.getPosition().isItSamePosition(enemy.getPosition())) {
                    System.out.println("ENEMY ATTACK!");
                    this.hero.setHealth(this.hero.getHealth() - enemy.getPower());
                    System.out.println("Life: " + this.hero.getHealth());

                    gui.setStatus("" + enemy.getName() + " attacked you. You lost: " + enemy.getPower() + " health.");
                    removeEnemy = true;
                    toRemove = enemy;
                }
            }
        }

        if (removeEnemy) {
            System.out.println("enemy removed");
            enemyList.remove(toRemove);
            gui.removeImage(toRemove);
        }
    }

    public void updateHerohealth() {
        if (this.hero.getHealth() <= 75) {
            gui.addStatusImage(new Red(new Position(6, 0)));
        }
    }

    public List<ImageTile> getTiles() {
        return tiles;
    }
}
