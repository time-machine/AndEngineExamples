package org.anddev.andengine.examples.game.snake.entity;

import org.anddev.andengine.examples.game.snake.adt.Direction;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class SnakeHead extends CellEntity {
  public SnakeHead(final int pCellX, final int pCellY,
      final TextureRegion pTextureRegion) {
    super(pCellX, pCellY, CELL_WIDTH, 2 * CELL_HEIGHT, pTextureRegion);
    setRotationCenterY(CELL_HEIGHT / 2);
  }

  public void setRotation(final Direction pDirection) {
    switch (pDirection) {
    case UP:
      setRotation(180);
      break;
    case DOWN:
      setRotation(0);
      break;
    case LEFT:
      setRotation(90);
      break;
    case RIGHT:
      setRotation(270);
      break;
    }
  }
}
