package pkg.game;

import pkg.models.AmmoCrate;
import pkg.models.Block;
import pkg.models.Enemy;
import pkg.models.Exit;
import pkg.models.GameObject;
import pkg.models.Player;
import pkg.view.BufferedImageLoader;
import pkg.view.Camera;
import pkg.view.KeyInput;
import pkg.view.MouseInput;
import pkg.view.SpriteSheet;
import pkg.view.Window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * @author Zack (RealTutsGML), Notch, Devin
 */
public class Game extends Canvas implements Runnable { // Canvas provides a surface to draw objects onto and runnable
                                                       // lets us use our game thread.

    //////////////////////////////////// data
    //////////////////////////////////// fields/////////////////////////////////////
    private static final long serialVersionUID = 1L; // Serialization isn't strictly necessary in the context of this
                                                     // template, but stops eclipse from flagging the class
    // These are player data fields. For now, they have been initialized here to be
    // displayed easily as an overlay.
    public int ammo = 100;
    public int hp = 100;
    public boolean nextLvl = false;
    private boolean isRunning = false;
    private Thread thread; // Threads allow a program to operate more efficiently by doing multiple things
                           // at the same time.
    private final Handler handler; // Handler facilitates interactions between objects.
    private final Camera camera; // The camera follows the Player object to make sure they stay in-frame
    private final SpriteSheet ss; // SpriteSheet is a collection of sprites (images) to be rendered in the program
    // A BufferedImage is a graphic that is loaded into the game using the
    // BufferedImageLoader class
    private BufferedImage level1 = null;
    private BufferedImage gameOver = null;
    private BufferedImage youWin = null;
    private BufferedImage sprite_sheet = null;
    private BufferedImage floor = null;

    // Game() constructor holds everything that must be initialized when the game
    // begins, such as levels, input listeners, the camera, etc.
    public Game() {
        new Window(1000, 563, "Game", this);

        handler = new Handler();
        camera = new Camera(0, 0);
        this.addKeyListener(new KeyInput(handler));

        BufferedImageLoader loader = new BufferedImageLoader();

        sprite_sheet = loader.loadImage("/fff_ss.png");

        ss = new SpriteSheet(sprite_sheet);
        floor = ss.grabImage(1, 5, 64, 64); // grabImage lets us pull from our sprite sheet, (row, column, width,
                                            // height)
        level1 = loader.loadImage("/wizard_level.png");
        this.addMouseListener(new MouseInput(handler, camera, this, ss));

        loadLevel(level1);

        gameOver = loader.loadImage("/game_over.png");
        youWin = loader.loadImage("/you_win.png");
    }

    public void start() { // called to start the thread/start running the game
        if (isRunning) {
            return;
        }

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() { // called to stop the thread/stop running the game
        if (!isRunning) {
            return;
        }

        isRunning = false;

        try {
            thread.join();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    // The run() method defines the elapsing of time in the game. It is our game
    // loop.
    // Currently, the game is set to run at 60 fps.
    // Fun fact: this game loop was provided by Notch, the creator of Minecraft.
    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }

        }
        stop();
    }

    public void tick() { // tick() updates our game each time it is executed. It is essential for our
                         // run() method above.

        for (GameObject obj : handler.getObjects()) {
            if (obj instanceof Player) {
                camera.tick(obj);
            }
        }

        if (hp == 0) {

            unloadLvl();

            camera.setX(0);
            camera.setY(0);
            loadLevel(gameOver);
            hp = 0;
        }
        if (nextLvl == true) {
            unloadLvl();
            camera.setX(0);
            camera.setY(0);
            loadLevel(youWin);
        }
        handler.tick();
    }

    public void unloadLvl() { // removes all GameObjects in the game
        for (int ii = 0; ii < 10; ii++) {
            for (GameObject tempObject : handler.getObjects()) {
                handler.removeObject(tempObject);
            }
        }
    }

    public void render() { // render defines all the graphical components of our game.
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        ////////////////////////////////////// anything drawn in-game goes here.

        g2d.translate(-camera.getX(), -camera.getY());

        for (int xx = 0; xx < 2160; xx += 32) {
            for (int yy = 0; yy < 2160; yy += 32) {
                g.drawImage(floor, xx, yy, null);
            }
        }

        handler.render(g);

        g2d.translate(camera.getX(), camera.getY());

        g.setColor(Color.gray);
        g.fillRect(5, 5, 200, 32);
        g.setColor(Color.green);
        g.fillRect(5, 5, hp * 2, 32);
        g.setColor(Color.black);
        g.drawRect(5, 5, 200, 32);

        g.setColor(Color.white);
        g.drawString("Ammo: " + ammo, 5, 50);
        //////////////////////////////////////
        g.dispose();
        bs.show();
    }

    // loadLevel takes a BufferedImage as an argument and loads it as a level in our
    // game.
    // We can use loadLevel like a level editor.
    // By default, loadLevel takes images like wizard_level.png and translates
    // colored pixels into in-game objects
    // Using loadLevel, we can easily design levels using even simple programs like
    // MS Paint.
    private void loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        // This for loop looks over a given image file and defines what objects will be
        // produced based on the colors presented in the image file.
        for (int xx = 0; xx < w; xx++)
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255 && green == 0 && blue == 0)
                    handler.addObject(new Block(xx * 64, yy * 64, handler, ss));

                if (green == 255 && blue == 0)
                    handler.addObject(new Enemy(xx * 64, yy * 64, handler, ss));

                if (blue == 255 && green == 0 && red == 0)
                    handler.addObject(new Player(xx * 64, yy * 64, handler, this, ss));

                if (blue == 255 && green == 255)
                    handler.addObject(new AmmoCrate(xx * 64, yy * 64, handler, ss));

                if (blue == 255 && red == 255)
                    handler.addObject(new Exit(xx * 64, yy * 64, handler, ss));

            }
    }

    // Our main method, which simply runs everything we have defined above in our
    // Game() constructor.
    public static void main(String[] args) {
        Game game = new Game();
        game.start();

        // this is a comment
    }
}
