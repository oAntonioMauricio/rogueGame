package pt.upskill.projeto1.objects.items;

import pt.upskill.projeto1.rogue.utils.Position;

public class Key extends Item {

    private String keyId;

    public Key(Position position, String keyId) {
        super(position);
        this.keyId = keyId;
    }

    @Override
    public String getName() {
        return "Key";
    }

    public String getKeyId() {
        return keyId;
    }

    @Override
    public boolean isConsumable() {
        return false;
    }
}
