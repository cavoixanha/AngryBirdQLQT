package com.xdpm.angrybirds.gamelevel.effect;

import org.andengine.entity.text.Text;

import com.xdpm.angrybirds.manager.ResourceManager;

public class TextScore extends BaseScore<Text> {
	
	public TextScore(float pX, float pY, CharSequence pText) {
		this.mEntity = new Text(pX, pY, ResourceManager.getInstance().getAngrybirdFont48(),
				pText, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
	}
}
