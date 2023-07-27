package pt.upskill.projeto1.objects.enemies;

import pt.upskill.projeto1.rogue.utils.Position;

public class BadGuy extends Enemy {
    private Position position;
    private int power = 25;

    public BadGuy(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "BadGuy";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Boolean isPossibleToWalk() {
        return true;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
