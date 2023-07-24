package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Floor;
import pt.upskill.projeto1.objects.Hero;
import pt.upskill.projeto1.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Engine {

    public void init(){
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

        List<ImageTile> tiles = new ArrayList<>();
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                tiles.add(new Floor(new Position(i, j)));
            }
        }

        Hero hero = new Hero(new Position(4, 3));
        tiles.add(hero);

        gui.setEngine(this);
        gui.newImages(tiles);
        gui.go();

        gui.setStatus("O jogo começou!");

        while (true){
            gui.update();
        }
    }

    public void notify(int keyPressed){
        if (keyPressed == KeyEvent.VK_DOWN){
            System.out.println("User pressed down key!");
        }
        if (keyPressed == KeyEvent.VK_UP){
            System.out.println("User pressed up key!");
        }
        if (keyPressed == KeyEvent.VK_LEFT){
            System.out.println("User pressed left key!");
        }
        if (keyPressed == KeyEvent.VK_RIGHT){
            System.out.println("User pressed right key!");
        }
    }

    public static void main(String[] args){
        Engine engine = new Engine();
        engine.init();
    }

}
