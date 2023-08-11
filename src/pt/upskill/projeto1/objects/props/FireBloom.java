package pt.upskill.projeto1.objects.props;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

public class FireBloom implements ImageTile, Serializable {
    private Position position;

    public FireBloom(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "FireBloom";
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
