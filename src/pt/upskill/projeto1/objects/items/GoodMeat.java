package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.rogue.utils.Position;

public class GoodMeat extends Item {
    private double health = 25;

    public GoodMeat(Position position) {
        super(position);
    }

    @Override
    public boolean isConsumable() {
        return true;
    }

    @Override
    public String getName() {
        return "GoodMeat";
    }

    public double getHealth() {
        return this.health;
    }
}
