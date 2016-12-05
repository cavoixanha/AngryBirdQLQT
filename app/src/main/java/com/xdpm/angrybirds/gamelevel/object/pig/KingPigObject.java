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

public class KingPigObject extends BasePigObject {

	public KingPigObject(float pX, float pY, float pRotation) {
		super(pX, pY, pRotation, ResourceManager.getInstance().getPigSpriteSheetTexturePackTextureRegionLibrary().get(PigConstants.KING_PIG_ID, 9, 1));
		// TODO Auto-generated constructor stub
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
					new Vector2((centerX - (halfWidth - 11.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 68.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 51.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 46.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 102.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 57.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 126.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 94.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 115.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 131.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 76.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 150.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 25.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 143.0f)) / pixelToMeterRatio),
					new Vector2((centerX - (halfWidth - 1.0f)) / pixelToMeterRatio, (centerY - (halfHeight - 113.0f)) / pixelToMeterRatio)
				}, 
				pBodyType, 
				PigConstants.PIG_FIXTURE_DEF);
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}
}
