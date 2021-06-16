package pkg.models;

import pkg.game.Game;
import pkg.game.Handler;
import pkg.view.SpriteSheet;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author Zack(RealTutsGML), Devin
 */
public class Player extends GameObject { //Simple player subclass for user-controlled game objects


    boolean dmgTaken = false;
    double timer = 0;
    int anim = 0;
    Game game;


    private final BufferedImage[] player_image = new BufferedImage[3];


    public Player(int x, int y, Handler handler, Game game, SpriteSheet ss) {
        super(x, y, handler, ss);
        this.game = game;

        player_image[0] = ss.grabImage(1, 1, 32, 48);
        player_image[1] = ss.grabImage(2, 1, 32, 48);
        player_image[2] = ss.grabImage(3, 1, 32, 48);

    }


    @Override
    public void tick() {


        x += velX;
        y += velY;

        collision();


        //movement
        if (handler.isUp()) velY = -5;
        else if (!handler.isDown()) velY = 0;

        if (handler.isDown()) velY = 5;
        else if (!handler.isUp()) velY = 0;

        if (handler.isRight()) velX = 5;
        else if (!handler.isLeft()) velX = 0;

        if (handler.isLeft()) velX = -5;
        else if (!handler.isRight()) velX = 0;

        if (dmgTaken == true) {
            timer++;

            if (timer >= 50) {
                timer = 0;
                dmgTaken = false;
            }

        }
    }


    private void collision() { //You can fix the sticking by creating separate floor and wall objects

        for (GameObject tempObject : handler.getObjects()) {

            if (tempObject instanceof Block) {

                if (getLeftBounds().intersects(tempObject.getBounds())) {

                    x = (tempObject.getX() + 32);


                }

                if (getRightBounds().intersects(tempObject.getBounds())) {

                    x = (tempObject.getX() - 32);

                }

                if (getTopBounds().intersects(tempObject.getBounds())) {

                    y = (tempObject.getY() + 32);

                }

                if (getBotBounds().intersects(tempObject.getBounds())) {

                    y = (tempObject.getY() - 48);

                }


            }

            if (tempObject instanceof Enemy) {

                if (getBounds().intersects(tempObject.getBounds())) {


                    if (dmgTaken == false) {
                        game.hp -= 20;
                        dmgTaken = true;

                    }


                }
            }
            if (tempObject instanceof AmmoCrate) {

                if (getBounds().intersects(tempObject.getBounds())) {

                    if (!(game.ammo >= 100))
                        handler.removeObject(tempObject);

                    game.ammo += 10;
                    if (game.ammo > 100)
                        game.ammo = 100;


                }

            }

            if (tempObject instanceof Exit) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    game.nextLvl = true;
                }
            }
        }
    }


    /**
     * Render() is called to draw objects into our game.
     *
     * @param Graphics g
     */
    @Override
    public void render(Graphics g) { //describes how the player object will be rendered in the game window

        if (dmgTaken == false || (((timer / 5) % 1) != 0 && ((timer / 4) % 1) != 0 && ((timer / 3) % 1) != 0)) {


            animation(g);


        }


    }

    /**
     * getTopBounds() represents the upper bounding box for our object (for differential collision).
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getTopBounds() {

        return new Rectangle(x + 5, y, 22, 10);
    }

    /**
     * getBotBounds() represents the lower bounding box for our object (for differential collision).
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getBotBounds() {

        return new Rectangle(x + 5, y + 38, 22, 10);
    }

    /**
     * getLeftBounds() represents the left bounding box for our object (for differential collision).
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getLeftBounds() {

        return new Rectangle(x, y + 5, 10, 38);
    }

    @Override
    /**
     * getRightBounds() represents the right bounding box for our object (for differential collision).
     * It is used for object collision to tell us when objects intersect each other.
     * @return new Rectangle (x position, y position, width, height);
     */
    public Rectangle getRightBounds() {

        return new Rectangle(x + 22, y + 5, 10, 38);
    }

    /**
     * getBounds() represents the actual space an object takes up.
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getBounds() {

        return new Rectangle(x, y, 32, 48);
    }


    /**
     * animation() lets us set animation sequences to be executed in our program
     *
     * @param Graphics object g
     */
    @Override
    public void animation(Graphics g) {
        if (velX != 0 || velY != 0) {
            if (anim > -1 && anim < 50) {
                g.drawImage(player_image[2], x, y, null);
                anim++;
            } else if (anim > 49 && anim < 100) {
                g.drawImage(player_image[1], x, y, null);
                anim++;
            } else if (anim == 100) {
                g.drawImage(player_image[1], x, y, null);
                anim = 0;
            }


        } else g.drawImage(player_image[0], x, y, null);
    }


}
