package pt.upskill.projeto1.objects;

import javafx.geometry.Pos;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.rogue.utils.Vector2D;

import java.util.List;

public abstract class Enemy implements ImageTile {

    public abstract void setPosition(Position position, List<ImageTile> tileList);

    public abstract Vector2D randomPosition();

    public abstract Vector2D moveToHero(Hero hero);
}
