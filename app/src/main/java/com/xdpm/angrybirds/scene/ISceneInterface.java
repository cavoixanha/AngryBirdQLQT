package com.xdpm.angrybirds.scene;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public interface ISceneInterface {
	
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack) throws Exception;
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack) throws Exception;
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack) throws Exception;
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack) throws Exception;
	
	
	public static interface OnLoadSceneCallBack {
		public void onLoadSceneFinish();
	}
	
	public static interface OnShowSceneCallBack {
		public void onShowSceneFinish();
	}
	
	public static interface OnHideSceneCallBack {
		public void onHideSceneFinish();
	}
	
	public static interface OnUnLoadSceneCallBack {
		public void onUnLoadSceneFinish();
	}
}
