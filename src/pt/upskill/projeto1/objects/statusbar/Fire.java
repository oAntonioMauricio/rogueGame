package pt.upskill.projeto1.objects.statusbar;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.props.Wall;
import pt.upskill.projeto1.objects.enemies.Enemy;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;
import java.util.List;

public class Fire implements ImageTile, FireTile, Serializable {

    private Position position;

    public Fire(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Fire";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean validateImpact() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();

        boolean stop = false;
        boolean toRemove = false;
        ImageTile tileToRemove = null;

        for (ImageTile tile : tiles) {
            if (this.position.getX() == tile.getPosition().getX() && this.position.getY() == tile.getPosition().getY()) {
                if (tile instanceof Enemy || tile instanceof Wall || tile instanceof Door) {
                    gui.setStatus("Hit on " + tile.getName());
                    stop = true;
                    if (tile instanceof Enemy) {
                        toRemove = true;
                        tileToRemove = tile;
                        break;
                    }

                }
            }
        }

        if (toRemove) {
            ((Enemy) tileToRemove).death(((Enemy) tileToRemove).getInitialHealth());
            // update statusbar to display the new score
            gameSingleton.getStatusBar().updateStatus();
            return false;
        }

        return !stop;

        // old
        /*
        for (ImageTile tile : tiles) {
            if (this.position.getX() == tile.getPosition().getX() && this.position.getY() == tile.getPosition().getY()) {
                if (tile instanceof Enemy || tile instanceof Wall || tile instanceof Door) {
                    gui.setStatus("Hit on " + tile.getName());
                    if (tile instanceof Enemy) {
                        ((Enemy) tile).death(((Enemy) tile).getHealth());
                        // update statusbar to display the new score
                        gameSingleton.getStatusBar().updateStatus();
                    }
                    return false;
                }
            }
        }



        return true;

         */
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
