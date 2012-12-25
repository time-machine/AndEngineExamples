package org.anddev.andengine.examples.game.snake.entity;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Frog extends AnimatedCellEntity {
  public Frog(final int pCellX, final int pCellY,
      final TiledTextureRegion pTiledTextureRegion) {
    super(pCellX, pCellY, CELL_WIDTH, CELL_HEIGHT, pTiledTextureRegion);
  }
}
