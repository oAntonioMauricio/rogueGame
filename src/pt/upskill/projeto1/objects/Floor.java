package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

public class Floor implements ImageTile {

    private Position position;

    public Floor(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Floor";
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
