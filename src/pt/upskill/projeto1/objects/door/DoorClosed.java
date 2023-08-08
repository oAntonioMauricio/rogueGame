package pt.upskill.projeto1.objects.door;

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

    public String getKey() {
        return key;
    }
}
