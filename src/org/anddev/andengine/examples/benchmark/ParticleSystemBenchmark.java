package org.anddev.andengine.examples.benchmark;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.modifier.AccelerationInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorInitializer;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.RotateInitializer;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.particle.modifier.VelocityInitializer;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class ParticleSystemBenchmark extends BaseBenchmark {
  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 320;
  private static final float RATE_MIN = 15;
  private static final float RATE_MAX = 20;
  private static final int PARTICLES_MAX = 200;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mParticleTextureRegion;

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera, false));
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
    final Scene scene = new Scene(1);
    scene.setBackgroundColor(0.0f, 0.0f, 0.0f);

    // lower left to lower right particle system
    {
      final ParticleSystem particleSystem = new ParticleSystem(0, CAMERA_HEIGHT,
          0, 0, RATE_MIN, RATE_MAX, PARTICLES_MAX, mParticleTextureRegion);
      particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

      particleSystem.addParticleInitializer(new VelocityInitializer(25, 35, -60, -90));
      particleSystem.addParticleInitializer(new AccelerationInitializer(5, 15));
      particleSystem.addParticleInitializer(new RotateInitializer(0.0f, 360.0f));
      particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 0.0f, 0.0f));

      particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
      particleSystem.addParticleModifier(new ExpireModifier(6.5f));
      particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));

      scene.getTopLayer().addEntity(particleSystem);
    }

    // lower right to lower left particle system
    {
      final ParticleSystem particleSystem = new ParticleSystem(CAMERA_WIDTH -32,
          CAMERA_HEIGHT, 0, 0, RATE_MIN, RATE_MAX, PARTICLES_MAX,
          mParticleTextureRegion);
      particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

      particleSystem.addParticleInitializer(new VelocityInitializer(-25, -35, -60, -90));
      particleSystem.addParticleInitializer(new AccelerationInitializer(-5, 15));
      particleSystem.addParticleInitializer(new RotateInitializer(0.0f, 360.0f));
      particleSystem.addParticleInitializer(new ColorInitializer(0.0f, 0.0f, 1.0f));

      particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
      particleSystem.addParticleModifier(new ExpireModifier(6.5f));
      particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));

      scene.getTopLayer().addEntity(particleSystem);
    }

    // upper left to upper right particle system
    {
      final ParticleSystem particleSystem = new ParticleSystem(0, -32, 0, 0,
          RATE_MIN, RATE_MAX, PARTICLES_MAX, mParticleTextureRegion);
      particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

      particleSystem.addParticleInitializer(new VelocityInitializer(25, 35, 60, 90));
      particleSystem.addParticleInitializer(new AccelerationInitializer(5, -15));
      particleSystem.addParticleInitializer(new RotateInitializer(0.0f, 360.0f));
      particleSystem.addParticleInitializer(new ColorInitializer(0.0f, 1.0f, 0.0f));

      particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
      particleSystem.addParticleModifier(new ExpireModifier(6.5f));
      particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));

      scene.getTopLayer().addEntity(particleSystem);
    }

    // upper right to upper left particle system
    {
      final ParticleSystem particleSystem = new ParticleSystem(CAMERA_WIDTH -32,
          -32, 0, 0, RATE_MIN, RATE_MAX, PARTICLES_MAX, mParticleTextureRegion);
      particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

      particleSystem.addParticleInitializer(new VelocityInitializer(-25, -35, 60, 90));
      particleSystem.addParticleInitializer(new AccelerationInitializer(-5, -15));
      particleSystem.addParticleInitializer(new RotateInitializer(0.0f, 360.0f));
      particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 1.0f, 0.0f));

      particleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.0f, 0, 5));
      particleSystem.addParticleModifier(new ExpireModifier(6.5f));
      particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f, 6.5f));

      scene.getTopLayer().addEntity(particleSystem);
    }

    return scene;
  }

  @Override
  protected float getBenchmarkDuration() {
    return 15;
  }

  @Override
  protected float getBenchmarkStartOffset() {
    return 5;
  }

}