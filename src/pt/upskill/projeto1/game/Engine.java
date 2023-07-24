package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.*;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Engine {

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init();
    }

    private List<ImageTile> tiles = new ArrayList<>();
    private Hero hero = new Hero(new Position(8, 8));
    private List<Enemy> enemyList = new ArrayList<>();

    public void init() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.setEngine(this);

        // add floor and walls
        addFloorAndWallsOnTiles();

        // add hero
        tiles.add(this.hero);

        // add the enemys
        enemyList.add(new Skeleton(new Position(2, 2), 20));
        enemyList.add(new Skeleton(new Position(3, 4), 20));
        tiles.addAll(enemyList);

        gui.newImages(tiles);
        gui.go();

        gui.setStatus("O jogo começou!");

        while (true) {
            gui.update();
        }
    }

    public void notify(int keyPressed) {
        if (keyPressed == KeyEvent.VK_DOWN) {
            System.out.println("User pressed down key!");

            hero.setPosition(hero.getPosition().plus(Direction.DOWN.asVector()), this.tiles);

            moveEnemy();
            checkIfHeroOnEnemy();
        }
        if (keyPressed == KeyEvent.VK_UP) {
            System.out.println("User pressed up key!");

            hero.setPosition(hero.getPosition().plus(Direction.UP.asVector()), this.tiles);

            moveEnemy();
            checkIfHeroOnEnemy();
        }
        if (keyPressed == KeyEvent.VK_LEFT) {
            System.out.println("User pressed left key!");

            hero.setPosition(hero.getPosition().plus(Direction.LEFT.asVector()), this.tiles);

            moveEnemy();
            checkIfHeroOnEnemy();
        }
        if (keyPressed == KeyEvent.VK_RIGHT) {
            System.out.println("User pressed right key!");

            hero.setPosition(hero.getPosition().plus(Direction.RIGHT.asVector()), this.tiles);

            moveEnemy();
            checkIfHeroOnEnemy();
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
        for (int i = 0; i < 10; i++) {
            tiles.add(new Wall(new Position(i, 0)));
            tiles.add(new Wall(new Position(i, 9)));
        }

        // draw walls vertical
        for (int i = 0; i < 10; i++) {
            tiles.add(new Wall(new Position(0, i)));
            tiles.add(new Wall(new Position(9, i)));
        }
    }

    public void moveEnemy() {
        // move enemy in random direction
        for (Enemy enemy : enemyList) {
            enemy.setPosition(enemy.getPosition().plus(enemy.moveToHero(this.hero)), this.tiles);
        }
    }

    public void checkIfHeroOnEnemy() {
        for (Enemy enemy : enemyList) {
            if (this.hero.getPosition().isItSamePosition(enemy.getPosition())) {
                System.out.println("SAME POSITION!");
            }
        }
    }

    public List<ImageTile> getTiles() {
        return tiles;
    }
}
