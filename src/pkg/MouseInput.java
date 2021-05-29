package pkg;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * @author Zack (RealTutsGML)
 *
 */
public class MouseInput extends MouseAdapter { //Keeps track of user mouse actions

	private Handler handler;
	private Camera camera;
	private Game game;
	private SpriteSheet ss;
	
	public MouseInput(Handler handler, Camera camera, Game game, SpriteSheet ss) {
		this.handler = handler;
		this.camera = camera;
		this.game = game;
		this.ss = ss;
	}
	public void mousePressed (MouseEvent e) {
		int mx = (int) (e.getX() + camera.getX());
		int my = (int) (e.getY() + camera.getY());
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Player && game.ammo > 0) {
				handler.addObject(new Bullet(tempObject.getX() + 16, tempObject.getY() + 24, ID.Bullet, handler, mx, my, ss));
				game.ammo --;
			}
		}
	}
}
