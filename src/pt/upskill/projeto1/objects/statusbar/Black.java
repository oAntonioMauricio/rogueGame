package pt.upskill.projeto1.objects.statusbar;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

public class Black implements ImageTile, Serializable {
    private Position position;

    public Black(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Black";
    }

    @Override
    public Position getPosition() {
        return position;
    }


}
