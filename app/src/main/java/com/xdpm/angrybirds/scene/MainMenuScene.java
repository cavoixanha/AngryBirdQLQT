package com.xdpm.angrybirds.scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.gamelevel.object.bird.MenuJumpBirdPool;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SceneManager;
import com.xdpm.angrybirds.object.button.BaseButton;
import com.xdpm.angrybirds.object.button.FanPageButton;
import com.xdpm.angrybirds.object.button.SettingButton;
import com.xdpm.angrybirds.scene.background.Background;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class MainMenuScene extends BaseScene {
	
	//==================================================
	// Properties
	//==================================================
	
	private SettingButton mButtonSetting;
	private FanPageButton mButtonFanpage;
	private BaseButton<Sprite> mButtonPlay;
	
	//==================================================
	// Constructors
	//==================================================
	
	public MainMenuScene() {
		super(true);
		MenuJumpBirdPool.setMainMenuScene(this);
		this.mBackground = new Background(this);
	}
	
	//==================================================
	// Methods
	//==================================================

	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack)
			throws Exception {		
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		
		// Repair for background
		this.mBackground.loadBackground();
		
		// Repair for jump bird
		MenuJumpBirdPool.onLoadPool();
		
		// Buttons
		TexturePackTextureRegionLibrary buttonTexturePackTextureRegionLibrary = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary();
		
		TextureRegion buttonBackgroundTextureRegion = buttonTexturePackTextureRegionLibrary.get(ButtonConstants.MENU_BACKGROUND_BUTTON_ID);
		this.mButtonSetting = new SettingButton(0, Constants.CAMERA_HEIGHT - buttonBackgroundTextureRegion.getHeight(), 1);
		this.mButtonFanpage = new FanPageButton(Constants.CAMERA_WIDTH - buttonBackgroundTextureRegion.getWidth(), Constants.CAMERA_HEIGHT - buttonBackgroundTextureRegion.getHeight(), 1);
		
		TextureRegion buttonPlayTextureRegion = buttonTexturePackTextureRegionLibrary.get(ButtonConstants.BUTTON_PLAY_IN_MENU_ID);
		Sprite buttonPlaySprite = new Sprite((Constants.CAMERA_WIDTH - buttonBackgroundTextureRegion.getWidth()) * 0.5f, 
				(Constants.CAMERA_HEIGHT - buttonBackgroundTextureRegion.getHeight()) * 0.5f, 
				buttonPlayTextureRegion, vertexBufferObjectManager);
		this.mButtonPlay = new BaseButton<Sprite>(buttonPlaySprite.getX(), buttonPlaySprite.getY(), 1, buttonPlaySprite) {
			
			@Override
			public void onButtonClick() {
				SceneManager.getInstance().showWorldSelectorScene();
			}
		};
		
		HUD hud = ResourceManager.getInstance().getHUD();
		hud.setOnAreaTouchListener(this);

		pOnLoadSceneCallBack.onLoadSceneFinish();
	}

	@Override
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {		
		// Repair camera
		ResourceManager.getInstance().getCamera().setMenuCamera();
		
		this.mBackground.showBackground();
		this.mBackground.enableText(true);
		this.mBackground.isMovingBackground(true);
		
		MenuJumpBirdPool.onShowPool();
		
		HUD hud = ResourceManager.getInstance().getHUD();
		hud.attachChild(this.mButtonFanpage.getEntity());
		hud.attachChild(this.mButtonSetting.getEntity());
		hud.attachChild(this.mButtonPlay.getEntity());
		hud.registerTouchArea(this.mButtonPlay.getEntity());	
		
		pOnShowSceneCallBack.onShowSceneFinish();
	}

	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		
		pOnHideSceneCallBack.onHideSceneFinish();
	}

	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack)
			throws Exception {
		
		MenuJumpBirdPool.onUnLoadPool();
		this.mBackground.unloadBackground();
		this.mBackground = null;
		
		this.detachChildren();
		this.clearChildScene();
		this.clearUpdateHandlers();
		this.clearEntityModifiers();
		this.clearTouchAreas();
		
		ResourceManager.getInstance().getHUD().detachChildren();
		ResourceManager.getInstance().getHUD().clearTouchAreas();
		
		pOnUnLoadSceneCallBack.onUnLoadSceneFinish();
	}
}
