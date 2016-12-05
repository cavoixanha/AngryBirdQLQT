package com.xdpm.angrybirds.scene.layer;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.common.MenuConstants;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SceneManager;
import com.xdpm.angrybirds.object.button.BaseButton;
import com.xdpm.angrybirds.scene.MainGameScene;

public class GameLevelFailedLayer extends BaseLayer {
	
	// =============================================
	// Properties
	// =============================================
	
	private Rectangle mPanel;
	
	private Text mLevelFailedText;
	
	private Sprite mPig;
	
	private BaseButton<Sprite> mMenuButton;
	private BaseButton<Sprite> mRePlayButton;
	private BaseButton<Sprite> mNextLevelButton;
	
	// =============================================
	// Constructors
	// =============================================
	public GameLevelFailedLayer() {
		super(true);
	}
	
	// =====================================================
	// Getters & Setters
	// =====================================================
	
	
	// =============================================
	// Methods from/for SuperClass/Interface
	// =============================================
	
	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack) {
		super.onLoadScene(pOnLoadSceneCallBack);
		
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		TexturePackTextureRegionLibrary buttonTextureRegionLibrary = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary();
		TexturePackTextureRegionLibrary menuTextureRegionLibrary = ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary();
		
		// Panel
		this.mPanel = new Rectangle((Constants.CAMERA_WIDTH - Constants.MENU_RESULT_WIDTH) * 0.5f, 0, 
				Constants.MENU_RESULT_WIDTH, Constants.CAMERA_HEIGHT, vertexBufferObjectManager);
		this.mPanel.setColor(Color.BLACK);
		
		// Level Cleared Text
		this.mLevelFailedText = new Text(this.mPanel.getWidth() * 0.5f, Constants.MENU_RESULT_MARGIN_TOP, 
				ResourceManager.getInstance().getAngrybirdFont48(), "LEVEL FAILED!", 
				new TextOptions(HorizontalAlign.CENTER),
				vertexBufferObjectManager);
		this.mLevelFailedText.setX((this.mPanel.getWidth() - this.mLevelFailedText.getWidth()) * 0.5f);
		
		// Pig
		ITextureRegion pigTextureRegion = menuTextureRegionLibrary.get(MenuConstants.MENU_RESULT_PIG_ID);
		this.mPig = new Sprite((this.mPanel.getWidth() - pigTextureRegion.getWidth()) * 0.5f, 
				(this.mPanel.getHeight() - pigTextureRegion.getHeight()) * 0.5f, 
				pigTextureRegion, vertexBufferObjectManager);
		
		// Buttons
		TextureRegion replayButtonTextureRegion = buttonTextureRegionLibrary.get(ButtonConstants.BUTTON_REPLAY_ID);
		Sprite replayButtonSprite = new Sprite((this.mPanel.getWidth() - replayButtonTextureRegion.getWidth()) * 0.5f, 
				Constants.CAMERA_HEIGHT - Constants.MENU_RESULT_MARGIN_BOTTOM - replayButtonTextureRegion.getHeight(), 
				replayButtonTextureRegion, vertexBufferObjectManager);
		this.mRePlayButton = new BaseButton<Sprite>(replayButtonSprite.getX(), replayButtonSprite.getY(), 1.0f, replayButtonSprite) {
			
			@Override
			public void onButtonClick() {
				hideScene();
				GameLevelManager.getInstance().replayCurrentGameLevel();
			}
		};
		
		TextureRegion menuButtonTextureRegion = buttonTextureRegionLibrary.get(ButtonConstants.BUTTON_MENU_ID);
		Sprite menuButtonSprite = new Sprite(this.mRePlayButton.getEntity().getX() - Constants.MENU_RESULT_ELEMENT_MARGIN - menuButtonTextureRegion.getWidth(), 
				this.mRePlayButton.getEntity().getY(), 
				menuButtonTextureRegion, vertexBufferObjectManager);
		this.mMenuButton = new BaseButton<Sprite>(menuButtonSprite.getX(), menuButtonSprite.getY(), 1.0f, menuButtonSprite) {
			
			@Override
			public void onButtonClick() {
				GameLevelFailedLayer.this.hideScene();
				SceneManager.getInstance().showLevelSelectorScene();
			}
		};
		
		TextureRegion nextLevelButtonTextureRegion = buttonTextureRegionLibrary.get(ButtonConstants.BUTTON_NEXT_LEVEL_ID);
		Sprite nextLevelButtonSprite = new Sprite(this.mRePlayButton.getEntity().getX() + this.mRePlayButton.getEntity().getWidth() +  Constants.MENU_RESULT_ELEMENT_MARGIN, 
				this.mRePlayButton.getEntity().getY(), 
				nextLevelButtonTextureRegion, vertexBufferObjectManager);
		this.mNextLevelButton = new BaseButton<Sprite>(nextLevelButtonSprite.getX(), nextLevelButtonSprite.getY(), 1.0f, nextLevelButtonSprite) {
			
			@Override
			public void onButtonClick() {
				GameLevelFailedLayer.this.hideScene();
				GameLevelManager.getInstance().showNextGameLevel();
			}
		};
		
		HUD hud = ResourceManager.getInstance().getHUD();
		hud.setOnAreaTouchListener(this);
		
		hud.registerTouchArea(this.mMenuButton.getEntity());
		hud.registerTouchArea(this.mRePlayButton.getEntity());
		hud.registerTouchArea(this.mNextLevelButton.getEntity());
		
		pOnLoadSceneCallBack.onLoadSceneFinish();
	}


	@Override
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {
		
		GameLevelManager.getInstance().pause();
		
		this.mPanel.attachChild(this.mLevelFailedText);
		this.mPanel.attachChild(this.mPig);
		this.mPanel.attachChild(this.mMenuButton.getEntity());
		this.mPanel.attachChild(this.mRePlayButton.getEntity());
		this.mPanel.attachChild(this.mNextLevelButton.getEntity());
		
		this.mMashRectangle.attachChild(this.mPanel);	
		ResourceManager.getInstance().getHUD().attachChild(this.mMashRectangle);
		
		pOnShowSceneCallBack.onShowSceneFinish();
	}


	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		
		GameLevelManager.getInstance().resume();
		
		pOnHideSceneCallBack.onHideSceneFinish();
	}

	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack) {
/*		for (int i = 0; i < this.mPanel.getChildCount(); i++) {
			this.mPanel.getChildByIndex(i).detachSelf();
			this.mPanel.getChildByIndex(i).dispose();
		}*/
		this.mPanel.detachChildren();
		this.mPanel.detachSelf();
		this.mPanel.dispose();
		
		HUD hud = ResourceManager.getInstance().getHUD();
		
		hud.unregisterTouchArea(this.mMenuButton.getEntity());
		hud.unregisterTouchArea(this.mRePlayButton.getEntity());
		hud.unregisterTouchArea(this.mNextLevelButton.getEntity());
		
		super.onUnLoadScene(pOnUnLoadSceneCallBack);
	}
	
}
