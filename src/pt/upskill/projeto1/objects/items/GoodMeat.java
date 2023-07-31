package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.rogue.utils.Position;

public class GoodMeat extends Item {

    private int health = 25;

    public GoodMeat(Position position) {
        super(position);
    }

    @Override
    public String getName() {
        return "GoodMeat";
    }

    public int getHealth() {
        return this.health;
    }
}
