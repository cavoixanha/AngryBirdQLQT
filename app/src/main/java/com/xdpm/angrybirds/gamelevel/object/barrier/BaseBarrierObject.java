package com.xdpm.angrybirds.gamelevel.object.barrier;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.BarrierConstants;
import com.xdpm.angrybirds.common.ScoreConstants;
import com.xdpm.angrybirds.gamelevel.effect.TextScore;
import com.xdpm.angrybirds.gamelevel.object.PhysicsObject;
import com.xdpm.angrybirds.manager.GameScoreManager;
import com.xdpm.angrybirds.manager.ResourceManager;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public abstract class BaseBarrierObject extends PhysicsObject<TiledSprite> {
	
	// =======================================================
	// Properties
	// =======================================================
	protected float mTextureRotation;
	protected char mState = BarrierConstants.NORMAL;
	
	private List<IOnBarrierDestroyedListener> mBarrierDestroyedListeners;
	
	// =======================================================
	// Constructors
	// =======================================================
	
	public BaseBarrierObject(float pX, float pY, float pTextureRotation) {
		super(pX, pY);
		this.mTextureRotation = pTextureRotation;
		this.mBarrierDestroyedListeners = new ArrayList<BaseBarrierObject.IOnBarrierDestroyedListener>();
	}
	

	// =======================================================
	// Methods
	// =======================================================
	
	public void setBarrierData(ITiledTextureRegion pTiledTextureRegion) {
		this.mEntity = new TiledSprite(this.mDesiredXY.x, this.mDesiredXY.y, pTiledTextureRegion, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		this.mEntity.setRotation(this.mTextureRotation);
		this.mEntity.setUserData(this);
		
		this.createBody(BodyType.DynamicBody, true, true);
	}
	
	public void registryBarrierDestroyedListener(IOnBarrierDestroyedListener pBarrierDestroyedListener) {
		this.mBarrierDestroyedListeners.add(pBarrierDestroyedListener);
	}
	
	public void unregistryBarrierDestroyedListener(IOnBarrierDestroyedListener pBarrierDestroyedListener) {
		if (this.mBarrierDestroyedListeners.contains(pBarrierDestroyedListener)) {
			this.mBarrierDestroyedListeners.remove(pBarrierDestroyedListener);
		}
	}
	
	public void clearBarrierDestroyedListener() {
		this.mBarrierDestroyedListeners.clear();
	}
	
	// =======================================================
	// Methods from/for SuperClass/Interface
	// =======================================================

	@Override
	public void onAreaTouched(TouchEvent pSceneTouchEvent) {
		
	}

	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		
	}
	
	@Override
	public void onBeginContact(Contact contact) {
		
	}

	@Override
	public void onEndContact(Contact contact) {
		
	}

	@Override
	public void onPostSolve(float impulse) {
		if (BarrierConstants.NORMAL == this.mState) {
			if (impulse >= (BarrierConstants.DAMAGE_3_IMPULSE - 0)) {
				this.mState = BarrierConstants.DAMAGE_3;
				this.mEntity.setCurrentTileIndex(BarrierConstants.DAMAGE_3_TILED_INDEX);
			} else if (impulse >= (BarrierConstants.DAMAGE_2_IMPULSE - 0)) {
				this.mState = BarrierConstants.DAMAGE_2;
				this.mEntity.setCurrentTileIndex(BarrierConstants.DAMAGE_2_TILED_INDEX);
			} else if (impulse >= (BarrierConstants.DAMAGE_1_IMPULSE - 0) * this.mBody.getMass()) {
				this.mState = BarrierConstants.DAMAGE_1;
				this.mEntity.setCurrentTileIndex(BarrierConstants.DAMAGE_1_TILED_INDEX);
			}
			
			int score = GameScoreManager.getInstance().increaseScoreForDamage(impulse);
			if (score > 0) {
				TextScore textScore = new TextScore(this.mEntity.getX() + this.mEntity.getWidth() * 0.5f, 
						this.mEntity.getY() + this.mEntity.getHeight() * 0.5f, Integer.toString(score));
				textScore.showScore();
			}
			
		} else if (BarrierConstants.DAMAGE_1 == this.mState) {
			if (impulse >= (BarrierConstants.DAMAGE_3_IMPULSE - BarrierConstants.DAMAGE_1_IMPULSE) * this.mBody.getMass()) {
				this.mState = BarrierConstants.DAMAGE_3;
				this.mEntity.setCurrentTileIndex(BarrierConstants.DAMAGE_3_TILED_INDEX);
			} else if (impulse >= (BarrierConstants.DAMAGE_2_IMPULSE - BarrierConstants.DAMAGE_1_IMPULSE)) {
				this.mState = BarrierConstants.DAMAGE_2;
				this.mEntity.setCurrentTileIndex(BarrierConstants.DAMAGE_2_TILED_INDEX);
			}
			
			int score = GameScoreManager.getInstance().increaseScoreForDamage(impulse);
			if (score > 0) {
				TextScore textScore = new TextScore(this.mEntity.getX() + this.mEntity.getWidth() * 0.5f, 
						this.mEntity.getY() + this.mEntity.getHeight() * 0.5f, Integer.toString(score));
				textScore.showScore();
			}
		} else if (BarrierConstants.DAMAGE_2 == this.mState) {
			if (impulse >= (BarrierConstants.DAMAGE_3_IMPULSE - BarrierConstants.DAMAGE_2_IMPULSE) * this.mBody.getMass()) {
				this.mState = BarrierConstants.DAMAGE_3;
				this.mEntity.setCurrentTileIndex(BarrierConstants.DAMAGE_3_TILED_INDEX);
			}
			
			int score = GameScoreManager.getInstance().increaseScoreForDamage(impulse);
			if (score > 0) {
				TextScore textScore = new TextScore(this.mEntity.getX() + this.mEntity.getWidth() * 0.5f, 
						this.mEntity.getY() + this.mEntity.getHeight() * 0.5f, Integer.toString(score));
				textScore.showScore();
			}
		} else if (BarrierConstants.DAMAGE_3 == this.mState) {
			if (null != this.mBarrierDestroyedListeners) {
				for (IOnBarrierDestroyedListener listener : this.mBarrierDestroyedListeners) {
					listener.onBarrierDestroyed(this);
				}
			}
			
			int score = GameScoreManager.getInstance().increaseScoreForDamage(impulse);
			if (score > 0) {
				TextScore textScore = new TextScore(this.mEntity.getX() + this.mEntity.getWidth() * 0.5f, 
						this.mEntity.getY() + this.mEntity.getHeight() * 0.5f, Integer.toString(score));
				textScore.showScore();
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
	
	public static interface IOnBarrierDestroyedListener {
		public void onBarrierDestroyed(BaseBarrierObject pBaseBarrierObject);
	}

}
