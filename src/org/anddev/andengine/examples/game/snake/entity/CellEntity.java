package org.anddev.andengine.examples.game.snake.entity;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class CellEntity extends Sprite implements SnakeConstants {
  protected int mCellX;
  protected int mCellY;

  public CellEntity(final int pCellX, final int pCellY, final int pWidth,
      final int pHeight, final TextureRegion pTextureRegion) {
    super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, pWidth, pHeight,
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

  public void setCell(final CellEntity pSnakePart) {
    setCell(pSnakePart.mCellX, pSnakePart.mCellY);
  }

  public void setCell(final int pCellX, final int pCellY) {
    mCellX = pCellX;
    mCellY = pCellY;
    setPosition(mCellX * CELL_WIDTH, mCellY * CELL_HEIGHT);
  }

  public boolean isInSameCell(final CellEntity pCellEntity) {
    return mCellX == pCellEntity.mCellX && mCellY == pCellEntity.mCellY;
  }
}
