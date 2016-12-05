package com.xdpm.angrybirds;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.gamelevel.object.arbalest.Arbalest;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject.IOnBirdFlyingListener;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class AngryBirdSmoothCamera extends SmoothCamera implements IOnBirdFlyingListener {

	// ===========================================================
	// Properties
	// ===========================================================
	
	private boolean mIsInBase = true;
	
	private Arbalest mArbalest;
	
	//private BaseBirdObject mBaseBirdChasedObject;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	public AngryBirdSmoothCamera() {
		super(0, 0, 
				Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT, 
				Constants.CAMERA_MAX_VELOCITY_X, Constants.CAMERA_MAX_VELOCITY_Y,
				Constants.CAMERA_MAX_ZOOM_FACTOR_CHANGE);
		this.setBoundsEnabled(true);
		this.setBounds(0, 0, 
				Constants.CAMERA_BOUND_WIDTH, Constants.CAMERA_BOUND_HEIGHT);
	}
	
	// ===========================================================
	// Getters & Setters
	// ===========================================================
	
	// ===========================================================
	// Methods from/for SupperClass/Interface
	// ===========================================================
	
	@Override
	public void setZoomFactor(float pZoomFactor) {
		if (pZoomFactor > Constants.CAMERA_MAX_ZOOM_FACTOR || pZoomFactor < Constants.CAMERA_MIN_ZOOM_FACTOR) {
			return;
		} 
		// When zoom in max, bound is ACTUAL size 800x480
		// When zoom out max, bound is BOUND_CAMERA size = 2x ACTUAL size
		this.setBounds(0, 0 + Constants.CAMERA_BOUND_HEIGHT * 0.625f  - Constants.CAMERA_BOUND_HEIGHT * 0.3125f / (pZoomFactor / Constants.CAMERA_MAX_ZOOM_FACTOR), 
				Constants.CAMERA_BOUND_WIDTH, 0 + Constants.CAMERA_BOUND_HEIGHT * 0.625f  + Constants.CAMERA_BOUND_HEIGHT * 0.1875f / (pZoomFactor / Constants.CAMERA_MAX_ZOOM_FACTOR));
		super.setZoomFactor(pZoomFactor);
	}
	
	@Override
	public void offsetCenter(float pX, float pY) {
		float zoomFactor = this.getZoomFactor();
		super.offsetCenter(- pX * Constants.CAMERA_OFFSET_X_MULTIFILY / zoomFactor, 0);
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	public void gotoBase() {
		if (!this.mIsInBase) {
			this.mIsInBase = true;
			GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(2.0f, new ITimerCallback() {
				
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					setChaseEntity(mArbalest.getFrontLayer());
				}
			}));
		}
	}
	
	public void setArbalest(Arbalest pArbalest) {
		this.mArbalest = pArbalest;
	}
	
	public void setMenuCamera() {
		this.setBounds(0, 0, 
				Constants.CAMERA_BOUND_WIDTH, Constants.CAMERA_BOUND_HEIGHT);
		this.setZoomFactorDirect(Constants.CAMERA_MAX_ZOOM_FACTOR);
		this.setCenterDirect(Constants.CAMERA_CENTER_X, Constants.CAMERA_CENTER_Y * 1.25f);
	}
	
	public void setSelectorCamera(){
		this.setBounds(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		this.setZoomFactorDirect(Constants.CAMERA_MAX_ZOOM_FACTOR);
		this.setCenterDirect(Constants.CAMERA_WIDTH*0.5f, Constants.CAMERA_HEIGHT*0.5f);
	}
	
	public void setGameCamera() {
		this.setBounds(0, 0, 
				Constants.CAMERA_BOUND_WIDTH, Constants.CAMERA_BOUND_HEIGHT);
		this.setZoomFactorDirect(Constants.CAMERA_MIN_ZOOM_FACTOR);
		this.setCenterDirect(Constants.CAMERA_CENTER_X, Constants.CAMERA_CENTER_Y);
	}
	
	public void setChasedBird(BaseBirdObject pBirdObject) {
		this.mIsInBase = false;
		this.removeChasedObject();
		pBirdObject.setBirdFlyingListener(this);
		this.setChaseEntity(pBirdObject.getEntity());
	} 
	
	public void removeChasedObject() {
		this.setChaseEntity(null);
	}

	@Override
	public void onBirdFlying(BaseBirdObject pBaseBirdObject) {
		if (pBaseBirdObject.getEntity().getY() < 0 || pBaseBirdObject.getEntity().getY() > this.getBoundsYMin()) {
			return;
		}
		
		this.setZoomFactor(Constants.CAMERA_MIN_ZOOM_FACTOR + pBaseBirdObject.getEntity().getY() /  this.getBoundsYMin() / 2);
	}
}
