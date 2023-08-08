package pt.upskill.projeto1.objects.door;

public class DoorWay extends Door {

    public DoorWay(int doorIndex, String nextRoom, int nextIndex, boolean open) {
        super(doorIndex, nextRoom, nextIndex, open);
    }

    @Override
    public String getName() {
        return "DoorWay";
    }
}
