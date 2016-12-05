package com.xdpm.angrybirds.gamelevel.object.pig;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.PigConstants;
import com.xdpm.angrybirds.gamelevel.GameLevel;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public class OldPigObject extends BasePigObject {

	public OldPigObject(float pX, float pY, float pRotation) {
		super(pX, pY, pRotation, ResourceManager.getInstance().getPigSpriteSheetTexturePackTextureRegionLibrary().get(PigConstants.OLD_PIG_ID, 9, 1));
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
					new Vector2((centerX - (halfWidth - 40.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 16.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 76.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 16.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 100.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 44.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 106.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 77.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 82.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 94.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 27.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 93.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 2.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 72.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 11.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 42.0f)) / pixelToMeterRatio)
				}, 
				pBodyType, 
				PigConstants.PIG_FIXTURE_DEF);
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}

}
