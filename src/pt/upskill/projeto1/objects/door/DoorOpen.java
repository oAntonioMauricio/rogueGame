package pt.upskill.projeto1.objects.door;

public class DoorOpen extends Door {

    public DoorOpen(int doorIndex, String nextRoom, int nextIndex, boolean open) {
        super(doorIndex, nextRoom, nextIndex, open);
    }

    @Override
    public String getName() {
        return "DoorOpen";
    }

}
