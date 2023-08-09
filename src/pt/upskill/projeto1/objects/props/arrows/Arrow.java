package pt.upskill.projeto1.objects.props.arrows;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

public class Arrow implements ImageTile, Serializable {

    private Position position;

    private String name;

    public Arrow(Position position, String name) {
        this.position = position;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
