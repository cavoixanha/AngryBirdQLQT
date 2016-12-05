package com.xdpm.angrybirds.object.button;

import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;

import com.xdpm.angrybirds.object.BaseObject;

public abstract class BaseButton<T extends IEntity> extends BaseObject {
	
	// =======================================================
	// Properties
	// =======================================================
	protected boolean mIsClicked = false;
	protected T mEntity;

	// =======================================================
	// Constructors
	// =======================================================
	
	public BaseButton(float pX, float pY, float pScale) {
		super(pX, pY);
	}
	
	public BaseButton(float pX, float pY, float pScale, T pEntity) {
		super(pX, pY);
		this.mEntity = pEntity;
		this.mEntity.setScale(pScale);
		this.mEntity.setUserData(this);
	}
	
	// =======================================================
	// Getters & Setters
	// =======================================================
	
	public T getEntity() {
		return this.mEntity;
	}
	
	// =======================================================
	// Methods from/for SuperClass/Interface
	// =======================================================
	
	@Override
	public void onAreaTouched(TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			this.onButtonClick();
		}
	}

	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		
	}
	
	public abstract void onButtonClick();
}
