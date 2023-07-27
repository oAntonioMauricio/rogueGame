package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.rogue.utils.Position;

public class Bat extends Enemy {
    private Position position;
    private int power;

    public Bat(Position position, int power) {
        this.position = position;
        this.power = power;
    }

    @Override
    public String getName() {
        return "Bat";
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
