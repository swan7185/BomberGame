package demolition;

import processing.core.PImage;

/** The type Moveable object. */
public abstract class MoveableObject implements DrawableObject {
  /** The App. */
  protected App app;

  /** The Map. */
  protected Map map;

  /** The X. */
  protected int x;

  /** The Y. */
  protected int y;

  /** The Symbol. */
  protected char symbol;

  /** The Orientation. */
  protected int Orientation = 0;

  /** The Each ori count. */
  protected int eachOriCount = 0;

  /** The Frame keep. */
  protected int frame_keep = (int) (0.2 * App.FPS);

  /** The Frame count. */
  protected int frame_count = 0;

  /** The Images. */
  protected PImage[][] images = new PImage[4][4];

  /** The Alive. */
  protected boolean alive = true;

  /**
   * Instantiates a new Moveable object.
   *
   * @param app the app
   */
  protected MoveableObject(App app) {
    this.app = app;
    this.map = app.getMap();
  }

  /**
   * Gets orientation.
   *
   * @return the orientation
   */
  public int getOrientation() {
    return Orientation;
  }

  /**
   * Sets orientation.
   *
   * @param orientation the orientation
   */
  public void setOrientation(int orientation) {
    Orientation = orientation;
  }

  /** Set the status of object and map when object dead. */
  public void dead() {
    this.alive = false;
    this.map.setGridStatus(x, y, ' ');
  }

  /**
   * Is alive boolean.
   *
   * @return the boolean
   */
  public boolean isAlive() {
    return alive;
  }

  /**
   * Sets position.
   *
   * @param nextX the next x
   * @param nextY the next y
   */
  public void setPosition(int nextX, int nextY) {
    map.setGridStatus(x, y, ' ');
    this.x = nextX;
    this.y = nextY;
    map.setGridStatus(nextX, nextY, symbol);
  }

  /**
   * Gets x position.
   *
   * @return the x
   */
  public int getX() {
    return x;
  }

  /**
   * Gets y position.
   *
   * @return the y
   */
  public int getY() {
    return y;
  }

  /**
   * On collision.
   *
   * @param nextGridStatus the next grid status
   * @param nextX the next x
   * @param nextY the next y
   */
  protected abstract void onCollision(char nextGridStatus, int nextX, int nextY);

  /** Move a step. */
  public void move() {
    if (!alive) return;
    int[] nextPosition = getNextPosition(Orientation);
    int nextX = nextPosition[0];
    int nextY = nextPosition[1];
    char nextGridStatus = map.getGridStatus(nextX, nextY);
    if (nextGridStatus == ' ' || nextGridStatus == 'G') {
      setPosition(nextX, nextY);
    } else {
      onCollision(nextGridStatus, nextX, nextY);
    }
  }

  /**
   * Get next position int [ ].
   *
   * @param orientation the orientation
   * @return int [ ], the next position
   */
  public int[] getNextPosition(int orientation) {
    int[] next = new int[2];
    int nextX = x, nextY = y;
    if (orientation == 0) {
      nextY++;
    } else if (orientation == 1) {
      nextX--;
    } else if (orientation == 2) {
      nextY--;
    } else if (orientation == 3) {
      nextX++;
    }
    next[0] = nextX;
    next[1] = nextY;
    return next;
  }

  /** Initialization Moveable object.*/
  @Override
  public void init() {
    x = -1;
    y = -1;
    for (int i = 0; i < map.getGridStatus().size(); i++) {
      char[] line = map.getGridStatus().get(i);
      for (int j = 0; j < line.length; j++) {
        if (line[j] == symbol) {
          x = j;
          y = App.TOP_HEIGHT + i;
          break;
        }
      }
    }
    if (x == -1 && y == -1) {
      alive = false;
    } else {
      setPosition(x, y);
      this.alive = true;
    }
  }

  /**
   * change the status of moveable object and draw in the Papplet, this method is called in every frame
   *
   * @param draw is draw moveable object's images
   */
  @Override
  public void tick(boolean draw) {
    if (alive) {
      if (draw) {
        app.image(
            this.images[Orientation][frame_count / frame_keep % 4],
            x * App.GRID_SIZE,
            y * App.GRID_SIZE - 16);
      }
    }
    frame_count++;
  }
}
