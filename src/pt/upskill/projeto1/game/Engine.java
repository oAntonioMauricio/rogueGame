package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.*;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.items.Item;
import pt.upskill.projeto1.objects.statusbar.Black;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.objects.statusbar.Green;
import pt.upskill.projeto1.objects.statusbar.Red;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.*;

public class Engine {

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init();
    }

    // SINGLETON BRANCH
    //
    // TODO: melhorar algoritmo de perseguição https://wumbo.net/formulas/distance-between-two-points-2d/
    //
    // TODO: MELHORAR STATUS BAR PARA VIDA
    // TODO: MELHORAR RELAÇÃO ENTRE SINGLETON E ENGINE !
    //

    // atributes 🔽
    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

    // methods 🔽

    public void init() {
        gui.setEngine(this);

        // initiate singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();

        // new !
        gameSingleton.addRoom("rooms/room0.txt");
        gameSingleton.addRoom("rooms/room1.txt");
        gameSingleton.addRoom("rooms/room2.txt");

        // load room based on index
        loadRoom(0);

        gui.go();
        gui.setStatus("O jogo começou!");

        while (true) {
            gui.update();
        }
    }

    public void loadRoom(int newRoomIndex) {

        // reset tiles and clear gui
        gui.clearImages();
        gui.clearStatus();

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        // load room in singleton
        gameSingleton.loadRoom(newRoomIndex);

        // add status bar
        addStatusBackground();
        addStatusInitial();

        // last gui update
        gui.newImages(gameSingleton.getTiles());
    }

    public void addStatusBackground() {
        System.out.println("building status background");
        for (int i = 0; i < 10; i++) {
            gui.addStatusImage(new Black(new Position(i, 0)));
        }
    }

    public void addStatusInitial() {
        System.out.println("building default status");
        // fireballs
        for (int i = 0; i < 3; i++) {
            gui.addStatusImage(new Fire(new Position(i, 0)));
        }

        // health
        for (int i = 3; i < 7; i++) {
            gui.addStatusImage(new Green(new Position(i, 0)));
        }
    }

    public void notify(int keyPressed) {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        // get hero from singleton
        Hero hero = gameSingleton.getHero();

        if (keyPressed == KeyEvent.VK_DOWN) {
            // System.out.println("User pressed down key!");
            Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector()));
            moveHero(nextPosition);

            turn();
        }
        if (keyPressed == KeyEvent.VK_UP) {
            // System.out.println("User pressed up key!");
            Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.UP.asVector()));
            moveHero(nextPosition);

            turn();
        }
        if (keyPressed == KeyEvent.VK_LEFT) {
            // System.out.println("User pressed left key!");
            Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.LEFT.asVector()));
            moveHero(nextPosition);

            turn();
        }
        if (keyPressed == KeyEvent.VK_RIGHT) {
            // System.out.println("User pressed right key!");
            Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.RIGHT.asVector()));
            moveHero(nextPosition);

            turn();
        }
        if (keyPressed == KeyEvent.VK_SPACE) {
            System.out.println("Space pressed");
        }
    }

    public void turn() {
        checkWhereHeroIs();

        moveEveryEnemy();
        checkIfEnemyOnHero();

        updateHeroHealth();
    }

    public void moveHero(Position nextPosition) {
        /**
         * receives the next position for the hero and checks if the move is possible.
         * if not the hero stays in its previous place
         */

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        Hero hero = gameSingleton.getHero();


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
            hero.setPosition(nextPosition);
        }
    }

    public void checkWhereHeroIs() {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        // get tiles from singleton
        List<ImageTile> tiles = gameSingleton.getTiles();
        // get roomList from singleton
        List<Room> roomList = gameSingleton.getRoomList();
        // get room index
        int roomIndex = gameSingleton.getRoomIndex();
        // get hero from singleton
        Hero hero = gameSingleton.getHero();


        ImageTile interaction = null;

        for (ImageTile tile : tiles) {
            if (Objects.equals(tile.getName(), "Hero") || Objects.equals(tile.getName(), "Floor")) {
                continue;
            }

            if (hero.getPosition().getX() == tile.getPosition().getX() && hero.getPosition().getY() == tile.getPosition().getY()) {
                System.out.println("hero is on top of: " + tile.getName());
                interaction = tile;
            }
        }

        if (interaction != null) {
            switch (interaction.getName()) {
                case "Skeleton", "Bat", "BadGuy" -> {
                    System.out.println("HIT ON ENEMY!");
                    // get enemy
                    int indexEnemy = roomList.get(roomIndex).getEnemyList().indexOf(interaction);
                    Enemy currentEnemy = roomList.get(roomIndex).getEnemyList().get(indexEnemy);

                    boolean removeEnemy = false;
                    Enemy toRemove = null;

                    if (hero.getPower() >= currentEnemy.getPower()) {
                        gui.setStatus("You destroyed: " + currentEnemy.getName());
                        removeEnemy = true;
                        toRemove = currentEnemy;
                    }
                    if (removeEnemy) {

                        roomList.get(roomIndex).getEnemyList().remove(toRemove);
                        tiles.remove(toRemove);
                        gui.removeImage(toRemove);
                    }
                }
                case "DoorOpen", "DoorClosed", "DoorWay" -> {
                    System.out.println("HERO ON DOOR!");
                    // get door
                    int indexDoor = roomList.get(roomIndex).getDoorList().indexOf(interaction);
                    Door door = roomList.get(roomIndex).getDoorList().get(indexDoor);

                    int nextRoom = door.nextRoomInt();
                    int nextDoorIndex = door.getNextIndex();
                    loadRoom(nextRoom);

                    // move to the door on the map
                    moveHero(roomList.get(nextRoom).getDoorList().get(nextDoorIndex).getPosition());

                    // move 1 step away from the door
                    if (hero.getPosition().getY() == 9) {
                        moveHero(hero.getPosition().plus(Objects.requireNonNull(Direction.UP.asVector())));
                    } else if (hero.getPosition().getY() == 0) {
                        moveHero(hero.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector())));
                    } else if (hero.getPosition().getX() == 0) {
                        moveHero(hero.getPosition().plus(Objects.requireNonNull(Direction.RIGHT.asVector())));
                    } else if (hero.getPosition().getX() == 9) {
                        moveHero(hero.getPosition().plus(Objects.requireNonNull(Direction.LEFT.asVector())));
                    }
                }
                case "GoodMeat" -> {
                    System.out.println("HERO ON GOODMEAT!");
                    // get item
                    int indexItem = roomList.get(roomIndex).getItemList().indexOf(interaction);
                    Item currentItem = roomList.get(roomIndex).getItemList().get(indexItem);

                    // effect
                    hero.setHealth(hero.getHealth() + currentItem.getHealth());
                    gui.setStatus("You ate " + currentItem.getName() + " and received " + currentItem.getHealth() + " hp.");
                    System.out.println("Current health: " + hero.getHealth());

                    // delete item
                    roomList.get(roomIndex).getItemList().remove(currentItem);
                    tiles.remove(currentItem);
                    gui.removeImage(currentItem);
                }
                default -> {
                    // default case
                }
            }
        }
    }

    public void checkIfEnemyOnHero() {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        // get tiles from singleton
        List<ImageTile> tiles = gameSingleton.getTiles();
        // get roomList from singleton
        List<Room> roomList = gameSingleton.getRoomList();
        // get room index
        int roomIndex = gameSingleton.getRoomIndex();
        // get hero from singleton
        Hero hero = gameSingleton.getHero();

        boolean removeEnemy = false;
        Enemy toRemove = null;

        for (Enemy enemy : roomList.get(roomIndex).getEnemyList()) {
            if (hero.getPosition().isItSamePosition(enemy.getPosition())) {
                System.out.println("ENEMY ATTACK!");
                hero.setHealth(hero.getHealth() - enemy.getPower());
                System.out.println("Life: " + hero.getHealth());

                gui.setStatus(enemy.getName() + " attacked you. You lost: " + enemy.getPower() + " health.");
                removeEnemy = true;
                toRemove = enemy;
            }
        }

        if (removeEnemy) {
            System.out.println("enemy removed");
            roomList.get(roomIndex).getEnemyList().remove(toRemove);
            tiles.remove(toRemove);
            gui.removeImage(toRemove);
        }
    }

    public void moveEnemy(Enemy enemy, Position nextPosition) {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        // get tiles from singleton
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
            enemy.setPosition(nextPosition);
        }
    }

    public void moveEveryEnemy() {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        // get roomList from singleton
        List<Room> roomList = gameSingleton.getRoomList();
        // get room index
        int roomIndex = gameSingleton.getRoomIndex();
        // get hero from singleton
        Hero hero = gameSingleton.getHero();


        // move every enemy in random direction
        for (Enemy enemy : roomList.get(roomIndex).getEnemyList()) {
            moveEnemy(enemy, enemy.getPosition().plus(enemy.moveToHero(hero)));
        }
    }

    public void updateHeroHealth() {

        // get singleton
        GameSingleton gameSingleton = GameSingleton.getInstance();
        // get hero from singleton
        Hero hero = gameSingleton.getHero();

        if (hero.getHealth() == 100) {
            gui.addStatusImage(new Green(new Position(6, 0)));
            gui.addStatusImage(new Green(new Position(5, 0)));
            gui.addStatusImage(new Green(new Position(4, 0)));
            gui.addStatusImage(new Green(new Position(3, 0)));
        }

        if (hero.getHealth() == 75) {
            gui.addStatusImage(new Red(new Position(6, 0)));
            gui.addStatusImage(new Green(new Position(5, 0)));
            gui.addStatusImage(new Green(new Position(4, 0)));
            gui.addStatusImage(new Green(new Position(3, 0)));
        }

        if (hero.getHealth() == 50) {
            gui.addStatusImage(new Red(new Position(6, 0)));
            gui.addStatusImage(new Red(new Position(5, 0)));
            gui.addStatusImage(new Green(new Position(4, 0)));
            gui.addStatusImage(new Green(new Position(3, 0)));
        }

        if (hero.getHealth() == 25) {
            gui.addStatusImage(new Red(new Position(6, 0)));
            gui.addStatusImage(new Red(new Position(5, 0)));
            gui.addStatusImage(new Red(new Position(4, 0)));
            gui.addStatusImage(new Green(new Position(3, 0)));
        }

        if (hero.getHealth() <= 0) {
            gui.addStatusImage(new Red(new Position(6, 0)));
            gui.addStatusImage(new Red(new Position(5, 0)));
            gui.addStatusImage(new Red(new Position(4, 0)));
            gui.addStatusImage(new Red(new Position(3, 0)));
        }

    }
}
