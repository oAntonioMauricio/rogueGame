package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.rogue.utils.Position;

public class GoodMeat extends Item {

    public GoodMeat(Position position) {
        super(position, 25);
    }

    @Override
    public String getName() {
        return "GoodMeat";
    }
}
