package com.xdpm.angrybirds.gamelevel.object.bird;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.modifier.IModifier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.ScoreConstants;
import com.xdpm.angrybirds.gamelevel.effect.SpriteScore;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public class YellowBirdObject extends SpecialBirdObject {

	public YellowBirdObject(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.YELLOW_BIRD_ID, 6, 1));
	}

	@Override
	public void doSpecialAction() {
		this.mEntity.animate(new long[] {200, 200, 0}, new int[] {3, 4, 2}, false);
		
		Vector2 velocity = this.mBody.getLinearVelocity();
		velocity.x = velocity.x * 1.25f;
		velocity.y = velocity.y * 1.25f;
		this.mBody.setLinearVelocity(velocity);
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
					new Vector2((centerX - (halfWidth - 34.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 13.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 47.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 20.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 57.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 35.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 56.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 48.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 38.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 55.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 18.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 50.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 14.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 38.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 19.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 24.0f)) / pixelToMeterRatio)
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
			this.mEntity.setCurrentTileIndex(5);
		}
	}

	@Override
	public void showRemainScore() {
		SpriteScore score = new SpriteScore(0, 0, ResourceManager.getInstance().getScoreSpriteSheetTexturePackTextureRegionLibrary().get(ScoreConstants.YELLOW_BIRD_REMAIN_SCORE_ID));
		score.getEntity().setPosition(this.mEntity.getX() - Math.abs(this.mEntity.getWidth() - score.getEntity().getWidth()) * 0.5f, 
				this.mEntity.getY() - score.getEntity().getHeight());
		score.showScore();
	}
}
