package demolition;

import processing.core.PImage;

/** The type Top bar. */
public class TopBar implements DrawableObject {
  /** The App. */
  protected App app;

  private PImage liveImage;
  private PImage clockImage;

  private int xLive;
  private int yLive;
  private int xClock;
  private int yClock;

  private int frame_count = 0;

  /**
   * Instantiates a new Top bar.
   *
   * @param app this app
   */
  public TopBar(App app) {
    this.app = app;
    xLive = (App.WIDTH / 4 + 1) * App.GRID_SIZE;
    yLive = App.GRID_SIZE - 16;
    xClock = (App.WIDTH / 2 + 1) * App.GRID_SIZE;
    yClock = App.GRID_SIZE - 16;
  }

  /**
   * Instantiates a new Top bar.
   *
   * @param app the app
   * @param loadImg the load img
   */
  public TopBar(App app, boolean loadImg) {
    this(app);
    if (loadImg) loadImg();
  }

  /** Load img. */
  public void loadImg() {
    this.liveImage = app.loadResourceImg("icons/player.png");
    this.clockImage = app.loadResourceImg("icons/clock.png");
  }

  /**
   * change the status of top bar and draw in the Papplet, this method is called in every frame
   *
   * @param draw is draw top bars images
   */
  public void tick(boolean draw) {
    if (draw) {
      app.drawText(String.valueOf(app.getBombGuy().getLives()), xLive + 48, yLive + 28, 18);
      app.image(liveImage, xLive, yLive);
      app.image(clockImage, xClock, yClock);
      app.drawText(String.valueOf(app.getSeconds()), xClock + 48, yClock + 28, 18);
    }
    if (frame_count % 60 == 0 && frame_count != 0) {
      app.setSeconds(app.getSeconds() - 1);
    }
    this.frame_count++;
  }

  /** Initialization top bar. */
  @Override
  public void init() {}
}
