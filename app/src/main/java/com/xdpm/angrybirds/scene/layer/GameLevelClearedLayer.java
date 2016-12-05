package com.xdpm.angrybirds.scene.layer;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.common.MenuConstants;
import com.xdpm.angrybirds.common.ScoreConstants;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.GameScoreManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SceneManager;
import com.xdpm.angrybirds.manager.SfxManager;
import com.xdpm.angrybirds.object.button.BaseButton;
import com.xdpm.angrybirds.scene.BaseScene;
import com.xdpm.angrybirds.scene.MainGameScene;

public class GameLevelClearedLayer extends BaseLayer {

	
	// =============================================
	// Properties
	// =============================================
	
	private Rectangle mPanel;
	
	private Text mLevelClearedText;
	private Text mScoreLabel;
	private Text mScoreText;
	
	private int tempScore = 10;
	
	private TiledSprite mLeftStarMenu;
	private TiledSprite mCenterStarMenu;
	private TiledSprite mRighStarMenu;
	
	private BaseButton<Sprite> mMenuButton;
	private BaseButton<Sprite> mRePlayButton;
	private BaseButton<Sprite> mNextLevelButton;
	
	// =============================================
	// Constructors
	// =============================================
	
	public GameLevelClearedLayer() {
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
		//this.setOnAreaTouchListener(this);
		super.onLoadScene(pOnLoadSceneCallBack);
		
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		TexturePackTextureRegionLibrary menuTexturePackTextureRegionLibrary = ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary();
		
		// Panel
		this.mPanel = new Rectangle((Constants.CAMERA_WIDTH - Constants.MENU_RESULT_WIDTH) * 0.5f, 0, 
				Constants.MENU_RESULT_WIDTH, Constants.CAMERA_HEIGHT, vertexBufferObjectManager);
		this.mPanel.setColor(Color.BLACK);
		
		// Level Cleared Text
		this.mLevelClearedText = new Text(this.mPanel.getWidth() * 0.5f, Constants.MENU_RESULT_MARGIN_TOP, 
				ResourceManager.getInstance().getAngrybirdFont48(), "LEVEL CLEARED!", 
				new TextOptions(HorizontalAlign.CENTER),
				vertexBufferObjectManager);
		this.mLevelClearedText.setX((this.mPanel.getWidth() - this.mLevelClearedText.getWidth()) * 0.5f);
		
		// ScoreText
		this.mScoreText = new Text(this.mPanel.getWidth() * 0.5f, this.mPanel.getHeight() * 0.6f, 
				ResourceManager.getInstance().getAngrybirdFont48(), "0", 
				9,
				vertexBufferObjectManager);
		this.mScoreText.setX((this.mPanel.getWidth() - this.mScoreText.getWidth()) * 0.5f);
		

		// Stars
		ITiledTextureRegion centerStarTextureRegion = menuTexturePackTextureRegionLibrary.get(MenuConstants.CENTER_STAR_ID, 2, 1);
		this.mCenterStarMenu = new TiledSprite((this.mPanel.getWidth() - centerStarTextureRegion.getWidth()) * 0.5f, 
				this.mLevelClearedText.getY() + this.mLevelClearedText.getHeight() + Constants.MENU_RESULT_LINE_HEIGHT, 
				centerStarTextureRegion, vertexBufferObjectManager);
		this.mCenterStarMenu.setCurrentTileIndex(1);
		
		ITiledTextureRegion leftStarTextureRegion = menuTexturePackTextureRegionLibrary.get(MenuConstants.LEFT_STAR_ID, 2, 1);
		this.mLeftStarMenu = new TiledSprite(this.mCenterStarMenu.getX() - Constants.MENU_RESULT_ELEMENT_MARGIN - leftStarTextureRegion.getWidth(), 
				this.mCenterStarMenu.getY(),
				leftStarTextureRegion, vertexBufferObjectManager);
		this.mLeftStarMenu.setCurrentTileIndex(1);
		
		ITiledTextureRegion rightStarTextureRegion = menuTexturePackTextureRegionLibrary.get(MenuConstants.RIGHT_STAR_ID, 2, 1);
		this.mRighStarMenu = new TiledSprite(this.mCenterStarMenu.getX() + this.mCenterStarMenu.getWidth() + Constants.MENU_RESULT_ELEMENT_MARGIN, 
				this.mCenterStarMenu.getY(), 
				rightStarTextureRegion, vertexBufferObjectManager);
		
		// Buttons
		TextureRegion replayButtonTextureRegion = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary().get(ButtonConstants.BUTTON_REPLAY_ID);
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
		
		TextureRegion menuButtonTextureRegion = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary().get(ButtonConstants.BUTTON_MENU_ID);
		Sprite menuButtonSprite = new Sprite(this.mRePlayButton.getEntity().getX() - Constants.MENU_RESULT_ELEMENT_MARGIN - menuButtonTextureRegion.getWidth(), 
				this.mRePlayButton.getEntity().getY(), 
				menuButtonTextureRegion, vertexBufferObjectManager);
		this.mMenuButton = new BaseButton<Sprite>(menuButtonSprite.getX(), menuButtonSprite.getY(), 1.0f, menuButtonSprite) {
			
			@Override
			public void onButtonClick() {
				hideScene();
				SceneManager.getInstance().showLevelSelectorScene();
			}
		};
		
		TextureRegion nextLevelButtonTextureRegion = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary().get(ButtonConstants.BUTTON_NEXT_LEVEL_ID);
		Sprite nextLevelButtonSprite = new Sprite(this.mRePlayButton.getEntity().getX() + this.mRePlayButton.getEntity().getWidth() +  Constants.MENU_RESULT_ELEMENT_MARGIN, 
				this.mRePlayButton.getEntity().getY(), 
				nextLevelButtonTextureRegion, vertexBufferObjectManager);
		this.mNextLevelButton = new BaseButton<Sprite>(nextLevelButtonSprite.getX(), nextLevelButtonSprite.getY(), 1.0f, nextLevelButtonSprite) {
			
			@Override
			public void onButtonClick() {
				hideScene();
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
		Debug.e("show lever cleard layer");
		GameLevelManager.getInstance().pause();
		
		this.mPanel.attachChild(this.mLevelClearedText);
		this.mPanel.attachChild(this.mScoreText);
		this.mPanel.attachChild(this.mLeftStarMenu);
		this.mPanel.attachChild(this.mCenterStarMenu);
		this.mPanel.attachChild(this.mRighStarMenu);
		this.mPanel.attachChild(this.mMenuButton.getEntity());
		this.mPanel.attachChild(this.mRePlayButton.getEntity());
		this.mPanel.attachChild(this.mNextLevelButton.getEntity());
		
		this.mMashRectangle.attachChild(this.mPanel);
		ResourceManager.getInstance().getHUD().attachChild(this.mMashRectangle);
		
		
		SfxManager.getInstance().playSlotMachineSound();
		ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(new TimerHandler(1/50f, true, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pUpdateHandler) {
				// TODO Auto-generated method stub
				Debug.e("Time pass");
				tempScore += ScoreConstants.UNIT_SCORE;
				mScoreText.setText(tempScore + "");
				mScoreText.setX((mPanel.getWidth() - mScoreText.getWidth()) * 0.5f);
				if(tempScore >= GameScoreManager.getInstance().getTotalScore()){
					mScoreText.setText(GameScoreManager.getInstance().getTotalScore() + "");
					SfxManager.getInstance().stopSlotMachineSound();
					SfxManager.getInstance().playLevelClearedSound();
					ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(pUpdateHandler);
				}
			}
		}));
		
		
		pOnShowSceneCallBack.onShowSceneFinish();
	}

	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		Debug.e("hide level cleared layer");
		GameLevelManager.getInstance().resume();
		
		pOnHideSceneCallBack.onHideSceneFinish();
	}
	
	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack) {

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
