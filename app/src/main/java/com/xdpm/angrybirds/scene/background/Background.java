package com.xdpm.angrybirds.scene.background;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.scene.BaseScene;
import com.xdpm.angrybirds.utils.SpriteUtils;

public class Background {
	
	// =============================================
	// Properties
	// =============================================
	
	protected Sprite mGroundParallel;
	protected Sprite mLowFrontParallel;
	protected Sprite mHighFrontParallel;
	protected Sprite mLowBehindParallel;
	protected Sprite mHighBehindParallel;
	protected Sprite mTopGroundParallel;
	protected Sprite mAngryBirdTextSprite;
	
	protected float mGroundParallelActualWidth;
	protected float mLowFrontParallelActualWidth;
	protected float mHighFrontParalleActualWidth;
	protected float mLowBehindParallelWidth;
	protected float mHighBehindParallelWidth;
	
	private LoopEntityModifier mGroundParallelLoopEntityModifier;
	private LoopEntityModifier mLowFrontParallelLoopEntityModifier;
	private LoopEntityModifier mHighFrontParallelLoopEntityModifier;
	private LoopEntityModifier mLowBehindParallelLoopEntityModifier;
	private LoopEntityModifier mHighBehindParallelLoopEntityModifier;
	
	protected BaseScene mBaseScene;
	
	// =============================================
	// Constructors
	// =============================================
	
	public Background(BaseScene pBaseScene) {
		this.mBaseScene = pBaseScene;
	}
	
	public void enableText(boolean pEnable) {
		this.mAngryBirdTextSprite.setVisible(pEnable);
	}
	
	public void isMovingBackground(boolean pIsMoving) {
		if (pIsMoving) {
			
			this.mGroundParallelLoopEntityModifier = new LoopEntityModifier(
					new MoveXModifier(Constants.MENU_GROUND_MOVING_SPEED, 0, - this.mGroundParallelActualWidth)
					);
			this.mLowFrontParallelLoopEntityModifier = new LoopEntityModifier(
					new MoveXModifier(Constants.MENU_GROUND_MOVING_SPEED, 0, - this.mLowFrontParallelActualWidth)
					);
			this.mHighFrontParallelLoopEntityModifier = new LoopEntityModifier(
					new MoveXModifier(Constants.MENU_GROUND_MOVING_SPEED * this.mHighFrontParalleActualWidth * 1.0f / this.mLowFrontParallelActualWidth, 0, - this.mHighFrontParalleActualWidth)
					);
			this.mLowBehindParallelLoopEntityModifier = new LoopEntityModifier(
					new MoveXModifier(Constants.MENU_TREE_MOVING_SPEED, 0, - this.mLowBehindParallelWidth)
					);
			this.mHighBehindParallelLoopEntityModifier = new LoopEntityModifier(
					new MoveXModifier(Constants.MENU_TREE_MOVING_SPEED, 0, - this.mHighBehindParallelWidth)
					);
			
			this.mGroundParallel.registerEntityModifier(this.mGroundParallelLoopEntityModifier);
			this.mLowFrontParallel.registerEntityModifier(this.mLowFrontParallelLoopEntityModifier);
			this.mHighFrontParallel.registerEntityModifier(this.mHighFrontParallelLoopEntityModifier);
			this.mLowBehindParallel.registerEntityModifier(this.mLowBehindParallelLoopEntityModifier);
			this.mHighBehindParallel.registerEntityModifier(this.mHighBehindParallelLoopEntityModifier);
			
		} else {
			this.mGroundParallel.setX(0);
			this.mLowFrontParallel.setX(0);
			this.mHighFrontParallel.setX(0);
			this.mLowBehindParallel.setX(0);
			this.mHighBehindParallel.setX(0);
			
			this.mGroundParallel.unregisterEntityModifier(this.mGroundParallelLoopEntityModifier);
			this.mLowFrontParallel.unregisterEntityModifier(this.mLowFrontParallelLoopEntityModifier);
			this.mHighFrontParallel.unregisterEntityModifier(this.mHighFrontParallelLoopEntityModifier);
			this.mLowBehindParallel.unregisterEntityModifier(this.mLowBehindParallelLoopEntityModifier);
			this.mHighBehindParallel.unregisterEntityModifier(this.mHighBehindParallelLoopEntityModifier);
		}
	}
	
	// =============================================
	// Methods from/for SuperClass/Interface
	// =============================================
	
