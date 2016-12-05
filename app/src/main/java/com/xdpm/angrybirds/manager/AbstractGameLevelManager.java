package com.xdpm.angrybirds.manager;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.util.debug.Debug;

public abstract class AbstractGameLevelManager {
	
	protected boolean mIsLoaded = false;
	
	public abstract void onLoadGameLevel(int pWorldId, int pStageId, int pLevelId, OnLoadGameLevelCallBack pOnLoadGameLevelCallBack) throws Exception;
	public abstract void onShowGameLevel(OnShowGameLevelCallBack pOnShowGameLevelCallBack) throws Exception;
	public abstract void onHideGameLevel(OnHideGameLevelCallBack pOnHideGameLevelCallBack) throws Exception;
	public abstract void onUnLoadGameLevel(OnUnLoadGameLevelCallBack pOnUnLoadGameLevelCallBack) throws Exception;
	
	public static interface OnLoadGameLevelCallBack {
		public void onLoadGameLevelFinish();
	}
	
	public static interface OnShowGameLevelCallBack {
		public void onShowGameLevelFinish();
	}
	
	public static interface OnHideGameLevelCallBack {
		public void onHideGameLevelFinish();
	}
	
	public static interface OnUnLoadGameLevelCallBack {
		public void onUnLoadGameLevelFinish();
	}
	
	public void createGameLevel(int pWorldId, int pStageId, int pLevelId) {
		final OnShowGameLevelCallBack onShowGameLevelCallBack = new OnShowGameLevelCallBack() {
			
			@Override
			public void onShowGameLevelFinish() {
				Debug.e("showGameFinish");
			}
		};
		
		final OnLoadGameLevelCallBack onLoadGameLevelCallBack = new OnLoadGameLevelCallBack() {
			
			@Override
			public void onLoadGameLevelFinish() {
				Debug.e("loadGameFinish");
				
				ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(new TimerHandler(2, new ITimerCallback() {
					
					@Override
					public void onTimePassed(TimerHandler pTimerHandlers) {
						SceneManager.getInstance().hideLoadingScreenLayer();
						try {	
							mIsLoaded = true;
							AbstractGameLevelManager.this.onShowGameLevel(onShowGameLevelCallBack);
						} catch (final Throwable pThrowable) {
							Debug.e("onShowGameLevel failed", pThrowable);
						}
						ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(pTimerHandlers);
					}
				}));
				
			}
		};
		
		try {
			SceneManager.getInstance().showLoadingScreenLayer();
			AbstractGameLevelManager.this.onLoadGameLevel(pWorldId, pStageId, pLevelId, onLoadGameLevelCallBack);
		} catch (final Throwable pThrowable) {
			Debug.e("onLoadGameLevel failed", pThrowable);
		}
	}
	
	public void destroyGameLevel() {
		final OnUnLoadGameLevelCallBack onUnLoadGameLevelCallBack = new OnUnLoadGameLevelCallBack() {
			
			@Override
			public void onUnLoadGameLevelFinish() {
				Debug.e("unloadGameFinish");
				mIsLoaded = false;
			}
		};
		
		final OnHideGameLevelCallBack onHideGameLevelCallBack = new OnHideGameLevelCallBack() {
			
			@Override
			public void onHideGameLevelFinish() {
				Debug.e("hideGameFinish");
				ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						try {		
							AbstractGameLevelManager.this.onUnLoadGameLevel(onUnLoadGameLevelCallBack);
						} catch (final Throwable pThrowable) {
							Debug.e("onUnLoadGameLevel failed", pThrowable);
						}
					}
				});
			}
		};
		
		try {
			AbstractGameLevelManager.this.onHideGameLevel(onHideGameLevelCallBack);
		} catch (final Throwable pThrowable) {
			Debug.e("onHideGameLevel failed", pThrowable);
		}
	}
}
