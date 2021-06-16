package pkg.models;

import pkg.game.Handler;
import pkg.view.SpriteSheet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * @author Devin
 */
public class Exit extends GameObject { //this is the magenta square that is the "goal" for our game.

    public Exit(int x, int y, Handler handler, SpriteSheet ss) {
        super(x, y, handler, ss);

    }

    /**
     * Tick() method is called to update/animate the game. Each execution of tick represents a new frame in the game.
     * Thus, it is an essential method for all GameObjects.
     */
    @Override
    public void tick() {
    }

    @Override
    public void animation(Graphics g) {
    }

    /**
     * Render() is called to draw objects into our game.
     *
     * @param Graphics g
     */
    @Override
    public void render(Graphics g) {
        g.setColor(Color.magenta);
        g.fillRect(x, y, 32, 32);
    }

    /**
     * getBounds() represents the actual space an object takes up.
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getBounds() {

        return new Rectangle(x, y, 32, 32);
    }

    //We can use the methods below to specify multiple differential bounds for our GameObjects.
    //However, for the demonstrative purposes of this template, this functionality is only used by our Player and Enemy objects.
    @Override
    public Rectangle getTopBounds() {

        return null;
    }

    @Override
    public Rectangle getBotBounds() {

        return null;
    }

    @Override
    public Rectangle getLeftBounds() {

        return null;
    }

    @Override
    public Rectangle getRightBounds() {

        return null;
    }

}
