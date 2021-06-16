package pkg.view;

import pkg.models.GameObject;

public class Camera {
    /**
     * @author Zack (RealTutsGML)
     */
    private float x;
    private float y;

    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Tick() method is called to update/animate the game. Each execution of tick represents a new frame in the game.
     * Thus, it is an essential method for our camera as well.
     */
    public void tick(GameObject object) {

        x += ((object.getX() - x) - 1000 / 2f) * 0.05f;
        y += ((object.getY() - y) - 563 / 2f) * 0.05f;

        if (x <= 0) x = 0;
        if (x >= 1063) x = 1063;
        if (y <= 0) y = 0;
        if (y >= 611) y = 611;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
