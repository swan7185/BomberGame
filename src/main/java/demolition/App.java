package demolition;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** The type Level config. */
class LevelConfig {
  private String path;

  /**
   * Gets path.
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Gets time.
   *
   * @return the time
   */
  public int getTime() {
    return time;
  }
}

/**
 * The type Config.
 *
 * @author shengnan wang
 * @date 2021 /11/06
 */
class Config {
  private List<LevelConfig> levels;
  private int lives;

  /**
   * Gets levels.
   *
   * @return the levels
   */
  public List<LevelConfig> getLevels() {
    return levels;
  }

  /**
   * Gets lives.
   *
   * @return the lives
   */
  public int getLives() {
    return lives;
  }
}

/** The type App. */
public class App extends PApplet {
  /** The constant TOP_HEIGHT. */
  public static final int TOP_HEIGHT = 2;

  /** The constant WIDTH. */
  public static final int WIDTH = 15;

  /** The constant HEIGHT. */
  public static final int HEIGHT = 15;

  /** The constant FPS. */
  // frame per second
  public static final int FPS = 60;

  /** The constant GRID_SIZE. */
  public static final int GRID_SIZE = 32;

  /** The Bomb list. */
  List<Bomb> bombList = new ArrayList<>();

  /** The Pressed key code. */
  int pressedKeyCode = -1;

  private int level;
  private Map map;
  private BombGuy bombGuy;
  private TopBar topBar;
  private int seconds;
  private Config config;
  private PFont font;
  private YellowEnemy yellowEnemy;
  private RedEnemy redEnemy;
  private int gameStatus = 0;

