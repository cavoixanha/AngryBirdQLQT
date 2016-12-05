package com.xdpm.angrybirds;

import android.os.AsyncTask;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class AsyncTaskLoader extends AsyncTask<IAsyncCallBack, Integer, Boolean> {

	private IAsyncCallBack[] mAsyncCallBacks;
	
	@Override
	protected Boolean doInBackground(IAsyncCallBack... params) {
		this.mAsyncCallBacks = params;
		for (IAsyncCallBack asyncCallBack : this.mAsyncCallBacks) {
			asyncCallBack.onLoad();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		for (IAsyncCallBack asyncCallBack : this.mAsyncCallBacks) {
			asyncCallBack.onComplete();
		}
	}
}
