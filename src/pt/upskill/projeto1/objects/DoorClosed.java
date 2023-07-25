package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

public class DoorClosed extends Door {

    private String key;

    public DoorClosed(int doorIndex, String nextRoom, int nextIndex, boolean open, String key) {
        super(doorIndex, nextRoom, nextIndex, open);
        this.key = key;
    }

    @Override
    public String getName() {
        return "DoorClosed";
    }

    @Override
    public Boolean isPossibleToWalk() {
        return super.isOpen();
    }
}
