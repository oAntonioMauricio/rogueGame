package pt.upskill.projeto1.objects.props;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

public class Trap implements ImageTile, Serializable {
    private Position position;

    private int damage = 25;

    public Trap(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Trap";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public int getDamage() {
        return damage;
    }
}
