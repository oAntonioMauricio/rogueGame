package pt.upskill.projeto1.objects.props;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

public class StatueNormal implements ImageTile, Serializable {
    private Position position;

    public StatueNormal(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "StatueNormal";
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
