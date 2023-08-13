package pt.upskill.projeto1.objects.props;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

public class WallCity implements ImageTile, Serializable {
    private Position position;

    public WallCity(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "WallCity";
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
