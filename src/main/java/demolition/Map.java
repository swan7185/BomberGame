package demolition;

import processing.core.PImage;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** The type Map. */
public class Map implements DrawableObject {
  private List<char[]> gridStatus;
  private App app;
  private PImage solidWallImg;
  private PImage brokenWallImg;
  private PImage emptyImg;
  private PImage goalImg;
  private int targetX;
  private int targetY;

  /**
   * Instantiates a new Map.
   *
   * @param app the app
   */
  public Map(App app) {
    this.app = app;
  }

  /**
   * Instantiates a new Map.
   *
   * @param app the app
   * @param loadImg the load img
   */
  public Map(App app, boolean loadImg) {
    this(app);
    if (loadImg) loadImg();
  }

  /** Load img. */
  public void loadImg() {
    brokenWallImg = app.loadResourceImg("broken/broken.png");
    solidWallImg = app.loadResourceImg("wall/solid.png");
    emptyImg = app.loadResourceImg("empty/empty.png");
    goalImg = app.loadResourceImg("goal/goal.png");
  }

  /**
   * Gets grid status.
   *
   * @return the grid status
   */
  public List<char[]> getGridStatus() {
    return gridStatus;
  }

  /**
   * Sets grid status.
   *
   * @param x the x
   * @param y the y
   * @param status the status
   */
  public void setGridStatus(int x, int y, char status) {
    this.gridStatus.get(y - App.TOP_HEIGHT)[x] = status;
  }

  /**
   * Gets target x.
   *
   * @return the target x
   */
  public int getTargetX() {
    return targetX;
  }

  /**
   * Gets target y.
   *
   * @return the target y
   */
  public int getTargetY() {
    return targetY;
  }

  /** reset the status of the map. */
  public void init() {
    String mapResource =
        Paths.get("map", app.getConfig().getLevels().get(app.getLevel() - 1).getPath()).toString();
    this.gridStatus = new ArrayList<>();
    URL resource = getClass().getClassLoader().getResource(mapResource);
    String mapPath = resource.getPath();
    FileInputStream stream = null;
    try {
      stream = new FileInputStream(mapPath);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    InputStreamReader read = new InputStreamReader(stream);
    BufferedReader br = new BufferedReader(read);

    try {
      String line = br.readLine();
      while (line != null) {
        gridStatus.add(line.toCharArray());
        line = br.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < gridStatus.size(); i++) {
      char[] lineY = gridStatus.get(i);
      for (int j = 0; j < lineY.length; j++) {
        char charX = lineY[j];
        if (charX == 'G') {
          targetX = j;
          targetY = i + App.TOP_HEIGHT;
          break;
        }
      }
    }
  }

  /**
   * Gets grid status.
   *
   * @param x the x
   * @param y the y
   * @return the grid status
   */
  public char getGridStatus(int x, int y) {
    if (x < 0 || x >= App.WIDTH || y < App.TOP_HEIGHT || y >= App.WIDTH) return '@';
    return gridStatus.get(y - App.TOP_HEIGHT)[x];
  }

  /**
   * draw map in Papplet
   *
   * @param draw is draw map images
   */
  @Override
  public void tick(boolean draw) {
    for (int i = 0; i < gridStatus.size(); i++) {
      char[] line = gridStatus.get(i);
      for (int j = 0; j < line.length; j++) {
        char object = line[j];
        int x = j * App.GRID_SIZE;
        int y = (i + App.TOP_HEIGHT) * App.GRID_SIZE;
        PImage image;
        if (object == 'W') {
          image = solidWallImg;
        } else if (object == 'B') {
          image = brokenWallImg;
        } else if (object == ' ') {
          image = emptyImg;
        } else if (object == 'G') {
          image = goalImg;
        } else {
          image = emptyImg;
        }
        if (draw) app.image(image, x, y);
      }
    }
  }
}
