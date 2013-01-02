package org.anddev.andengine.examples;

import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

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
    mSubMenuTexture = new Texture(256, 128, TextureOptions.BILINEAR);
    mMenuOkTextureRegion = TextureRegionFactory.createFromAsset(
        mSubMenuTexture, this, "gfx/menu_ok.png", 0, 0);
    mMenuBackTextureRegion = TextureRegionFactory.createFromAsset(
        mSubMenuTexture, this, "gfx/menu_back.png", 0, 50);

    getEngine().getTextureManager().loadTexture(mSubMenuTexture);
  }

  @Override
  public boolean onMenuItemClicked(final MenuScene pMenuScene,
      final IMenuItem pMenuItem, final float pMenuItemLocalX,
      final float pMenuItemLocalY) {
    switch (pMenuItem.getID()) {
    case MENU_RESET:
      mMainScene.reset();
      mMenuScene.back();
      return true;
    case MENU_QUIT:
      pMenuScene.setChildSceneModal(mSubMenuScene);
      return true;
    case MENU_QUIT_BACK:
      mSubMenuScene.back();
      return true;
    case MENU_QUIT_OK:
      finish();
      return true;
    default:
      return false;
    }
  }

  @Override
  protected void createMenuScene() {
    super.createMenuScene();

    mSubMenuScene = new MenuScene(mCamera);
    mSubMenuScene.addMenuItem(new SpriteMenuItem(MENU_QUIT_OK,
        mMenuOkTextureRegion));
    mSubMenuScene.addMenuItem(new SpriteMenuItem(MENU_QUIT_BACK,
        mMenuBackTextureRegion));
    mSubMenuScene.setMenuAnimator(new SlideMenuAnimator());
    mSubMenuScene.buildAnimations();

    mSubMenuScene.setBackgroundEnabled(false);
    mSubMenuScene.setOnMenuItemClickListener(this);
  }
}