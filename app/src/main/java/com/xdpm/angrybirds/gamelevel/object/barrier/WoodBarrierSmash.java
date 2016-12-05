package com.xdpm.angrybirds.gamelevel.object.barrier;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.BarrierConstants;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public class WoodBarrierSmash extends BaseBarrierSmash {

	public WoodBarrierSmash(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().getWoodBarrierSpriteSheetTexturePackTextureRegionLibrary().get(BarrierConstants.SMASH_WOOD_ID, 3, 1));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createBody(BodyType pBodyType, boolean pUpdatePosition,
			boolean pUpdateRotation) {
		this.mBody = PhysicsFactory.createBoxBody(GameLevelManager.getInstance().getPhysicsWorld(), this.mEntity, pBodyType, BarrierConstants.WOOD_BARRIER_FIXTURE_DEF);
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}

}
