package com.xdpm.angrybirds.gamelevel.effect;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.util.math.MathUtils;

import com.xdpm.angrybirds.common.CloudConstants;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public class BirdContactCloud extends BaseObjectEffect implements IUpdateHandler {
	
	private float mMaxScale = 1.0f;
	
	public BirdContactCloud(float pX, float pY) {
		super(pX, pY, ResourceManager.getInstance().getCloudSpriteSheetTexturePackTextureRegionLibrary().get(CloudConstants.BIRD_CLOUD_ID, 4, 1));
		
		int cloudSize = MathUtils.random(0, 3);
		this.mEntity.setCurrentTileIndex(cloudSize);
	}

	@Override
	public void onUpdate(float pTimeElapsed) {
		this.mEntity.setScale(this.mMaxScale = this.mMaxScale -= 0.05f);
		 
		if (this.mMaxScale <= 0) {
			GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(BirdContactCloud.this);
			ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
				
				@Override
				public void run() {
					mEntity.detachSelf();
					mEntity.dispose();
				}
			});
		}
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void startEffect() {

	}
}
