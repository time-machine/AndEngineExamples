package org.anddev.andengine.examples.game.snake.entity;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class BaseSnakePart extends Sprite implements SnakeConstants {
  protected int mCellX;
  protected int mCellY;

  public BaseSnakePart(final int pCellX, final int pCellY,
      final TextureRegion pTextureRegion) {
    super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT,
        pTextureRegion);
    mCellX = pCellX;
    mCellY = pCellY;
  }

  public int getCellX() {
    return mCellX;
  }

  public int getCellY() {
    return mCellY;
  }

  public void setCell(final BaseSnakePart pSnakePart) {
    setCell(pSnakePart.mCellX, pSnakePart.mCellY);
  }

  public void setCell(final int pCellX, final int pCellY) {
    mCellX = pCellX;
    mCellY = pCellY;
    setPosition(mCellX * CELL_WIDTH, mCellY * CELL_HEIGHT);
  }
}
