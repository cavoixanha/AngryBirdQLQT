package com.xdpm.angrybirds.manager;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.util.texturepack.TexturePack;
import org.andengine.util.texturepack.TexturePackLoader;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;
import org.andengine.util.texturepack.exception.TexturePackParseException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;
import android.util.Log;

import com.xdpm.angrybirds.AngryBirdSmoothCamera;
import com.xdpm.angrybirds.common.Constants;

/**
 * This class manage all resources
 * All method get/set resource put in this class
 * @author ChuongNK
 *
 */
public class ResourceManager {
	
	//==================================================
	// Properties
	//==================================================
	
	private BaseGameActivity mBaseGameActivity;
	private AngryBirdSmoothCamera mCamera;
	private HUD mHUD = new HUD();
	
	public BaseGameActivity getBaseGameActivity() {
		return mBaseGameActivity;
	}

	private TexturePackTextureRegionLibrary mBirdSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mPigSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mArbalestSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mIceBarrierSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mStoneBarrierSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mWoodBarrierSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mCloudSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mScoreSpriteSheetTexturePackTextureRegionLibrary;
	
	private TexturePackTextureRegionLibrary mButtonSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mMenuSpriteSheetTexturePackTextureRegionLibrary;
	private TexturePackTextureRegionLibrary mSplashSpriteSheetTexturePackTextureRegionLibrary;

	// =====================================================
	// LevelSelector
	// =====================================================
	private TexturePackTextureRegionLibrary mLeveSelectorSheetTexturePackTextureRegionLibrary;
	
	private TextureRegion groundPaneTextureRegion;
	private TextureRegion oneLevelSelectorTextureRegion;
	private TextureRegion twoLevelSelectorTextureRegion;
	private TextureRegion threeLevelSelectorTextureRegion;
	private TextureRegion fourLevelSelectorTextureRegion;
	private TextureRegion fiveLevelSelectorTextureRegion;
	private TextureRegion sixLevelSelectorTextureRegion;
	private TextureRegion sevenLevelSelectorTextureRegion;
	private TextureRegion levelChooseTextureRegion;
	

	// ====================================================
	// Background
	// ====================================================
	
	private TextureRegion mAngryBirdTextTextureRegion;
	private TextureRegion mGroundParallelTextureRegion;
	private TextureRegion mLowFrontParallelTextureRegion;
	private TextureRegion mHighFrontParallelTextureRegion;
	private TextureRegion mLowBehindParallelTextureRegion;
	private TextureRegion mHighBehindParallelTextureRegion;
	private TextureRegion mTopGroundParallelTextureRegion;
	
	private BitmapTextureAtlas textureAtlas;
	
	//=================================================
	// Font
	//=================================================	
	
	private Font angrybirdFont48;
	
	private Music titleThemeMusic;
	
	//=================================================
	// Object Sound
	//=================================================
	
	private Sound mBirdMiscA1;
	private Sound mBirdMiscA2;
	private Sound mBirdMiscA3;
	private Sound mBirdMiscA4;
	private Sound mBirdMiscA5;
	private Sound mBirdMiscA6;
	
	private Sound mBlackBirdFlyingSound;
	private Sound mBlueBirdFlyingSound;
	private Sound mRedBirdFlyingSound;
	private Sound mWhiteBirdFlyingSound;
	private Sound mYellowBirdFlyingSound;
	
	
	//=================================================
	// LevelSound
	//=================================================
	private Sound mLevelClearedSound;
	private Sound mSlotMachineSound;
	
	//==================================================
	// Constructors
	//==================================================
	
