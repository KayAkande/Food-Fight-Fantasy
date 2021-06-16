package pkg.models;

import pkg.game.Handler;
import pkg.view.SpriteSheet;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Zack (RealTutsGML), Devin
 */
public class Enemy extends GameObject {

    Random r = new Random();
    int choose = 0;
    int hp = 100;
    boolean slowed = false;
    double speed = 1;
    int timer = 0;
    private final BufferedImage[] enemy_image = new BufferedImage[3];

    public Enemy(int x, int y, Handler handler, SpriteSheet ss) {
        super(x, y, handler, ss);
        enemy_image[0] = ss.grabImage(4, 1, 32, 32);
        enemy_image[1] = ss.grabImage(5, 1, 32, 32);
        enemy_image[2] = ss.grabImage(6, 1, 32, 32);
    }

    /**
     * Tick() method is called to update/animate the game. Each execution of tick represents a new frame in the game.
     * Thus, it is an essential method for all GameObjects.
     */
    @Override
    public void tick() {
        x += velX;
        y += velY;


        choose = r.nextInt(50);

        for (GameObject tempObject : handler.getObjects()) {

            if (tempObject instanceof Block) {
                if (getLeftBounds().intersects(tempObject.getBounds())) {

                    x = (tempObject.getX() + 48);
                    velX *= -1;

                }

                if (getRightBounds().intersects(tempObject.getBounds())) {

                    x = (tempObject.getX() - 64);
                    velX *= -1;

                }

                if (getTopBounds().intersects(tempObject.getBounds())) {

                    y = (tempObject.getY() + 48);
                    velY *= -1;
                }

                if (getBotBounds().intersects(tempObject.getBounds())) {

                    y = (tempObject.getY() - 48);
                    velY *= -1;
                }


            } else if (choose == 0) {
                velX = (float) ((r.nextInt(4 - -4) + -4) * speed);
                velY = (float) ((r.nextInt(4 - -4) + -4) * speed);
            }

            if (tempObject instanceof Bullet) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    hp -= 50;
                    handler.removeObject(tempObject);
                    velX *= 0.1;
                    velY *= 0.1;
                    slowed = true;

                }
            }
        }
        if (slowed == true) {
            speed = 0.1;
            timer++;

            if (timer == 50) {
                speed = 1;
                timer = 0;
                slowed = false;
            }

        }

        if (hp <= 0) handler.removeObject(this);
	}


    /**
     * Render() is called to draw objects into our game.
     *
     * @param Graphics g
     */
    @Override
    public void render(Graphics g) {
        animation(g);


    }

    /**
     * getBounds() represents the actual space an object takes up.
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    public Rectangle getBounds() {

        return new Rectangle(x, y, 32, 32);
    }

    /**
     * Enemy objects have a bigger invisible bounding box for block collision to stop them from going into corners.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    public Rectangle getBoundsBig() {

        return new Rectangle(x - 16, y - 16, 64, 64);
    }

    /**
     * Represents the upper bounding box for our object (for differential collision).
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getTopBounds() {

        return new Rectangle(x - 11, y - 16, 54, 32);
    }

    /**
     * Represents the lower bounding box for our object (for differential collision).
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getBotBounds() {

        return new Rectangle(x - 11, y + 16, 54, 32);
    }

    /**
     * Represents the left bounding box for our object (for differential collision).
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getLeftBounds() {

        return new Rectangle(x - 16, y - 11, 32, 54);
    }

    /**
     * Represents the right bounding box for our object (for differential collision).
     * It is used for object collision to tell us when objects intersect each other.
     *
     * @return new Rectangle (x position, y position, width, height);
     */
    @Override
    public Rectangle getRightBounds() {

        return new Rectangle(x + 16, y - 11, 32, 54);

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
                g.drawImage(enemy_image[1], x, y, null);
                anim++;
            } else if (anim > 49 && anim < 100) {
                g.drawImage(enemy_image[2], x, y, null);
                anim++;
            } else if (anim == 100) {
                g.drawImage(enemy_image[1], x, y, null);
                anim = 0;
            }


        } else g.drawImage(enemy_image[0], x, y, null);
    }
}
	

