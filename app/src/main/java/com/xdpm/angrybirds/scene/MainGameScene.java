package com.xdpm.angrybirds.scene;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.AngryBirdSmoothCamera;
import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SceneManager;
import com.xdpm.angrybirds.object.BaseObject;
import com.xdpm.angrybirds.object.button.BaseButton;
import com.xdpm.angrybirds.scene.background.Background;
import com.xdpm.angrybirds.scene.layer.GameLevelFailedLayer;
import com.xdpm.angrybirds.scene.layer.GameLevelPauseLayer;
import com.xdpm.angrybirds.scene.layer.GameLevelClearedLayer;
import com.xdpm.angrybirds.setting.Settings;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class MainGameScene extends BaseScene implements IPinchZoomDetectorListener, IOnSceneTouchListener, IScrollDetectorListener {
	
	// =================================================================
	// Properties
	// =================================================================
	private AngryBirdSmoothCamera mCamera = ResourceManager.getInstance().getCamera();
	private float mInitiateZoomFactor;
	private PinchZoomDetector mPinchZoomDetector;
	private SurfaceScrollDetector mSurfaceScrollDetector;
	
	private static int mWorldId = 1;
	public static void setmWorldId(int mWorldId) {
		MainGameScene.mWorldId = mWorldId;
	}
	private int mStageId = 1;
	private int mLevelId = 1;
	
	private GameLevelClearedLayer mGameLevelClearedLayer;
	private GameLevelFailedLayer mGameLevelFailedLayer;
	private GameLevelPauseLayer mGameLevelPauseLayer;
	
	private boolean mStopScrolling = false;
	
	private BaseButton<Sprite> mButtonPause;
	
	// =================================================================
	// Constructors
	// =================================================================
	
	public MainGameScene() {
		super(true);
		this.mBackground = new Background(this);
		this.mGameLevelClearedLayer = new GameLevelClearedLayer();
		this.mGameLevelFailedLayer = new GameLevelFailedLayer();
		this.mGameLevelPauseLayer = new GameLevelPauseLayer();
	}
	
	// =================================================================
	// Methods from/for SuperClass/Interface
	// =================================================================

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent, float pZoomFactor) {
		this.mCamera.setZoomFactor(this.mInitiateZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent,
			float pZoomFactor) {
		this.mCamera.setZoomFactor(this.mInitiateZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent) {
		this.mInitiateZoomFactor = this.mCamera.getZoomFactor();
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pITouchArea, float pTouchLocalX, float pTouchLocalY) {
		super.onAreaTouched(pSceneTouchEvent, pITouchArea, pTouchLocalX,
				pTouchLocalY);
		
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			BaseObject object = (BaseObject) ((IEntity) pITouchArea).getUserData();
			if (null != object) {
				this.mStopScrolling = true;
			}
			break;
		case TouchEvent.ACTION_UP: case TouchEvent.ACTION_CANCEL:
			if (this.mStopScrolling == true) {
				this.mStopScrolling = false;
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		this.mPinchZoomDetector.onSceneTouchEvent(pScene, pSceneTouchEvent);
		
		if (this.mPinchZoomDetector.isZooming()) {
			this.mSurfaceScrollDetector.setEnabled(false);
		} else {
			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_UP: case TouchEvent.ACTION_CANCEL:
				this.mSurfaceScrollDetector.setEnabled(false);
				this.mStopScrolling = false;
				break;
			case TouchEvent.ACTION_MOVE:
				if (!this.mStopScrolling) {
					this.mSurfaceScrollDetector.setEnabled(true);
					this.mSurfaceScrollDetector.onTouchEvent(pSceneTouchEvent);
				}
				break;
			default:
				break;
			}
		}
		
		if (null != GameLevelManager.getInstance().getCurrentGameLevel()) {
			GameLevelManager.getInstance().getCurrentGameLevel().onSceneTouchEvent(pSceneTouchEvent);
		}
		
		return true;
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerId, float pDistanceX, float pDistanceY) {
		this.mCamera.offsetCenter(pDistanceX, pDistanceY);
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerId, float pDistanceX,
			float pDistanceY) {
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerId, float pDistanceX,
			float pDistanceY) {	
	}

	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack)
			throws Exception {
		
		// Read id saved in Shared Preference from Level Selector
//		this.mWorldId = Settings.getInstance().getWorldId();
//		this.mStageId = Settings.getInstance().getStageId();
//		this.mLevelId = Settings.getInstance().getLevelId();
		
		//fake game level load
		// change worldID
		//this.mWorldId = 1;
		this.mStageId = 1;
		this.mLevelId = Settings.getInstance().getLevelId() % 10 + 1;
		Log.d("Scene","this.mLevelId: " + Settings.getInstance().getLevelId());
		
		// Repair Game Data
		GameLevelManager.getInstance().setMainGameScene(this);
		
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		
		// Repair background
		this.mBackground.loadBackground();
		
		// Button Pause
		Sprite buttonPauseSprite = new Sprite(0, 0, 
				ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary().get(ButtonConstants.BUTTON_PAUSE_ID),
				vertexBufferObjectManager);
		this.mButtonPause = new BaseButton<Sprite>(buttonPauseSprite.getX(), buttonPauseSprite.getY(), 1, buttonPauseSprite) {
			@Override
			public void onButtonClick() {
				mGameLevelPauseLayer.showScene();
			}
		};
		
		// Pinch Zoom
		this.mPinchZoomDetector = new PinchZoomDetector(this);
		this.mPinchZoomDetector.setEnabled(true);
		this.mSurfaceScrollDetector = new SurfaceScrollDetector(this);
		this.mSurfaceScrollDetector.setTriggerScrollMinimumDistance(1.0f);
		
		HUD hud = ResourceManager.getInstance().getHUD();
		hud.setOnAreaTouchListener(this);
		
		this.setOnSceneTouchListener(this);
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		
		pOnLoadSceneCallBack.onLoadSceneFinish();
	}

	@Override
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {
		
		// Repair camera
		this.mCamera.setGameCamera();
		
		this.mBackground.showBackground();
		this.mBackground.enableText(false);
		this.mBackground.isMovingBackground(false);
		
		GameLevelManager.getInstance().createGameLevel(this.mWorldId, this.mStageId, this.mLevelId);
		
		HUD hud = ResourceManager.getInstance().getHUD();
		hud.registerTouchArea(this.mButtonPause.getEntity());
		hud.attachChild(this.mButtonPause.getEntity());
		
		pOnShowSceneCallBack.onShowSceneFinish();
	}

	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		
		ResourceManager.getInstance().getHUD().detachChildren();
		ResourceManager.getInstance().getHUD().clearTouchAreas();
		
		pOnHideSceneCallBack.onHideSceneFinish();
	}

	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack)
			throws Exception {

		this.mBackground.unloadBackground();
		this.mBackground = null;
		
		GameLevelManager.getInstance().destroyGameLevel();

		this.detachChildren();
		this.clearEntityModifiers();
		this.clearTouchAreas();
		this.clearUpdateHandlers();

		pOnUnLoadSceneCallBack.onUnLoadSceneFinish();
	}
	
	// =================================================================
	// Methods
	// =================================================================

	public void showGameLevelClearedLayer() {
		this.mGameLevelClearedLayer.showScene();
	}
	
	public void showGameLevelFailedLayer() {
		this.mGameLevelFailedLayer.showScene();
	}
	
	public void disableButtonPause() {
		ResourceManager.getInstance().getHUD().unregisterTouchArea(this.mButtonPause.getEntity());
	}
	
	public void enableButtonPause() {
		ResourceManager.getInstance().getHUD().registerTouchArea(this.mButtonPause.getEntity());
	}
}
