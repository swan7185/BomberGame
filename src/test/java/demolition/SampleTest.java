package demolition;

import org.junit.jupiter.api.Test;
import processing.core.PConstants;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SampleTest {

  private void setUp(App app) {
    app.loadConfig(Paths.get(System.getProperty("user.dir"), "config.json").toString());
    app.setLevel(1);
    Map map = new Map(app, false);
    app.setMap(map);

    BombGuy bombGuy = new BombGuy(app, app.getConfig().getLives(), false);
    app.setBombGuy(bombGuy);
    YellowEnemy yellowEnemy = new YellowEnemy(app, false);
    app.setYellowEnemy(yellowEnemy);
    RedEnemy redEnemy = new RedEnemy(app, false);
    app.setRedEnemy(redEnemy);
    TopBar topBar = new TopBar(app, false);
    app.setTopBar(topBar);

    app.init();
  }

  @Test
  public void simpleTest() {
    assertEquals(480, App.HEIGHT * App.GRID_SIZE);
  }

  @Test
  public void testMapTarget() {
    App app = new App();
    setUp(app);
    assertEquals(
        app.getMap().getGridStatus(app.getMap().getTargetX(), app.getMap().getTargetY()), 'G');
  }

  @Test
  public void testRedEnemyInit() {
    App app = new App();
    setUp(app);
    assertEquals(app.getRedEnemy().isAlive(), true);
  }

  @Test
  public void testRedEnemyOrientation() {
    App app = new App();
    setUp(app);
    RedEnemy redEnemy = app.getRedEnemy();
    Map map = app.getMap();
    int Ori = app.getRedEnemy().Orientation;

    redEnemy.getNextPosition(Ori);
    app.getRedEnemy().move();
    assertEquals(
        app.getMap().getGridStatus(app.getRedEnemy().getX(), app.getRedEnemy().getY()), 'R');
    assertNotEquals(redEnemy.Orientation, Ori);
  }

  @Test
  public void testYellowEnemyInit() {
    App app = new App();
    setUp(app);
    assertEquals(app.getYellowEnemy().isAlive(), true);
  }

  @Test
  public void testYellowEnemyOrientation() {
    App app = new App();
    setUp(app);

    YellowEnemy yellowEnemy = app.getYellowEnemy();
    Map map = app.getMap();
    int Ori = app.getYellowEnemy().Orientation;
    while (map.getGridStatus(yellowEnemy.getX(), (yellowEnemy.getY() + 1)) != 'W'
        && map.getGridStatus(yellowEnemy.getX(), (yellowEnemy.getY() + 1)) != 'B') {
      yellowEnemy.move();
    }
    yellowEnemy.move();

    assertEquals(
        app.getMap().getGridStatus(app.getYellowEnemy().getX(), app.getYellowEnemy().getY()), 'Y');
    assertEquals(yellowEnemy.Orientation, Ori + 1);
  }

  @Test
  public void testBombGuyInit() {
    App app = new App();
    setUp(app);
    assertEquals(app.getBombGuy().isAlive(), true);
    assertEquals(3, app.getBombGuy().getLives());
    app.getBombGuy().dead();
    assertEquals(2, app.getBombGuy().getLives());
  }

  @Test
  public void testBombGuyMove() {
    App app = new App();
    setUp(app);
    Map map = app.getMap();
    app.getBombGuy().move();

    assertEquals(app.getMap().getGridStatus(app.getBombGuy().getX(), app.getBombGuy().getY()), 'P');
    assertEquals(app.getBombGuy().Orientation, 0);
    assertEquals(1, app.getBombGuy().x);
    assertEquals(4, app.getBombGuy().y);

    BombGuy bombGuy = app.getBombGuy();
    app.setPressedKeyCode(PConstants.RIGHT);
    app.onKeyPressed(false);
    app.onKeyReleased();
    bombGuy.move();
    assertEquals(3, bombGuy.getOrientation());

    app.setPressedKeyCode(PConstants.DOWN);
    app.onKeyPressed(false);
    app.onKeyReleased();
    assertEquals(0, bombGuy.getOrientation());

    app.setPressedKeyCode(PConstants.LEFT);
    app.onKeyPressed(false);
    app.onKeyReleased();
    assertEquals(1, bombGuy.getOrientation());

    app.setPressedKeyCode(PConstants.UP);
    app.onKeyPressed(false);
    app.onKeyReleased();
    assertEquals(2, bombGuy.getOrientation());
  }

  @Test
  public void testWin() {
    App app = new App();
    setUp(app);
    BombGuy bombGuy = app.getBombGuy();
    app.setPressedKeyCode(PConstants.RIGHT);
    app.onKeyPressed(false);
    app.onKeyReleased();

    for (int i = 0; i < 2; i++) {
      bombGuy.setPosition(app.getMap().getTargetX() - 1, app.getMap().getTargetY());
      app.setPressedKeyCode(PConstants.RIGHT);
      app.onKeyPressed(false);
      app.onKeyReleased();
      if (i == 0) {
        assertEquals(2, app.getLevel());
      } else if (i == 1) {
        assertEquals(1, app.getGameStatus());
      }
    }
    app.tick(false);
    app.tick(false);
  }

  @Test
  public void testBomb() {
    App app = new App();
    setUp(app);
    app.setPressedKeyCode(' ');
    app.onKeyPressed(false);
    assertEquals(1, app.getBombList().size());
    Bomb bomb = app.getBombList().get(0);

    for (int i = 0; i < 3; i++) {
      app.setPressedKeyCode(PConstants.RIGHT);
      app.onKeyPressed(false);
      app.onKeyReleased();
    }
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 60; j++) {
        app.tick(false);
      }
    }
    app.tick(false);
    assertEquals(178, app.getSeconds());
    assertEquals("detonates", bomb.getBombStatus());
    for (int j = 0; j < 30; j++) {
      app.tick(false);
    }
    assertEquals("close", bomb.getBombStatus());
    app.tick(false);
  }

  @Test
  public void TestMeetEnemy() {
    App app = new App();
    setUp(app);
    BombGuy bombGuy = app.getBombGuy();
    RedEnemy redEnemy = app.getRedEnemy();
    YellowEnemy yellowEnemy = app.getYellowEnemy();

    bombGuy.setPosition(redEnemy.getX() - 1, redEnemy.getY());

    app.setPressedKeyCode(PConstants.RIGHT);
    app.onKeyPressed(false);
    app.onKeyReleased();
    app.tick(false);
    assertEquals(2, bombGuy.getLives());

    bombGuy.setPosition(yellowEnemy.getX() - 1, yellowEnemy.getY());

    app.setPressedKeyCode(PConstants.RIGHT);
    app.onKeyPressed(false);
    app.onKeyReleased();
    app.tick(false);
    assertEquals(1, bombGuy.getLives());
  }

  @Test
  public void TestEnemyMeet() {
    App app = new App();
    setUp(app);
    BombGuy bombGuy = app.getBombGuy();
    RedEnemy redEnemy = app.getRedEnemy();
    YellowEnemy yellowEnemy = app.getYellowEnemy();

    bombGuy.setPosition(redEnemy.getX() - 1, redEnemy.getY());

    for (int j = 0; j < 60 * 50; j++) {
      app.tick(false);
    }

    int yellowEnemyX = yellowEnemy.getX();
    int yellowEnemyY = yellowEnemy.getY();
    for (int j = 0; j < 60 * 1; j++) {
      app.tick(false);
    }
    bombGuy.setPosition(yellowEnemyX, yellowEnemyY);

    for (int j = 0; j < 60 * 50; j++) {
      app.tick(false);
    }

    assertEquals(1, bombGuy.getLives());

    app.setPressedKeyCode(' ');
    app.onKeyPressed(false);
    app.onKeyReleased();

    for (int j = 0; j < 60 * 5; j++) {
      app.tick(false);
    }

    assertEquals(0, bombGuy.getLives());
    assertEquals(-1, app.getGameStatus());
  }

  @Test
  public void TestBombMoveableObject() {
    App app = new App();
    setUp(app);
    BombGuy bombGuy = app.getBombGuy();
    RedEnemy redEnemy = app.getRedEnemy();
    YellowEnemy yellowEnemy = app.getYellowEnemy();

    bombGuy.setPosition(redEnemy.getX() - 1, redEnemy.getY());
    app.setPressedKeyCode(' ');
    app.onKeyPressed(false);
    app.onKeyReleased();

    bombGuy.setPosition(redEnemy.getX() - 4, redEnemy.getY());

    for (int j = 0; j < 150; j++) {
      app.tick(false);
    }
    assertEquals(false, redEnemy.alive);

    bombGuy.setPosition(yellowEnemy.getX() - 1, yellowEnemy.getY());
    app.setPressedKeyCode(' ');
    app.onKeyPressed(false);
    app.onKeyReleased();

    bombGuy.setPosition(1, 3);
    for (int j = 0; j < 160; j++) {
      app.tick(false);
    }

    assertEquals(false, yellowEnemy.alive);

    app.setPressedKeyCode(' ');
    app.onKeyPressed(false);
    app.onKeyReleased();

    bombGuy.setPosition(1, 3);
    for (int j = 0; j < 150; j++) {
      app.tick(false);
    }
    assertEquals(2, bombGuy.getLives());
  }
}
