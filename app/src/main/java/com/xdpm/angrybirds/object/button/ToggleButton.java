package com.xdpm.angrybirds.object.button;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.manager.ResourceManager;

public abstract class ToggleButton extends BaseButton<Sprite>{

	// =======================================================
	// Properties
	// =======================================================
	
	private Sprite mDeactiveSprite;
	
	public ToggleButton(float pX, float pY, float pScale, TextureRegion pTextureRegion) {
		super(pX, pY, pScale);
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		
		this.mEntity = new Sprite(pX, pY, pTextureRegion, vertexBufferObjectManager);
		this.mDeactiveSprite = new Sprite(0, 0, 
				ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary().get(ButtonConstants.BUTTON_DEACTIVE_ID), 
				vertexBufferObjectManager);
		
		this.mEntity.setScale(pScale);
		
		this.mEntity.setUserData(this);
		this.mDeactiveSprite.setUserData(this);
	}

	@Override
	public void onButtonClick() {
		if (!this.mIsClicked) {
			this.mEntity.attachChild(this.mDeactiveSprite);
		} else {
			ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
				
				@Override
				public void run() {
					mDeactiveSprite.detachSelf();					
				}
			});

		}
		this.mIsClicked = !this.mIsClicked;
	}
}
