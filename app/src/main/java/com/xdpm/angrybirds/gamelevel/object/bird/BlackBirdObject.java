package com.xdpm.angrybirds.gamelevel.object.bird;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.renderscript.Sampler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.ScoreConstants;
import com.xdpm.angrybirds.gamelevel.GameLevel;
import com.xdpm.angrybirds.gamelevel.effect.SpriteScore;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject.IOnBirdDestroyedListener;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.scene.MainGameScene;
import com.xdpm.angrybirds.scene.layer.GameLevelFailedLayer;

public class BlackBirdObject extends SpecialBirdObject {

	private boolean mIsHasMakeExplosion = false;
	
	public BlackBirdObject(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.BLACK_BIRD_ID, 7, 1));
	}

	@Override
	public void doSpecialAction() {
		if (!this.mIsHasMakeExplosion) {
			this.mIsHasMakeExplosion = true;
			this.makeExplosion();
		}
	}

	@Override
	protected void tallJump() {
	
	}

	@Override
	protected void shortJump() {
		
	}
	
	@Override
	public void createBody(BodyType pBodyType, boolean pUpdatePosition,
			boolean pUpdateRotation) {
		float halfWidth = this.mEntity.getWidth() * 0.5f;
		float halfHeight = this.mEntity.getHeight() * 0.5f;
		float centerX = 0;
		float centerY = 0;
		float pixelToMeterRatio = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		
		this.mBody = PhysicsFactory.createPolygonBody(GameLevelManager.getInstance().getPhysicsWorld(), 
				this.mEntity, 
				new Vector2[] 
				{
					new Vector2((centerX - (halfWidth - 30.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 16.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 54.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 25.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 64.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 48.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 58.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 72.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 35.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 82.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 9.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 74.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - (-2.0f))) / pixelToMeterRatio, (centerY - (halfHeight - 52.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 6.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 27.0f)) / pixelToMeterRatio)
				}, 
				pBodyType, 
				BirdConstants.BIRD_FIXTURE_DEF);
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}
	
	@Override
	public void showRemainScore() {
		SpriteScore score = new SpriteScore(0, 0, ResourceManager.getInstance().getScoreSpriteSheetTexturePackTextureRegionLibrary().get(ScoreConstants.BLACK_BIRD_REMAIN_SCORE_ID));
		score.getEntity().setPosition(this.mEntity.getX() - Math.abs(this.mEntity.getWidth() - score.getEntity().getWidth()) * 0.5f, 
				this.mEntity.getY() - score.getEntity().getHeight());
		score.showScore();
	}
	
	@Override
	public void onDestroy() {
		final PhysicsWorld physicsWorld = GameLevelManager.getInstance().getPhysicsWorld();
		
		float halfWidth = this.mEntity.getWidth() * 0.5f;
		float halfHeight = this.mEntity.getHeight() * 0.5f;
		
		float centerX = this.mEntity.getX() + halfWidth;
		float centerY = this.mEntity.getY() + halfHeight;
		
		ExplosionSmash[] smashs = new ExplosionSmash[8];
		
		smashs[0] = new ExplosionSmash(centerX - (halfWidth - 30.0f), centerY - (halfHeight - 16.0f));
		smashs[1] = new ExplosionSmash(centerX - (halfWidth - 54.0f), centerY - (halfHeight - 25.0f));
		smashs[2] = new ExplosionSmash(centerX - (halfWidth - 64.0f), centerY - (halfHeight - 48.0f));
		smashs[3] = new ExplosionSmash(centerX - (halfWidth - 58.0f), centerY - (halfHeight - 72.0f));
		smashs[4] = new ExplosionSmash(centerX - (halfWidth - 35.0f), centerY - (halfHeight - 82.0f));
		smashs[5] = new ExplosionSmash(centerX - (halfWidth - 9.0f), centerY - (halfHeight - 74.0f));
		smashs[6] = new ExplosionSmash(centerX - (halfWidth - (-2.0f)), centerY - (halfHeight - 52.0f));
		smashs[7] = new ExplosionSmash(centerX - (halfWidth - 6.0f), centerY - (halfHeight - 27.0f));
		
		float velocity = 1000;
		
		for (int i = 0; i < smashs.length; i++) {
			smashs[i].setVelocity(velocity * MathUtils.atan2(smashs[i].getEntity().getY() - this.mEntity.getY(), smashs[i].getEntity().getX() - this.mEntity.getX()));
		}
		
		for (int i = 0; i < smashs.length; i++) {
			physicsWorld.registerPhysicsConnector(smashs[i].getPhysicsConnector());
			smashs[i].boom();
		}
		
		// Bad, must edit
		
		GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(pTimerHandler);
				if (null != mBirdDestroyedListeners) {
					for (IOnBirdDestroyedListener listener : mBirdDestroyedListeners) {
						listener.onBirdDestroyed(BlackBirdObject.this);
					}
				}
			}
		}));
	}
	
	private void makeExplosion() {	
		this.mEntity.animate(new long[] {200, 200, 200}, new int[] {4, 5, 6}, false);
		
		GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(0.6f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().getCamera().gotoBase();
				destroy(true);
			}
		}));	
	}
	
	@Override
	public void onPostSolve(float impulse) {
		if (BirdConstants.STATE_IS_FLYING == this.mBirdState) {
			this.mBirdState = BirdConstants.STATE_IS_DAMAGED;
			ResourceManager.getInstance().getCamera().gotoBase();
			if (!this.mIsHasMakeExplosion) {
				this.mIsHasMakeExplosion = true;
				this.makeExplosion();
			}
		}
	}
}
