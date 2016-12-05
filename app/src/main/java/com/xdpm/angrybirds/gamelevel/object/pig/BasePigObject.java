package com.xdpm.angrybirds.gamelevel.object.pig;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.common.PigConstants;
import com.xdpm.angrybirds.common.ScoreConstants;
import com.xdpm.angrybirds.gamelevel.effect.SpriteScore;
import com.xdpm.angrybirds.gamelevel.object.PhysicsObject;
import com.xdpm.angrybirds.manager.EffectManager;
import com.xdpm.angrybirds.manager.GameScoreManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public abstract class BasePigObject extends PhysicsObject<AnimatedSprite> {

	private List<IOnPigDestroyedListener> mPigDestroyedListeners;
	private long mLastAnimationDuration;
	private char mLastAction = PigConstants.ACTION_NORMAL;
	
	protected char mState = PigConstants.NORMAL; 
	
	public BasePigObject(float pX, float pY, float pRotation, ITiledTextureRegion pTiledTextureRegion) {
		super(pX, pY);
		this.mEntity = new AnimatedSprite(pX, pY, pTiledTextureRegion, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		this.mEntity.setRotation(pRotation);
		this.mEntity.setUserData(this);
		
		this.createBody(BodyType.DynamicBody, true, true);
		this.mPigDestroyedListeners = new ArrayList<BasePigObject.IOnPigDestroyedListener>();
	}
	
	public void registryPigDestroyedListener(IOnPigDestroyedListener pPigDestroyedListener) {
		this.mPigDestroyedListeners.add(pPigDestroyedListener);
	}
	
	public void unregistryPigDestroyedListener(IOnPigDestroyedListener pPigDestroyedListener) {
		if (this.mPigDestroyedListeners.contains(pPigDestroyedListener)) {
			this.mPigDestroyedListeners.remove(pPigDestroyedListener);
		}
	}
	
	public void clearPigDestroyedListener() {
		this.mPigDestroyedListeners.clear();
	}
	
	@Override
	public void onAreaTouched(TouchEvent pSceneTouchEvent) {
		
	}

	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		
	}
	
	@Override
	public void onDestroy() {
		SpriteScore score = new SpriteScore(0, 0, ResourceManager.getInstance().getScoreSpriteSheetTexturePackTextureRegionLibrary().get(ScoreConstants.PIG_DESTROYED_SCORE_ID));
		score.getEntity().setPosition(this.mEntity.getX() - Math.abs(this.mEntity.getWidth() - score.getEntity().getWidth()) * 0.5f, 
				this.mEntity.getY() - score.getEntity().getHeight());
		score.showScore();
		EffectManager.showPigDestroyEffect(this);
		GameScoreManager.getInstance().increaseScoreForDestroyPig();
	}
	
	@Override
	public void onUpdate(float timeElapsed) {
		if (this.mLastAnimationDuration <= 0) {
			this.doRandomAction();
		} else {
			this.mLastAnimationDuration -= 1.0 / 60;
		}
		
		if (this.mEntity.getX() <= 0 - this.mEntity.getWidth() || this.mEntity.getX() > Constants.CAMERA_BOUND_WIDTH) {
			
			if (null != this.mPigDestroyedListeners) {
				for (IOnPigDestroyedListener listener : this.mPigDestroyedListeners) {
					listener.onPigDestroyed(this);
				}
			}
			
			this.destroy(false);
		}
	}
	
	@Override
	public void onBeginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostSolve(float impulse) {
		if (PigConstants.NORMAL == this.mState) {
			if (impulse >= (PigConstants.DAMAGE_2_IMPULSE - 0)) {
				this.mState = PigConstants.DAMAGE_2;
			} else if (impulse >= (PigConstants.DAMAGE_1_IMPULSE - 0)) {
				this.mState = PigConstants.DAMAGE_1;
			}
		} else if (PigConstants.DAMAGE_1 == this.mState) {
			if (impulse >= (PigConstants.DAMAGE_2_IMPULSE - PigConstants.DAMAGE_1_IMPULSE)) {
				this.mState = PigConstants.DAMAGE_2;
			}
		} else if (PigConstants.DAMAGE_2 == this.mState) {
			if (null != this.mPigDestroyedListeners) {
				for (IOnPigDestroyedListener listener : this.mPigDestroyedListeners) {
					listener.onPigDestroyed(this);
				}
			}
			this.destroy(true);
		}
	}

	@Override
	public void onPreSolve(Contact contact, Manifold manifold) {

	}
	
	// =======================================================
	// Interfaces
	// =======================================================
	private void doRandomAction() {
		int doThisAction = MathUtils.random(0, 99);
		if (doThisAction % 20 != 0) {
			return;
		}
		
		char action = (char) MathUtils.random(PigConstants.ACTION_NORMAL, PigConstants.ACTION_LAUGH);
		
		if (this.mLastAction == action) {
			this.mEntity.setCurrentTileIndex(this.mState * 3);
			return;
		}
		
		this.mLastAction = action;
		
		switch (action) {
		case PigConstants.ACTION_NORMAL:
			this.mEntity.setCurrentTileIndex(this.mState * 3);
			break;
		case PigConstants.ACTION_WINK:
			this.mEntity.animate(new long[] {PigConstants.ACTION_WINK_ANIMATE_DURATION, PigConstants.ACTION_NORMAL_ANIMATE_DURATION}, 
								new int[] {this.mState * 3 + 1, this.mState * 3}, false);
			this.mLastAnimationDuration += (PigConstants.ACTION_WINK_ANIMATE_DURATION + PigConstants.ACTION_NORMAL_ANIMATE_DURATION) / 1000;
			break;
		case PigConstants.ACTION_LAUGH:
			this.mEntity.animate(new long[] {PigConstants.ACTION_LAUGH_ANIMATE_DURATION, PigConstants.ACTION_NORMAL_ANIMATE_DURATION}, 
								new int[] {this.mState * 3 + 2, this.mState * 3}, false);
			this.mLastAnimationDuration += (PigConstants.ACTION_LAUGH_ANIMATE_DURATION + PigConstants.ACTION_NORMAL_ANIMATE_DURATION) / 1000;
			
			break;
		default:
			break;
		}
	}
		
	// =======================================================
	// Interfaces
	// =======================================================

	public static interface IOnPigDestroyedListener {
		public void onPigDestroyed(BasePigObject pBasePigObject);
	}
}
