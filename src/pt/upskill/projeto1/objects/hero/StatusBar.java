package pt.upskill.projeto1.objects.hero;

import pt.upskill.projeto1.game.GameSingleton;
import pt.upskill.projeto1.game.Room;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.items.Hammer;
import pt.upskill.projeto1.objects.items.Item;
import pt.upskill.projeto1.objects.items.Key;
import pt.upskill.projeto1.objects.statusbar.Black;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.objects.statusbar.Green;
import pt.upskill.projeto1.objects.statusbar.Red;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusBar implements Serializable {
    // propreties 🔽
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

    // methods 🔽
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

    // HP IS ALSO UPDATED IN UPDATE STATUS!
    // SCORE IS ALSO UPDATED!
    // SECOND STATUS IS ALSO UPDATED HERE
    public void updateStatus() {
        // singelton 🔽
        GameSingleton gameSingleton = GameSingleton.getInstance();
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // reset status
        gui.clearStatus();

        // update HP
        updateHeroHP();

        // System.out.println("updating status...");
        // black background
        for (int i = 0; i < 10; i++) {
            gui.addStatusImage(new Black(new Position(i, 0)));
        }

        // fireballs
        for (ImageTile fireball : getStatusBarList().get(0)) {
            gui.addStatusImage(fireball);
        }

        // health
        for (ImageTile health : getStatusBarList().get(1)) {
            gui.addStatusImage(health);
        }

        // items
        for (ImageTile item : getItemArray()) {
            if (item != null) {
                gui.addStatusImage(item);
            }
        }


        // update score
        gui.setStatusTwo("Score: " + gameSingleton.getScore() + " Power: " + gameSingleton.getHero().getPower());
    }

    public void updateHeroHP() {
        GameSingleton gameSingleton = GameSingleton.getInstance();
        Hero hero = gameSingleton.getHero();

        ImageTile[] healthArray = getHealthArray();

        if (hero.getHealth() == 100) {
            for (int i = 0, j = 3; i < 4; i++, j++) {
                healthArray[i] = new Green(new Position(j, 0));
            }
        }

        if (hero.getHealth() == 75) {
            healthArray[0] = new Green(new Position(3, 0));
            healthArray[1] = new Green(new Position(4, 0));
            healthArray[2] = new Green(new Position(5, 0));
            healthArray[3] = new Red(new Position(6, 0));
        }

        if (hero.getHealth() == 50) {
            healthArray[0] = new Green(new Position(3, 0));
            healthArray[1] = new Green(new Position(4, 0));
            healthArray[2] = new Red(new Position(5, 0));
            healthArray[3] = new Red(new Position(6, 0));
        }

        if (hero.getHealth() == 25) {
            healthArray[0] = new Green(new Position(3, 0));
            healthArray[1] = new Red(new Position(4, 0));
            healthArray[2] = new Red(new Position(5, 0));
            healthArray[3] = new Red(new Position(6, 0));
        }

        if (hero.getHealth() <= 0) {
            healthArray[0] = new Red(new Position(3, 0));
            healthArray[1] = new Red(new Position(4, 0));
            healthArray[2] = new Red(new Position(5, 0));
            healthArray[3] = new Red(new Position(6, 0));
        }
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

            // check if it can be dropped to continue
            if (currentSlot.dropItem()) {
                // add item to room
                roomList.get(roomIndex).getItemList().add(currentSlot);
                tiles.add(currentSlot);
                gui.addImage(currentSlot);

                if (currentSlot instanceof Key) {
                    gui.setStatus("You removed: " + ((Key) currentSlot).getKeyId());
                } else if (currentSlot instanceof Hammer) {
                    // remove hammer power
                    hero.setPower(hero.getPower() - ((Hammer) currentSlot).getItemPower());
                    gui.setStatus("You removed the Hammer and lost " + ((Hammer) currentSlot).getItemPower() + ". Total power: " + hero.getPower());
                } else {
                    gui.setStatus("You removed: " + currentSlot.getName());
                }

                // remove item from status bar
                this.statusBar.get(2)[slot] = null;

                // organize and update at the end
                organizeItemArray();
                updateStatus();
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

    public void setStatusBar(List<ImageTile[]> statusBar) {
        this.statusBar = statusBar;
    }
}
