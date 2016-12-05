package com.xdpm.angrybirds.scene.layer;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.xdpm.angrybirds.common.SplashConstants;
import com.xdpm.angrybirds.manager.ResourceManager;

public class LoadingLayer extends BaseLayer {

	// =====================================================
	// Properties
	// =====================================================

	private Sprite mLoadingTextSprite;
	private static LoadingLayer mLoadingLayer;
	
	// =====================================================
	// Constructors 
	// =====================================================
	
	private LoadingLayer() {
		super(false);
	}
	
	// =====================================================
	// Getters & Setters
	// =====================================================
	
	public static LoadingLayer getInstance() {
		if (null == mLoadingLayer) {
			mLoadingLayer = new LoadingLayer();
		}
		return mLoadingLayer;
	}
	// =====================================================
	// Methods from/for SuperClass/Interface
	// =====================================================
	
	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack) {
		super.onLoadScene(pOnLoadSceneCallBack);
		
		TextureRegion loadingTextTextureRegion = ResourceManager.getInstance().getSplashSpriteSheetTexturePackTextureRegionLibrary().get(SplashConstants.LOADING_SPLASH_ID);
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		
		this.mLoadingTextSprite = new Sprite((this.mMashRectangle.getWidth() - loadingTextTextureRegion.getWidth()) * 0.5f, 
				(this.mMashRectangle.getHeight() - loadingTextTextureRegion.getHeight()) * 0.5f, 
				loadingTextTextureRegion, vertexBufferObjectManager);
		
		pOnLoadSceneCallBack.onLoadSceneFinish();
	}

	@Override
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {
		this.mMashRectangle.attachChild(this.mLoadingTextSprite);
		ResourceManager.getInstance().getHUD().attachChild(this.mMashRectangle);
		
		pOnShowSceneCallBack.onShowSceneFinish();
	}

	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				mMashRectangle.detachChildren();
				mMashRectangle.detachSelf();
			}
		});
		
		pOnHideSceneCallBack.onHideSceneFinish();
	}
}
