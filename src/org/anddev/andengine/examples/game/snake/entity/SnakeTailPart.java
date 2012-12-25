package org.anddev.andengine.examples.game.snake.entity;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class SnakeTailPart extends CellEntity implements Cloneable {
  public SnakeTailPart(final SnakeHead pSnakeHead,
      final TextureRegion pTextureRegion) {
    this(pSnakeHead.mCellX, pSnakeHead.mCellY, pTextureRegion);
  }

  public SnakeTailPart(final int pCellX, final int pCellY,
      final TextureRegion pTextureRegion) {
    super(pCellX, pCellY, CELL_WIDTH, CELL_HEIGHT, pTextureRegion);
  }

  @Override
  protected SnakeTailPart clone() {
    return new SnakeTailPart(mCellX, mCellY, getTextureRegion());
  }
}
