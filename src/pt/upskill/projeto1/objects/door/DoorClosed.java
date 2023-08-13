package pt.upskill.projeto1.objects.door;

public class DoorClosed extends Door {

    private String key;
    private String name;

    public DoorClosed(String name, int doorIndex, String nextRoom, int nextIndex, boolean open, String key) {
        super(doorIndex, nextRoom, nextIndex, open);
        this.key = key;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }
}
