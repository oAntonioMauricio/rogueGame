package pt.upskill.projeto1.objects.hero;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.game.Room;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.items.Hammer;
import pt.upskill.projeto1.objects.items.Item;
import pt.upskill.projeto1.objects.items.Key;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.objects.statusbar.Green;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class StatusBar {

    private List<ImageTile[]> statusBar;

    public StatusBar() {
        this.statusBar = new ArrayList<>();

        ImageTile[] fireballs = {new Fire(new Position(0, 0)), new Fire(new Position(1, 0)), new Fire(new Position(2, 0))};
        ImageTile[] health = {new Green(new Position(3, 0)), new Green(new Position(4, 0)), new Green(new Position(5, 0)), new Green(new Position(6, 0))};
        ImageTile[] items = {null, null, null};

        // index 0
        this.statusBar.add(fireballs);
        // index 1
        this.statusBar.add(health);
        // index 2
        this.statusBar.add(items);
    }

    public List<ImageTile[]> getStatusBarList() {
        return statusBar;
    }

    public ImageTile[] getFireballsArray() {
        return getStatusBarList().get(0);
    }

    public ImageTile[] getHealthArray() {
        return getStatusBarList().get(1);
    }

    public ImageTile[] getItemArray() {
        return getStatusBarList().get(2);
    }

    public void removeItem(int slot) {
        // access singletons
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        GameSingleton gameSingleton = GameSingleton.getInstance();
        List<ImageTile> tiles = gameSingleton.getTiles();
        List<Room> roomList = gameSingleton.getRoomList();
        Hero hero = gameSingleton.getHero();

        Item currentSlot = (Item) this.statusBar.get(2)[slot];

        if (currentSlot == null) {
            gui.setStatus("You don't have an item in the slot: " + (slot + 1));
        } else {
            // get room index // this is here because it's PRIMITIVE
            int roomIndex = gameSingleton.getRoomIndex();

            // drop item and check if it can be dropped
            if (currentSlot.dropItem()) {
                // add item to room
                roomList.get(roomIndex).getItemList().add(currentSlot);
                tiles.add(currentSlot);
                gui.addImage(currentSlot);

                // TIRAR O DANO DO HAMMER AQUI?!
                if (currentSlot instanceof Key) {
                    gui.setStatus("You removed: " + ((Key) currentSlot).getKeyId());
                } else if (currentSlot instanceof Hammer) {
                    hero.setPower(hero.getPower() - ((Hammer) currentSlot).getItemPower());
                    gui.setStatus("You removed the Hammer and lost " + ((Hammer) currentSlot).getItemPower() + ". Total power: " + hero.getPower());
                } else {
                    gui.setStatus("You removed: " + currentSlot.getName());
                }

                // remove item from status bar
                this.statusBar.get(2)[slot] = null;

                // organize at the end
                organizeItemArray();
            }
        }
    }

    public int itemArrayEmptyIndex() {
        // return -1 if array is full

        for (int i = 0; i < getItemArray().length; i++) {
            if (getItemArray()[i] == null) {
                return i;
            }
        }

        return -1;
    }

    public void organizeItemArray() {
        for (int i = 1; i < this.statusBar.get(2).length; i++) {
            if (this.statusBar.get(2)[i - 1] == null && this.statusBar.get(2)[i] instanceof Item) {
                // update position on the status bar
                ((Item) this.statusBar.get(2)[i]).setPosition(new Position((7 + (i - 1)), 0));
                // place it one to the left
                this.statusBar.get(2)[i - 1] = this.statusBar.get(2)[i];
                this.statusBar.get(2)[i] = null;
            }
        }
    }
}
