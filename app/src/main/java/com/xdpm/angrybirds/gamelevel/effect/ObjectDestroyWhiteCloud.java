package com.xdpm.angrybirds.gamelevel.effect;

import com.xdpm.angrybirds.common.CloudConstants;
import com.xdpm.angrybirds.manager.ResourceManager;

public class ObjectDestroyWhiteCloud extends BaseObjectEffect {

	public ObjectDestroyWhiteCloud(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().getCloudSpriteSheetTexturePackTextureRegionLibrary().get(CloudConstants.DESTROY_CLOUD_WHITE_ID, 5, 1));
	}

	@Override
	public void startEffect() {
		long[] animationDuration = new long[this.mEntity.getTileCount()];
		for (int i = 0; i < this.mEntity.getTileCount(); i++) {
			animationDuration[i] = CloudConstants.DESTROY_CLOUD_ANIMATION_DURATION / this.mEntity.getTileCount();
		}
		this.mEntity.animate(animationDuration);
	}
}
