package com.xdpm.angrybirds.gamelevel.object.bird;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.modifier.IModifier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.ScoreConstants;
import com.xdpm.angrybirds.gamelevel.effect.SpriteScore;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.scene.MainGameScene;

public class BlueBirdObject extends SpecialBirdObject {

	private boolean mIsSplited = false;
	
	public BlueBirdObject(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.BLUE_BIRD_ID, 4, 1));
	}

	@Override
	public void doSpecialAction() {		
		if (!this.mIsSplited) {
			
			this.mIsSplited = true;
			
			PhysicsWorld physicsWorld = GameLevelManager.getInstance().getPhysicsWorld();
			MainGameScene mainGameScene = GameLevelManager.getInstance().getMainGameScene();
			Entity objectLayer = GameLevelManager.getInstance().getCurrentGameLevel().getObjectLayer();
			
			// Create two birds
			BlueBirdObject higherBird = new BlueBirdObject(this.mEntity.getX(), this.mEntity.getY() - this.mEntity.getHeight());
			BlueBirdObject lowerBird = new BlueBirdObject(this.mEntity.getX(), this.mEntity.getY() + this.mEntity.getHeight());

			// Set state of birds is plying
			higherBird.setBirdState(BirdConstants.STATE_IS_FLYING);
			lowerBird.setBirdState(BirdConstants.STATE_IS_FLYING);
			
			// Destroy old body
			physicsWorld.unregisterPhysicsConnector(higherBird.mPhysicsConnector);
			physicsWorld.unregisterPhysicsConnector(lowerBird.mPhysicsConnector);
			higherBird.mPhysicsConnector = null;
			lowerBird.mPhysicsConnector = null;
			
			physicsWorld.destroyBody(higherBird.mBody);
			physicsWorld.destroyBody(lowerBird.mBody);
			higherBird.mBody = null;
			lowerBird.mBody = null;
			
			// Re-create body
			lowerBird.createBody(BodyType.DynamicBody, true, true);
			higherBird.createBody(BodyType.DynamicBody, true, true);
			
			physicsWorld.registerPhysicsConnector(higherBird.getPhysicsConnector());
			physicsWorld.registerPhysicsConnector(lowerBird.getPhysicsConnector());
			
			// Calculate velocity
			Vector2 currentVelocity = this.mBody.getLinearVelocity();
			
			higherBird.getBody().setLinearVelocity(currentVelocity.x * 1.1f, currentVelocity.y);
			lowerBird.getBody().setLinearVelocity(currentVelocity.x * 0.9f, currentVelocity.y);
			
			// Attach to scene
			mainGameScene.registerUpdateHandler(higherBird);
			mainGameScene.registerUpdateHandler(lowerBird);
			
			objectLayer.attachChild(higherBird.getEntity());
			objectLayer.attachChild(lowerBird.getEntity());
		}
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
					new Vector2((centerX - (halfWidth - 11.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 4.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 22.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 2.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 29.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 10.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 30.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 11.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 25.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 28.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 14.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 29.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 6.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 23.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 6.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 12.0f)) / pixelToMeterRatio)
				}, 
				pBodyType, 
				BirdConstants.BIRD_FIXTURE_DEF);
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}

	@Override
	public void onPostSolve(float impulse) {
		super.onPostSolve(impulse);
		if (BirdConstants.STATE_IS_DAMAGED == this.mBirdState) {
			this.mEntity.setCurrentTileIndex(3);
		}
	}

	@Override
	public void showRemainScore() {
		SpriteScore score = new SpriteScore(0, 0, ResourceManager.getInstance().getScoreSpriteSheetTexturePackTextureRegionLibrary().get(ScoreConstants.BLUE_BIRD_REMAIN_SCORE_ID));
		score.getEntity().setPosition(this.mEntity.getX() - Math.abs(this.mEntity.getWidth() - score.getEntity().getWidth()) * 0.5f, 
				this.mEntity.getY() - score.getEntity().getHeight());
		score.showScore();
	}
}
