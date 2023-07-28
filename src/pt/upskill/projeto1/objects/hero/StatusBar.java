package pt.upskill.projeto1.objects.hero;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.statusbar.Fire;
import pt.upskill.projeto1.objects.statusbar.Green;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class StatusBar {
    private List<ImageTile[]> statusBar;

    public StatusBar() {
        this.statusBar = new ArrayList<ImageTile[]>();

        ImageTile[] fireballs = {new Fire(new Position(0, 0)), new Fire(new Position(1, 0)), new Fire(new Position(2, 0))};
        ImageTile[] health = {new Green(new Position(3, 0)), new Green(new Position(4, 0)), new Green(new Position(5, 0)), new Green(new Position(6, 0))};
        ImageTile[] items = {};

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
}
