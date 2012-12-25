package org.anddev.andengine.examples.game.snake.entity;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Food extends CellEntity {
  public Food(final int pCellX, final int pCellY, final int pWidth, final int pHeight,
      final TextureRegion pTextureRegion) {
    super(pCellX, pCellY, pWidth, pHeight, pTextureRegion);
  }
}
