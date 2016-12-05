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
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.ScoreConstants;
import com.xdpm.angrybirds.gamelevel.effect.SpriteScore;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public class RedBirdObject extends BaseBirdObject {

	public RedBirdObject(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.RED_BIRD_ID, 4, 1));
	}

	@Override
	public void createBody(BodyType pBodyType, boolean pUpdatePosition, boolean pUpdateRotation) {
		float halfWidth = this.mEntity.getWidth() * 0.5f;
		float halfHeight = this.mEntity.getHeight() * 0.5f;
		float centerX = 0;
		float centerY = 0;
		float pixelToMeterRatio = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		
		this.mBody = PhysicsFactory.createPolygonBody(GameLevelManager.getInstance().getPhysicsWorld(), 
				this.mEntity, 
				new Vector2[] 
				{
					new Vector2((centerX - (halfWidth - 19.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 9.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 35.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 9.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 44.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 17.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 47.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 33.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 39.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 44.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 18.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 44.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 8.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 34.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 10.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 19.0f)) / pixelToMeterRatio)
				}, 
				pBodyType, 
				BirdConstants.BIRD_FIXTURE_DEF);
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}
	
	@Override
	public void onPostSolve(float impulse) {
		if (this.mEntity.getCurrentTileIndex() != 3) {
			this.mEntity.setCurrentTileIndex(3);
		}
		super.onPostSolve(impulse);
	}

	@Override
	public void showRemainScore() {
		SpriteScore score = new SpriteScore(0, 0, ResourceManager.getInstance().getScoreSpriteSheetTexturePackTextureRegionLibrary().get(ScoreConstants.RED_BIRD_REMAIN_SCORE_ID));
		score.getEntity().setPosition(this.mEntity.getX() - Math.abs(this.mEntity.getWidth() - score.getEntity().getWidth()) * 0.5f, 
				this.mEntity.getY() - score.getEntity().getHeight());
		score.showScore();
	}
}
