package pt.upskill.projeto1.game;

import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.objects.hero.StatusBar;

import java.io.Serializable;
import java.util.List;

public class Save implements Serializable {

    private int roomIndex;
    private List<Room> roomList;
    private Hero hero;
    private StatusBar statusBar;

    public Save(int roomIndex, List<Room> roomList, Hero hero, StatusBar statusBar) {
        this.roomIndex = roomIndex;
        this.roomList = roomList;
        this.hero = hero;
        this.statusBar = statusBar;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public Hero getHero() {
        return hero;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }
}
