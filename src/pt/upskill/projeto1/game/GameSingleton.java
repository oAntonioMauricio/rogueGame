package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Floor;
import pt.upskill.projeto1.objects.Hero;
import pt.upskill.projeto1.objects.Wall;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class GameSingleton {
    private static GameSingleton instance;
    private List<ImageTile> tiles;
    private int roomIndex;
    private List<Room> roomList;
    private Hero hero;

    private GameSingleton() {
        tiles = new ArrayList<>();
        roomIndex = 0;
        roomList = new ArrayList<>();
        hero = new Hero(new Position(8, 8), 100);
    }

    public static GameSingleton getInstance() {
        if (instance == null) {
            instance = new GameSingleton();
        }

        return instance;
    }

    // Methods 🔽
    public void loadRoom(int newRoomIndex) {
        setRoomIndex(newRoomIndex);
        tiles.removeAll(tiles);

        System.out.println("building floor");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tiles.add(new Floor(new Position(i, j)));
            }
        }

        // add hero
        addTile(hero);

        // add walls
        tiles.addAll(roomList.get(roomIndex).getWallList());

        // add the doors
        tiles.addAll(roomList.get(roomIndex).getDoorList());

        // add the enemies
        tiles.addAll(roomList.get(roomIndex).getEnemyList());

        // add the items
        tiles.addAll(roomList.get(roomIndex).getItemList());

    }

    public void addRoom(String filename) {
        roomList.add(new Room(filename));
    }

    public void addTile(ImageTile tile) {
        tiles.add(tile);
    }


    // Getters 🔽
    public List<Room> getRoomList() {
        return roomList;
    }

    public List<ImageTile> getTiles() {
        return tiles;
    }

    public Hero getHero() {
        return hero;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    // Setter 🔽

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }
}
