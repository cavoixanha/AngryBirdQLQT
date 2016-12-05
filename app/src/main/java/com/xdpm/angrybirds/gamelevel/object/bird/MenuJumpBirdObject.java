package com.xdpm.angrybirds.gamelevel.object.bird;

import org.andengine.entity.modifier.JumpModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.object.BaseObject;

public abstract class MenuJumpBirdObject extends BaseObject {

	// ==========================================================
	// Properties
	// ==========================================================
	private AnimatedSprite mEntity;
	private JumpModifier mJumpModifier;
	private int mTag;
	
	// ==========================================================
	// Constructors
	// ==========================================================

	public MenuJumpBirdObject(float pX, float pY, float pScale, ITiledTextureRegion pITiledTextureRegion) {
		super(pX, pY);
		this.mEntity = new AnimatedSprite(pX, pY, pITiledTextureRegion, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		this.mEntity.setScale(pScale);
		this.mEntity.setUserData(this);
	}

	public AnimatedSprite getEntity() {
		return this.mEntity;
	}
	
	public int getTag() {
		return this.mTag;
	}
	
	// ==========================================================
	// Methods from/for SuperClass/Interface
	// ==========================================================
	
	@Override
	public void onAreaTouched(TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			this.actionWhenTouch();
		}
	}
	
	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		
	}
	
	public void registerEntityModifier(JumpModifier pJumpModifier) {
		this.mJumpModifier = pJumpModifier;
		this.mJumpModifier.setAutoUnregisterWhenFinished(true);
		this.mEntity.registerEntityModifier(this.mJumpModifier);
	}

	protected void actionWhenTouch() {
		this.mEntity.animate(new long[] {BirdConstants.ACTION_LAUGH_ANIMATE_DURATION, BirdConstants.ACTION_NORMAL_ANIMATE_DURATION},
				new int[] {BirdConstants.TILED_LAUGH, BirdConstants.TILED_NORMAL});
		this.laught();
	}
	
	protected abstract void laught();
}
