package org.andengine.examples;

import org.anddev.andengine.entity.menu.MenuItem;
import org.anddev.andengine.entity.menu.MenuScene;
import org.anddev.andengine.entity.menu.animator.SlideMenuAnimator;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureRegion;
import org.anddev.andengine.opengl.texture.TextureRegionFactory;


public class SubMenuExample extends MenuExample {
  private static final int MENU_QUIT_OK = MenuExample.MENU_QUIT + 1;
  private static final int MENU_QUIT_BACK = MENU_QUIT_OK + 1;

  private MenuScene mSubMenuScene;

  private Texture mSubMenuTexture;
  private TextureRegion mMenuOkTextureRegion;
  private TextureRegion mMenuBackTextureRegion;

  @Override
  public void onLoadResources() {
    super.onLoadResources();
    mSubMenuTexture = new Texture(256, 128);
    mMenuOkTextureRegion =  TextureRegionFactory.createFromAsset(
        mSubMenuTexture, this, "gfx/menu_ok.png", 0, 0);
    mMenuBackTextureRegion =  TextureRegionFactory.createFromAsset(
        mSubMenuTexture, this, "gfx/menu_back.png", 0, 50);
    getEngine().loadTexture(mSubMenuTexture);
  }

  @Override
  public void onMenuItemClicked(final MenuScene pMenuScene,
      final MenuItem pMenuItem) {
    switch (pMenuItem.getMenuID()) {
    case MENU_RESET:
      mMainScene.reset();
      mMenuScene.back();
      break;
    case MENU_QUIT:
      pMenuScene.setChildSceneModal(mSubMenuScene);
      break;
    case MENU_QUIT_BACK:
      mSubMenuScene.back();
      break;
    case MENU_QUIT_OK:
      finish();
      break;
    default:
      break;
    }
  }

  @Override
  protected MenuScene createMenuScene() {
    final MenuScene mainMenuScene = super.createMenuScene();

    mSubMenuScene = new MenuScene(mCamera);
    mSubMenuScene.addMenuItem(new MenuItem(MENU_QUIT_OK, mMenuOkTextureRegion));
    mSubMenuScene.addMenuItem(new MenuItem(MENU_QUIT_BACK,
        mMenuBackTextureRegion));
    mSubMenuScene.setMenuAnimator(new SlideMenuAnimator());
    mSubMenuScene.buildAnimations();

    mSubMenuScene.setBackgroundEnabled(false);
    mSubMenuScene.setOnMenuItemClickerListener(this);

    return mainMenuScene;
  }
}
