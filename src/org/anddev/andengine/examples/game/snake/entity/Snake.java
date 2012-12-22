package org.anddev.andengine.examples.game.snake.entity;

import java.util.LinkedList;

import org.anddev.andengine.entity.layer.DynamicCapacityLayer;
import org.anddev.andengine.examples.game.snake.adt.Direction;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Snake extends DynamicCapacityLayer {
  private final SnakeHead mHead;
  private final LinkedList<SnakeTailPart> mTail = new LinkedList<SnakeTailPart>();

  private Direction mDirection;
  private boolean mGrow;
  private final TextureRegion mTailPartTextureRegion;

  public Snake(final Direction pInitialDirection, final int pCellX,
      final int pCellY, final TextureRegion pHeadTextureRegion,
      final TextureRegion pTailPartTextureRegion) {
    mDirection = pInitialDirection;
    mTailPartTextureRegion = pTailPartTextureRegion;
    mHead = new SnakeHead(pCellX, pCellY, pHeadTextureRegion);
    addEntity(mHead);
  }

  public Direction getDirection() {
    return mDirection;
  }

  public void setDirection(final Direction pDirection) {
    mDirection = pDirection;
  }

  public int getTailLength() {
    return mTail.size();
  }

  public int getNextX() {
    return Direction.addToX(mDirection, mHead.getCellX());
  }

  public int getNextY() {
    return Direction.addToY(mDirection, mHead.getCellY());
  }

  public void move() {
    if (mGrow) {
      // if the snake should grow, simply add a new part in the front of the
      // tail, where the head currently is
      final SnakeTailPart newTailPart = new SnakeTailPart(mHead,
          mTailPartTextureRegion);
      addEntity(newTailPart);
      mTail.addFirst(newTailPart);
    }
    else {
      if (mTail.isEmpty() == false) {
        // first move the end of the tail to where the head currently is
        final SnakeTailPart tailEnd = mTail.removeLast();
        tailEnd.setCell(mHead);
        mTail.addFirst(tailEnd);
      }
    }

    // move the head into the direction of the snake
    mHead.setCell(getNextX(), getNextY());
  }
}
