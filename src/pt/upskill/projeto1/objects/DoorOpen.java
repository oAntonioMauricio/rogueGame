package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

public class DoorOpen extends Door {

    public DoorOpen(int doorIndex, String nextRoom, int nextIndex, boolean open) {
        super(doorIndex, nextRoom, nextIndex, open);
    }

    @Override
    public String getName() {
        return "DoorOpen";
    }

    @Override
    public Boolean isPossibleToWalk() {
        return super.isOpen();
    }
}
