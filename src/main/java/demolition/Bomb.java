package demolition;

import processing.core.PImage;

/** The type Bomb. */
public class Bomb implements DrawableObject {
  private int[][] directions = new int[][] {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
  private String[][] imageNames =
      new String[][] {
        {"explosion/vertical", "explosion/end_top"},
        {"explosion/vertical", "explosion/end_bottom"},
        {"explosion/horizontal", "explosion/end_left"},
        {"explosion/horizontal", "explosion/end_right"}
      };

  private App app;
  private Map map;
  private int x;
  private int y;
  private int frame_each;
  private int frame_count;
  private String bombStatus;
  private PImage[] beforeBomb = new PImage[8];
  private PImage center;
  private PImage[][] explosionImages = new PImage[4][2];

  /**
   * Instantiates a new Bomb.
   *
   * @param app this app
   */
  public Bomb(App app) {
    this.app = app;
    this.map = app.getMap();

    this.frame_each = 120 / 8;
    this.frame_count = 0;
    bombStatus = "open";
    init();
  }

  /** Load images. */
  public void loadImages() {
    for (int imageNo = 1; imageNo <= 8; imageNo++) {
      beforeBomb[imageNo - 1] = app.loadResourceImg("bomb/bomb" + imageNo + ".png");
    }
//i代表4个方向，y表示每个方向要走多少步
    for (int i = 0; i < directions.length; i++) {
      for (int j = 0; j < 2; j++) {
        explosionImages[i][j] = app.loadResourceImg(imageNames[i][j] + ".png");
      }
    }
    center = app.loadResourceImg("explosion/centre.png");
  }

  /**
   * Sets position.
   *
   * @param x bomb's x position
   * @param y bomb's y position
   */
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Gets bomb status.
   *
   * @return the bomb status
   */
  public String getBombStatus() {
    return bombStatus;
  }

  /**
   * Change the status of grid at (x,y) in the map,and draw explosion image
   *
   * @param x bombGuy's x position
   * @param y bombGuy's y position
   * @param image explosion's image
   * @param isDraw is draw bomb's image
   */

  //会不会把人炸死（判断此位置是否有人，有人就炸死）
  private char explosionAt(int x, int y, PImage image, boolean isDraw) {
    char previousStatus = map.getGridStatus(x, y);
    if (previousStatus == '@' || previousStatus == 'W' || previousStatus == 'G') {
      return previousStatus;
    }

    if (previousStatus == 'Y') {
      app.getYellowEnemy().dead();
    } else if (previousStatus == 'R') {
      app.getRedEnemy().dead();
    } else if (previousStatus == 'P') {
      app.getBombGuy().dead();
    } else if (previousStatus == 'B') {
      map.setGridStatus(x, y, ' ');
    }

    if (isDraw) {
      app.image(image, x * App.GRID_SIZE, y * App.GRID_SIZE);
    }
    map.setGridStatus(x, y, 'E');
    return previousStatus;
  }

  /**
   * set explosion sprites in 4 directions.
   *
   * @param isDraw is draw explosion image
   */
  public void explosion(boolean isDraw) {
    if (bombStatus == "detonates" || bombStatus == "explosion") {
      explosionAt(x, y, center, isDraw);//画上中心位置的爆炸图
    } else if (bombStatus == "close") {
      map.setGridStatus(x, y, ' ');
    }
    for (int i = 0; i < directions.length; i++) {
      int[] direction = directions[i];
      for (int explosionLength = 1;
          explosionLength <= explosionImages[i].length;
          explosionLength++) {
        int explosionX = x + direction[0] * explosionLength;
        int explosionY = y + direction[1] * explosionLength;
        if (bombStatus == "close") {
          char preStatus = map.getGridStatus(explosionX, explosionY);
          if (preStatus == 'E') {
            map.setGridStatus(explosionX, explosionY, ' ');
          }
        } else {
          char preStatus =
                  explosionAt(explosionX, explosionY, explosionImages[i][explosionLength - 1], isDraw);
          if (preStatus == '@' || preStatus == 'W' || preStatus == 'B') {
            break;
          }
        }
      }
    }
  }

  /** Initialization bomb. */
  @Override
  public void init() {}

  /**
   * change the status of bomb and draw in the Papplet, this method is called in every frame
   *
   * @param draw is draw bombs' images
   */
  @Override
  public void tick(boolean draw) {
    if (bombStatus == "close") return;
    if (frame_count < 120) {
      if (draw) {
        app.image(beforeBomb[frame_count / frame_each], x * App.GRID_SIZE, y * App.GRID_SIZE);
      }
    } else if (frame_count == 120) {
      bombStatus = "detonates";
      explosion(draw);
    } else if (frame_count < 150) {
      bombStatus = "explosion";
      explosion(draw);
    } else if (frame_count == 150) {
      bombStatus = "close";
      explosion(draw);
    }
    frame_count++;
  }
}
