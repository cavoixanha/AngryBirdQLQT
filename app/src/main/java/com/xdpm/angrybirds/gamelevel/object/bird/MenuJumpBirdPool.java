package com.xdpm.angrybirds.gamelevel.object.bird;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.JumpModifier;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SfxManager;
import com.xdpm.angrybirds.scene.MainMenuScene;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class MenuJumpBirdPool implements IUpdateHandler {
	
	// ==========================================================
	// Properties
	// ==========================================================
	
	private static final char BLACK_BIRD = 0;
	private static final char BLUE_BIRD = BLACK_BIRD + 1;
	private static final char RED_BIRD = BLUE_BIRD + 1;
	private static final char WHITE_BIRD = RED_BIRD + 1;
	private static final char YELLOW_BIRD = WHITE_BIRD + 1;
	private static final int MAX_NUM_OF_BIRD = 10;
	
	private static MenuJumpBirdPool mMenuJumpBirdPool = new MenuJumpBirdPool();
	private static MainMenuScene mMainMenuScene;
	
	private static int mCurrentNumOfBird = 0;
	
	private static ITiledTextureRegion mBlackBirdTextureRegion;
	private static ITiledTextureRegion mBlueBirdTextureRegion;
	private static ITiledTextureRegion mRedBirdTextureRegion;
	private static ITiledTextureRegion mWhiteBirdTextureRegion;
	private static ITiledTextureRegion mYellowBirdTextureRegion;
	
	// ==========================================================
	// Methods from/for SuperClass/Interface
	// ==========================================================
	
	public static void onLoadPool() {
		
		mBlackBirdTextureRegion = ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.BLACK_BIRD_ID, 7, 1);
		mBlueBirdTextureRegion = ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.BLUE_BIRD_ID, 4, 1);
		mRedBirdTextureRegion = ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.RED_BIRD_ID, 4, 1);
		mWhiteBirdTextureRegion = ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.WHITE_BIRD_ID, 7, 1);
		mYellowBirdTextureRegion = ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.YELLOW_BIRD_ID, 6, 1);
	}
	
	public static void onShowPool() {
		
		ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(mMenuJumpBirdPool);
	}
	
	public static void onUnLoadPool() {
		mCurrentNumOfBird = 0;
		ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(mMenuJumpBirdPool);
	}
	
	public static void setMainMenuScene(MainMenuScene pMainMenuScene) {
		mMainMenuScene = pMainMenuScene;
	}

	@Override
	public void onUpdate(float timeElapsed) {
		char doAction = (char) MathUtils.random(0, 99);
		
		if (doAction % 50 != 0) {
			return;
		}
		
		if (mCurrentNumOfBird >= MAX_NUM_OF_BIRD) {
			return;
		}
		
		float scale = MathUtils.random(BirdConstants.MENU_JUMP_BIRD_MIN_SCALE, BirdConstants.MENU_JUMP_BIRD_MAX_SCALE);
		
		char birdType = (char) MathUtils.random(BLACK_BIRD, YELLOW_BIRD);
		switch (birdType) {
		case BLACK_BIRD:
			
			final MenuJumpBirdObject blackBird = new MenuJumpBirdObject(0, 0, 
					scale, 
					mBlackBirdTextureRegion) {	
				@Override
				protected void laught() {
					SfxManager.getInstance().playRedBirdPlayingSound();
				}
			};
			
			this.createBird(blackBird);
			break;
		case BLUE_BIRD:
			final MenuJumpBirdObject blueBird = new MenuJumpBirdObject(0, 0, 
					scale, 
					mBlueBirdTextureRegion) {	
				@Override
				protected void laught() {
					SfxManager.getInstance().playRedBirdPlayingSound();
				}
			};
			
			this.createBird(blueBird);		
			break;
			
		case RED_BIRD:
			
			final MenuJumpBirdObject redBird = new MenuJumpBirdObject(0, 0,
					scale,
					mRedBirdTextureRegion) {
				
				@Override
				protected void laught() {
					SfxManager.getInstance().playRedBirdPlayingSound();
				}
			};

			this.createBird(redBird);
			break;
		case WHITE_BIRD:
			final MenuJumpBirdObject whiteBird = new MenuJumpBirdObject(0, 0, 
					scale, 
					mWhiteBirdTextureRegion) {	
				@Override
				protected void laught() {
					SfxManager.getInstance().playRedBirdPlayingSound();
				}
			};
			
			this.createBird(whiteBird);
			break;
		case YELLOW_BIRD:
			
			final MenuJumpBirdObject yellowBird = new MenuJumpBirdObject(0, 0, 
					scale,
					mYellowBirdTextureRegion) {
				
				@Override
				protected void laught() {
					SfxManager.getInstance().playRedBirdPlayingSound();
				}
			};

			this.createBird(yellowBird);
			break;
		default:
			break;
		}
	}

	@Override
	public void reset() {
		
	}
	
	
	private void createBird(final MenuJumpBirdObject pBird) {
		
		float startCoordinateX = MathUtils.random(- Constants.CAMERA_BOUND_WIDTH * 0.5f, Constants.CAMERA_BOUND_WIDTH * 0.45f);
		float endCoordinateX = MathUtils.random(Constants.CAMERA_BOUND_WIDTH * 0.55f, Constants.CAMERA_BOUND_WIDTH * 1.5f);
		float coordinateY = Constants.CAMERA_BOUND_HEIGHT 
				- ResourceManager.getInstance().getGroundParallelTextureRegion().getHeight()
				- pBird.getEntity().getHeight();
		float jumpHeight = MathUtils.random(pBird.getEntity().getHeight(), Constants.CAMERA_BOUND_HEIGHT);
		
		JumpModifier jumpModifier = new JumpModifier(Constants.MENU_BIRD_JUMP_DURATION, 
				startCoordinateX, 
				endCoordinateX, 
				coordinateY, 
				coordinateY, 
				jumpHeight, new IEntityModifier.IEntityModifierListener() {
					
					@Override
					public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1) {
						mCurrentNumOfBird += 1;
					}
					
					@Override
					public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1) {
						mCurrentNumOfBird -= 1;
						destroyBird(pBird);
					}
				});
		
		mMainMenuScene.registerTouchArea(pBird.getEntity());
		mMainMenuScene.attachChild(pBird.getEntity());
		
		pBird.registerEntityModifier(jumpModifier);
	}
	
	private void destroyBird(final MenuJumpBirdObject pBird) {
		
		mMainMenuScene.unregisterTouchArea(pBird.getEntity());
		ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				pBird.getEntity().detachSelf();
				pBird.getEntity().dispose();
			}
		});
	}
}
