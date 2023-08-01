package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.rogue.utils.Position;

public class Hammer extends Item {

    private int itemPower = 25;

    public Hammer(Position position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Hammer";
    }

    public int getItemPower() {
        return itemPower;
    }
}