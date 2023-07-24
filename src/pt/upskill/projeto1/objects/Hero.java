package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.List;
import java.util.Objects;

public class Hero implements ImageTile {

    private Position position;

    public Hero(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Hero";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position, List<ImageTile> tileList) {
        boolean move = true;

        for (ImageTile tile : tileList) {
            if (position.getX() == tile.getPosition().getX() && position.getY() == tile.getPosition().getY()) {
                System.out.println("You stepped on: " + tile.getName());
                if (Objects.equals(tile.getName(), "Wall")) {
                    move = !move;
                }
            }
        }

        if (move) {
            this.position = position;
        }

    }
}
