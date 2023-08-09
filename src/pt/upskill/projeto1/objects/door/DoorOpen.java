package pt.upskill.projeto1.objects.door;

public class DoorOpen extends Door {

    private String name = "DoorOpen";

    public DoorOpen(int doorIndex, String nextRoom, int nextIndex, boolean open) {
        super(doorIndex, nextRoom, nextIndex, open);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
