package org.anddev.andengine.examples.game.snake.entity;

import java.util.LinkedList;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.examples.game.snake.adt.Direction;
import org.anddev.andengine.examples.game.snake.adt.SnakeSuicideException;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Snake extends Entity {
  private final SnakeHead mHead;
  private final LinkedList<SnakeTailPart> mTail = new LinkedList<SnakeTailPart>();

  private Direction mDirection;
  private boolean mGrow;
  private final TextureRegion mTailPartTextureRegion;
  private Direction mLastMoveDirection;

  public Snake(final Direction pInitialDirection, final int pCellX,
      final int pCellY, final TiledTextureRegion pHeadTextureRegion,
      final TextureRegion pTailPartTextureRegion) {
    super(0,0);
    mTailPartTextureRegion = pTailPartTextureRegion;
    mHead = new SnakeHead(pCellX, pCellY, pHeadTextureRegion);
    addChild(mHead);
    setDirection(pInitialDirection);
  }

  public Direction getDirection() {
    return mDirection;
  }

  public void setDirection(final Direction pDirection) {
    if (mLastMoveDirection != Direction.opposite(pDirection)) {
      mDirection = pDirection;
      mHead.setRotation(pDirection);
    }
  }

  public int getTailLength() {
    return mTail.size();
  }

  public SnakeHead getHead() {
    return mHead;
  }

  public void grow() {
    mGrow = true;
  }

  public int getNextX() {
    return Direction.addToX(mDirection, mHead.getCellX());
  }

  public int getNextY() {
    return Direction.addToY(mDirection, mHead.getCellY());
  }

  public void move() throws SnakeSuicideException {
    mLastMoveDirection = mDirection;

    if (mGrow) {
      mGrow = false;
      // if the snake should grow, simply add a new part in the front of the
      // tail, where the head currently is
      final SnakeTailPart newTailPart = new SnakeTailPart(mHead,
          mTailPartTextureRegion);
      addChild(newTailPart);
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

    // check if head collides with tail
    for (int i = mTail.size() - 1; i >= 0; i--) {
      if (mHead.isInSameCell(mTail.get(i))) {
        throw new SnakeSuicideException();
      }
    }
  }
}
