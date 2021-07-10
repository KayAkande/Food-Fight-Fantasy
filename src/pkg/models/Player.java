package pkg.models;

import pkg.game.Game;
import pkg.game.Handler;
import pkg.view.SpriteSheet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author Zack(RealTutsGML), Devin
 */
public class Player extends GameObject { //Simple player subclass for user-controlled game objects

	boolean faceRight = true;
	boolean dmgTaken = false;
	double timer = 0;
	int anim = 0;
	Game game;

	private final BufferedImage[] player_image = new BufferedImage[50]; {


		for (int i = 0; i < 50; i++) {
			if (i<5) {
				player_image[i] = ss.grabImage(i+1, 1, 192, 192);
			}
			else if (i<10) {
				player_image[i] = ss.grabRImage(i-4, 1, 192, 192);
			}
			else if (i<15) {
				player_image[i] = ss.grabImage(i-9, 2, 192, 192);
			}
			else if (i<20) {
				player_image[i] = ss.grabRImage(i-14, 2, 192, 192);
			}
			else if (i<25) {
				player_image[i] = ss.grabImage(i-19, 3, 192, 192);
			}
			else if (i<30) {
				player_image[i] = ss.grabRImage(i-24, 3, 192, 192);
			}


		}}



	public Player(int x, int y, Handler handler, Game game, SpriteSheet ss) {
		super(x, y, handler, ss);
		this.game = game;





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

		if (handler.isLeft() && handler.isRight()) velX = 0;

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

					x = (tempObject.getX() + 64);


				}

				if (getRightBounds().intersects(tempObject.getBounds())) {

					x = (tempObject.getX() - 182);

				}

				if (getTopBounds().intersects(tempObject.getBounds())) {

					y = (tempObject.getY() + 64);

				}

				if (getBotBounds().intersects(tempObject.getBounds())) {

					y = (tempObject.getY() - 182);

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

		return new Rectangle(x + 10, y, 162, 10);
	}

	/**
	 * getBotBounds() represents the lower bounding box for our object (for differential collision).
	 * It is used for object collision to tell us when objects intersect each other.
	 *
	 * @return new Rectangle (x position, y position, width, height);
	 */
	@Override
	public Rectangle getBotBounds() {

		return new Rectangle(x + 10, y + 172, 162, 10);
	}

	/**
	 * getLeftBounds() represents the left bounding box for our object (for differential collision).
	 * It is used for object collision to tell us when objects intersect each other.
	 *
	 * @return new Rectangle (x position, y position, width, height);
	 */
	@Override
	public Rectangle getLeftBounds() {

		return new Rectangle(x, y + 10, 10, 162);
	}

	@Override
	/**
	 * getRightBounds() represents the right bounding box for our object (for differential collision).
	 * It is used for object collision to tell us when objects intersect each other.
	 * @return new Rectangle (x position, y position, width, height);
	 */
	public Rectangle getRightBounds() {

		return new Rectangle(x + 172, y + 10, 10, 162);
	}

	/**
	 * getBounds() represents the actual space an object takes up.
	 * It is used for object collision to tell us when objects intersect each other.
	 *
	 * @return new Rectangle (x position, y position, width, height);
	 */
	@Override
	public Rectangle getBounds() {

		return new Rectangle(x, y, 192, 192);
	}


