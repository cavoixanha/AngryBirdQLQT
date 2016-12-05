package com.xdpm.angrybirds.gamelevel.effect;

import com.xdpm.angrybirds.common.CloudConstants;
import com.xdpm.angrybirds.manager.ResourceManager;

public class BirdFlyingCloud extends BaseObjectEffect {

	public BirdFlyingCloud(float pX, float pY, int pSize) {
		super(pX, pY, ResourceManager.getInstance().getCloudSpriteSheetTexturePackTextureRegionLibrary().get(CloudConstants.BIRD_CLOUD_ID, 4, 1));
		
		this.mEntity.setCurrentTileIndex(pSize);
	}

	@Override
	public void startEffect() {

	}
}
