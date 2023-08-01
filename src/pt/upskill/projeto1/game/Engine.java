package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.door.DoorClosed;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.objects.hero.StatusBar;
import pt.upskill.projeto1.objects.items.GoodMeat;
import pt.upskill.projeto1.objects.items.Item;
import pt.upskill.projeto1.objects.items.Key;
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
    // PERGUNTA: Statusbar no singleton ou como atributo do heroi?
    //
    // TODO: melhorar algoritmo de perseguição https://wumbo.net/formulas/distance-between-two-points-2d/
    //
    // TODO: MELHORAR RELAÇÃO ENTRE SINGLETON E ENGINE!
    // TODO: metodo para afastar da porta dentro do hero
    //

    // atributes 🔽
    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
    private GameSingleton gameSingleton = GameSingleton.getInstance();
    private Hero hero = gameSingleton.getHero();
    private List<ImageTile> tiles = gameSingleton.getTiles();
    private List<Room> roomList = gameSingleton.getRoomList();
    private StatusBar statusBar = gameSingleton.getStatusBar();

    // methods 🔽
    public void init() {
        gui.setEngine(this);

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

    public void loadRoom(int nextRoom) {

        // reset gui
        gui.clearImages();
        gui.clearStatus();

        // load room in singleton
        gameSingleton.loadRoom(nextRoom);

        // add status bar
        addStatusBackground();
        updateStatusBar();

        // last gui update
        gui.newImages(tiles);
    }

    public void addStatusBackground() {
        System.out.println("building status background");
        for (int i = 0; i < 10; i++) {
            gui.addStatusImage(new Black(new Position(i, 0)));
        }
    }

    public void updateStatusBar() {
        // reset status
        gui.clearStatus();

        // System.out.println("updating status...");
        // black background
        for (int i = 0; i < 10; i++) {
            gui.addStatusImage(new Black(new Position(i, 0)));
        }

        // fireballs
        for (ImageTile fireball : statusBar.getStatusBarList().get(0)) {
            gui.addStatusImage(fireball);
        }

        // health
        for (ImageTile health : statusBar.getStatusBarList().get(1)) {
            gui.addStatusImage(health);
        }

        // items
        for (ImageTile item : statusBar.getItemArray()) {
            if (item != null) {
                gui.addStatusImage(item);
            }
        }


    }

    public void notify(int keyPressed) {

        if (keyPressed == KeyEvent.VK_DOWN) {
            // System.out.println("User pressed down key!");
            Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector()));
            hero.move(nextPosition);

            turn();
        }
        if (keyPressed == KeyEvent.VK_UP) {
            // System.out.println("User pressed up key!");
            Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.UP.asVector()));
            hero.move(nextPosition);

            turn();
        }
        if (keyPressed == KeyEvent.VK_LEFT) {
            // System.out.println("User pressed left key!");
            Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.LEFT.asVector()));
            hero.move(nextPosition);

            turn();
        }
        if (keyPressed == KeyEvent.VK_RIGHT) {
            // System.out.println("User pressed right key!");
            Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.RIGHT.asVector()));
            hero.move(nextPosition);

            turn();
        }
        if (keyPressed == KeyEvent.VK_1) {
            // System.out.println("Remove first item");

            ImageTile[] items = statusBar.getItemArray();
            Item currentItem = (Item) items[0];

            if (currentItem == null) {
                gui.setStatus("You don't have an item in the first slot.");
            } else {
                // let's drop the item on the floor. on top of the hero
                // get room index // this is here because it's PRIMITIVE
                int roomIndex = gameSingleton.getRoomIndex();

                // drop item and check if it can be dropped
                if (currentItem.dropItem()) {
                    // add item to room
                    roomList.get(roomIndex).getItemList().add(currentItem);
                    tiles.add(currentItem);
                    gui.addImage(currentItem);

                    if (currentItem instanceof Key) {
                        gui.setStatus("You removed: " + ((Key) currentItem).getKeyId());
                    } else {
                        gui.setStatus("You removed: " + currentItem.getName());
                    }

                    // remove item from status bar
                    items[0] = null;
                }

            }

            statusBar.organizeItemArray();
            updateStatusBar();
        }
        if (keyPressed == KeyEvent.VK_SPACE) {
            ImageTile[] fireballs = statusBar.getStatusBarList().get(0);
            for (int i = 0; i < fireballs.length; i++) {
                if (fireballs[i] instanceof Fire) {
                    fireballs[i] = new Black(new Position(i, 0));
                    break;
                }
            }
            updateStatusBar();
        }
    }

    public void turn() {
        checkWhereHeroIs();

        moveEveryEnemy();
        checkIfEnemyOnHero();

        updateHeroHealth();

        updateStatusBar();
    }

    public void checkWhereHeroIs() {

        // get room index // this is here because it's PRIMITIVE
        int roomIndex = gameSingleton.getRoomIndex();

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
                    // get door
                    int indexDoor = roomList.get(roomIndex).getDoorList().indexOf(interaction);
                    Door door = roomList.get(roomIndex).getDoorList().get(indexDoor);

                    // check if door needs key
                    if (!door.isOpen()) {
                        boolean gotTheKey = false;

                        DoorClosed doorClosed = (DoorClosed) door;
                        String keyToOpenDoor = doorClosed.getKey();

                        // check inventory for the right key
                        ImageTile[] itemArray = gameSingleton.getStatusBar().getItemArray();

                        for (ImageTile item : itemArray) {
                            if (item instanceof Key) {
                                System.out.println("Got a key!");
                                System.out.println(((Key) item).getKeyId());
                                System.out.println(keyToOpenDoor);
                                if (Objects.equals(((Key) item).getKeyId(), keyToOpenDoor)) {
                                    System.out.println("You got the key to this door!");
                                    gui.setStatus("You opened " + doorClosed.getName() + " with " + keyToOpenDoor);
                                    gotTheKey = true;
                                    doorClosed.setOpen(true);

                                    // unlock the door in the next room
                                    int nextRoom = doorClosed.nextRoomInt();
                                    int nextDoorIndex = doorClosed.getNextIndex();
                                    roomList.get(nextRoom).getDoorList().get(nextDoorIndex).setOpen(true);
                                }
                            }
                        }

                        if (!gotTheKey) {
                            gui.setStatus("You don't have the key to this door.");

                            // move 1 step away from the door
                            if (hero.getPosition().getY() == 9) {
                                hero.move(hero.getPosition().plus(Objects.requireNonNull(Direction.UP.asVector())));
                            } else if (hero.getPosition().getY() == 0) {
                                hero.move(hero.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector())));
                            } else if (hero.getPosition().getX() == 0) {
                                hero.move(hero.getPosition().plus(Objects.requireNonNull(Direction.RIGHT.asVector())));
                            } else if (hero.getPosition().getX() == 9) {
                                hero.move(hero.getPosition().plus(Objects.requireNonNull(Direction.LEFT.asVector())));
                            }
                        }

                    }

                    // this needs to run again. if you got the key this will check true right after
                    if (door.isOpen()) {
                        int nextRoom = door.nextRoomInt();
                        System.out.println("next room: " + nextRoom);
                        int nextDoorIndex = door.getNextIndex();

                        loadRoom(nextRoom);

                        // move to the door on the map
                        hero.move(roomList.get(nextRoom).getDoorList().get(nextDoorIndex).getPosition());

                        // move 1 step away from the door
                        if (hero.getPosition().getY() == 9) {
                            hero.move(hero.getPosition().plus(Objects.requireNonNull(Direction.UP.asVector())));
                        } else if (hero.getPosition().getY() == 0) {
                            hero.move(hero.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector())));
                        } else if (hero.getPosition().getX() == 0) {
                            hero.move(hero.getPosition().plus(Objects.requireNonNull(Direction.RIGHT.asVector())));
                        } else if (hero.getPosition().getX() == 9) {
                            hero.move(hero.getPosition().plus(Objects.requireNonNull(Direction.LEFT.asVector())));
                        }
                    }


                }
                case "Key" -> {
                    // get key
                    int indexItem = roomList.get(roomIndex).getItemList().indexOf(interaction);
                    Key currentKey = (Key) roomList.get(roomIndex).getItemList().get(indexItem);

                    // effect
                    ImageTile[] itemsArray = gameSingleton.getStatusBar().getItemArray();
                    int emptyIndex = gameSingleton.getStatusBar().itemArrayEmptyIndex();

                    if (emptyIndex == -1) {
                        gui.setStatus("Inventory is full!");
                    } else {
                        itemsArray[emptyIndex] = currentKey;
                        // seven is the start on the items position on the status bar
                        currentKey.setPosition(new Position(7 + emptyIndex, 0));
                        System.out.println("Got " + currentKey.getKeyId());
                        gui.setStatus("You picked " + currentKey.getKeyId());

                        // delete item
                        roomList.get(roomIndex).getItemList().remove(currentKey);
                        tiles.remove(currentKey);
                        gui.removeImage(currentKey);
                    }
                }
                case "GoodMeat" -> {
                    // get item
                    int indexItem = roomList.get(roomIndex).getItemList().indexOf(interaction);
                    GoodMeat currentItem = (GoodMeat) roomList.get(roomIndex).getItemList().get(indexItem);

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

    public void moveEveryEnemy() {

        // get room index. !careful it's an int
        int roomIndex = gameSingleton.getRoomIndex();

        // move every enemy in random direction
        for (Enemy enemy : roomList.get(roomIndex).getEnemyList()) {
            // moveEnemy(enemy, enemy.getPosition().plus(enemy.moveToHero(hero)));
            enemy.move(enemy.getPosition().plus(enemy.moveToHero(hero)));
        }
    }

    public void checkIfEnemyOnHero() {

        // get room index
        int roomIndex = gameSingleton.getRoomIndex();

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

    public void updateHeroHealth() {

        ImageTile[] healthArray = gameSingleton.getStatusBar().getStatusBarList().get(1);

        if (hero.getHealth() == 100) {
            for (int i = 0, j = 3; i < 4; i++, j++) {
                healthArray[i] = new Green(new Position(j, 0));
            }
        }

        if (hero.getHealth() == 75) {
            healthArray[0] = new Green(new Position(3, 0));
            healthArray[1] = new Green(new Position(4, 0));
            healthArray[2] = new Green(new Position(5, 0));
            healthArray[3] = new Red(new Position(6, 0));
        }

        if (hero.getHealth() == 50) {
            healthArray[0] = new Green(new Position(3, 0));
            healthArray[1] = new Green(new Position(4, 0));
            healthArray[2] = new Red(new Position(5, 0));
            healthArray[3] = new Red(new Position(6, 0));
        }

        if (hero.getHealth() == 25) {
            healthArray[0] = new Green(new Position(3, 0));
            healthArray[1] = new Red(new Position(4, 0));
            healthArray[2] = new Red(new Position(5, 0));
            healthArray[3] = new Red(new Position(6, 0));
        }

        if (hero.getHealth() <= 0) {
            healthArray[0] = new Red(new Position(3, 0));
            healthArray[1] = new Red(new Position(4, 0));
            healthArray[2] = new Red(new Position(5, 0));
            healthArray[3] = new Red(new Position(6, 0));
        }

    }
}
