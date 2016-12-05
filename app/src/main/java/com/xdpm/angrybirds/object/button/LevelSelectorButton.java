package com.xdpm.angrybirds.object.button;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.xdpm.angrybirds.manager.SceneManager;
import com.xdpm.angrybirds.setting.Settings;

public class LevelSelectorButton extends BaseButton<Sprite>{

	// =======================================================
	// Properties
	// =======================================================
	
	private int mWorldId;
	private int mStageId;
	private int mLevelId;
	
	// =======================================================
	// Constructors
	// =======================================================
	
	public LevelSelectorButton(float pX, float pY, float pScale, int pLevelId) {
		super(pX, pY, pScale);
	}
	
	public LevelSelectorButton(float pX, float pY, float pScale, int pStageId, Sprite pEntity){
		super(pX, pY, pScale, pEntity);
		this.mLevelId = pStageId;
	}

	// =======================================================
	// Getters & Setters
	// =======================================================
	
	public int getWordlId() {
		return this.mWorldId;
	}
	
	public void setWorldId(int pWorldId) {
		this.mWorldId = pWorldId;
	}
	
	public int getStageId() {
		return this.mStageId;
	}
	
	public void setStageId(int pStageId) {
		this.mStageId = pStageId;
	}
	
	public int getLevelId() {
		return this.mLevelId;
	}
	
	public void setLevelId(int pLevelId) {
		this.mLevelId = pLevelId;
	}
	
	// =======================================================
	// Method from/for SuperClass/Interface
	// =======================================================
	
	@Override
	public void onAreaTouched(TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			this.onButtonClick();
		}
	}
	@Override
	public void onButtonClick() {
		Settings.getInstance().setWorldId(this.mWorldId);
		Settings.getInstance().setStagedId(this.mStageId);
		Settings.getInstance().setLevelId(this.mLevelId);
		
		SceneManager.getInstance().showMainGameScene();
	}

}
