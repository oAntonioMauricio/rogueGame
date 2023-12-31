package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.leaderboard.LeaderBoard;
import pt.upskill.projeto1.leaderboard.Score;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.door.DoorClosed;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.objects.items.Flail;
import pt.upskill.projeto1.objects.props.CityFloor;
import pt.upskill.projeto1.objects.props.FireBloom;
import pt.upskill.projeto1.objects.statusbar.StatusBar;
import pt.upskill.projeto1.objects.items.GoodMeat;
import pt.upskill.projeto1.objects.items.Hammer;
import pt.upskill.projeto1.objects.items.Key;
import pt.upskill.projeto1.objects.props.Trap;
import pt.upskill.projeto1.objects.props.arrows.Arrow;
import pt.upskill.projeto1.objects.statusbar.Fire;
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

    // main_branch
    //
    // UPDATES: abstract class for statues

    // 🟩 Attributes
    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
    private GameSingleton gameSingleton = GameSingleton.getInstance();
    private Hero hero = gameSingleton.getHero();
    private List<ImageTile> tiles = gameSingleton.getTiles();
    private List<Room> roomList = gameSingleton.getRoomList();
    private StatusBar statusBar = gameSingleton.getStatusBar();
    private boolean fireballMode = false;

    // 🟩 Methods
    public void init() {

        gui.setEngine(this);

        // read every file in the dir
        File[] files = new File("rooms").listFiles();
        assert files != null;

        Arrays.sort(files, Comparator.comparing(file -> {
            String fileName = file.getName();
            // Remove non-digit characters and parse the remaining as an integer
            return Integer.parseInt(fileName.replaceAll("[^0-9]", ""));
        }));

        System.out.println("TESTE");
        for (File file : files) {
            System.out.println(file);
        }

        for (File file : files) {
            System.out.println(file.toString());
            gameSingleton.addRoom(file.toString());
        }

        // load room based on index
        loadRoom(0);

        gui.go();
        gui.setStatus("Welcome. You have " + hero.getHealth() + " HP and " + hero.getPower() + " power.");
        gui.showMessage("Pixel Dugeon", "You're trying to find the secret red gem." + System.getProperty("line.separator") + "Rumors say it's hidden at the castle." + System.getProperty("line.separator") + System.getProperty("line.separator") +
                "Use the arrows to move." + System.getProperty("line.separator") +
                "Spacebar to send a fireball that kills instantly." + System.getProperty("line.separator") +
                "1, 2 and 3 to drop or use items." + System.getProperty("line.separator") +
                "S to save game at the checkpoints" + System.getProperty("line.separator") +
                "L to load game." + System.getProperty("line.separator") +
                "R to reset game." + System.getProperty("line.separator") +
                "ESC to see this message again.");

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

        if (!fireballMode) {
            if (keyPressed ==KeyEvent.VK_ESCAPE) {
                gui.showMessage("Pixel Dugeon", "You're trying to find the secret red gem." + System.getProperty("line.separator") + "Rumors says it's hidden at the castle." + System.getProperty("line.separator") + System.getProperty("line.separator") +
                        "Use the arrows to move." + System.getProperty("line.separator") +
                        "Spacebar to send a fireball that kills instantly." + System.getProperty("line.separator") +
                        "1, 2 and 3 to drop or use items." + System.getProperty("line.separator") +
                        "S to save game at the checkpoints" + System.getProperty("line.separator") +
                        "L to load game." + System.getProperty("line.separator") +
                        "R to reset game." + System.getProperty("line.separator") +
                        "ESC to see this message again.");
            }
            if (keyPressed == KeyEvent.VK_F1) {
                System.out.println("Dev mode");

                String userinput = gui.showInputDialog("Dev Move", "Enter command");

                if (Objects.equals(userinput, "resetscores")) {
                    LeaderBoard leaderBoardReset = new LeaderBoard();
                    leaderBoardReset.reset();
                    gui.showMessage("Success", "Highscores back to zero.");
                }

                if (Objects.equals(userinput, "scores")) {
                    // show leaderboard
                    try {
                        FileInputStream fileIn = new FileInputStream("scores/scores.dat");
                        ObjectInputStream in = new ObjectInputStream(fileIn);
                        LeaderBoard loadedLeaderBoard = (LeaderBoard) in.readObject();

                        in.close();
                        fileIn.close();

                        for (Score score : loadedLeaderBoard.getLeaderboard()) {
                            System.out.println(score);
                        }

                    } catch (IOException e) {
                        System.out.println("Erro a ler o ficheiro com o leaderboard!");
                    } catch (ClassNotFoundException e) {
                        System.out.println("Não foi possível converter o objeto gravado no leaderboard!");
                    }
                }

                if (Objects.equals(userinput, "load")) {
                    loadRoom(Integer.parseInt(gui.showInputDialog("Room number", "Num")));
                }

                if (Objects.equals(userinput, "hp")) {
                    hero.setHealth(100.0);
                    statusBar.updateStatus();
                }

            }
            if (keyPressed == KeyEvent.VK_DOWN) {
                // System.out.println("User pressed down key!");
                hero.setPreviousPosition();
                Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.DOWN.asVector()));

                hero.move(nextPosition);
                turn();
            }
            if (keyPressed == KeyEvent.VK_UP) {
                // System.out.println("User pressed up key!");
                hero.setPreviousPosition();
                Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.UP.asVector()));

                hero.move(nextPosition);
                turn();
            }
            if (keyPressed == KeyEvent.VK_LEFT) {
                // System.out.println("User pressed left key!");
                hero.setPreviousPosition();
                Position nextPosition = hero.getPosition().plus(Objects.requireNonNull(Direction.LEFT.asVector()));

                hero.move(nextPosition);
                turn();
            }
            if (keyPressed == KeyEvent.VK_RIGHT) {
                // System.out.println("User pressed right key!");
                hero.setPreviousPosition();
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
            if (keyPressed == KeyEvent.VK_SPACE) {

                ImageTile[] fireballs = statusBar.getFireballsArray();

                for (ImageTile fireball : fireballs) {
                    if (fireball instanceof Fire) {
                        fireballMode = true;

                        Arrow arrowUp = new Arrow(new Position(hero.getPosition().getX(), hero.getPosition().getY() - 1), "ArrowUp");
                        Arrow arrowRight = new Arrow(new Position(hero.getPosition().getX() + 1, hero.getPosition().getY()), "ArrowRight");
                        Arrow arrowDown = new Arrow(new Position(hero.getPosition().getX(), hero.getPosition().getY() + 1), "ArrowDown");
                        Arrow arrowLeft = new Arrow(new Position(hero.getPosition().getX() - 1, hero.getPosition().getY()), "ArrowLeft");

                        tiles.add(arrowUp);
                        tiles.add(arrowRight);
                        tiles.add(arrowDown);
                        tiles.add(arrowLeft);

                        gui.addImage(arrowUp);
                        gui.addImage(arrowRight);
                        gui.addImage(arrowDown);
                        gui.addImage(arrowLeft);

                        gui.setStatus("Send your fireball! Press space again to cancel.");
                        break;
                    }
                }

                if (!fireballMode) {
                    gui.setStatus("You don't have fireballs.");
                }
            }
            //
            // save and load game
            //
            if (keyPressed == KeyEvent.VK_S) {
                boolean canSave = false;

                for (ImageTile interaction : hero.checkWhereHeroIs()) {
                    if (interaction instanceof CityFloor) {
                        canSave = true;
                        System.out.println("Saving game...");

                        try {
                            FileOutputStream fileOut = new FileOutputStream("saves/save.dat");
                            ObjectOutputStream out = new ObjectOutputStream(fileOut);
                            out.writeObject(gameSingleton);
                            gui.setStatus("Game Saved.");
                            out.close();
                            fileOut.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            System.out.println("Erro a salvar o mapa no ficheiro!");
                        }
                    }
                }

                if (!canSave) {
                    gui.setStatus("Can't save here.");
                }
            }
            if (keyPressed == KeyEvent.VK_L) {
                System.out.println("Loading game...");

                try {
                    FileInputStream fileIn = new FileInputStream("saves/save.dat");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    GameSingleton savedSingleton = (GameSingleton) in.readObject();

                    gameSingleton.loadGame(savedSingleton);
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
            if (keyPressed == KeyEvent.VK_R) {

                System.out.println("Loading new game...");

                try {
                    FileInputStream fileIn = new FileInputStream("saves/newGame.dat");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    GameSingleton savedSingleton = (GameSingleton) in.readObject();

                    gameSingleton.loadGame(savedSingleton);
                    loadRoom(gameSingleton.getRoomIndex());

                    gui.setStatus("Welcome again. You have " + hero.getHealth() + " HP and " + hero.getPower() + " power.");

                    in.close();
                    fileIn.close();
                } catch (IOException e) {
                    System.out.println("Erro a ler o ficheiro com o save do mapa!");
                } catch (ClassNotFoundException e) {
                    System.out.println("Não foi possível converter o objeto salvo no mapa!");
                }

            }
        }

        /**
         * Fireball mode ON
         */
        else {
            if (keyPressed == KeyEvent.VK_DOWN) {
                hero.sendFireball(Direction.DOWN);
                removeArrows();
                fireballMode = false;
            }
            if (keyPressed == KeyEvent.VK_UP) {
                hero.sendFireball(Direction.UP);
                removeArrows();
                fireballMode = false;
            }
            if (keyPressed == KeyEvent.VK_LEFT) {
                hero.sendFireball(Direction.LEFT);
                removeArrows();
                fireballMode = false;
            }
            if (keyPressed == KeyEvent.VK_RIGHT) {
                hero.sendFireball(Direction.RIGHT);
                removeArrows();
                fireballMode = false;
            }
            if (keyPressed == KeyEvent.VK_SPACE) {
                removeArrows();
                fireballMode = false;
                gui.setStatus("Fireball canceled.");
            }
        }

    }

    // 🟩 Game Turns
    public void turn() {
        gameSingleton.setScore(gameSingleton.getScore() - 1);

        checkInteraction();

        moveEveryEnemy();

        statusBar.updateStatus();
    }

    public void checkInteraction() {
        // get room index // this is here because it's PRIMITIVE
        int roomIndex = gameSingleton.getRoomIndex();

        ArrayList<ImageTile> interactions = hero.checkWhereHeroIs();

        for (ImageTile interaction : interactions) {
            if (interaction instanceof Enemy) {
                System.out.println("HIT ON ENEMY!");
                // get enemy
                int indexEnemy = roomList.get(roomIndex).getEnemyList().indexOf(interaction);
                Enemy currentEnemy = roomList.get(roomIndex).getEnemyList().get(indexEnemy);

                // fight func
                hero.fight(currentEnemy);
            } else if (interaction instanceof Door) {
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
                        hero.moveAwayFromTheDoor();
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
                    hero.moveAwayFromTheDoor();
                }
            } else if (interaction instanceof Key) {
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
            } else if (interaction instanceof Hammer) {
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

                    // increment score
                    if (!currentItem.getAlreadyPickedUp()) {
                        gameSingleton.setScore(gameSingleton.getScore() + currentItem.getPoints());
                        currentItem.setAlreadyPickedUp(true);
                        // gui message
                        gui.setStatus("You picked the Hammer and gained " + currentItem.getItemPower() + " power and " + currentItem.getPoints() + " points. Total power: " + hero.getPower());
                    } else {
                        // gui message
                        gui.setStatus("You picked the Hammer and gained " + currentItem.getItemPower() + " power. Total power: " + hero.getPower());
                    }

                    // delete item
                    roomList.get(roomIndex).getItemList().remove(currentItem);
                    tiles.remove(currentItem);
                    gui.removeImage(currentItem);
                }
            } else if (interaction instanceof Flail) {
                // get item
                int indexItem = roomList.get(roomIndex).getItemList().indexOf(interaction);
                Flail currentItem = (Flail) roomList.get(roomIndex).getItemList().get(indexItem);

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

                    // increment score
                    if (!currentItem.getAlreadyPickedUp()) {
                        gameSingleton.setScore(gameSingleton.getScore() + currentItem.getPoints());
                        currentItem.setAlreadyPickedUp(true);
                        // gui message
                        gui.setStatus("You picked the Flail and gained " + currentItem.getItemPower() + " power and " + currentItem.getPoints() + " points. Total power: " + hero.getPower());
                    } else {
                        // gui message
                        gui.setStatus("You picked the Flail and gained " + currentItem.getItemPower() + " power. Total power: " + hero.getPower());
                    }

                    // delete item
                    roomList.get(roomIndex).getItemList().remove(currentItem);
                    tiles.remove(currentItem);
                    gui.removeImage(currentItem);
                }
            } else if (interaction instanceof GoodMeat) {
                // get item
                int indexItem = roomList.get(roomIndex).getItemList().indexOf(interaction);
                GoodMeat currentItem = (GoodMeat) roomList.get(roomIndex).getItemList().get(indexItem);

                // storage
                ImageTile[] itemsArray = gameSingleton.getStatusBar().getItemArray();
                int emptyIndex = gameSingleton.getStatusBar().itemArrayEmptyIndex();

                if (emptyIndex == -1) {
                    gui.setStatus("Inventory is full!");
                } else {
                    itemsArray[emptyIndex] = currentItem;
                    // seven is the start on the items position on the status bar
                    currentItem.setPosition(new Position(7 + emptyIndex, 0));

                    gui.setStatus("You picked GoodMeat. Use it to restore " + currentItem.getHealth() + " HP.");

                    // delete item
                    roomList.get(roomIndex).getItemList().remove(currentItem);
                    tiles.remove(currentItem);
                    gui.removeImage(currentItem);
                }
            } else if (interaction instanceof Trap) {
                // get trap
                int indexTrap = roomList.get(roomIndex).getPropsList().indexOf(interaction);
                Trap currentTrap = (Trap) roomList.get(roomIndex).getPropsList().get(indexTrap);

                gui.setStatus("This trap caused damage!");
                hero.setHealth(hero.getHealth() - currentTrap.getDamage());
            } else if (interaction instanceof CityFloor) {
                gui.setStatus("You found a safe spot. You can save your game here with the S key.");
            } else if (interaction instanceof FireBloom) {
                // end game
                int playerScore = gameSingleton.getScore() + 1;

                gui.showMessage("Congratulations!",
                        "You finished the game with " + (playerScore) + " points.");

                // show leaderboard
                try {
                    FileInputStream fileIn = new FileInputStream("scores/scores.dat");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    LeaderBoard loadedLeaderBoard = (LeaderBoard) in.readObject();

                    in.close();
                    fileIn.close();

                    // check top scores and change if it's a highscore
                    loadedLeaderBoard.checkTopScores(playerScore);

                    gui.showMessage("Top Scores",
                            loadedLeaderBoard +
                                    System.getProperty("line.separator"));

                    hero.setPosition(new Position(21, 21));
                    gui.setStatus("Thanks for playing. Press R to play again :)");

                } catch (IOException e) {
                    System.out.println("Erro a ler o ficheiro com o leaderboard!");
                } catch (ClassNotFoundException e) {
                    System.out.println("Não foi possível converter o objeto gravado no leaderboard!");
                }
            }
            //
            // break to deal with one interaction at a time when it's an enemy
            // this means you can't pick items without killing the enemy on top of it
            //
            if (interaction instanceof Enemy) {
                break;
            }
        }

    }

    public void moveEveryEnemy() {

        // get room index. !careful it's an int
        int roomIndex = gameSingleton.getRoomIndex();

        // move every enemy in random direction
        // fight if the next position is the hero position
        for (Enemy enemy : roomList.get(roomIndex).getEnemyList()) {
            enemy.setPreviousPosition();
            enemy.move(enemy.getPosition().plus(enemy.moveToHero(hero)));
        }
    }

    // 🟩 UI
    public void removeArrows() {
        // remove from tiles too !!
        List<ImageTile> tilesToRemove = new ArrayList<>();

        for (ImageTile tile : tiles) {
            if (tile instanceof Arrow) {
                tilesToRemove.add(tile);
            }
        }

        for (ImageTile tile : tilesToRemove) {
            tiles.remove(tile);
            gui.removeImage(tile);
        }

    }
}
