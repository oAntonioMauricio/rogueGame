package pt.upskill.projeto1.objects.enemies;

import pt.upskill.projeto1.rogue.utils.Position;

public class Skeleton extends Enemy {
    private Position position;
    private int initialHealth = 50;
    private int health = 50;
    private int points = 50;

    public Skeleton(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Skeleton";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public double getPower() {
        return this.power * 2;
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
    public int getInitialHealth() {
        return this.initialHealth;
    }

    @Override
    public void setHealth(int newHealth) {
        this.health = newHealth;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


}
