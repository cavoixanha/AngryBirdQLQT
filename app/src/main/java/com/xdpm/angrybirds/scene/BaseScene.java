package com.xdpm.angrybirds.scene;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.debug.Debug;

import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.object.BaseObject;
import com.xdpm.angrybirds.scene.background.Background;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public abstract class BaseScene extends Scene implements ISceneInterface, IOnAreaTouchListener {
	//==================================================
	// Properties
	//==================================================
	
	protected Background mBackground;
	
	private boolean mIsLoaded = false;
	private boolean mIsShown = false;
	
	private boolean mIsUnLoadOnHidden;
	
	//==================================================
	// Constructors
	//==================================================
	public BaseScene() {
		this(false);
	}
	
	public BaseScene(boolean pIsUnLoadOnHidden) {
		this.mIsUnLoadOnHidden = pIsUnLoadOnHidden;
		this.setBackgroundEnabled(false);
		this.setOnAreaTouchListener(this);
	}
	
	//==================================================
	// Getters and Setters
	//==================================================
	
	public boolean isLoaded() {
		return this.mIsLoaded;
	}
	
	public boolean isShown() {
		return this.mIsShown;
	}

	public boolean isUnLoadOnHidden() {
		return this.mIsUnLoadOnHidden;
	}
	
	//==================================================
	// Methods from/for SuperClass/Interface
	//==================================================

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pITouchArea, float pTouchLocalX,
			float pTouchLocalY) {
		BaseObject object = (BaseObject) ((IEntity) pITouchArea).getUserData();
		if (null != object) {
			object.onAreaTouched(pSceneTouchEvent);
			return true;
		}
		return false;
	}
	
	//==================================================
	// Methods
	//==================================================
	
	public void showScene() {
		
		final OnShowSceneCallBack onShowSceneCallBack = new OnShowSceneCallBack() {
			
			@Override
			public void onShowSceneFinish() {
				mIsShown = true;
			}
		};
		
		final OnLoadSceneCallBack onLoadSceneCallBack = new OnLoadSceneCallBack() {
			
			@Override
			public void onLoadSceneFinish() {
				try {
					mIsLoaded = true;
					BaseScene.this.onShowScene(onShowSceneCallBack);
				} catch (final Throwable pThrowable) {
					Debug.e("onShowScene failed", pThrowable);
				}
			}
		};
		
		try {
			if (!this.mIsLoaded) {
				BaseScene.this.onLoadScene(onLoadSceneCallBack);
			} else {
				onLoadSceneCallBack.onLoadSceneFinish();
			}
		} catch (final Throwable pThrowable) {
			Debug.e("onLoadScene failed", pThrowable);
		}
	}
	
	public void hideScene() {	
		
		final OnUnLoadSceneCallBack onUnLoadSceneCallBack = new OnUnLoadSceneCallBack() {
			
			@Override
			public void onUnLoadSceneFinish() {
				mIsLoaded = false;
			}
		};
		
		final OnHideSceneCallBack onHideSceneCallBack = new OnHideSceneCallBack() {
			@Override
			public void onHideSceneFinish() {
				mIsShown = false;
				
				if (mIsUnLoadOnHidden) {
					ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
						
						@Override
						public void run() {
							try {
								BaseScene.this.onUnLoadScene(onUnLoadSceneCallBack);	
							} catch (final Throwable pThrowable) {
								Debug.e("onUnLoadScene failed", pThrowable);
							}	
						}
					});
				}
			}
		};
		
		try {
			BaseScene.this.onHideScene(onHideSceneCallBack);
		} catch (final Throwable pThrowable) {
			Debug.e("onHideScene failed", pThrowable);
		}
	}
}
