package com.xdpm.angrybirds.gamelevel.object.bird;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import com.xdpm.angrybirds.common.BirdConstants;

public abstract class SpecialBirdObject extends BaseBirdObject {

	public SpecialBirdObject(float pX, float pY,
			ITiledTextureRegion pITiledTextureRegion) {
		super(pX, pY, pITiledTextureRegion);
	}
	
	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		if (BirdConstants.STATE_IS_FLYING == this.mBirdState) {
			this.doSpecialAction();
		}
		super.onSceneTouchEvent(pSceneTouchEvent);
	}
	
	public abstract void doSpecialAction();
}
