package org.anddev.andengine.examples;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.SmoothEngine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.modifier.AccelerationInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorInitializer;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.RotationInitializer;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.particle.modifier.VelocityInitializer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class ParticleSystemNexusExample extends BaseExample {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;
  private static final float RATE_MIN = 8;
  private static final float RATE_MAX = 12;
  private static final int PARTICLES_MAX = 200;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mParticleTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new SmoothEngine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(32, 32, TextureOptions.BILINEAR);
    mParticleTextureRegion = TextureRegionFactory.createFromAsset(mTexture, this,
        "gfx/particle.png", 0, 0);
    getEngine().getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    getEngine().registerPreFrameHandler(new FPSLogger());

    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.0f, 0.0f, 0.0f);

    // lower left to lower right particle system
    {
      final ParticleSystem particleSystem = new ParticleSystem(-32,
          CAMERA_HEIGHT - 32, 0, 0, RATE_MIN, RATE_MAX, PARTICLES_MAX,
          mParticleTextureRegion);
      particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

      particleSystem.addParticleInitializer(new VelocityInitializer(35, 45, 0, -10));
      particleSystem.addParticleInitializer(new AccelerationInitializer(5, -11));
      particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
      particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 1.0f, 0.0f));

      particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
      particleSystem.addParticleModifier(new ExpireModifier(6.5f));
      particleSystem.addParticleModifier(new ColorModifier(1.0f, 1.0f, 1.0f,
          1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
      particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));

      scene.getTopLayer().addEntity(particleSystem);
    }

    // lower right to lower left particle system
    {
      final ParticleSystem particleSystem = new ParticleSystem(CAMERA_WIDTH,
          CAMERA_HEIGHT - 32, 0, 0, RATE_MIN, RATE_MAX, PARTICLES_MAX,
          mParticleTextureRegion);
      particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

      particleSystem.addParticleInitializer(new VelocityInitializer(-35, -45, 0, -10));
      particleSystem.addParticleInitializer(new AccelerationInitializer(-5, -11));
      particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
      particleSystem.addParticleInitializer(new ColorInitializer(0.0f, 1.0f, 0.0f));

      particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
      particleSystem.addParticleModifier(new ExpireModifier(6.5f));
      particleSystem.addParticleModifier(new ColorModifier(0.0f, 1.0f, 1.0f,
          1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
      particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));

      scene.getTopLayer().addEntity(particleSystem);
    }

    // upper left to upper right particle system
    {
      final ParticleSystem particleSystem = new ParticleSystem(-32, 0, 0, 0,
          RATE_MIN, RATE_MAX, PARTICLES_MAX, mParticleTextureRegion);
      particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

      particleSystem.addParticleInitializer(new VelocityInitializer(35, 45, 0, 10));
      particleSystem.addParticleInitializer(new AccelerationInitializer(5, 11));
      particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
      particleSystem.addParticleInitializer(new ColorInitializer(0.0f, 0.0f, 1.0f));

      particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
      particleSystem.addParticleModifier(new ExpireModifier(6.5f));
      particleSystem.addParticleModifier(new ColorModifier(0.0f, 1.0f, 0.0f,
          1.0f, 1.0f, 1.0f, 2.5f, 5.5f));
      particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));

      scene.getTopLayer().addEntity(particleSystem);
    }

    // upper right to upper left particle system
    {
      final ParticleSystem particleSystem = new ParticleSystem(CAMERA_WIDTH,
          0, 0, 0, RATE_MIN, RATE_MAX, PARTICLES_MAX, mParticleTextureRegion);
      particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

      particleSystem.addParticleInitializer(new VelocityInitializer(-35, -45, 0, 10));
      particleSystem.addParticleInitializer(new AccelerationInitializer(-5, 11));
      particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
      particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 0.0f, 0.0f));

      particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
      particleSystem.addParticleModifier(new ExpireModifier(6.5f));
      particleSystem.addParticleModifier(new ColorModifier(1.0f, 1.0f, 0.0f,
          1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
      particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));

      scene.getTopLayer().addEntity(particleSystem);
    }

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }
}
