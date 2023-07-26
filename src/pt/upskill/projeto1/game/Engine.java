package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.*;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Engine {

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init();
    }

    //
    // TODO: melhorar algoritmo de perseguição https://wumbo.net/formulas/distance-between-two-points-2d/
    //
    // perguntar sobre a func readFile (demasiado grande?, posso simplificar?); construção das portas correta?
    //

    private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
    private ReadRooms readRooms = new ReadRooms();

    // private final List<ImageTile> statusBar = new ArrayList<>();

    private List<ImageTile> tiles = new ArrayList<>();
    private List<Door> doorList = new ArrayList<>();
    private Hero hero = new Hero(new Position(8, 8), 100);
    private List<Enemy> enemyList = new ArrayList<>();

    public void init() {
        gui.setEngine(this);

        // add status bar
        addStatusBackground();
        addStatusInitial();

        // add floor
        addFloor();

        // read file and draw room
        readRooms.readFile("rooms/room2.txt", doorList, enemyList, tiles);

        // add hero
        tiles.add(this.hero);

        // add the enemies
        tiles.addAll(enemyList);

        // add the doors
        tiles.addAll(doorList);

        gui.newImages(this.tiles);
        gui.go();

        gui.setStatus("O jogo começou!");

        while (true) {
            gui.update();
        }
    }

    public void notify(int keyPressed) {
        if (keyPressed == KeyEvent.VK_DOWN) {
            // System.out.println("User pressed down key!");

            hero.setPosition(hero.getPosition().plus(Direction.DOWN.asVector()), this.tiles);
            turn();
        }
        if (keyPressed == KeyEvent.VK_UP) {
            // System.out.println("User pressed up key!");

            hero.setPosition(hero.getPosition().plus(Direction.UP.asVector()), this.tiles);
            turn();
        }
        if (keyPressed == KeyEvent.VK_LEFT) {
            // System.out.println("User pressed left key!");

            hero.setPosition(hero.getPosition().plus(Direction.LEFT.asVector()), this.tiles);
            turn();
        }
        if (keyPressed == KeyEvent.VK_RIGHT) {
            // System.out.println("User pressed right key!");

            hero.setPosition(hero.getPosition().plus(Direction.RIGHT.asVector()), this.tiles);
            turn();
        }
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

    /*
    public void readFile(String fileName) {
        System.out.println("building " + fileName);
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            int col = 0;

            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();

                if (linha.contains("#")) {
                    List<String> chars = List.of(linha.split(" "));
                    // continue if only contains the "#"
                    if (chars.size() == 1) continue;
                    // catch doors
                    System.out.println(chars);
                    if (isNumber(chars.get(1))) {
                        System.out.println("🔼 Got a door");
                        int doorIndex = Integer.parseInt(chars.get(1));
                        String nextRoom = chars.get(3);
                        int nextIndex = Integer.parseInt(chars.get(4));
                        switch (chars.get(2)) {
                            case "D" -> {
                                if (chars.size() > 5) {
                                    // System.out.println("Create DoorClosed");
                                    doorList.add(new DoorClosed(doorIndex, nextRoom, nextIndex, false, chars.get(4)));
                                } else {
                                    // System.out.println("Create DoorOpen");
                                    doorList.add(new DoorOpen(doorIndex, nextRoom, nextIndex, true));
                                }
                            }
                            case "E" ->
                                // System.out.println("Create DoorWay");
                                    doorList.add(new DoorWay(doorIndex, nextRoom, nextIndex, true));
                            default -> System.out.println("Invalid door type on .txt");
                        }
                    }

                    //ignore next steps for # fields
                    continue;
                }

                // build map
                String[] chars = linha.split("");
                for (int i = 0; i < 10; i++) {
                    if (Objects.equals(chars[i], "W")) {
                        tiles.add(new Wall(new Position(i, col)));
                    } else if (isNumber(chars[i])) {
                        doorList.get(Integer.parseInt(chars[i])).setPosition(new Position(i, col));
                    } else if (Objects.equals(chars[i], "S")) {
                        enemyList.add(new Skeleton(new Position(i, col), 30));
                    }
                }
                // go to next column
                col++;
            }

            for (Door myDoor : doorList) {
                System.out.println(myDoor);
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    } */

    public void turn() {
        checkIfHeroOnEnemy(false);

        moveEnemy();
        checkIfHeroOnEnemy(true);

        updateHerohealth();
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

                    gui.setStatus(enemy.getName() + " attacked you. You lost: " + enemy.getPower() + " health.");
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

    // get this out of here ASAP
    public boolean isNumber(String str) {
        try {
            Integer.parseInt(str); // or Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
