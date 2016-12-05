package com.xdpm.angrybirds.scene.layer;

import org.andengine.entity.primitive.Rectangle;

import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.scene.BaseScene;

public abstract class BaseLayer extends BaseScene {

	// =============================================
	// Properties
	// =============================================
	protected Rectangle mMashRectangle;

	// =============================================
	// Constructors
	// =============================================
	
	public BaseLayer() {
		this(true);
	}
	
	public BaseLayer(boolean pIsUnloadOnHidden) {
		super(pIsUnloadOnHidden);
	}
	
	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack) {
		this.mMashRectangle = new Rectangle(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		this.mMashRectangle.setColor(0, 0, 0, 0.6f);
		//this.attachChild(this.mMashRectangle);
	}
	
	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack) {
		this.mMashRectangle.detachChildren();
		this.mMashRectangle.detachSelf();
		this.clearTouchAreas();
		
		pOnUnLoadSceneCallBack.onUnLoadSceneFinish();
	}
}
