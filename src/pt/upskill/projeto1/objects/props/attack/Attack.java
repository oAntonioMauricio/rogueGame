package pt.upskill.projeto1.objects.props.attack;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.objects.props.Wall;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;
import java.util.List;

public class Attack implements ImageTile, FireTile, Serializable {
    private Position position;

    public Attack(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Attack";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public boolean validateImpact() {
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        // turns off immediately because it's at the enemy spot
        return false;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
