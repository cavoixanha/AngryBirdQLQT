package com.xdpm.angrybirds.gamelevel.object.pig;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.PigConstants;
import com.xdpm.angrybirds.gamelevel.GameLevel;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public class SmallPigObject extends BasePigObject {

	public SmallPigObject(float pX, float pY, float pRotation) {
		super(pX, pY, pRotation, ResourceManager.getInstance().getPigSpriteSheetTexturePackTextureRegionLibrary().get(PigConstants.SMALL_PIG_ID, 9, 1));
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
					new Vector2((centerX - (halfWidth - 12.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 7.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 33.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 6.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 45.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 18.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 47.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 34.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 32.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 45.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 14.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 45.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 2.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 36.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 1.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 18.0f)) / pixelToMeterRatio)
				}, 
				pBodyType, 
				PigConstants.PIG_FIXTURE_DEF);
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}
}
