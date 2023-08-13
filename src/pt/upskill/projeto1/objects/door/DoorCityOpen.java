package pt.upskill.projeto1.objects.door;

public class DoorCityOpen extends Door{
    private String name = "DoorCityOpen";

    public DoorCityOpen(int doorIndex, String nextRoom, int nextIndex, boolean open) {
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
