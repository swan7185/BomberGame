package demolition;

/** The type Red enemy. */
public class RedEnemy extends MoveableObject {
  private String[] imageNames =
      new String[] {
        "red_enemy/red_down", "red_enemy/red_left", "red_enemy/red_up", "red_enemy/red_right"
      };

  /**
   * Instantiates a new Red enemy.
   *
   * @param app the app
   */
  public RedEnemy(App app) {
    super(app);
    this.map = app.getMap();
    this.symbol = 'R';
  }

  /**
   * Instantiates a new Red enemy.
   *
   * @param app the app
   * @param loadImg the load img
   */
  public RedEnemy(App app, boolean loadImg) {
    this(app);
    if (loadImg) loadImg();
  }

  /** Load img. */
  public void loadImg() {
    for (int imageNo = 1; imageNo <= 4; imageNo++) {
      for (int ori = 0; ori < 4; ori++) {
        images[ori][imageNo - 1] = app.loadResourceImg(imageNames[ori] + imageNo + ".png");
      }
    }
  }

  /**
   * Define the action when collision on wall, block and enemy
   *
   * @param nextGridStatus next Grid Status
   * @param nextX the nextX position
   * @param nextY the nextY position
   */
  @Override
  protected void onCollision(char nextGridStatus, int nextX, int nextY) {
    if (nextGridStatus == 'W' || nextGridStatus == 'B') {
      while (true) {
        int newOrientation = (int) (0 + Math.random() * 4);
        int[] nextPos = this.getNextPosition(newOrientation);
        int newX = nextPos[0];
        int newY = nextPos[1];
        char newGridStatus = map.getGridStatus(newX, newY);
        if (newGridStatus != 'W' && newGridStatus != 'B') {
          Orientation = newOrientation;
          break;
        } else continue;
      }
    } else if (nextGridStatus == 'P') {
      this.app.getBombGuy().dead();
    }
  }

  /** Initialization red enemy.*/
  @Override
  public void init() {
    super.init();
  }

  /**
   * change the status of red enemy and draw in the Papplet, this method is called in every frame
   *
   * @param draw is draw red enemy's images
   */
  @Override
  public void tick(boolean draw) {
    if (frame_count % 60 == 0) move();
    super.tick(draw);
  }
}
