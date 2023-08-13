package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.rogue.utils.Position;

public class Flail extends Item {
    private int itemPower = 25;

    private int points = 30;

    public Flail(Position position) {
        super(position);
    }

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public String getName() {
        return "Flail";
    }

    public int getItemPower() {
        return itemPower;
    }

    public int getPoints() {
        return points;
    }
}
