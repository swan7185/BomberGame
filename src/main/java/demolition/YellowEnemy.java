package demolition;

/** The type Yellow enemy. */
public class YellowEnemy extends MoveableObject {
  private String[] imageNames =
      new String[] {
        "yellow_enemy/yellow_down", "yellow_enemy/yellow_left",
        "yellow_enemy/yellow_up", "yellow_enemy/yellow_right"
      };

  /**
   * Instantiates a new Yellow enemy.
   *
   * @param app the app
   */
  public YellowEnemy(App app) {
    super(app);
    this.map = app.getMap();
    this.symbol = 'Y';
  }

  /**
   * Instantiates a new Yellow enemy.
   *
   * @param app the app
   * @param loadImg the load img
   */
  public YellowEnemy(App app, boolean loadImg) {
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
      Orientation = (Orientation + 1) % 4;
    } else if (nextGridStatus == 'P') {
      this.app.getBombGuy().dead();
    }
  }

  /** Initialization yellow enemy.*/
  @Override
  public void init() {
    super.init();
  }

  /**
   * change the status of yellow enemy and draw in the Papplet, this method is called in every frame
   *
   * @param draw is draw yellow enemy's images
   */
  @Override
  public void tick(boolean draw) {
    if (frame_count % 60 == 0) move();
    super.tick(draw);
  }
}
