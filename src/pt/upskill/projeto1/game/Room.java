package pt.upskill.projeto1.game;

import pt.upskill.projeto1.objects.*;
import pt.upskill.projeto1.objects.enemies.BadGuy;
import pt.upskill.projeto1.objects.enemies.Bat;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.enemies.Skeleton;
import pt.upskill.projeto1.objects.items.GoodMeat;
import pt.upskill.projeto1.objects.items.Item;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class
Room {
    private List<Wall> wallList = new ArrayList<>();
    private List<Door> doorList = new ArrayList<>();
    private List<Enemy> enemyList = new ArrayList<>();
    private List<Item> itemList = new ArrayList<>();

    public Room(String fileName) {
        readFile(fileName);
    }

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

                    // catch keys
                    if (Objects.equals(chars.get(1), "k")) {
                        System.out.println("🔼 Got a key.");
                    }

                    //ignore next steps for # fields
                    continue;
                }

                // build map
                String[] chars = linha.split("");
                for (int i = 0; i < 10; i++) {
                    if (Objects.equals(chars[i], "W")) {
                        wallList.add(new Wall(new Position(i, col)));
                    } else if (isNumber(chars[i])) {
                        doorList.get(Integer.parseInt(chars[i])).setPosition(new Position(i, col));
                    } else if (Objects.equals(chars[i], "S")) {
                        enemyList.add(new Skeleton(new Position(i, col)));
                    } else if (Objects.equals(chars[i], "B")) {
                        enemyList.add(new Bat(new Position(i, col)));
                    } else if (Objects.equals(chars[i], "G")) {
                        enemyList.add(new BadGuy(new Position(i, col)));
                    } else if (Objects.equals(chars[i], "m")) {
                        itemList.add(new GoodMeat(new Position(i, col)));
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

    }

    public boolean isNumber(String str) {
        try {
            Integer.parseInt(str); // or Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<Wall> getWallList() {
        return wallList;
    }

    public List<Door> getDoorList() {
        return doorList;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public List<Item> getItemList() {
        return itemList;
    }
}
