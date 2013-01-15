package org.anddev.andengine.examples;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.anddev.andengine.entity.particle.modifier.AlphaInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorInitializer;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.RotationInitializer;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.entity.particle.modifier.VelocityInitializer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.LayoutGameActivity;

import android.widget.Toast;

public class XMLLayoutExample extends LayoutGameActivity {
  private static final int CAMERA_WIDTH = 720;
  private static final int CAMERA_HEIGHT = 480;

  private Camera mCamera;
  private Texture mTexture;
  private TextureRegion mParticleTextureRegion;

  @Override
  public Engine onLoadEngine() {
    Toast.makeText(this, "Touch the screen to move the particlesystem.",
        Toast.LENGTH_LONG).show();
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
        new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera));
  }

  @Override
  public void onLoadResources() {
    mTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mParticleTextureRegion = TextureRegionFactory.createFromAsset(mTexture,
        this, "gfx/particle_point.png", 0, 0);
    mEngine.getTextureManager().loadTexture(mTexture);
  }

  @Override
  public Scene onLoadScene() {
    mEngine.registerUpdateHandler(new FPSLogger());

    final Scene scene = new Scene(1);

    final CircleOutlineParticleEmitter particleEmitter =
        new CircleOutlineParticleEmitter(CAMERA_WIDTH * 0.5f,
            CAMERA_HEIGHT * 0.5f + 20, 80);
    final ParticleSystem particleSystem = new ParticleSystem(particleEmitter,
        60, 60, 360, mParticleTextureRegion);

    scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
      @Override
      public boolean onSceneTouchEvent(final Scene pScene,
          final TouchEvent pSceneTouchEvent) {
        particleEmitter.setCenter(pSceneTouchEvent.getX(),
            pSceneTouchEvent.getY());
        return true;
      }
    });

    particleSystem.addParticleInitializer(new ColorInitializer(1, 0, 0));
    particleSystem.addParticleInitializer(new AlphaInitializer(0));
    particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
    particleSystem.addParticleInitializer(new VelocityInitializer(-2, 2, -20,
        -10));
    particleSystem.addParticleInitializer(new RotationInitializer(0, 360));

    particleSystem.addParticleModifier(new ScaleModifier(1, 2, 0, 5));
    particleSystem.addParticleModifier(new ColorModifier(1, 1, 0, 0.5f, 0, 0, 0,
        3));
    particleSystem.addParticleModifier(new ColorModifier(1, 1, 0.5f, 1, 0, 1, 4,
        6));
    particleSystem.addParticleModifier(new AlphaModifier(0, 1, 0, 1));
    particleSystem.addParticleModifier(new AlphaModifier(1, 0, 5, 6));
    particleSystem.addParticleModifier(new ExpireModifier(6, 6));

    scene.getLastChild().attachChild(particleSystem);

    return scene;
  }

  @Override
  public void onLoadComplete() {
  }

  @Override
  protected int getLayoutID() {
    return R.layout.xmllayoutexample;
  }

  @Override
  protected int getRenderSurfaceViewID() {
    return R.id.xmllayoutexample_rendersurfaceview;
  }
}
