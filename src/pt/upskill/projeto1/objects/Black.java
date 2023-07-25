package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

public class Black implements ImageTile {
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

    @Override
    public Boolean isPossibleToWalk() {
        return false;
    }
}
