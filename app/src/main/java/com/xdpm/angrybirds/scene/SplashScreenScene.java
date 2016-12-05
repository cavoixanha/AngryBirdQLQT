package com.xdpm.angrybirds.scene;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.common.SplashConstants;
import com.xdpm.angrybirds.manager.ResourceManager;

public class SplashScreenScene extends BaseScene {
	
	//==================================================
	// Properties
	//==================================================
	private BitmapTextureAtlas mSplashTextureAtlas;
	private TextureRegion mSplashScreenBackgroundTextureRegion;
	private Sprite mSplashScreenBackgroundSprite;
	
	private TextureRegion mSplashScreenLoadingTextTextureRegion;
	private Sprite mSplashScreenLoadingTextSprite;
	
	//==================================================
	// Properties
	//==================================================
	
	public SplashScreenScene() {
		super(true);
	}

	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack)
			throws Exception {
		
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		TexturePackTextureRegionLibrary splashTexturePackTextureRegionLibrary = ResourceManager.getInstance().getSplashSpriteSheetTexturePackTextureRegionLibrary();
		this.mSplashScreenBackgroundSprite = new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
				splashTexturePackTextureRegionLibrary.get(SplashConstants.SPLASH_ID),
				vertexBufferObjectManager);
		this.mSplashScreenLoadingTextTextureRegion = splashTexturePackTextureRegionLibrary.get(SplashConstants.LOADING_SPLASH_ID);
		this.mSplashScreenLoadingTextSprite = new Sprite(Constants.CAMERA_WIDTH - mSplashScreenLoadingTextTextureRegion.getWidth(), 
				Constants.CAMERA_HEIGHT - mSplashScreenLoadingTextTextureRegion.getHeight(), this.mSplashScreenLoadingTextTextureRegion,
				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		
		pOnLoadSceneCallBack.onLoadSceneFinish();
	}

	@Override
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {
		// TODO Auto-generated method stub
		ResourceManager.getInstance().getCamera().setSelectorCamera();
		
		this.attachChild(this.mSplashScreenBackgroundSprite);
		this.attachChild(this.mSplashScreenLoadingTextSprite);
		
		pOnShowSceneCallBack.onShowSceneFinish();
	}

	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		// TODO Auto-generated method stub
		//this.detachChildren();
		//this.clearTouchAreas();
		
		pOnHideSceneCallBack.onHideSceneFinish();
	}

	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack)
			throws Exception {
		// TODO Auto-generated method stub
		this.detachChildren();
		this.clearChildScene();
		this.clearUpdateHandlers();
		this.clearEntityModifiers();
		this.clearTouchAreas();
		
		pOnUnLoadSceneCallBack.onUnLoadSceneFinish();
	}
}