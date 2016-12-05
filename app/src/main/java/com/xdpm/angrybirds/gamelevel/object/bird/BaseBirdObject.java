package com.xdpm.angrybirds.gamelevel.object.bird;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.JumpModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import android.util.FloatMath;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.gamelevel.object.PhysicsObject;
import com.xdpm.angrybirds.manager.EffectManager;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SfxManager;

/**
 * All BirdObject in game Should extends this class
 * 
 * @author Nguyen Anh Tuan
 * 
 */
public abstract class BaseBirdObject extends PhysicsObject<AnimatedSprite> {
	
	// =================================================================
	// Properties
	// =================================================================
	
	protected char mBirdState = BirdConstants.STATE_ON_GROUND;
	
	protected float mLastAnimationDuration = 0;
	protected float mLastDrawingCloud = 0;
	
	protected char mLastAction = BirdConstants.ACTION_NORMAL;
	protected char mLastActionJump = BirdConstants.ACTION_NORMAL;
	
	protected IOnBirdChangePositionListener mBirdChangePositionListener;
	protected IOnBirdFlyingListener mBirdFlyingListener;
	protected List<IOnBirdShotListener> mBirdShotListeners;
	protected List<IOnBirdDestroyedListener> mBirdDestroyedListeners;
	
	// =================================================================
	// Constructor
	// =================================================================

