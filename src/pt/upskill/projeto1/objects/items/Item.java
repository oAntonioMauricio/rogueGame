package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.game.Room;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Floor;
import pt.upskill.projeto1.objects.Wall;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;
import java.util.List;

public abstract class Item implements ImageTile, Serializable {
    private Position position;

    private boolean alreadyPickedUp = false;

    public Item(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setAlreadyPickedUp(boolean alreadyPickedUp) {
        this.alreadyPickedUp = alreadyPickedUp;
    }

    public boolean getAlreadyPickedUp() {
        return this.alreadyPickedUp;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean dropItem() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        Hero hero = gameSingleton.getHero();

        int heroX = hero.getPosition().getX();
        int heroY = hero.getPosition().getY();

        int dropTop = hero.getPosition().getY() - 1;
        int dropDown = hero.getPosition().getY() + 1;
        int dropLeft = hero.getPosition().getX() - 1;
        int dropRight = hero.getPosition().getX() + 1;

        int toDropX = heroX;
        int toDropY = dropTop;

        boolean dropAvailable = true;

        for (ImageTile tile : gameSingleton.getTiles()) {
            if (tile.getPosition().getX() == heroX && tile.getPosition().getY() == dropTop) {
                if (tile instanceof Wall || tile instanceof Item || tile instanceof Door) {
                    dropAvailable = false;
                }
            }
        }

        if (dropAvailable) {
            this.position = new Position(toDropX, toDropY);
            return true;
        } else {
            gui.setStatus("Can't drop the item here.");
            return false;
        }
    }

}
