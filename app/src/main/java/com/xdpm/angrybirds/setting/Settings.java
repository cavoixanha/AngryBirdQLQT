package com.xdpm.angrybirds.setting;

import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.manager.ResourceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.Editable;

public class Settings {
	
	// =================================================================
	// Properties
	// =================================================================
	
	private static Settings mSettings;
	private SharedPreferences mSharedPreferences;
	
	// =================================================================
	// Constructors
	// =================================================================
	
	public Settings() {
		this.initSetting(ResourceManager.getInstance().getBaseGameActivity().getBaseContext());
	}
	
	public static Settings getInstance() {
		if (null == mSettings) {
			mSettings = new Settings();
		}
		return mSettings;
	}
	
	// =================================================================
	// Methods from/for SuperClass/Interface
	// =================================================================
	
	private void initSetting(Context context){
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public boolean getPrefSoundSetting(){
		return mSharedPreferences.getBoolean(Constants.PREF_SOUND_SETTING, true);
	}
	
	public void setPrefSoundSetting(boolean flag){
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean(Constants.PREF_SOUND_SETTING, flag);
		editor.commit();
	}
	
	public int getWorldId() {
		return mSharedPreferences.getInt(Constants.PREF_WORLD_ID, 1);
	}
	
	public void setWorldId(int pWorldId) {
		Editor editor = mSharedPreferences.edit();
		editor.putInt(Constants.PREF_WORLD_ID, pWorldId);
		editor.commit();
	}
	
	public int getLastestWorldId() {
		return Constants.WORLD_POACHED_EGGS_ID;
	}
	
	public int getStageId() {
		return mSharedPreferences.getInt(Constants.PREF_STAGE_ID, 1);
	}
	
	public void setStagedId(int pStageId) {
		Editor editor = mSharedPreferences.edit();
		editor.putInt(Constants.PREF_STAGE_ID, pStageId);
		editor.commit();
	}
	
	public int getLevelId() {
		return mSharedPreferences.getInt(Constants.PREF_LEVEL_ID, 1);
	}
	
	public void setLevelId(int pLevelId) {
		Editor editor = mSharedPreferences.edit();
		editor.putInt(Constants.PREF_LEVEL_ID, pLevelId);
		editor.commit();
	}
}