	public BaseBirdObject(float pX, float pY, ITiledTextureRegion pTiledTextureRegion) {
		super(pX, pY);
		this.mEntity = new AnimatedSprite(pX, pY, pTiledTextureRegion,
				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		this.mEntity.setUserData(this);
		this.createBody(BodyType.StaticBody, false, false);
		
		this.mBirdDestroyedListeners = new ArrayList<BaseBirdObject.IOnBirdDestroyedListener>();
		this.mBirdShotListeners = new ArrayList<BaseBirdObject.IOnBirdShotListener>();
	}

	// =================================================================
	// Getters & Setters
	// =================================================================
	
	public char getBirdState() {
		return this.mBirdState;
	}
	
	public void setBirdState(char pBirdState) {
		this.mBirdState = pBirdState;
	}
	
	public void setBirdChangedPositionListener(IOnBirdChangePositionListener pBirdChangePositionListener) {
		this.mBirdChangePositionListener = pBirdChangePositionListener;
	}
		
	public void setBirdFlyingListener(IOnBirdFlyingListener pBirdFlyingListener) {
		this.mBirdFlyingListener = pBirdFlyingListener;
	}
	
	
	public void registryBirdShotListener(IOnBirdShotListener pBirdShotListener) {
		this.mBirdShotListeners.add(pBirdShotListener);
	}
	
	public void unregistryBirdShotListener(IOnBirdShotListener pBirdShotListener) {
		if (this.mBirdShotListeners.contains(pBirdShotListener)) {
			this.mBirdShotListeners.remove(pBirdShotListener);
		}
	}
	
	public void clearBirdShotListener() {
		this.mBirdShotListeners.clear();
	}
	
	public void registryBirdDestroyedListener(IOnBirdDestroyedListener pBirdDestroyedListener) {
		this.mBirdDestroyedListeners.add(pBirdDestroyedListener);
	}
	
	public void unregistryBirdDestroyedListener(IOnBirdDestroyedListener pBirdDestroyedListener) {
		if (this.mBirdDestroyedListeners.contains(pBirdDestroyedListener)) {
			this.mBirdDestroyedListeners.remove(pBirdDestroyedListener);
		}
	}
	
	public void clearBirdDestroyedListener() {
		this.mBirdDestroyedListeners.clear();
	}
	
	// =================================================================
	// Methods for/from SuperClass/Interfaces
	// =================================================================
	
	public abstract void showRemainScore();
	
	@Override
	public void onAreaTouched(TouchEvent pSceneTouchEvent) {
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			this.grab();
			break;
		case TouchEvent.ACTION_UP: case TouchEvent.ACTION_CANCEL:
			this.shoot();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_MOVE:
			Vector2 position = Vector2Pool.obtain(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			this.dragTo(position);
			Vector2Pool.recycle(position);
			break;
		case TouchEvent.ACTION_UP: case TouchEvent.ACTION_CANCEL:
			this.shoot();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Thread to update status of BirdObject
	 */
	public void onUpdate(float timeElapsed) {
		super.onUpdate(timeElapsed);
		switch (this.mBirdState) {
		
		case BirdConstants.STATE_ON_GROUND: 
		case BirdConstants.STATE_IS_READY:
		case BirdConstants.STATE_IS_GRABBED:
			if (this.mLastAnimationDuration <= 0) {
				this.doRandomAction();
			} else {
				this.mLastAnimationDuration -= 1.0f / 60; // 1/30 frame per second
			}
			break;
		case BirdConstants.STATE_IS_FLYING:
			if (null != this.mBirdFlyingListener) {
				this.mBirdFlyingListener.onBirdFlying(this);
			}
			
			if (this.mEntity.getX() <= 0 - this.mEntity.getWidth() || this.mEntity.getX() > Constants.CAMERA_BOUND_WIDTH) {
				ResourceManager.getInstance().getCamera().gotoBase();
				
				if (null != this.mBirdDestroyedListeners) {
					for (IOnBirdDestroyedListener listener : this.mBirdDestroyedListeners) {
						listener.onBirdDestroyed(this);
					}
				}
				
				this.destroy(false);
			}
			
			if (this.mLastDrawingCloud <= 0) {
				EffectManager.showBirdFlyingEffect(this);
				this.mLastDrawingCloud += 0.05f;
			} else {
				this.mLastDrawingCloud -= 1.0f / 60;
			}
			break;
		/*case BirdConstants.STATE_IS_DAMAGED:
			if (!this.mIsDestroyed) {
				ResourceManager.getInstance().getCamera().gotoBase();
				
				if (null != mBirdDestroyedListeners) {
					for (IOnBirdDestroyedListener listener : mBirdDestroyedListeners) {
						listener.onBirdDestroyed(BaseBirdObject.this);
					}
				}
				
				GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(5f, new ITimerCallback() {
					
					@Override
					public void onTimePassed(TimerHandler pTimerHanlder) {
						GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(pTimerHanlder);
						destroy(true);						
					}
				}));
			}
			break;*/
		default:
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		EffectManager.showBirdDestroyEffect(this);
	}
	
	@Override
	public void reset() {
		
	}

	@Override
	public void onBeginContact(Contact contact) {
		
	}

	@Override
	public void onEndContact(Contact contact) {
		
	}

	@Override
	public void onPostSolve(float impulse) {
		if (BirdConstants.STATE_IS_FLYING == this.mBirdState) {
			this.mBirdState = BirdConstants.STATE_IS_DAMAGED;
			if (impulse * this.mBody.getMass() >= BirdConstants.DAMAGE_IMPULSE) {
				EffectManager.showBirdContactEffect(this);
			}
			
			ResourceManager.getInstance().getCamera().gotoBase();
			
			GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(5f, new ITimerCallback() {
				
				@Override
				public void onTimePassed(TimerHandler pTimerHanlder) {
					GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(pTimerHanlder);
					destroy(true);						
				}
			}));
			
			if (null != mBirdDestroyedListeners) {
				for (IOnBirdDestroyedListener listener : mBirdDestroyedListeners) {
					listener.onBirdDestroyed(BaseBirdObject.this);
				}
			}
		}
	}

	@Override
	public void onPreSolve(Contact contact, Manifold manifold) {
		
	}
	
	// =================================================================
	// Methods 
	// =================================================================
	
		// =====================================
		// Private Methods
		// =====================================
	
	/**
	 * Calculate distance from DesiredXY to another Position
	 * @param pPosition Position want to calculate distance to
	 * @return Distance from Center of DesiredXY to pPosition
	 */
	
	private float distanceFromCenterDesiredXYTo(Vector2 pPosition) {
		return MathUtils.distance(this.mDesiredXY.x + this.mEntity.getWidth()/ 2, this.mDesiredXY.y + this.mEntity.getHeight() / 2, 
									pPosition.x, pPosition.y);
	}
	
	private float distanceToCenterDesiredXY() {
		return MathUtils.distance(this.mDesiredXY.x + this.mEntity.getWidth() / 2, this.mDesiredXY.y + this.mEntity.getHeight() / 2,
									this.mEntity.getX() + this.mEntity.getWidth() / 2, this.mEntity.getY() + this.mEntity.getHeight() / 2);
	}
	
	
	
	/**
	 * Grab the bird
	 */
	private void grab() {	
		if (BirdConstants.STATE_IS_READY == this.mBirdState) {
			this.mBirdState = BirdConstants.STATE_IS_GRABBED;
		}
	}

	/**
	 * Drag the BirdObject to a Position. Limited the delta length from current position to DesiredXY.
	 * @param pPosition Position that want to drag to.
	 */
	private void dragTo(Vector2 pPosition) {
		
		if (BirdConstants.STATE_IS_GRABBED == this.mBirdState) {
			float distance = Math.round(this.distanceFromCenterDesiredXYTo(pPosition));
			
			if (distance > Constants.ARBALEST_MAX_DELTA_LENGTH) {
				distance = Constants.ARBALEST_MAX_DELTA_LENGTH;
			}
			
			float angle = (float) Math.atan2(pPosition.y - (this.mDesiredXY.y + this.mEntity.getHeight() / 2), pPosition.x - (this.mDesiredXY.x + this.mEntity.getWidth() / 2));
			this.mEntity.setX(distance * FloatMath.cos(angle) + this.mDesiredXY.x);
			this.mEntity.setY(distance * FloatMath.sin(angle) + this.mDesiredXY.y);
			
			if (null != this.mBirdChangePositionListener) {
				this.mBirdChangePositionListener.onBirdChangedPosition(this.mEntity.getX(), this.mEntity.getY());
			}
		}
	}
	
	/**
	 * Move the BirdObject to wherever.
	 * @param pPosition Position want to move to.
	 */
	private void moveTo(Vector2 pPosition) {
		this.mEntity.setPosition(pPosition.x, pPosition.y);
		
		if (null != this.mBirdChangePositionListener) {
			this.mBirdChangePositionListener.onBirdChangedPosition(this.mEntity.getX(), this.mEntity.getY());
		}
	}
	
	/**
	 * Shot the BirdObject.
	 */
	private void shoot() {
		
		if (BirdConstants.STATE_IS_GRABBED == this.mBirdState) {
			
			float distance = Math.round(this.distanceToCenterDesiredXY());
			
			if (distance < Constants.ARBALEST_MIN_DELTA_LENGTH) {
				this.moveTo(this.mDesiredXY);
				this.mBirdState = BirdConstants.STATE_IS_READY;
				return;
			}
			
			EffectManager.hideBirdFlyingEffect();
			
			float angle = (float) Math.atan2(this.mEntity.getY() - this.mDesiredXY.y, 
											this.mEntity.getX() - this.mDesiredXY.x);		
			
			float force = BirdConstants.BIRD_FORCE_MULTIFLIER * distance;
			
			this.createBody(BodyType.DynamicBody, true, true);
			GameLevelManager.getInstance().getPhysicsWorld().registerPhysicsConnector(this.mPhysicsConnector);
			
			Vector2 f = Vector2Pool.obtain(- force * FloatMath.cos(angle) * this.mBody.getMass(), - force * FloatMath.sin(angle) * this.mBody.getMass());
			Vector2 cp = Vector2Pool.obtain(this.mBody.getWorldCenter());
			this.mBody.applyForce(f, cp);
			
			this.mBirdState = BirdConstants.STATE_IS_FLYING;
			this.mEntity.setCurrentTileIndex(BirdConstants.TILED_LAUGH);
			
			Vector2Pool.recycle(f);
			Vector2Pool.recycle(cp);
			
			if (null != this.mBirdShotListeners) {
				for (IOnBirdShotListener listener : this.mBirdShotListeners) {
					listener.onBirdShot();
				}
			}
			
			ResourceManager.getInstance().getCamera().setChasedBird(this);
		}
	}
	
	protected void tallJump() {
		if (BirdConstants.ACTION_TALL_JUMP == this.mLastActionJump) {
			return;
		}
		
		this.mLastActionJump = BirdConstants.ACTION_TALL_JUMP;
		
		boolean clockWiseRotation = (MathUtils.random(0, 1) == 1);
		if (clockWiseRotation) {
			
			SequenceEntityModifier sequenceEntityModifier = new SequenceEntityModifier(
					new MoveYModifier(300.0f / 1000, this.mDesiredXY.y, this.mDesiredXY.y - this.mEntity.getHeight() * 0.5f),
					new RotationModifier(300.0f / 1000, 0, 360),
					new MoveYModifier(300.0f / 1000, this.mDesiredXY.y - this.mEntity.getHeight() * 0.5f, this.mDesiredXY.y),
					new MoveYModifier(50.0f / 1000, this.mDesiredXY.y, this.mDesiredXY.y - this.mEntity.getHeight() * 0.125f),
					new MoveYModifier(50.0f / 1000, this.mDesiredXY.y - this.mEntity.getHeight() * 0.125f, this.mDesiredXY.y)
					);
			sequenceEntityModifier.setAutoUnregisterWhenFinished(true);	
			
			this.mEntity.registerEntityModifier(sequenceEntityModifier);
			
		} else {
			
			SequenceEntityModifier sequenceEntityModifier = new SequenceEntityModifier(
					new MoveYModifier(300.0f / 1000, this.mDesiredXY.y, this.mDesiredXY.y - this.mEntity.getHeight() * 0.5f),
					new RotationModifier(300.0f / 1000, 0, - 360),
					new MoveYModifier(300.0f / 1000, this.mDesiredXY.y - this.mEntity.getHeight() * 0.5f, this.mDesiredXY.y),
					new MoveYModifier(50.0f / 1000, this.mDesiredXY.y, this.mDesiredXY.y - this.mEntity.getHeight() * 0.125f),
					new MoveYModifier(50.0f / 1000, this.mDesiredXY.y - this.mEntity.getHeight() * 0.125f, this.mDesiredXY.y)
					);
			sequenceEntityModifier.setAutoUnregisterWhenFinished(true);
			
			this.mEntity.registerEntityModifier(sequenceEntityModifier);
		}
		
		this.mLastAnimationDuration += 1000.0f / 1000;
	}
	
	protected void shortJump() {
		if (BirdConstants.ACTION_SHORT_JUMP == this.mLastActionJump) {
			return;
		}
		this.mLastActionJump = BirdConstants.ACTION_SHORT_JUMP;
		
		SequenceEntityModifier sequenceEntityModifier = new SequenceEntityModifier(
				new MoveYModifier(200.0f / 1000, this.mDesiredXY.y, this.mDesiredXY.y - this.mEntity.getHeight() * 0.25f),
				new MoveYModifier(200.0f / 1000, this.mDesiredXY.y - this.mEntity.getHeight() * 0.25f, this.mDesiredXY.y),
				new MoveYModifier(50.0f / 1000, this.mDesiredXY.y, this.mDesiredXY.y - this.mEntity.getHeight() * 0.125f),
				new MoveYModifier(50.0f / 1000, this.mDesiredXY.y - this.mEntity.getHeight() * 0.125f, this.mDesiredXY.y)
				);
		sequenceEntityModifier.setAutoUnregisterWhenFinished(true);
		
		this.mEntity.registerEntityModifier(sequenceEntityModifier);
		
		this.mLastAnimationDuration += 500.0f / 1000;
	}

	/**
	 * Do some random angry action
	 */
	private void doRandomAction() {
		int doThisAction = MathUtils.random(0, 99);
		if (doThisAction % 20 != 0) {
			return;
		}
		
		char action = (char) MathUtils.random(BirdConstants.ACTION_NORMAL, BirdConstants.ACTION_SHORT_JUMP);
		
		if (this.mLastAction == action) {
			this.mEntity.setCurrentTileIndex(BirdConstants.TILED_NORMAL);
			return;
		}
		
		this.mLastAction = action;
		
		switch (action) {
		case BirdConstants.ACTION_NORMAL:
			this.mEntity.setCurrentTileIndex(BirdConstants.TILED_NORMAL);
			break;
		case BirdConstants.ACTION_WINK:
			this.mEntity.animate(new long[] {BirdConstants.ACTION_WINK_ANIMATE_DURATION, BirdConstants.ACTION_NORMAL_ANIMATE_DURATION}, 
								new int[] {BirdConstants.TILED_WINK, BirdConstants.TILED_NORMAL}, false);
			this.mLastAnimationDuration += (BirdConstants.ACTION_WINK_ANIMATE_DURATION + BirdConstants.ACTION_NORMAL_ANIMATE_DURATION) / 1000;
			break;
		case BirdConstants.ACTION_LAUGH:
			this.mEntity.animate(new long[] {BirdConstants.ACTION_LAUGH_ANIMATE_DURATION, BirdConstants.ACTION_NORMAL_ANIMATE_DURATION}, 
								new int[] {BirdConstants.TILED_LAUGH, BirdConstants.TILED_NORMAL}, false);
			this.mLastAnimationDuration += (BirdConstants.ACTION_LAUGH_ANIMATE_DURATION + BirdConstants.ACTION_NORMAL_ANIMATE_DURATION) / 1000;
			
			SfxManager.getInstance().playBirdMisc();
			
			break;
		case BirdConstants.ACTION_TALL_JUMP:
			if (BirdConstants.STATE_IS_READY == this.mBirdState 
			|| BirdConstants.STATE_IS_GRABBED == this.mBirdState) {
				return;
			}
			this.tallJump();
			this.mLastAnimationDuration += BirdConstants.ACTION_JUMP_DURATION / 1000;
			break;
		case BirdConstants.ACTION_SHORT_JUMP:
			if (BirdConstants.STATE_IS_READY == this.mBirdState 
			|| BirdConstants.STATE_IS_GRABBED == this.mBirdState) {
				return;
			}
			this.shortJump();
			this.mLastAnimationDuration += BirdConstants.ACTION_JUMP_DURATION / 1000;
			break;
		default:
			break;
		}
	}
		// =====================================
		// Public Methods
		// =====================================
	
	public void jumpToArbalest(Vector2 pArbalestCenterPosition) {
		if (BirdConstants.STATE_ON_GROUND == this.mBirdState) {
			
			this.mEntity.clearEntityModifiers();
			this.mEntity.setRotation(0);
			this.moveTo(this.mDesiredXY);
			
			JumpModifier jumpModifier = new JumpModifier(BirdConstants.ACTION_JUMP_DURATION / 1000, this.mDesiredXY.x, pArbalestCenterPosition.x - this.mEntity.getWidth() * 0.25f, 
					this.mDesiredXY.y, pArbalestCenterPosition.y - this.mEntity.getHeight() / 2, Math.abs(pArbalestCenterPosition.y - this.mDesiredXY.y + this.mEntity.getHeight()),
					new IEntityModifier.IEntityModifierListener() {
						
						@Override
						public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1) {
							GameLevelManager.getInstance().getPhysicsWorld().unregisterPhysicsConnector(mPhysicsConnector);
							GameLevelManager.getInstance().getPhysicsWorld().destroyBody(mBody);
							mBody = null;
							mPhysicsConnector = null;
						}
						
						@Override
						public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1) {
							GameLevelManager.getInstance().getMainGameScene().registerTouchArea(mEntity);
						}
					});
			jumpModifier.setAutoUnregisterWhenFinished(true);
			
			this.mEntity.registerEntityModifier(jumpModifier);
			this.mBirdState = BirdConstants.STATE_IS_READY;
			this.mDesiredXY.x = pArbalestCenterPosition.x - this.mEntity.getWidth() * 0.25f;
			this.mDesiredXY.y = pArbalestCenterPosition.y - this.mEntity.getHeight() * 0.5f;
		}
	}
	
	
	// =================================================================
	// Interfaces
	// =================================================================
	
	public static interface IOnBirdChangePositionListener {
		public void onBirdChangedPosition(float pX, float pY);
	}
	
	public static interface IOnBirdShotListener {
		public void onBirdShot();
	}
	
	public static interface IOnBirdFlyingListener {
		public void onBirdFlying(BaseBirdObject pBaseBirdObject);
	}
	
	public static interface IOnBirdDestroyedListener {
		public void onBirdDestroyed(BaseBirdObject pBaseBirdObject);
	}
}
