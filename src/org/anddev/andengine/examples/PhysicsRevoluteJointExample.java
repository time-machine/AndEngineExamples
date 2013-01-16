package org.anddev.andengine.examples;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class PhysicsRevoluteJointExample extends BasePhysicsJointExample {
  @Override
  public Scene onLoadScene() {
    final Scene scene = super.onLoadScene();
    initJoints(scene);
    Toast.makeText(this, "In this example, the revolute joints have their " +
        "motor enabled.", Toast.LENGTH_LONG).show();
    return scene;
  }

  private void initJoints(final Scene scene) {
    final int centerX = CAMERA_WIDTH / 2;
    final int centerY = CAMERA_HEIGHT / 2;

    final int spriteWidth = mBoxFaceTextureRegion.getWidth();
    final int spriteHeight = mBoxFaceTextureRegion.getHeight();

    final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(10,
        0.5f, 0.5f);

    for (int i = 0; i < 3; i++) {
      final float anchorFaceX = centerX - spriteWidth * 0.5f + 220 * (i - 1);
      final float anchorFaceY = centerY - spriteHeight * 0.5f;

      final AnimatedSprite anchorFace = new AnimatedSprite(anchorFaceX,
          anchorFaceY, mBoxFaceTextureRegion);
      final Body anchorBody = PhysicsFactory.createBoxBody(mPhysicsWorld,
          anchorFace, BodyType.StaticBody, objectFixtureDef);

      final AnimatedSprite movingFace = new AnimatedSprite(anchorFaceX,
          anchorFaceY + 90, mCircleFaceTextureRegion);
      final Body movingBody = PhysicsFactory.createCircleBody(mPhysicsWorld,
          movingFace, BodyType.DynamicBody, objectFixtureDef);

      anchorFace.animate(200);
      movingFace.animate(200);

      scene.getLastChild().attachChild(anchorFace);
      scene.getLastChild().attachChild(movingFace);

      final Line connectionLine = new Line(anchorFaceX + spriteWidth / 2,
          anchorFaceY + spriteHeight / 2, anchorFaceX + spriteWidth / 2,
          anchorFaceY + spriteHeight / 2);
      scene.getFirstChild().attachChild(connectionLine);

      mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(anchorFace,
          anchorBody, true, true, false, false) {
        @Override
        public void onUpdate(final float pSecondsElapsed) {
          super.onUpdate(pSecondsElapsed);
          final Vector2 movingBodyWorldCenter = movingBody.getWorldCenter();
          connectionLine.setPosition(connectionLine.getX1(),
              connectionLine.getY1(),
              movingBodyWorldCenter.x *
                  PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
              movingBodyWorldCenter.y *
                  PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
        }
      });

      mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(movingFace,
          movingBody, true, true, false, false));

      final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
      revoluteJointDef.initialize(anchorBody, movingBody,
          anchorBody.getWorldCenter());
      revoluteJointDef.enableMotor = true;
      revoluteJointDef.motorSpeed = 10;
      revoluteJointDef.maxMotorTorque = 200;

      mPhysicsWorld.createJoint(revoluteJointDef);
    }
  }
}
