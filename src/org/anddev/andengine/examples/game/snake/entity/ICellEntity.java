package org.anddev.andengine.examples.game.snake.entity;

public interface ICellEntity {
  public abstract int getCellX();
  public abstract int getCellY();

  public abstract void setCell(ICellEntity pCellEntity);
  public abstract void setCell(int pCellX, int pCellY);

  public abstract boolean isInSameCell(ICellEntity pCellEntity);
}
