package pt.upskill.projeto1.objects.enemies;

import pt.upskill.projeto1.rogue.utils.Position;

public class Bat extends Enemy {
    private Position position;
    private int initialHealth = 25;
    private int health = 25;
    private int points = 25;

    public Bat(Position position) {
        this.position = position;
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
    public double getPower() {
        return this.power * 1;
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
