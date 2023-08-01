package pt.upskill.projeto1.objects.hero;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.items.Item;
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
