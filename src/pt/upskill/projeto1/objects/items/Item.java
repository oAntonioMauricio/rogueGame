package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

public abstract class Item implements ImageTile {

    private Position position;
    private int health;

    public Item(Position position, int health) {
        this.position = position;
        this.health = health;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Boolean isPossibleToWalk() {
        return true;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

}
