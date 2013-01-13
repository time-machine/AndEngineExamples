package org.anddev.andengine.examples.app.cityradar;

import java.util.ArrayList;
import java.util.HashMap;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.layer.ILayer;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.examples.adt.City;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.builder.ITextureBuilder.TextureSourcePackingException;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.location.ILocationListener;
import org.anddev.andengine.sensor.location.LocationProviderStatus;
import org.anddev.andengine.sensor.orientation.IOrientationListener;
import org.anddev.andengine.sensor.orientation.OrientationData;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class CityRadarActivity extends BaseGameActivity implements
    IOrientationListener, ILocationListener {
  private static final boolean USE_MOCK_LOCATION = false;
  private static final boolean USE_ACTUAL_LOCATION = !USE_MOCK_LOCATION;

  private static final int CAMERA_WIDTH = 480;
  private static final int CAMERA_HEIGHT = 800;

  private static final int GRID_SIZE = 80;

  private static final int LAYER_COUNT = 1;
  private static final int LAYER_CITIES = 0;

  private Camera mCamera;

  private BuildableTexture mBuildableTexture;

  private TextureRegion mRadarPointTextureRegion;
  private TextureRegion mRadarTextureRegion;

  private Texture mFontTexture;
  private Font mFont;

  private final Location mUserLocation;

  private final ArrayList<City> mCities = new ArrayList<City>();
  private final HashMap<City, Sprite> mCityToCitySpriteMap = new HashMap<City,
      Sprite>();
  private final HashMap<City, Text> mCityToCityNameTextMap = new HashMap<City,
      Text>();

  public CityRadarActivity() {
    mCities.add(new City("London", 51.509, -0.118));
    mCities.add(new City("New York", 40.713, -74.006));
    mCities.add(new City("Beijing", 39.929, 116.388));
    mCities.add(new City("Sydney", -33.850, 151.200));
    mCities.add(new City("Berlin", 51.518, 13.408));
    mCities.add(new City("Rio", -22.908, -43.196));
    mCities.add(new City("New Delhi", 28.636, 77.224));
    mCities.add(new City("Cape Town", -33.926, 18.424));

    mUserLocation = new Location(LocationManager.GPS_PROVIDER);

    if (USE_MOCK_LOCATION) {
      mUserLocation.setLatitude(51.518);
      mUserLocation.setLongitude(13.408);
    }
  }

  @Override
  public Engine onLoadEngine() {
    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
        new FillResolutionPolicy(), mCamera));
  }

  @Override
  public void onLoadResources() {
    // init font
    mFontTexture = new Texture(256, 256,
        TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    mFont = new Font(mFontTexture, Typeface.DEFAULT, 12, true, Color.WHITE);

    mEngine.getFontManager().loadFont(mFont);
    mEngine.getTextureManager().loadTexture(mFontTexture);

    // init texture regions
    mBuildableTexture = new BuildableTexture(512, 256,
        TextureOptions.BILINEAR_PREMULTIPLYALPHA);

    mRadarTextureRegion = TextureRegionFactory.createFromAsset(
        mBuildableTexture, this, "gfx/radar.png");
    mRadarPointTextureRegion = TextureRegionFactory.createFromAsset(
        mBuildableTexture, this, "gfx/radarpoint.png");

    try {
      mBuildableTexture.build(new BlackPawnTextureBuilder(1));
    }
    catch (final TextureSourcePackingException e) {
      Debug.e(e);
    }

    mEngine.getTextureManager().loadTexture(mBuildableTexture);
  }

  @Override
  public Scene onLoadScene() {
    final Scene scene = new Scene(LAYER_COUNT);

    final HUD hud = new HUD();
    mCamera.setHUD(hud);

    // background
    initBackground(hud.getBottomLayer());

    // TODO
    return null;
  }

  private void initBackground(final ILayer pLayer) {
    // vertical grid lines
    for (int i = GRID_SIZE / 2; i < CAMERA_WIDTH; i += GRID_SIZE) {
      // TODO
    }
  }

  @Override
  public void onLoadComplete() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onOrientationChanged(final OrientationData pOrientationData) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onLocationProviderEnabled() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onLocationChanged(final Location pLocation) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onLocationLost() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onLocationProviderDisabled() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onLocationProviderStatusChanged(
      final LocationProviderStatus pLocationProviderStatus, final Bundle pBundle) {
    // TODO Auto-generated method stub

  }

}
