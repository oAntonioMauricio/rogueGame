package pt.upskill.projeto1.objects.props.arrows;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

public class ArrowUp implements ImageTile, Serializable {

    private Position position;

    public ArrowUp(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "ArrowUp";
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
