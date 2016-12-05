package com.xdpm.angrybirds.scene.layer;

public class ConfirmQuitLayer extends BaseLayer {

	@Override
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {		
	}

	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		pOnHideSceneCallBack.onHideSceneFinish();
	}

}
