package com.xdpm.angrybirds.object.button;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SfxManager;

public class SettingButton extends RotationButton{

	public SettingButton(float pX, float pY, float pScale) {
		super(pX, pY, pScale, ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary().get(ButtonConstants.MENU_SETTING_BUTTON_ID), Orientation.Vertical);
		this.mMenuItems = new Entity(0, 0);

		TexturePackTextureRegionLibrary buttonTexturePackTextureRegionLibrary = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary();
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		
		TextureRegion buttonInfoTextureRegion = buttonTexturePackTextureRegionLibrary.get(ButtonConstants.BUTTON_INFO_ID);
		Sprite buttonInfoSprite = new Sprite((this.mMenuSprite.getWidth() - buttonInfoTextureRegion.getWidth()) * 0.5f, 0, 
				buttonInfoTextureRegion, vertexBufferObjectManager);
		BaseButton<Sprite> buttonInfo = new BaseButton<Sprite>(buttonInfoSprite.getX(), 
				buttonInfoSprite.getY(), 1, buttonInfoSprite) {
			
			@Override
			public void onButtonClick() {
				Debug.e("Info");
			}
		};
		
		TextureRegion buttonMusicTextureRegion = buttonTexturePackTextureRegionLibrary.get(ButtonConstants.BUTTON_MUSIC_ID);
		ToggleButton buttonMusic = new ToggleButton(buttonInfo.getEntity().getX(), buttonInfo.getEntity().getY() + buttonInfo.getEntity().getHeight(),
				1, buttonMusicTextureRegion) {
			@Override
			public void onButtonClick() {
				super.onButtonClick();
				SfxManager.getInstance().setMusicMuted(mIsClicked);
			}
		};
		
		ResourceManager.getInstance().getHUD().registerTouchArea(buttonMusic.getEntity());
		ResourceManager.getInstance().getHUD().registerTouchArea(buttonInfo.getEntity());
		
		this.mMenuItems.attachChild(buttonMusic.getEntity());
		this.mMenuItems.attachChild(buttonInfo.getEntity());
		
		this.mMenuSprite.attachChild(this.mMenuItems);
		this.mMenuSprite.setHeight(this.mMenuItems.getChildCount() * ((Sprite) this.mMenuItems.getChildByIndex(0)).getHeight() + this.mEntity.getHeight() * 0.5f);
	}

}
