package org.anddev.andengine.examples.game.snake.adt;

public enum Direction {
  UP, DOWN, LEFT, RIGHT;

  public static int addToX(final Direction pDirection, final int pX) {
    switch (pDirection) {
    case DOWN:
    case UP:
      return pX;
    case LEFT:
      return pX - 1;
    case RIGHT:
      return pX + 1;
    default:
      throw new IllegalArgumentException();
    }
  }

  public static int addToY(final Direction pDirection, final int pY) {
    switch (pDirection) {
    case LEFT:
    case RIGHT:
      return pY;
    case UP:
      return pY - 1;
    case DOWN:
      return pY + 1;
    default:
      throw new IllegalArgumentException();
    }
  }
}
