package com.xdpm.angrybirds.scene.layer;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SceneManager;
import com.xdpm.angrybirds.manager.SfxManager;
import com.xdpm.angrybirds.object.button.BaseButton;
import com.xdpm.angrybirds.object.button.ToggleButton;
import com.xdpm.angrybirds.scene.MainGameScene;

public class GameLevelPauseLayer extends BaseLayer {

	// =======================================================
	// Properties
	// =======================================================
	
	private Rectangle mPanel;
	
	private BaseButton<Sprite> mButtonPlay;
	private BaseButton<Sprite> mButtonRePlay;
	private BaseButton<Sprite> mButtonMenu;
	private BaseButton<Sprite> mButtonHelp;
	
	private ToggleButton mButtonSound;
	
	// =============================================
	// Constructors
	// =============================================
	
	public GameLevelPauseLayer() {
		super(true);
	}
	
	// =====================================================
	// Getters & Setters
	// =====================================================
	
	
	// =======================================================
	// Methods from/for SuperClass/Interface
	// =======================================================
	
	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack) {		
		super.onLoadScene(pOnLoadSceneCallBack);
		
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		TexturePackTextureRegionLibrary buttonTextureRegionLibrary = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary();
		
		// Panel
		this.mPanel = new Rectangle(-132, 0, 132, Constants.CAMERA_HEIGHT, vertexBufferObjectManager);
		this.mPanel.setColor(Color.BLACK);
		
		// Button Play 
		TextureRegion buttonPlayTextureRegion = buttonTextureRegionLibrary.get(ButtonConstants.BUTTON_PLAY_IN_GAME_ID);
		Sprite buttonPlaySprite = new Sprite((this.mPanel.getWidth() - buttonPlayTextureRegion.getWidth()) / 2, 4, 
				buttonPlayTextureRegion, vertexBufferObjectManager);
		this.mButtonPlay = new BaseButton<Sprite>(buttonPlaySprite.getX(), buttonPlaySprite.getY(), 1, buttonPlaySprite) {
			
			@Override
			public void onButtonClick() {
				hideScene();
			}
		};
		
		
		// Button RePlay
		TextureRegion buttonRePlayTextureRegion = buttonTextureRegionLibrary.get(ButtonConstants.BUTTON_REPLAY_ID);
		Sprite buttonRePlaySprite = new Sprite((this.mPanel.getWidth() - buttonRePlayTextureRegion.getWidth()) / 2, 
				Constants.CAMERA_HEIGHT / 2 - buttonRePlayTextureRegion.getHeight(), 
				buttonRePlayTextureRegion, vertexBufferObjectManager);
		this.mButtonRePlay = new BaseButton<Sprite>(buttonRePlaySprite.getX(), buttonRePlaySprite.getY(), 1, buttonRePlaySprite) {

			@Override
			public void onButtonClick() {
				hideScene();
				GameLevelManager.getInstance().replayCurrentGameLevel();
			}
			
		};
		
		// Button Menu
		TextureRegion buttonMenuTextureRegion = buttonTextureRegionLibrary.get(ButtonConstants.BUTTON_MENU_ID);
		Sprite buttonMenuprite = new Sprite((this.mPanel.getWidth() - buttonMenuTextureRegion.getWidth()) / 2, 
				Constants.CAMERA_HEIGHT / 2, 
				buttonMenuTextureRegion, vertexBufferObjectManager);
		this.mButtonMenu = new BaseButton<Sprite>(buttonMenuprite.getX(), buttonMenuprite.getY(), 1, buttonMenuprite) {

			@Override
			public void onButtonClick() {
				hideScene();
				SceneManager.getInstance().showLevelSelectorScene();
			}
			
		};
		
		// Button Sound
		TextureRegion buttonSoundTextureRegion = buttonTextureRegionLibrary.get(ButtonConstants.BUTTON_SOUND_ID);
		this.mButtonSound = new ToggleButton(4, Constants.CAMERA_HEIGHT - buttonSoundTextureRegion.getHeight() - 4, 1, buttonSoundTextureRegion) {
			@Override
			public void onButtonClick() {
				super.onButtonClick();
				SfxManager.getInstance().setMusicMuted(mIsClicked);
			}
		};
		
		// Button Help
		TextureRegion buttonHelpTextureRegion = buttonTextureRegionLibrary.get(ButtonConstants.BUTTON_HELP_ID);
		Sprite buttonHelprite = new Sprite(4 + this.mButtonSound.getEntity().getX() + this.mButtonSound.getEntity().getWidth(), 
				Constants.CAMERA_HEIGHT - buttonHelpTextureRegion.getHeight() - 4,
				buttonHelpTextureRegion, vertexBufferObjectManager);
		this.mButtonHelp = new BaseButton<Sprite>(buttonHelprite.getX(), buttonHelprite.getY(), 1, buttonHelprite) {

			@Override
			public void onButtonClick() {
			}
			
		};
		
		HUD hud = ResourceManager.getInstance().getHUD();
		hud.setOnAreaTouchListener(this);
		
		hud.registerTouchArea(this.mButtonPlay.getEntity());
		hud.registerTouchArea(this.mButtonRePlay.getEntity());
		hud.registerTouchArea(this.mButtonMenu.getEntity());
		hud.registerTouchArea(this.mButtonSound.getEntity());
		hud.registerTouchArea(this.mButtonHelp.getEntity());
		
		pOnLoadSceneCallBack.onLoadSceneFinish();
		
	}

	@Override
	public void onShowScene(final OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {		
		
		GameLevelManager.getInstance().pause();
		
		this.mPanel.attachChild(this.mButtonPlay.getEntity());
		this.mPanel.attachChild(this.mButtonRePlay.getEntity());
		this.mPanel.attachChild(this.mButtonMenu.getEntity());
		this.mPanel.attachChild(this.mButtonSound.getEntity());
		this.mPanel.attachChild(this.mButtonHelp.getEntity());

		PathModifier slideInModifier = new PathModifier(0.5f,
				new Path(new float[] {this.mPanel.getX() - this.mPanel.getWidth(), 0}, new float[] {0, 0}),
				new IEntityModifier.IEntityModifierListener() {
					
					@Override
					public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1) {
					}
					
					@Override
					public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1) {
						pOnShowSceneCallBack.onShowSceneFinish();
					}
				});
		slideInModifier.setAutoUnregisterWhenFinished(true);
		
		this.mPanel.registerEntityModifier(slideInModifier);
		this.mMashRectangle.attachChild(this.mPanel);
		
		ResourceManager.getInstance().getHUD().attachChild(this.mMashRectangle);
	}

	@Override
	public void onHideScene(final OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		
		GameLevelManager.getInstance().resume();
		
		PathModifier slideOutModifier = new PathModifier(0.5f,
				new Path(new float[] {0, this.mPanel.getX() - this.mPanel.getWidth()}, new float[] {0, 0}),
				new PathModifier.IPathModifierListener() {
					
					@Override
					public void onPathWaypointStarted(PathModifier arg0, IEntity arg1, int arg2) {
						
					}
					
					@Override
					public void onPathWaypointFinished(PathModifier arg0, IEntity arg1, int arg2) {
						
					}
					
					@Override
					public void onPathStarted(PathModifier arg0, IEntity arg1) {
						
					}
					
					@Override
					public void onPathFinished(PathModifier arg0, IEntity arg1) {
						pOnHideSceneCallBack.onHideSceneFinish();
					}
				});
		slideOutModifier.setAutoUnregisterWhenFinished(true);
		
		this.mPanel.registerEntityModifier(slideOutModifier);
	}
	
	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack) {
		
		this.mPanel.detachChildren();
		this.mPanel.detachSelf();
		this.mPanel.dispose();
		
		HUD hud = ResourceManager.getInstance().getHUD();
		
		hud.unregisterTouchArea(this.mButtonPlay.getEntity());
		hud.unregisterTouchArea(this.mButtonRePlay.getEntity());
		hud.unregisterTouchArea(this.mButtonMenu.getEntity());
		hud.unregisterTouchArea(this.mButtonSound.getEntity());
		hud.unregisterTouchArea(this.mButtonHelp.getEntity());
		
		super.onUnLoadScene(pOnUnLoadSceneCallBack);
	}
}
