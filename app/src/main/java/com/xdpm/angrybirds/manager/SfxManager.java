package com.xdpm.angrybirds.manager;

import org.andengine.audio.music.Music;
import org.andengine.util.math.MathUtils;

public class SfxManager {
	
	private Music mMusic;
	
	private static final int BIRD_MISC_A1 = 1;
	private static final int BIRD_MISC_A2 = BIRD_MISC_A1 + 1;
	private static final int BIRD_MISC_A3 = BIRD_MISC_A2 + 1;
	private static final int BIRD_MISC_A4 = BIRD_MISC_A3 + 1;
	private static final int BIRD_MISC_A5 = BIRD_MISC_A4 + 1;
	private static final int BIRD_MISC_A6 = BIRD_MISC_A5 + 1;
	
	// ====================================================
	// Variables
	// ====================================================
	public boolean mSoundsMuted;
	public boolean mMusicMuted;
	
	private boolean mPlayFromPaused = false;
	
	//==================================================
	// Properties
	//==================================================
	
	private ResourceManager mResourceManager = ResourceManager.getInstance();
	private static SfxManager mSfxManager;
	
	private SfxManager() {
		mMusic = ResourceManager.getInstance().getTitleThemeMusic();
		mMusic.setLooping(true);
	}
	
	public static SfxManager getInstance() {
		if (null == mSfxManager) {
			mSfxManager = new SfxManager();
		}
		return mSfxManager;
	}
	
	//==================================================
	// Properties
	//==================================================
	
	public void playBirdMisc() {
		int miscNo = MathUtils.random(BIRD_MISC_A1, BIRD_MISC_A6);
		switch (miscNo) {
		case BIRD_MISC_A1:
			this.mResourceManager.getBirdMiscA1().play();
			break;
		case BIRD_MISC_A2:
			this.mResourceManager.getBirdMiscA2().play();		
			break;
		case BIRD_MISC_A3:
			this.mResourceManager.getBirdMiscA3().play();
			break;
		case BIRD_MISC_A4:
			this.mResourceManager.getBirdMiscA4().play();
			break;
		case BIRD_MISC_A5:
			this.mResourceManager.getBirdMiscA5().play();
			break;
		case BIRD_MISC_A6:
			this.mResourceManager.getBirdMiscA6().play();
			break;
					
		default:
			break;
		}
	}
	
	public void playRedBirdPlayingSound() {
		this.mResourceManager.getRedBirdFlyingSound().play();
	}
	
	//==================================================
	// Control music
	//==================================================
	public void setMusicMuted(final boolean pMuted) {
		getInstance().mMusicMuted = pMuted;
		if(getInstance().mMusicMuted) 
			getInstance().mMusic.pause(); 
		else 
			getInstance().mMusic.play();
	}
	
	public boolean toggleMusicMuted() {
		getInstance().mMusicMuted = !getInstance().mMusicMuted;
		if(getInstance().mMusicMuted) 
			getInstance().mMusic.pause(); 
		else 
			getInstance().mMusic.play();
		return getInstance().mMusicMuted;
	}
	
	
	public void playMusic() {
		if(!getInstance().mMusicMuted){
			if(getInstance().mPlayFromPaused){
				getInstance().mPlayFromPaused = false;
				getInstance().mMusic.seekTo(0);
			}
			getInstance().mMusic.play();
		}
	}
	
	public void pauseMusic() {
		getInstance().mMusic.pause();
		getInstance().mPlayFromPaused = true;
	}
	
	public void stopMusic(){
		getInstance().mMusic.stop();
	}
	
	public void resumeMusic() {
		if(!getInstance().mMusicMuted)
			getInstance().mMusic.resume();
	}
	
	public float getMusicVolume() {
		return getInstance().mMusic.getVolume();
	}
	
	public void setMusicVolume(final float pVolume) {
		getInstance().mMusic.setVolume(pVolume);
	}
	
	
	//==================================================
	// LevelSound
	//==================================================
	public void playLevelClearedSound(){
		ResourceManager.getInstance().getLevelClearedSound().play();
	}
	
	public void playSlotMachineSound(){
		ResourceManager.getInstance().getSlotMachineSound().play();
	}
	
	public void stopSlotMachineSound(){
		ResourceManager.getInstance().getSlotMachineSound().stop();
	}
	
	//==================================================
	// Properties
	//==================================================
}