	/**
	 * animation() lets us set animation sequences to be executed in our program
	 *
	 * @param Graphics object g
	 */
	@Override
	public void animation(Graphics g) {
		/*
		g.setColor(Color.blue);
		g.drawRect(x + 10, y, 162, 10);
		g.drawRect(x + 10, y + 172, 162, 10);
		g.drawRect(x, y + 10, 10, 162);
		g.drawRect(x + 172, y + 10, 10, 162); 
		 
		*/


		if (handler.isRight() && !handler.isLeft()) {
			faceRight = true;
			if (anim > -1 && anim < 10) {
				g.drawImage(player_image[1], x, y, null);
				anim++;
			} else if (anim > 9 && anim < 20) {
				g.drawImage(player_image[2], x, y, null);
				anim++;
			} else if (anim > 19 && anim < 30) {
				g.drawImage(player_image[3], x, y, null);
				anim++;
			} else if (anim > 29 && anim < 40) {
				g.drawImage(player_image[4], x, y, null);
				anim++;
			} else if (anim == 40) {
				g.drawImage(player_image[4], x, y, null);
				anim = 0;
			}
		}
		else if (handler.isLeft() && !handler.isRight()) {
			faceRight = false;
			if (anim > -1 && anim < 10) {
				g.drawImage(player_image[6], x, y, null);
				anim++;
			} else if (anim > 9 && anim < 20) {
				g.drawImage(player_image[7], x, y, null);
				anim++;
			} else if (anim > 19 && anim < 30) {
				g.drawImage(player_image[8], x, y, null);
				anim++;
			} else if (anim > 29 && anim < 40) {
				g.drawImage(player_image[9], x, y, null);
				anim++;
			} else if (anim == 40) {
				g.drawImage(player_image[9], x, y, null);
				anim = 0;
			}
		}
		else if (handler.isUp() && !handler.isDown()) {
			if (faceRight) {
				if	 (anim > -1 && anim < 10) {
					g.drawImage(player_image[10], x, y, null);
					anim++;
				}
				else if (anim > 9 && anim < 20) {
					g.drawImage(player_image[11], x, y, null);
					anim++;
				}
				else if (anim > 19 && anim < 30) {
					g.drawImage(player_image[12], x, y, null);
					anim++;
				}
				else if (anim > 29 && anim < 40) {
					g.drawImage(player_image[13], x, y, null);
					anim++;
				}
				else if (anim == 40) {
					g.drawImage(player_image[13], x, y, null);
					anim = 0;
				}
			}
			else {
				if	 (anim > -1 && anim < 10) {
					g.drawImage(player_image[15], x, y, null);
					anim++;
				}
				else if (anim > 9 && anim < 20) {
					g.drawImage(player_image[16], x, y, null);
					anim++;
				}
				else if (anim > 19 && anim < 30) {
					g.drawImage(player_image[17], x, y, null);
					anim++;
				}
				else if (anim > 29 && anim < 40) {
					g.drawImage(player_image[18], x, y, null);
					anim++;
				}
				else if (anim == 40) {
					g.drawImage(player_image[18], x, y, null);
					anim = 0;
				}			 

			}

		}
		else if (handler.isDown() && !handler.isUp()) {
			if (faceRight) {
				if	 (anim > -1 && anim < 10) {
					g.drawImage(player_image[20], x, y, null);
					anim++;
				}
				else if (anim > 9 && anim < 20) {
					g.drawImage(player_image[21], x, y, null);
					anim++;
				}
				else if (anim > 19 && anim < 30) {
					g.drawImage(player_image[22], x, y, null);
					anim++;
				}
				else if (anim > 29 && anim < 40) {
					g.drawImage(player_image[23], x, y, null);
					anim++;
				}
				else if (anim == 40) {
					g.drawImage(player_image[24], x, y, null);
					anim = 0;
				}
			}
			else {
				if	 (anim > -1 && anim < 10) {
					g.drawImage(player_image[25], x, y, null);
					anim++;
				}
				else if (anim > 9 && anim < 20) {
					g.drawImage(player_image[26], x, y, null);
					anim++;
				}
				else if (anim > 19 && anim < 30) {
					g.drawImage(player_image[27], x, y, null);
					anim++;
				}
				else if (anim > 29 && anim < 40) {
					g.drawImage(player_image[28], x, y, null);
					anim++;
				}
				else if (anim == 40) {
					g.drawImage(player_image[28], x, y, null);
					anim = 0;
				}			 

			}

		}
		else {
			if (faceRight == true) g.drawImage(player_image[0], x, y, null);
			else g.drawImage(player_image[5], x, y, null);
		}
	}


}
