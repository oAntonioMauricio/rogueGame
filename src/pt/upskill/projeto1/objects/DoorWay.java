package pt.upskill.projeto1.objects;

public class DoorWay extends Door {

    public DoorWay(int doorIndex, String nextRoom, int nextIndex, boolean open) {
        super(doorIndex, nextRoom, nextIndex, open);
    }

    @Override
    public String getName() {
        return "DoorWay";
    }

    @Override
    public Boolean isPossibleToWalk() {
        return super.isOpen();
    }
}