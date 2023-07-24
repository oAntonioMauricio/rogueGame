package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

public class Wall implements ImageTile {

    private Position position;

    public Wall(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Wall";
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
