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
    // TODO: CREATE SINGLETON
    //

    // atributes 🔽

    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
    private List<ImageTile> tiles = new ArrayList<>();
    int roomIndex = 0;
    private List<Room> roomList = new ArrayList<>();
    private Hero hero = new Hero(new Position(8, 8), 100);

    // methods 🔽

    public void init() {
        gui.setEngine(this);

        // read file and draw room
        roomList.add(new Room("rooms/room0.txt"));
        roomList.add(new Room("rooms/room1.txt"));
        roomList.add(new Room("rooms/room2.txt"));

        // load room based on index
        loadRoom(0);

        gui.go();
        gui.setStatus("O jogo começou!");

        while (true) {
            gui.update();
        }
    }

    public void loadRoom(int newRoomIndex) {
        setRoomIndex(newRoomIndex);
        // reset tiles and clear gui
        tiles.removeAll(tiles);
        gui.clearImages();

        // add status bar
        addStatusBackground();
        addStatusInitial();

        // add floor
        addFloor();

        // add hero
        tiles.add(this.hero);

        // add walls
        tiles.addAll(roomList.get(roomIndex).getWallList());

        // add the doors
        tiles.addAll(roomList.get(roomIndex).getDoorList());

        // add the enemies
        tiles.addAll(roomList.get(roomIndex).getEnemyList());

        // add the items
        tiles.addAll(roomList.get(roomIndex).getItemList());

        gui.newImages(this.tiles);
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

    public void addFloor() {
        System.out.println("building floor");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tiles.add(new Floor(new Position(i, j)));
            }
        }
    }

    public void notify(int keyPressed) {
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
        boolean move = true;

        for (ImageTile tile : tiles) {
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
        ImageTile interaction = null;

        for (ImageTile tile : tiles) {
            if (Objects.equals(tile.getName(), "Hero") || Objects.equals(tile.getName(), "Floor")) {
                continue;
            }

            if (this.hero.getPosition().getX() == tile.getPosition().getX() && this.hero.getPosition().getY() == tile.getPosition().getY()) {
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
                    if (this.hero.getPower() >= currentEnemy.getPower()) {
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
                    if (this.hero.getPosition().getY() == 9) {
                        moveHero(hero.getPosition().plus(Objects.requireNonNull(Direction.UP.asVector())));
                    } else if (this.hero.getPosition().getY() == 0) {
                        moveHero(hero.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector())));
                    } else if (this.hero.getPosition().getX() == 0) {
                        moveHero(hero.getPosition().plus(Objects.requireNonNull(Direction.RIGHT.asVector())));
                    } else if (this.hero.getPosition().getX() == 9) {
                        moveHero(hero.getPosition().plus(Objects.requireNonNull(Direction.LEFT.asVector())));
                    }
                }
                case "GoodMeat" -> {
                    System.out.println("HERO ON GOODMEAT!");
                    // get item
                    int indexItem = roomList.get(roomIndex).getItemList().indexOf(interaction);
                    Item currentItem = roomList.get(roomIndex).getItemList().get(indexItem);

                    // effect
                    this.hero.setHealth(this.hero.getHealth() + currentItem.getHealth());
                    gui.setStatus("You ate " + currentItem.getName() + " and received " + currentItem.getHealth() + " hp.");
                    System.out.println("Current health: " + this.hero.getHealth());

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
        boolean removeEnemy = false;
        Enemy toRemove = null;

        for (Enemy enemy : roomList.get(roomIndex).getEnemyList()) {
            if (this.hero.getPosition().isItSamePosition(enemy.getPosition())) {
                System.out.println("ENEMY ATTACK!");
                this.hero.setHealth(this.hero.getHealth() - enemy.getPower());
                System.out.println("Life: " + this.hero.getHealth());

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
        // move every enemy in random direction
        for (Enemy enemy : roomList.get(roomIndex).getEnemyList()) {
            moveEnemy(enemy, enemy.getPosition().plus(enemy.moveToHero(this.hero)));
        }
    }

    public void updateHeroHealth() {
        if (this.hero.getHealth() == 100) {
            gui.addStatusImage(new Green(new Position(6, 0)));
            gui.addStatusImage(new Green(new Position(5, 0)));
            gui.addStatusImage(new Green(new Position(4, 0)));
            gui.addStatusImage(new Green(new Position(3, 0)));
        }

        if (this.hero.getHealth() == 75) {
            gui.addStatusImage(new Red(new Position(6, 0)));
            gui.addStatusImage(new Green(new Position(5, 0)));
            gui.addStatusImage(new Green(new Position(4, 0)));
            gui.addStatusImage(new Green(new Position(3, 0)));
        }

        if (this.hero.getHealth() == 50) {
            gui.addStatusImage(new Red(new Position(6, 0)));
            gui.addStatusImage(new Red(new Position(5, 0)));
            gui.addStatusImage(new Green(new Position(4, 0)));
            gui.addStatusImage(new Green(new Position(3, 0)));
        }

        if (this.hero.getHealth() == 25) {
            gui.addStatusImage(new Red(new Position(6, 0)));
            gui.addStatusImage(new Red(new Position(5, 0)));
            gui.addStatusImage(new Red(new Position(4, 0)));
            gui.addStatusImage(new Green(new Position(3, 0)));
        }

        if (this.hero.getHealth() <= 0) {
            gui.addStatusImage(new Red(new Position(6, 0)));
            gui.addStatusImage(new Red(new Position(5, 0)));
            gui.addStatusImage(new Red(new Position(4, 0)));
            gui.addStatusImage(new Red(new Position(3, 0)));
        }

    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }
}
