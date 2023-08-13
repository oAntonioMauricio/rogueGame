package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.props.CityFloor;
import pt.upskill.projeto1.objects.props.Wall;
import pt.upskill.projeto1.objects.door.Door;
import pt.upskill.projeto1.objects.hero.Hero;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;

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

    public abstract boolean isConsumable();

    public boolean dropItem() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        Hero hero = gameSingleton.getHero();

        int heroX = hero.getPosition().getX();
        int heroY = hero.getPosition().getY();

        boolean dropAvailable = true;

        for (ImageTile tile : gameSingleton.getTiles()) {
            if (tile.getPosition().getX() == heroX && tile.getPosition().getY() == heroY) {
                if (tile instanceof Wall || tile instanceof Item || tile instanceof Door || tile instanceof CityFloor) {
                    dropAvailable = false;
                }
            }
        }

        if (dropAvailable) {
            this.position = new Position(heroX, heroY);

            return true;
        } else {
            gui.setStatus("Can't drop the item here.");
            return false;
        }
    }

}
