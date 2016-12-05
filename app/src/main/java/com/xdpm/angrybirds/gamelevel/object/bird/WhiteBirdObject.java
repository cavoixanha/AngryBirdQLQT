package com.xdpm.angrybirds.gamelevel.object.bird;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.gamelevel.GameLevel;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public class WhiteBirdObject extends SpecialBirdObject {

	public WhiteBirdObject(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.WHITE_BIRD_ID, 7, 1));
	}

	@Override
	public void doSpecialAction() {
		
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
					new Vector2((centerX - (halfWidth - 53.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 23.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 75.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 45.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 80.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 73.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 65.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 94.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 35.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 96.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 13.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 80.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 14.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 53.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 28.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 32.0f)) / pixelToMeterRatio)
				}, 
				pBodyType, 
				BirdConstants.BIRD_FIXTURE_DEF);
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}

	@Override
	public void showRemainScore() {

	}

}
