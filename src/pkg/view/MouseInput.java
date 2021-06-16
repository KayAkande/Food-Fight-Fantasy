package pkg.view;

import pkg.game.Game;
import pkg.game.Handler;
import pkg.models.Bullet;
import pkg.models.GameObject;
import pkg.models.Player;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Zack (RealTutsGML)
 */
public class MouseInput extends MouseAdapter { //Keeps track of user mouse actions

    private final Handler handler;
    private final Camera camera;
    private final Game game;
    private final SpriteSheet ss;

    public MouseInput(Handler handler, Camera camera, Game game, SpriteSheet ss) {
        this.handler = handler;
        this.camera = camera;
        this.game = game;
        this.ss = ss;
    }

    public void mousePressed(MouseEvent e) {
        int mx = (int) (e.getX() + camera.getX());
        int my = (int) (e.getY() + camera.getY());

        for (GameObject tempObject : handler.getObjects()) {

            if (tempObject instanceof Player && game.ammo > 0) {
                handler.addObject(new Bullet(tempObject.getX() + 16, tempObject.getY() + 24, handler, mx, my, ss));
                game.ammo--;
            }
        }
    }
}
