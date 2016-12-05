package com.xdpm.angrybirds.manager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.ui.activity.BaseGameActivity;

import com.xdpm.angrybirds.scene.BaseScene;
import com.xdpm.angrybirds.scene.LevelSelectorScene;
//import com.xdpm.angrybirds.scene.WorldSelectorScene;
import com.xdpm.angrybirds.scene.MainGameScene;
import com.xdpm.angrybirds.scene.MainMenuScene;
import com.xdpm.angrybirds.scene.SplashScreenScene;
import com.xdpm.angrybirds.scene.WorldSelectorScene;
import com.xdpm.angrybirds.scene.layer.GameLevelClearedLayer;
import com.xdpm.angrybirds.scene.layer.GameLevelFailedLayer;
import com.xdpm.angrybirds.scene.layer.GameLevelPauseLayer;
//import com.xdpm.angrybirds.scene.SplashScreenScene;
//import com.xdpm.angrybirds.scene.LevelSelectorScene;
import com.xdpm.angrybirds.scene.layer.LoadingLayer;

public class SceneManager {

	//==================================================
	// Properties
	//==================================================
	
	public enum Scenes {
		SPLASH_SCREEN,
		MAIN_MENU,
		WORLD_SELECTOR,
		LEVEL_SELECTOR,
		MAIN_GAME
	}
	
	private BaseScene mCurrentScene;
	private Scenes mScene;
	
	private LoadingLayer mLoadingLayer = LoadingLayer.getInstance();
	
	private WorldSelectorScene mLevelSelectorScene;
	private LevelSelectorScene mStageSelectorScene;
	private static SceneManager mSceneManager = null;
	
	//==================================================
	// Constructors
	//==================================================
	private SceneManager() {
	}
	
	public static SceneManager getInstance() {
		if (null == mSceneManager) {
			mSceneManager = new SceneManager();
		}
		return mSceneManager;
	}
	
	//==================================================
	// Methods
	//==================================================
	
	public void showSplashScreenScene() {
		this.mScene = Scenes.SPLASH_SCREEN;
		ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {			
			@Override
			public void run() {								
				mScene = Scenes.SPLASH_SCREEN;
				SceneManager.getInstance().showScene(new SplashScreenScene());
			}
		});
	}
	
	public void showWorldSelectorScene(){
		this.mScene = Scenes.WORLD_SELECTOR;
		if (null == this.mLevelSelectorScene) {
			this.mLevelSelectorScene = new WorldSelectorScene();
		}
		this.showScene(this.mLevelSelectorScene);
	}
	
	public void showLevelSelectorScene(){
		this.mScene = Scenes.LEVEL_SELECTOR;
		if (null == this.mStageSelectorScene) {
			this.mStageSelectorScene = new LevelSelectorScene();
		}
		this.showScene(this.mStageSelectorScene);
	}
	
	public void showMainMenuScene() {
		this.mScene = Scenes.MAIN_MENU;
		this.showScene(new MainMenuScene());
	}
	
	public void showMainGameScene() {
		this.mScene = Scenes.MAIN_GAME;
		this.showScene(new MainGameScene());
	}
	
	public void showLoadingScreenLayer() {
		this.mLoadingLayer.showScene();
	}
	
	public void hideLoadingScreenLayer() {
		this.mLoadingLayer.hideScene();
	}
	
	public void showPreviousScene() {
		if (Scenes.SPLASH_SCREEN == this.mScene || Scenes.MAIN_MENU == this.mScene) {
			BaseGameActivity activity = ResourceManager.getInstance().getBaseGameActivity();
			activity.getEngine().runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					mCurrentScene.hideScene();
					ResourceManager.getInstance().getBaseGameActivity().finish();
					//walk around, not a good solution but make it work first
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
		} else if (Scenes.MAIN_GAME == this.mScene) {
			this.showLevelSelectorScene();
		} else if(Scenes.LEVEL_SELECTOR == this.mScene){
			this.showWorldSelectorScene();
		} else if (Scenes.WORLD_SELECTOR == this.mScene) {
			this.showMainMenuScene();
		}
	}
	
	private void showScene(final BaseScene pScene) {
		if (null != this.mCurrentScene) {
			this.mCurrentScene.hideScene();
			
			IUpdateHandler update = new IUpdateHandler() {

				@Override
				public void onUpdate(float pElapsedTime) {
					if (mCurrentScene.isUnLoadOnHidden()) {
						if (mCurrentScene.isLoaded() == false) {
							mCurrentScene = pScene;
							mCurrentScene.showScene();
							ResourceManager.getInstance().getBaseGameActivity().getEngine().setScene(mCurrentScene);
							ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(this);
						}
					} else {
						if (mCurrentScene.isShown() == false) {
							mCurrentScene = pScene;
							mCurrentScene.showScene();
							ResourceManager.getInstance().getBaseGameActivity().getEngine().setScene(mCurrentScene);
							ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(this);
						}
					}
				}

				@Override
				public void reset() {
					
				}
				
			};
			
			ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(update);
		} else {
			this.mCurrentScene = pScene;
			this.mCurrentScene.showScene();
			ResourceManager.getInstance().getBaseGameActivity().getEngine().setScene(pScene);
		}
				
		switch (mScene) {
		case MAIN_MENU:
		case WORLD_SELECTOR:
		case LEVEL_SELECTOR:
			SfxManager.getInstance().playMusic();
			break;
		case MAIN_GAME:
			SfxManager.getInstance().pauseMusic();
			break;

		default:
			break;
		}
	}
}
