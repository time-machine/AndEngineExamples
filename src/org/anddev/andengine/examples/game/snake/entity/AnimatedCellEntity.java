package org.anddev.andengine.examples.game.snake.entity;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.examples.game.snake.util.constants.SnakeConstants;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public abstract class AnimatedCellEntity extends AnimatedSprite implements
    SnakeConstants, ICellEntity {
  protected int mCellX;
  protected int mCellY;

  public AnimatedCellEntity(final int pCellX, final int pCellY, final int pWidth, final int pHeight,
      final TiledTextureRegion pTiledTextureRegion) {
    super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, pWidth, pHeight,
        pTiledTextureRegion);
    mCellX = pCellX;
    mCellY = pCellY;
  }

  @Override
  public int getCellX() {
    return mCellX;
  }

  @Override
  public int getCellY() {
    return mCellY;
  }

  @Override
  public void setCell(final ICellEntity pCellEntity) {
    setCell(pCellEntity.getCellX(), pCellEntity.getCellY());
  }

  @Override
  public void setCell(final int pCellX, final int pCellY) {
    mCellX = pCellX;
    mCellY = pCellY;
    setPosition(mCellX * CELL_WIDTH, mCellY * CELL_HEIGHT);
  }

  @Override
  public boolean isInSameCell(final ICellEntity pCellEntity) {
    return mCellX == pCellEntity.getCellX() && mCellY == pCellEntity.getCellY();
  }
}
