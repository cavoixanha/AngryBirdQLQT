package com.xdpm.angrybirds.gamelevel;

public class Level {
	
	private int mWorldId;
	private int mStageId;
	private int mLevelId;
	private float mHighScore;
	private boolean mIsUnLocked;
	
	public int getWorldId() {
		return mWorldId;
	}
	public int getStageId() {
		return mStageId;
	}
	public int getLevelId() {
		return mLevelId;
	}
	public float getHighScore() {
		return mHighScore;
	}
	public boolean isIsUnLocked() {
		return mIsUnLocked;
	}
	public void setWorldId(int mWorldId) {
		this.mWorldId = mWorldId;
	}
	public void setStageId(int mStageId) {
		this.mStageId = mStageId;
	}
	public void setLevelId(int mLevelId) {
		this.mLevelId = mLevelId;
	}
	public void setHighScore(float mHighScore) {
		this.mHighScore = mHighScore;
	}
	public void setIsUnLocked(boolean mIsUnLocked) {
		this.mIsUnLocked = mIsUnLocked;
	}	
}
