package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Floor;
import pt.upskill.projeto1.objects.Hero;
import pt.upskill.projeto1.objects.Wall;
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

    private Hero hero = new Hero(new Position(8, 8));
    private List<ImageTile> tiles = new ArrayList<>();

    public void init() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.setEngine(this);

        addFloorAndWallsOnTiles();
        tiles.add(this.hero);

        gui.newImages(tiles);
        gui.go();

        gui.setStatus("O jogo começou!");

        while (true) {
            gui.update();
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

    public void notify(int keyPressed) {
        if (keyPressed == KeyEvent.VK_DOWN) {
            System.out.println("User pressed down key!");
            Direction direction = Direction.DOWN;
            hero.setPosition(hero.getPosition().plus(direction.asVector()), this.tiles);
        }
        if (keyPressed == KeyEvent.VK_UP) {
            System.out.println("User pressed up key!");
            Direction direction = Direction.UP;
            hero.setPosition(hero.getPosition().plus(direction.asVector()), this.tiles);
        }
        if (keyPressed == KeyEvent.VK_LEFT) {
            System.out.println("User pressed left key!");
            Direction direction = Direction.LEFT;
            hero.setPosition(hero.getPosition().plus(direction.asVector()), this.tiles);
        }
        if (keyPressed == KeyEvent.VK_RIGHT) {
            System.out.println("User pressed right key!");
            Direction direction = Direction.RIGHT;
            hero.setPosition(hero.getPosition().plus(direction.asVector()), this.tiles);
        }
    }

    public List<ImageTile> getTiles() {
        return tiles;
    }
}
