package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.door.DoorClosed;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.objects.hero.StatusBar;
import pt.upskill.projeto1.objects.items.GoodMeat;
import pt.upskill.projeto1.objects.items.Hammer;
import pt.upskill.projeto1.objects.items.Item;
import pt.upskill.projeto1.objects.items.Key;
import pt.upskill.projeto1.objects.statusbar.Black;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.objects.statusbar.Green;
import pt.upskill.projeto1.objects.statusbar.Red;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class Engine {

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init();
    }

    // save_game branch
    //
    // PERGUNTA: Statusbar no singleton ou como atributo do heroi?
    //
    // PENSAR: Mudar armazenamento/organização dos items? Fora do checkWhereHeroIs para statusbar? ou gamestate?
    //
    // TODO: melhorar algoritmo de perseguição https://wumbo.net/formulas/distance-between-two-points-2d/
    // TODO: MELHORAR RELAÇÃO ENTRE SINGLETON E ENGINE
    // TODO: metodo para afastar da porta dentro do hero

    // atributes 🔽
    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
    private GameSingleton gameSingleton = GameSingleton.getInstance();
    private Hero hero = gameSingleton.getHero();
    private List<ImageTile> tiles = gameSingleton.getTiles();
    private List<Room> roomList = gameSingleton.getRoomList();
    private StatusBar statusBar = gameSingleton.getStatusBar();


    // TEST

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
        gui.setStatus("Welcome. You have " + hero.getHealth() + " HP and " + hero.getPower() + " power.");

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

        // update status bar
        statusBar.updateStatus();

        // last gui update
        gui.newImages(tiles);
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
            statusBar.removeItem(0);
        }
        if (keyPressed == KeyEvent.VK_2) {
            // System.out.println("Remove second item");
            statusBar.removeItem(1);
        }
        if (keyPressed == KeyEvent.VK_3) {
            // System.out.println("Remove third item");
            statusBar.removeItem(2);
        }
        if (keyPressed == KeyEvent.VK_8) {
            System.out.println("Saving game...");

            try {
                FileOutputStream fileOut = new FileOutputStream("saves/save.dat");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(gameSingleton);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Erro a salvar o mapa no ficheiro!");
            }

        }
        if (keyPressed == KeyEvent.VK_9) {
            System.out.println("Loading game...");

            try {
                FileInputStream fileIn = new FileInputStream("saves/save.dat");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                GameSingleton loadedSave = (GameSingleton) in.readObject();

                gameSingleton.loadGame(loadedSave.getRoomIndex(), loadedSave.getRoomList(), loadedSave.getHero(), loadedSave.getStatusBar());

                loadRoom(gameSingleton.getRoomIndex());
                gui.setStatus("Game Loaded. You have " + hero.getHealth() + " HP and " + hero.getPower() + " power.");

                in.close();
                fileIn.close();
            } catch (IOException e) {
                System.out.println("Erro a ler o ficheiro com o save do mapa!");
            } catch (ClassNotFoundException e) {
                System.out.println("Não foi possível converter o objeto salvo no mapa!");
            }

        }
        if (keyPressed == KeyEvent.VK_SPACE) {

            ImageTile[] fireballs = statusBar.getFireballsArray();

            for (int i = 0; i < fireballs.length; i++) {
                if (fireballs[i] instanceof Fire) {
                    gui.setStatus("Sending fireball!");

                    ((Fire) fireballs[i]).setPosition(hero.getPosition());
                    FireBallThread fireball = new FireBallThread(Direction.UP, (FireTile) fireballs[i]);
                    gui.addImage(fireballs[i]);
                    fireball.start();

                    fireballs[i] = new Black(new Position(i, 0));
                    break;
                }
            }

            statusBar.updateStatus();
        }
    }

    public void turn() {
        checkWhereHeroIs();

        moveEveryEnemy();

        checkIfEnemyOnHero();

        statusBar.updateStatus();
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

                    // fight func
                    hero.fight(currentEnemy);
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
                                    // System.out.println("You got the key to this door!");
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
                            gui.setStatus("You need the " + keyToOpenDoor + " to open this door.");

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

                    // storage
                    ImageTile[] itemsArray = gameSingleton.getStatusBar().getItemArray();
                    int emptyIndex = gameSingleton.getStatusBar().itemArrayEmptyIndex();

                    if (emptyIndex == -1) {
                        gui.setStatus("Inventory is full!");
                    } else {
                        itemsArray[emptyIndex] = currentKey;
                        // seven is the start on the items position on the status bar
                        currentKey.setPosition(new Position(7 + emptyIndex, 0));
                        gui.setStatus("You picked " + currentKey.getKeyId());

                        // delete item
                        roomList.get(roomIndex).getItemList().remove(currentKey);
                        tiles.remove(currentKey);
                        gui.removeImage(currentKey);
                    }
                }
                case "Hammer" -> {
                    // get item
                    int indexItem = roomList.get(roomIndex).getItemList().indexOf(interaction);
                    Hammer currentItem = (Hammer) roomList.get(roomIndex).getItemList().get(indexItem);

                    // storage
                    ImageTile[] itemsArray = gameSingleton.getStatusBar().getItemArray();
                    int emptyIndex = gameSingleton.getStatusBar().itemArrayEmptyIndex();

                    if (emptyIndex == -1) {
                        gui.setStatus("Inventory is full!");
                    } else {
                        itemsArray[emptyIndex] = currentItem;
                        // seven is the start on the items position on the status bar
                        currentItem.setPosition(new Position(7 + emptyIndex, 0));

                        // effect
                        hero.setPower(hero.getPower() + currentItem.getItemPower());
                        gui.setStatus("You picked the Hammer and gained " + currentItem.getItemPower() + " power. Total power: " + hero.getPower());

                        // delete item
                        roomList.get(roomIndex).getItemList().remove(currentItem);
                        tiles.remove(currentItem);
                        gui.removeImage(currentItem);
                    }
                }
                case "GoodMeat" -> {
                    // get item
                    int indexItem = roomList.get(roomIndex).getItemList().indexOf(interaction);
                    GoodMeat currentItem = (GoodMeat) roomList.get(roomIndex).getItemList().get(indexItem);

                    // effect
                    hero.setHealth(hero.getHealth() + currentItem.getHealth());
                    gui.setStatus("You ate " + currentItem.getName() + " and received " + currentItem.getHealth() + " HP. Your HP is: " + hero.getHealth());

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

        for (Enemy enemy : roomList.get(roomIndex).getEnemyList()) {
            if (hero.getPosition().isItSamePosition(enemy.getPosition())) {
                System.out.println("Enemy attack!");
                enemy.fight(hero);
                break;
            }
        }
    }
}
