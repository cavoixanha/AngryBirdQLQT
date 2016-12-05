package com.xdpm.angrybirds.utils;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;

public class SpriteUtils {
		
	public static Sprite createRepeatingSprite(ITextureRegion repeatTextureRegion,float repeatWidth, float repeatHeight, BaseGameActivity activity) {
		final ITextureRegion textureRegion = repeatTextureRegion.deepCopy();
		textureRegion.setTextureHeight(repeatHeight);
		textureRegion.setTextureWidth(repeatWidth);
		final Sprite repeatSprite = new Sprite(0, 0, textureRegion, activity.getVertexBufferObjectManager());
		return repeatSprite;
	}
}
