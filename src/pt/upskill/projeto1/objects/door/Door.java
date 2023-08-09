package pt.upskill.projeto1.objects.door;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

public abstract class Door implements ImageTile, Serializable {
    private Position position;
    private int doorIndex;
    private String nextRoom;
    private int nextIndex;
    private boolean open;

    public Door(int doorIndex, String nextRoom, int nextIndex, boolean open) {
        this.doorIndex = doorIndex;
        this.nextRoom = nextRoom;
        this.nextIndex = nextIndex;
        this.open = open;
    }

    public abstract void setName(String name);

    public int getDoorIndex() {
        return doorIndex;
    }

    public void setDoorIndex(int doorIndex) {
        this.doorIndex = doorIndex;
    }

    public String getNextRoom() {
        return nextRoom;
    }

    public int nextRoomInt() {
        return Integer.parseInt(getNextRoom().replaceAll("[^0-9]", ""));
    }

    public void setNextRoom(String nextRoom) {
        this.nextRoom = nextRoom;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Door{" +
                "position=" + position +
                ", doorIndex=" + doorIndex +
                ", nextRoom='" + nextRoom + '\'' +
                ", nextIndex=" + nextIndex +
                ", open=" + open +
                '}';
    }
}
