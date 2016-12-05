package com.xdpm.angrybirds.gamelevel.effect;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import com.xdpm.angrybirds.manager.ResourceManager;

public class SpriteScore extends BaseScore<Sprite> {
		
	public SpriteScore(float pX, float pY, ITextureRegion pTextureRegion) {
		this.mEntity = new Sprite(pX, pY, pTextureRegion, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
	}
}
