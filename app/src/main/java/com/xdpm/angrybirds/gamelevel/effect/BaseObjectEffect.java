package com.xdpm.angrybirds.gamelevel.effect;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.xdpm.angrybirds.manager.ResourceManager;

public abstract class BaseObjectEffect {

	protected AnimatedSprite mEntity;
	
	public BaseObjectEffect(float pX, float pY, ITiledTextureRegion pTextureRegion) {
		this.mEntity = new AnimatedSprite(pX, pY, pTextureRegion, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
	}
	
	public Sprite getEntity() {
		return this.mEntity;
	}
	
	public abstract void startEffect();
}
