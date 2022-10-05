package demolition;

/** The type Bomb guy. */
public class BombGuy extends MoveableObject {
  private int lives;
  private String[] imageNames =
      new String[] {
        "player/player", "player/player_left",
        "player/player_up", "player/player_right"
      };

  /**
   * Instantiates a new Bomb guy.
   *
   * @param app this app
   * @param lives bomb guy's lives
   */
  BombGuy(App app, int lives) {
    super(app);
    this.map = map;
    this.symbol = 'P';
    this.lives = lives;
  }

  /**
   * Instantiates a new Bomb guy.
   *
   * @param app this app
   * @param lives bomb guy's lives
   * @param loadImg the load img
   */
  public BombGuy(App app, int lives, boolean loadImg) {
    this(app, lives);
    if (loadImg) loadImg();
  }

  /**
   * Gets lives.
   *
   * @return the lives
   */
  public int getLives() {
    return lives;
  }

  /** Load img. */
  public void loadImg() {
    for (int imageNo = 1; imageNo <= 4; imageNo++) {
      for (int ori = 0; ori < 4; ori++) {
        images[ori][imageNo - 1] = app.loadResourceImg(imageNames[ori] + imageNo + ".png");
      }
    }
  }

  /** Settings of status while bomb guy lost lives. */
  @Override
  public void dead() {
    if (alive) {
      this.lives--;
      alive = false;
    }
  }

  /** bomb guy moves a step. If next grid is target, level up */
  @Override
  public void move() {
    super.move();
    if (x == this.app.getMap().getTargetX() && y == this.app.getMap().getTargetY()) {
      app.levelUp();
    }
  }

  /**
   * Check the status of nextGridStatus, if on collision, change the status of objects
   *
   * @param nextGridStatus next Grid Status
   * @param nextX bomb guy's next x position
   * @param nextY bomb guy's next Y position
   */
  @Override
  protected void onCollision(char nextGridStatus, int nextX, int nextY) {
    if (nextGridStatus == 'W' || nextGridStatus == 'B') {
      return;
    } else if (nextGridStatus == 'R' || nextGridStatus == 'Y') {
      this.dead();
    }
  }

  /** Initialization status of Bomb guy. */
  @Override
  public void init() {
    super.init();
  }
}