  /** Instantiates a new App. */
  public App() {}

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    PApplet.main("demolition.App");
  }

  /**
   * Gets bomb list.
   *
   * @return the bomb list
   */
  public List<Bomb> getBombList() {
    return bombList;
  }

  /**
   * Sets top bar.
   *
   * @param topBar the top bar
   */
  public void setTopBar(TopBar topBar) {
    this.topBar = topBar;
  }

  /**
   * Gets game status.
   *
   * @return the game status
   */
  public int getGameStatus() {
    return gameStatus;
  }

  /**
   * Gets seconds.
   *
   * @return the seconds
   */
  public int getSeconds() {
    return seconds;
  }

  /**
   * Sets seconds.
   *
   * @param seconds the seconds
   */
  public void setSeconds(int seconds) {
    this.seconds = seconds;
  }

  /**
   * Gets config.
   *
   * @return the config
   */
  public Config getConfig() {
    return config;
  }

  /**
   * Gets bomb guy.
   *
   * @return the bomb guy
   */
  public BombGuy getBombGuy() {
    return bombGuy;
  }

  /**
   * Sets bomb guy.
   *
   * @param bombGuy the bomb guy
   */
  public void setBombGuy(BombGuy bombGuy) {
    this.bombGuy = bombGuy;
  }

  /**
   * Gets yellow enemy.
   *
   * @return the yellow enemy
   */
  public YellowEnemy getYellowEnemy() {
    return yellowEnemy;
  }

  /**
   * Sets yellow enemy.
   *
   * @param yellowEnemy the yellow enemy
   */
  public void setYellowEnemy(YellowEnemy yellowEnemy) {
    this.yellowEnemy = yellowEnemy;
  }

  /**
   * Gets red enemy.
   *
   * @return the red enemy
   */
  public RedEnemy getRedEnemy() {
    return redEnemy;
  }

  /**
   * Sets red enemy.
   *
   * @param redEnemy the red enemy
   */
  public void setRedEnemy(RedEnemy redEnemy) {
    this.redEnemy = redEnemy;
  }

  /**
   * Gets level.
   *
   * @return the level
   */
  public int getLevel() {
    return level;
  }

  /**
   * Sets level.
   *
   * @param level the level
   */
  public void setLevel(int level) {
    this.level = level;
  }

  /**
   * Gets map.
   *
   * @return the map
   */
  public Map getMap() {
    return map;
  }

  /**
   * Sets map.
   *
   * @param map the map
   */
  public void setMap(Map map) {
    this.map = map;
  }

  public void settings() {
    size(480, HEIGHT * GRID_SIZE);
  }

  /**
   * Load resource img p image.
   *
   * @param path the path
   * @return the p image
   */
  public PImage loadResourceImg(String path) {
    URL resource = getClass().getClassLoader().getResource(path);
    return loadImage(resource.getPath());
  }

  /** Set the initial state of each variable. */
  public void init() {
    bombList.clear();
    map.init();
    bombGuy.init();
    yellowEnemy.init();
    redEnemy.init();
    topBar.init();
    this.seconds = this.config.getLevels().get(this.level - 1).getTime();
  }

  /**
   * Load config.
   *
   * @param path the path
   */
  public void loadConfig(String path) {
    try {
      Gson json = new Gson();
      JsonReader reader = null;
      File f = new File(path);
      reader = new JsonReader(new FileReader(f));
      config = json.fromJson(reader, Config.class);
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  /** App setup. */
  public void setup() {
    loadConfig(Paths.get(System.getProperty("user.dir"), "config.json").toString());
    this.level = 1;
    font = createFont("PressStart2P-Regular.ttf", 18);
    frameRate(FPS);
    // set up map
    map = new Map(this, true);
    bombGuy = new BombGuy(this, config.getLives(), true);
    yellowEnemy = new YellowEnemy(this, true);
    redEnemy = new RedEnemy(this, true);
    topBar = new TopBar(this, true);
    init();

  }

  @Override
  public void keyReleased() {
    this.onKeyReleased();
  }

  /** Bomb guy's moving on key released. */
  public void onKeyReleased() {
    if (pressedKeyCode == DOWN || pressedKeyCode == UP || pressedKeyCode == LEFT || pressedKeyCode == RIGHT) {
      bombGuy.move();
    }
    pressedKeyCode = -1;
  }

  /**
   * Bomb guy moves while press keycode
   *
   * @param isLoadImage is load bomb guy image
   */
  public void onKeyPressed(boolean isLoadImage) {
    if (pressedKeyCode == DOWN) {
      bombGuy.setOrientation(0);
    } else if (pressedKeyCode == LEFT) {
      bombGuy.setOrientation(1);
    } else if (pressedKeyCode == UP) {
      bombGuy.setOrientation(2);
    } else if (pressedKeyCode == RIGHT) {
      bombGuy.setOrientation(3);
    } else if (pressedKeyCode == ' ') {
      Bomb bomb = new Bomb(this);
      if (isLoadImage) {
        bomb.loadImages();
      }
      bombList.add(bomb);
      bomb.setPosition(bombGuy.getX(), bombGuy.getY());
    }
  }

  /**
   * Sets pressed key code.
   *
   * @param pressedKeyCode the pressed key code
   */
  public void setPressedKeyCode(int pressedKeyCode) {
    this.pressedKeyCode = pressedKeyCode;
  }

  @Override
  public void keyPressed() {
    pressedKeyCode = keyCode;
    onKeyPressed(true);
  }

  /** Level up. */
  public void levelUp() {
    if (this.level == 2) {
      // win
      gameStatus = 1;
    } else {
      this.level++;
      init();
    }
  }

  /**
   * App Tick.
   *
   * @param draw the draw
   */
  public void tick(boolean draw) {
    if (gameStatus == -1) {
      return;
    }
    if (draw) {
      this.clear();
      background(255, 121, 0);
    }
    if (bombGuy.getLives() <= 0 || this.seconds <= 0) {
      if (draw) {
        drawText("GAME OVER", WIDTH / 3 * GRID_SIZE, HEIGHT / 2 * GRID_SIZE, 20);
      }
      gameStatus = -1;
      return;
    } else if (gameStatus == 1) {
      if (draw) {
        drawText("YOU WIN", WIDTH / 3 * GRID_SIZE, HEIGHT / 2 * GRID_SIZE, 20);
      }
      gameStatus = -1;
      return;
    }
    map.tick(draw);
    Iterator<Bomb> iterator = bombList.iterator();
    while (iterator.hasNext()) {
      Bomb bomb = iterator.next();
      if (bomb.getBombStatus().equals("close")) {
        iterator.remove();
      } else {
        bomb.tick(draw);
      }
    }
    topBar.tick(draw);
    bombGuy.tick(draw);
    yellowEnemy.tick(draw);
    redEnemy.tick(draw);
    if (!bombGuy.isAlive()) {
      init();
    }
  }

  /**
   * Draw textFont.
   *
   * @param text the text
   * @param x the x
   * @param y the y
   * @param size the size
   */
  public void drawText(String text, int x, int y, int size) {
    textFont(font, size);
    fill(0);
    text(text, x, y);
  }

  /** App draw. Draw each object. */
  @Override
  public void draw() {
    tick(true);
  }
}