	public void loadBackground() {
		final BaseGameActivity baseGameActivity = ResourceManager.getInstance().getBaseGameActivity();
		
		// Text
		TextureRegion angrybirdText = ResourceManager.getInstance().getAngryBirdTextTextureRegion();
		this.mAngryBirdTextSprite = new Sprite((Constants.CAMERA_BOUND_WIDTH - angrybirdText.getWidth()) * 0.5f,
				(Constants.CAMERA_BOUND_HEIGHT - angrybirdText.getHeight()) * 0.5f, 
				angrybirdText, 
				baseGameActivity.getVertexBufferObjectManager()); 
		
		// Ground
		TextureRegion groundTextureRegion = ResourceManager.getInstance().getGroundParallelTextureRegion();
		this.mGroundParallelActualWidth = groundTextureRegion.getWidth();
		this.mGroundParallel = SpriteUtils.createRepeatingSprite(groundTextureRegion, 
				(Constants.CAMERA_BOUND_WIDTH / groundTextureRegion.getWidth() + 1) * groundTextureRegion.getWidth(), 
				groundTextureRegion.getHeight(), baseGameActivity);
		this.mGroundParallel.setPosition(0, Constants.CAMERA_BOUND_HEIGHT - groundTextureRegion.getHeight());
		
		// Font low
		TextureRegion lowFrontTextureRegion = ResourceManager.getInstance().getLowFrontParallelTextureRegion();
		this.mLowFrontParallelActualWidth = lowFrontTextureRegion.getWidth();
		this.mLowFrontParallel = SpriteUtils.createRepeatingSprite(lowFrontTextureRegion, 
				(Constants.CAMERA_BOUND_WIDTH / lowFrontTextureRegion.getWidth() + 1) * lowFrontTextureRegion.getWidth(),
				lowFrontTextureRegion.getHeight(), baseGameActivity);
		this.mLowFrontParallel.setPosition(0, this.mGroundParallel.getY() - lowFrontTextureRegion.getHeight()+12);
		
		// Front high 
		TextureRegion highFrontTextureRegion = ResourceManager.getInstance().getHighFrontParallelTextureRegion();
		this.mHighFrontParalleActualWidth = highFrontTextureRegion.getWidth();
		this.mHighFrontParallel = SpriteUtils.createRepeatingSprite(highFrontTextureRegion,
				(Constants.CAMERA_BOUND_WIDTH / highFrontTextureRegion.getWidth() + 1) * highFrontTextureRegion.getWidth(), 
				highFrontTextureRegion.getHeight(), baseGameActivity);
		this.mHighFrontParallel.setPosition(0, this.mGroundParallel.getY() - highFrontTextureRegion.getHeight());
		
		// Behind high
		TextureRegion highBehindTextureRegion = ResourceManager.getInstance().getHighBehindParallelTextureRegion();
		this.mHighBehindParallelWidth = highBehindTextureRegion.getWidth();
		this.mHighBehindParallel = SpriteUtils.createRepeatingSprite(highBehindTextureRegion, 
				(Constants.CAMERA_BOUND_WIDTH / highBehindTextureRegion.getWidth() + 1) * highBehindTextureRegion.getWidth(), 
				highBehindTextureRegion.getHeight(), baseGameActivity);
		this.mHighBehindParallel.setPosition(0, this.mGroundParallel.getY() - highBehindTextureRegion.getHeight()+12);
		
		
		// Behind low
		TextureRegion lowBehindTextureRegion = ResourceManager.getInstance().getLowBehindParallelTextureRegion();
		this.mLowBehindParallelWidth = lowBehindTextureRegion.getWidth();
		this.mLowBehindParallel = SpriteUtils.createRepeatingSprite(lowBehindTextureRegion, 
				(Constants.CAMERA_BOUND_WIDTH / lowBehindTextureRegion.getWidth() + 1) * lowBehindTextureRegion.getWidth(), 
				lowBehindTextureRegion.getHeight(), baseGameActivity);
		this.mLowBehindParallel.setPosition(0, this.mHighBehindParallel.getY() - lowBehindTextureRegion.getHeight()+12);
		
		// Top 
		TextureRegion topTextureRegion = ResourceManager.getInstance().getTopGroundParallelTextureRegion();
		this.mTopGroundParallel = SpriteUtils.createRepeatingSprite(topTextureRegion, 
				(Constants.CAMERA_BOUND_WIDTH / topTextureRegion.getWidth() + 1) * topTextureRegion.getWidth(), 
				Constants.CAMERA_BOUND_HEIGHT - this.mLowBehindParallel.getY(), baseGameActivity);
		this.mTopGroundParallel.setPosition(0, 0);
	}
	
	public void showBackground() {
		// Attach to scene
		this.mBaseScene.attachChild(this.mTopGroundParallel);
		this.mBaseScene.attachChild(this.mLowBehindParallel);
		this.mBaseScene.attachChild(this.mHighBehindParallel);
		//this.mBaseScene.attachChild(this.mHighFrontParallel);
		//this.mBaseScene.attachChild(this.mLowFrontParallel);
		this.mBaseScene.attachChild(this.mGroundParallel);
		this.mBaseScene.attachChild(this.mAngryBirdTextSprite);
	}
	
	public void unloadBackground() {
		
		this.mGroundParallel.unregisterEntityModifier(this.mGroundParallelLoopEntityModifier);
		this.mLowFrontParallel.unregisterEntityModifier(this.mLowFrontParallelLoopEntityModifier);
		this.mHighFrontParallel.unregisterEntityModifier(this.mHighFrontParallelLoopEntityModifier);
		this.mLowBehindParallel.unregisterEntityModifier(this.mLowBehindParallelLoopEntityModifier);
		this.mHighBehindParallel.unregisterEntityModifier(this.mHighBehindParallelLoopEntityModifier);
		
		this.mGroundParallel.dispose();
		this.mLowFrontParallel.dispose();
		this.mHighFrontParallel.dispose();
		this.mLowBehindParallel.dispose();
		this.mHighBehindParallel.dispose();
		this.mTopGroundParallel.dispose();
		this.mAngryBirdTextSprite.dispose();
		
		this.mGroundParallel.detachSelf();
		this.mLowFrontParallel.detachSelf();
		this.mHighFrontParallel.detachSelf();
		this.mLowBehindParallel.detachSelf();
		this.mHighBehindParallel.detachSelf();
		this.mTopGroundParallel.detachSelf();
		this.mAngryBirdTextSprite.detachSelf();	
		
		this.mGroundParallel = null;
		this.mLowFrontParallel = null;
		this.mHighFrontParallel = null;
		this.mLowBehindParallel = null;
		this.mHighBehindParallel = null;
		this.mTopGroundParallel = null;
		this.mAngryBirdTextSprite = null;
		
		this.mGroundParallelLoopEntityModifier = null;
		this.mLowFrontParallelLoopEntityModifier = null;
		this.mHighFrontParallelLoopEntityModifier = null;
		this.mLowBehindParallelLoopEntityModifier = null;
		this.mHighBehindParallelLoopEntityModifier = null;
	}
}
