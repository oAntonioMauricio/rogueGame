package pt.upskill.projeto1.objects.enemies;

import pt.upskill.projeto1.rogue.utils.Position;

public class Thief extends Enemy {
    private Position position;
    private int health = 50;
    private int power = 50;

    private int points = 75;

    public Thief(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Thief";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(int newHealth) {
        this.health = newHealth;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
