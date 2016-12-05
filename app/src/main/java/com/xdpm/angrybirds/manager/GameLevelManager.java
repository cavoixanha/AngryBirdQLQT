package com.xdpm.angrybirds.manager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.debug.Debug;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.xdpm.angrybirds.gamelevel.GameLevel;
import com.xdpm.angrybirds.gamelevel.object.ObjectContactListener;
import com.xdpm.angrybirds.scene.MainGameScene;
import com.xdpm.angrybirds.scene.layer.GameLevelClearedLayer;
import com.xdpm.angrybirds.scene.layer.GameLevelFailedLayer;
import com.xdpm.angrybirds.scene.layer.GameLevelPauseLayer;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class GameLevelManager extends AbstractGameLevelManager {
	
	// =================================================================
	// Properties
	// =================================================================
	
	private static GameLevelManager mGameLevelManager;
	
	private MainGameScene mMainGameScene;
	private PhysicsWorld mPhysicsWorld;
	private ObjectContactListener mObjectContactListener;
	
	private GameLevel mCurrentGameLevel;
	
	// =================================================================
	// Constructors
	// =================================================================
	
	private GameLevelManager() {
	}
	
	// =================================================================
	// Getters & Setters
	// =================================================================
	
	public static GameLevelManager getInstance() {
		if (null == mGameLevelManager) {
			mGameLevelManager = new GameLevelManager();
		}
		return mGameLevelManager;
	}
	
	public PhysicsWorld getPhysicsWorld() {
		return this.mPhysicsWorld;
	}
	
	public MainGameScene getMainGameScene() {
		return this.mMainGameScene;
	}
	
	public GameLevel getCurrentGameLevel() {
		return this.mCurrentGameLevel;
	}
	
	// =================================================================
	// Methods from/for SuperClass/Interface
	// =================================================================
	
	public void setMainGameScene(MainGameScene pMainGameScene) {
		this.mMainGameScene = pMainGameScene;
	}
	
	public void showNextGameLevel() {
		if (this.mCurrentGameLevel.isHasNext()) {
			this.destroyGameLevel();
			
			final IUpdateHandler update = new IUpdateHandler() {

				@Override
				public void onUpdate(float pElapsedTime) {
					if (mIsLoaded == false) {
						mCurrentGameLevel = mCurrentGameLevel.getNextLevel();
						createGameLevel(mCurrentGameLevel.getWorldId(), mCurrentGameLevel.getStageId(), mCurrentGameLevel.getLevelId());
						ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(this);
					}
				}

				@Override
				public void reset() {
				}
				
			};
			
			ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(update);
		}
	}
	
	public void replayCurrentGameLevel() {
		this.destroyGameLevel();
		
		final IUpdateHandler update = new IUpdateHandler() {

			@Override
			public void onUpdate(float pElapsedTime) {
				if (mIsLoaded == false) {
					createGameLevel(mCurrentGameLevel.getWorldId(), mCurrentGameLevel.getStageId(), mCurrentGameLevel.getLevelId());
					ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(this);
				}
			}

			@Override
			public void reset() {
			}
			
		};
		
		ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(update);
	}
	
	public void pause() {
		this.mMainGameScene.setIgnoreUpdate(true);
		this.mMainGameScene.setOnAreaTouchListener(null);
		this.mMainGameScene.setOnSceneTouchListener(null);
		this.mMainGameScene.disableButtonPause();
		ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(this.mPhysicsWorld);
	}
	
	public void resume() {
		this.mMainGameScene.setIgnoreUpdate(false);
		this.mMainGameScene.setOnAreaTouchListener(this.mMainGameScene);
		this.mMainGameScene.setOnSceneTouchListener(this.mMainGameScene);
		this.mMainGameScene.enableButtonPause();
		ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(this.mPhysicsWorld);
	}
	
	public void onLevelCleared() {
		this.mMainGameScene.registerUpdateHandler(new TimerHandler(1f * this.mCurrentGameLevel.getRemainBirds() + 2, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mMainGameScene.unregisterUpdateHandler(pTimerHandler);
				mMainGameScene.showGameLevelClearedLayer();
			}
		}));
	}
	
	public void onLevelFailed() {
		this.mMainGameScene.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mMainGameScene.unregisterUpdateHandler(pTimerHandler);
				mMainGameScene.showGameLevelFailedLayer();
			}
		}));
	}

	@Override
	public void onLoadGameLevel(int pWorldId, int pStageId, int pLevelId, OnLoadGameLevelCallBack pOnLoadGameLevelCallBack)
			throws Exception {
		
		if (null == this.mPhysicsWorld) {
			this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
			this.mPhysicsWorld.setAutoClearForces(true);
		}
		
		if (null == this.mObjectContactListener) {
			this.mObjectContactListener = new ObjectContactListener();
		}

		this.mCurrentGameLevel = new GameLevel(pWorldId, pStageId, pLevelId);
		this.mCurrentGameLevel.onLoadGameLevel();
		
		pOnLoadGameLevelCallBack.onLoadGameLevelFinish();
	}

	@Override
	public void onShowGameLevel(OnShowGameLevelCallBack pOnShowGameLevelCallBack)
			throws Exception {
		
		this.mCurrentGameLevel.onShowGameLevel();
		
		//ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(this);
		ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(this.mPhysicsWorld);
		this.mPhysicsWorld.setContactListener(this.mObjectContactListener);
		
		pOnShowGameLevelCallBack.onShowGameLevelFinish();
	}

	@Override
	public void onHideGameLevel(OnHideGameLevelCallBack pOnHideGameLevelCallBack)
			throws Exception {

		this.mCurrentGameLevel.onHideGameLevel();
		
		pOnHideGameLevelCallBack.onHideGameLevelFinish();
	}

	@Override
	public void onUnLoadGameLevel(OnUnLoadGameLevelCallBack pOnUnLoadGameLevelCallBack)
			throws Exception {
		
		this.mCurrentGameLevel.onUnLoadGameLevel();
		
		//ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(this);
		
		while (this.mPhysicsWorld.getBodies().hasNext()) {
			this.mPhysicsWorld.destroyBody(this.mPhysicsWorld.getBodies().next());
		}
		this.mPhysicsWorld.clearPhysicsConnectors();
		this.mPhysicsWorld.setContactListener(null);
		this.mObjectContactListener = null;
		ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(this.mPhysicsWorld);
		this.mPhysicsWorld = null;
		
		this.mMainGameScene.clearTouchAreas();
		this.mMainGameScene.clearUpdateHandlers();
		pOnUnLoadGameLevelCallBack.onUnLoadGameLevelFinish();
	}	
}