	private ResourceManager() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(Constants.DEFAULT_ASSET_BASE_IMAGE);
		MusicFactory.setAssetBasePath(Constants.DEFAULT_ASSET_BASE_MUSIC);
		SoundFactory.setAssetBasePath(Constants.DEFAULT_ASSET_BASE_SOUND);	
		FontFactory.setAssetBasePath(Constants.DEFAULT_ASSET_BASE_FONT);
	}
	
	private static ResourceManager mResourceManager = null;
	

	public static ResourceManager getInstance() {
		if (null == mResourceManager) {
			mResourceManager = new ResourceManager();
		}
		return mResourceManager;
	}
	
	//==================================================
	// Methods
	//==================================================
	
	/*
	 * Load only SplashSceen Resource
	 */
	public void loadSplashSceneResource(){
		TexturePack menuTexturePack;
		try {
			menuTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/splash/splash.xml", "images/splash/");
			menuTexturePack.loadTexture();
			this.mSplashSpriteSheetTexturePackTextureRegionLibrary = menuTexturePack.getTexturePackTextureRegionLibrary();
		} catch (TexturePackParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean loadTitleMenuResource() {
		try {
			this.titleThemeMusic = MusicFactory.createMusicFromAsset(mBaseGameActivity.getEngine().getMusicManager(), mBaseGameActivity, "title_theme.mp3");			
			
			//==============================================
			// LevelSelector Scene Texture Region
			//==============================================
			TexturePack WorldSelectorTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/menu/World_Selector.xml", "images/menu/");
			WorldSelectorTexturePack.loadTexture();
			this.mLeveSelectorSheetTexturePackTextureRegionLibrary = WorldSelectorTexturePack.getTexturePackTextureRegionLibrary();
			
			groundPaneTextureRegion = createTextureRegion(mBaseGameActivity, 868, 468, "levelselector/BACKGROUNDS_LRS.png");
			oneLevelSelectorTextureRegion = createTextureRegion(mBaseGameActivity, 1017, 833, 300, 378, 135, 375, 
					"levelselector/LEVELSELECTION_SHEET_2.png", TextureOptions.DEFAULT);
			twoLevelSelectorTextureRegion = createTextureRegion(mBaseGameActivity, 1017, 833, 440, 0, 135, 375, 
					"levelselector/LEVELSELECTION_SHEET_2.png", TextureOptions.DEFAULT);
			threeLevelSelectorTextureRegion = createTextureRegion(mBaseGameActivity, 1017, 833, 440, 378, 135, 375, 
					"levelselector/LEVELSELECTION_SHEET_2.png", TextureOptions.DEFAULT);
			fourLevelSelectorTextureRegion = createTextureRegion(mBaseGameActivity, 1017, 833, 580, 0, 135, 375, 
					"levelselector/LEVELSELECTION_SHEET_2.png", TextureOptions.DEFAULT);
			fiveLevelSelectorTextureRegion = createTextureRegion(mBaseGameActivity, 1017, 833, 160, 0, 135, 375, 
					"levelselector/LEVELSELECTION_SHEET_2.png", TextureOptions.DEFAULT);
			sixLevelSelectorTextureRegion = createTextureRegion(mBaseGameActivity, 1017, 833, 160, 378, 135, 375, 
					"levelselector/LEVELSELECTION_SHEET_2.png", TextureOptions.DEFAULT);
			sevenLevelSelectorTextureRegion = createTextureRegion(mBaseGameActivity, 1017, 833, 300, 0, 135, 375, 
					"levelselector/LEVELSELECTION_SHEET_2.png", TextureOptions.DEFAULT);
			levelChooseTextureRegion = createTextureRegion(mBaseGameActivity, 1489, 808, 830, 713, 92, 92, 
					"levelselector/LEVELSELECTION_SHEET.png", TextureOptions.DEFAULT);
			//loadFont();
			loadAngryBirdFont();
			
			return true;
		} catch (Exception ex) {
			Log.e("ResourceManager", "Can't load title menu resource");
			Log.e("ResourceManager", ex.getMessage());
			return false;
		}
	}
	
	public boolean loadBackgroundResource(int pWorldId) {
		try {
			
			this.mAngryBirdTextTextureRegion = createTextureRegion(this.mBaseGameActivity, 409, 92, "background/ANGRY_BIRDS_TEXT_MENU_BACKGROUNDS.png");
			this.mGroundParallelTextureRegion = this.createTextureRegion(this.mBaseGameActivity, 346, 206, "background/poachedeggs/GROUND"+pWorldId+".png", TextureOptions.REPEATING_BILINEAR);
			this.mLowFrontParallelTextureRegion = this.createTextureRegion(this.mBaseGameActivity, 342, 40, "background/poachedeggs/LOW_FRONT_PARALLEL"+pWorldId+".png", TextureOptions.REPEATING_BILINEAR);
			this.mHighFrontParallelTextureRegion = this.createTextureRegion(this.mBaseGameActivity, 442, 76, "background/poachedeggs/HIGH_FRONT_PARALLEL"+pWorldId+".png", TextureOptions.REPEATING_BILINEAR);
			this.mLowBehindParallelTextureRegion = this.createTextureRegion(this.mBaseGameActivity, 490, 310, "background/poachedeggs/LOW_BEHIND_PARALLEL"+pWorldId+".png", TextureOptions.REPEATING_BILINEAR);
			this.mHighBehindParallelTextureRegion = this.createTextureRegion(this.mBaseGameActivity, 488, 222, "background/poachedeggs/HIGH_BEHIND_PARALLEL"+pWorldId+".png", TextureOptions.REPEATING_BILINEAR);
			this.mTopGroundParallelTextureRegion = this.createTextureRegion(this.mBaseGameActivity, 10, 10, "background/poachedeggs/TOP_GROUND_PARALLEL"+pWorldId+".png", TextureOptions.REPEATING_BILINEAR);
			
			return true;
		} catch (Exception ex) {
			Log.e("ResourceManager", "Can't load poeachedeggs background resource");
			Log.e("ResourceManager", ex.getMessage());
			
			return false;
		}
	}
	
	public void loadAngryBirdFont() {
		
	}
	
	public boolean loadGameResources(){
		try {
			
			//==============================================
			// Birds
			//==============================================
			final TexturePack birdTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/bird/bird.xml", "images/bird/");
			birdTexturePack.loadTexture();
			this.mBirdSpriteSheetTexturePackTextureRegionLibrary = birdTexturePack.getTexturePackTextureRegionLibrary();
			
			//==============================================
			// Pigs
			//==============================================
			final TexturePack pigTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/pig/pig.xml", "images/pig/");
			pigTexturePack.loadTexture();
			this.mPigSpriteSheetTexturePackTextureRegionLibrary = pigTexturePack.getTexturePackTextureRegionLibrary();
			
			//==============================================
			// Arbalest
			//==============================================
			final TexturePack arbalestTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/bird/bird.xml", "images/bird/");
			arbalestTexturePack.loadTexture();
			this.mArbalestSpriteSheetTexturePackTextureRegionLibrary = arbalestTexturePack.getTexturePackTextureRegionLibrary();
	
			
			//==============================================
			// Barriers
			//==============================================
			final TexturePack iceBarrierTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/barrier/ice/ice.xml", "images/barrier/ice/");
			iceBarrierTexturePack.loadTexture();
			this.mIceBarrierSpriteSheetTexturePackTextureRegionLibrary = iceBarrierTexturePack.getTexturePackTextureRegionLibrary();
			
			final TexturePack stoneBarrierTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/barrier/stone/stone.xml", "images/barrier/stone/");
			stoneBarrierTexturePack.loadTexture();
			this.mStoneBarrierSpriteSheetTexturePackTextureRegionLibrary = stoneBarrierTexturePack.getTexturePackTextureRegionLibrary();
			
			final TexturePack woodBarrierTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/barrier/wood/wood.xml", "images/barrier/wood/");
			woodBarrierTexturePack.loadTexture();
			this.mWoodBarrierSpriteSheetTexturePackTextureRegionLibrary = woodBarrierTexturePack.getTexturePackTextureRegionLibrary();
			
			//==============================================
			// Clouds
			//==============================================
			final TexturePack cloudTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/cloud/cloud.xml", "images/cloud/");
			cloudTexturePack.loadTexture();
			this.mCloudSpriteSheetTexturePackTextureRegionLibrary = cloudTexturePack.getTexturePackTextureRegionLibrary();

			//==============================================
			// Menus
			//==============================================
			final TexturePack menuTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/menu/gamemenu.xml", "images/menu/");
			menuTexturePack.loadTexture();
			this.mMenuSpriteSheetTexturePackTextureRegionLibrary = menuTexturePack.getTexturePackTextureRegionLibrary();
			
			//==============================================
			// Buttons
			//==============================================
			final TexturePack buttonTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/button/button.xml", "images/button/");
			buttonTexturePack.loadTexture();
			this.mButtonSpriteSheetTexturePackTextureRegionLibrary = buttonTexturePack.getTexturePackTextureRegionLibrary();
						
			//==============================================
			// Score
			//==============================================
			final TexturePack scoreTexturePack = new TexturePackLoader(this.mBaseGameActivity.getAssets(), this.mBaseGameActivity.getTextureManager()).loadFromAsset("images/score/score.xml", "images/score/");
			scoreTexturePack.loadTexture();
			this.mScoreSpriteSheetTexturePackTextureRegionLibrary = scoreTexturePack.getTexturePackTextureRegionLibrary();
			
			//==============================================
			// Sounds
			//==============================================
			this.mBirdMiscA1 = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/bird_misc_a1.mp3");
			this.mBirdMiscA2 = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/bird_misc_a2.mp3");
			this.mBirdMiscA3 = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/bird_misc_a3.mp3");
			this.mBirdMiscA4 = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/bird_misc_a4.mp3");
			this.mBirdMiscA5 = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/bird_misc_a5.mp3");
			this.mBirdMiscA6 = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/bird_misc_a6.mp3");
			
			this.mBlackBirdFlyingSound = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/red_bird_flying.mp3");
			this.mBlueBirdFlyingSound = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/red_bird_flying.mp3");
			this.mRedBirdFlyingSound = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/red_bird_flying.mp3");
			this.mWhiteBirdFlyingSound = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/red_bird_flying.mp3");
			this.mYellowBirdFlyingSound = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "bird/red_bird_flying.mp3");
			
			this.mLevelClearedSound = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "level/level_clear_military.mp3");
			this.mSlotMachineSound = SoundFactory.createSoundFromAsset(this.mBaseGameActivity.getSoundManager(), this.mBaseGameActivity.getApplicationContext(), "level/slot_machine.mp3");
			//==============================================
			// Fonts
			//==============================================
			this.angrybirdFont48 = FontFactory.createFromAsset(mBaseGameActivity.getFontManager(), 
					mBaseGameActivity.getTextureManager(), 
					256, 256, TextureOptions.BILINEAR, 
					this.mBaseGameActivity.getAssets(), "ANGRYBIRD_FONT.TTF", 48,
					true, Color.WHITE_ABGR_PACKED_INT);
			this.angrybirdFont48.load();
			
			return true;
		}
		catch(Exception ex){
			Log.e("ResourceManager", "Can't load resource");
			Log.e("ResourceManager", ex.getMessage());
			return false;
		}
	}
	
	/*
	 * Create TextureRegion from a big picture
	 */
	private TextureRegion createTextureRegion(BaseGameActivity activity, int width, int height, int px, int py, int pWidth, int pHeight, String imageFile, TextureOptions option){
		try{
			TextureRegion textureRegion = createTextureRegion(activity, width, height, imageFile, option);
			textureRegion.set(px, py, pWidth, pHeight);
			return textureRegion;
		}
		catch (Exception ex){
			return null;
		}
	}
	
	private TextureRegion createTextureRegion(BaseGameActivity activity ,int width, int height, String imageFile){
		try{
			textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), width, height, TextureOptions.BILINEAR);
			TextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, activity, imageFile,0,0);
			textureAtlas.load();
			
			return textureRegion;
		}
		catch(Exception ex){
			Log.e("ResourceManager", "Can't createTextureRegion");
			Log.e("ResourceManager", ex.getMessage());
			return null;
		}
	}
	
	private TextureRegion createTextureRegion(BaseGameActivity activity ,int width, int height, String imageFile, TextureOptions option){
		try{
			textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), width, height, option);
			TextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, activity, imageFile,0,0);
			textureAtlas.load();
			
			return textureRegion;
		}
		catch(Exception ex){
			Log.e("ResourceManager", "Can't createTextureRegion");
			Log.e("ResourceManager", ex.getMessage());
			return null;
		}
	}
	
	private TiledTextureRegion createTiledTextureRegion(BaseGameActivity activity, int width, int height, String imageFile, int textureX, int textureY, int tileColumns, int tileRows) {
		try {
			textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), width, height);
			TiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, activity, imageFile, textureX, textureY, tileColumns, tileRows);
			textureAtlas.load();
			return tiledTextureRegion;
			
		} catch (Exception ex) {
			return null;
		}
	}
	
	// ===========================================================
	// Set/Get method
	// ===========================================================
	
	public void setBaseGameActivity(BaseGameActivity pBaseGameActivity) {
		
		this.mBaseGameActivity = pBaseGameActivity;
	}
	
	public AngryBirdSmoothCamera getCamera() {
		return mCamera;
	}

	public void setCamera(AngryBirdSmoothCamera pCamera) {
		this.mCamera = pCamera;
		this.mCamera.setHUD(this.mHUD);
	}
	
	public HUD getHUD() {
		return mHUD;
	}
	
	public Music getTitleThemeMusic() {
		return titleThemeMusic;
	}
	
	public TextureRegion getGroundPaneTextureRegion() {
		return groundPaneTextureRegion;
	}

	public TextureRegion getOneLevelSelectorTextureRegion() {
		return oneLevelSelectorTextureRegion;
	}

	public TextureRegion getTwoLevelSelectorTextureRegion() {
		return twoLevelSelectorTextureRegion;
	}
	public TextureRegion getThreeLevelSelectorTextureRegion() {
		return threeLevelSelectorTextureRegion;
	}

	public TextureRegion getFourLevelSelectorTextureRegion() {
		return fourLevelSelectorTextureRegion;
	}

	public TextureRegion getFiveLevelSelectorTextureRegion() {
		return fiveLevelSelectorTextureRegion;
	}

	public TextureRegion getSixLevelSelectorTextureRegion() {
		return sixLevelSelectorTextureRegion;
	}

	public TextureRegion getSevenLevelSelectorTextureRegion() {
		return sevenLevelSelectorTextureRegion;
	}

	public TextureRegion getLevelChooseTextureRegion() {
		return levelChooseTextureRegion;
	}

	public Sound getBirdMiscA1() {
		return mBirdMiscA1;
	}

	public Sound getBirdMiscA2() {
		return mBirdMiscA2;
	}

	public Sound getBirdMiscA3() {
		return mBirdMiscA3;
	}

	public Sound getBirdMiscA4() {
		return mBirdMiscA4;
	}

	public Sound getBirdMiscA5() {
		return mBirdMiscA5;
	}

	public Sound getBirdMiscA6() {
		return mBirdMiscA6;
	}
	
	public Sound getRedBirdFlyingSound() {
		return mRedBirdFlyingSound;
	}

	public Sound getBlackBirdFlyingSound() {
		return mBlackBirdFlyingSound;
	}

	public Sound getBlueBirdFlyingSound() {
		return mBlueBirdFlyingSound;
	}

	public Sound getWhiteBirdFlyingSound() {
		return mWhiteBirdFlyingSound;
	}

	public Sound getYellowBirdFlyingSound() {
		return mYellowBirdFlyingSound;
	}

	public TexturePackTextureRegionLibrary getBirdSpriteSheetTexturePackTextureRegionLibrary() {
		return mBirdSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getPigSpriteSheetTexturePackTextureRegionLibrary() {
		return mPigSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getButtonSpriteSheetTexturePackTextureRegionLibrary() {
		return mButtonSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getIceBarrierSpriteSheetTexturePackTextureRegionLibrary() {
		return mIceBarrierSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getStoneBarrierSpriteSheetTexturePackTextureRegionLibrary() {
		return mStoneBarrierSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getWoodBarrierSpriteSheetTexturePackTextureRegionLibrary() {
		return mWoodBarrierSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getMenuSpriteSheetTexturePackTextureRegionLibrary() {
		return mMenuSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getSplashSpriteSheetTexturePackTextureRegionLibrary() {
		return mSplashSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getArbalestSpriteSheetTexturePackTextureRegionLibrary() {
		return mArbalestSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getCloudSpriteSheetTexturePackTextureRegionLibrary() {
		return mCloudSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TexturePackTextureRegionLibrary getScoreSpriteSheetTexturePackTextureRegionLibrary() {
		return mScoreSpriteSheetTexturePackTextureRegionLibrary;
	}

	public TextureRegion getAngryBirdTextTextureRegion() {
		return mAngryBirdTextTextureRegion;
	}

	public TextureRegion getGroundParallelTextureRegion() {
		return mGroundParallelTextureRegion;
	}

	public TextureRegion getLowFrontParallelTextureRegion() {
		return mLowFrontParallelTextureRegion;
	}

	public TextureRegion getHighFrontParallelTextureRegion() {
		return mHighFrontParallelTextureRegion;
	}

	public TextureRegion getLowBehindParallelTextureRegion() {
		return mLowBehindParallelTextureRegion;
	}

	public TextureRegion getHighBehindParallelTextureRegion() {
		return mHighBehindParallelTextureRegion;
	}

	public TextureRegion getTopGroundParallelTextureRegion() {
		return mTopGroundParallelTextureRegion;
	}

	public Font getAngrybirdFont48() {
		return angrybirdFont48;
	}

	public TexturePackTextureRegionLibrary getmLeveSelectorSheetTexturePackTextureRegionLibrary() {
		return mLeveSelectorSheetTexturePackTextureRegionLibrary;
	}

	public Sound getLevelClearedSound() {
		return mLevelClearedSound;
	}

	public Sound getSlotMachineSound() {
		return mSlotMachineSound;
	}
}
