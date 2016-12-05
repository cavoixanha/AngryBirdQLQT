package com.xdpm.angrybirds.object.button;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.manager.ResourceManager;

public class FanPageButton extends RotationButton {

	public FanPageButton(float pX, float pY, float pScale) {
		super(pX, pY, pScale, ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary().get(ButtonConstants.MENU_FANPAGE_BUTTON_ID), Orientation.Vertical);
		this.mMenuItems = new Entity(0, 0);
		
		TexturePackTextureRegionLibrary buttonTexturePackTextureRegionLibrary = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary();
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		
		TextureRegion buttonFacebookTextureRegion = buttonTexturePackTextureRegionLibrary.get(ButtonConstants.BUTTON_FACEBOOK_ID);
		Sprite buttonFacebookSprite = new Sprite((this.mMenuSprite.getWidth() - buttonFacebookTextureRegion.getWidth()) * 0.5f, 0, 
				buttonFacebookTextureRegion, vertexBufferObjectManager);
		BaseButton<Sprite> buttonFacebook = new BaseButton<Sprite>(buttonFacebookSprite.getX(), 
				buttonFacebookSprite.getY(), 1, buttonFacebookSprite) {
			
			@Override
			public void onButtonClick() {
				Debug.e("Facebook");
			}
		};
		
		TextureRegion buttonTwisterTextureRegion = buttonTexturePackTextureRegionLibrary.get(ButtonConstants.BUTTON_TWISTER_ID);
		Sprite buttonTwisterSprite = new Sprite(buttonFacebook.getEntity().getX(), buttonFacebook.getEntity().getY() + buttonFacebook.getEntity().getHeight(), 
				buttonTwisterTextureRegion, vertexBufferObjectManager);
		BaseButton<Sprite> buttonTwister = new BaseButton<Sprite>(buttonTwisterSprite.getX(), 
				buttonTwisterSprite.getY(), 1, buttonTwisterSprite) {
			
			@Override
			public void onButtonClick() {
				Debug.e("Info");
			}
		};
		
		ResourceManager.getInstance().getHUD().registerTouchArea(buttonFacebook.getEntity());
		ResourceManager.getInstance().getHUD().registerTouchArea(buttonTwister.getEntity());
		
		this.mMenuItems.attachChild(buttonFacebook.getEntity());
		this.mMenuItems.attachChild(buttonTwister.getEntity());
		
		this.mMenuSprite.attachChild(this.mMenuItems);
		this.mMenuSprite.setHeight(this.mMenuItems.getChildCount() * ((Sprite) this.mMenuItems.getChildByIndex(0)).getHeight() + this.mEntity.getHeight() * 0.5f);
	}

}
