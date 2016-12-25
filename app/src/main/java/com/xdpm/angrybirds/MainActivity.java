package com.xdpm.angrybirds;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;


import android.view.KeyEvent;

import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SceneManager;
import com.xdpm.angrybirds.setting.Settings;

/**
 * Main game activity
 * @author ChuongNK
 *
 */
public class MainActivity extends BaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================
	
	private AngryBirdSmoothCamera mCamera;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
		
		
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new AngryBirdSmoothCamera();
		EngineOptions engineOption = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
								new FillResolutionPolicy(), this.mCamera);
		engineOption.getAudioOptions().setNeedsMusic(true);
		engineOption.getAudioOptions().setNeedsSound(true);
		return engineOption;
	}
	@Override
	public void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		ResourceManager.getInstance().setBaseGameActivity(this);
		ResourceManager.getInstance().setCamera(this.mCamera);
		ResourceManager.getInstance().loadSplashSceneResource();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		
		SceneManager.getInstance().showSplashScreenScene();
		pOnCreateSceneCallback.onCreateSceneFinished(null);
	}
	
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback()
		{
	          public void onTimePassed(final TimerHandler pTimerHandler){
	        	  mEngine.unregisterUpdateHandler(pTimerHandler);
	              ResourceManager.getInstance().loadTitleMenuResource();
		          ResourceManager.getInstance().loadGameResources();
		          SceneManager.getInstance().showMainMenuScene();
              }
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
			SceneManager.getInstance().showPreviousScene();
			return true;
		}
		return false;
	}
}
