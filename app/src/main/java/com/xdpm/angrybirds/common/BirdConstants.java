package com.xdpm.angrybirds.common;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * 
 * @author Nguyen Anh Tuan
 * 
 */
public interface BirdConstants {
	
	//==========================================================
	// Do random action fields
	//==========================================================
	
	public static final char ACTION_NORMAL = 0;
	public static final char ACTION_WINK = ACTION_NORMAL + 1;
	public static final char ACTION_LAUGH = ACTION_WINK + 1;
	public static final char ACTION_TALL_JUMP = ACTION_LAUGH + 1;
	public static final char ACTION_SHORT_JUMP = ACTION_TALL_JUMP + 1;

	//==========================================================
	// Animation fields
	//==========================================================
	
	public static final char TILED_NORMAL = 0;
	public static final char TILED_WINK = TILED_NORMAL + 1;
	public static final char TILED_LAUGH = TILED_WINK + 1;
	public static final char TILED_DAMAGED = TILED_LAUGH + 1;
	
	public static final long ACTION_NORMAL_ANIMATE_DURATION = 0;
	public static final long ACTION_WINK_ANIMATE_DURATION = 1000;
	public static final long ACTION_LAUGH_ANIMATE_DURATION = 1000;
	public static final long ACTION_JUMP_DURATION = 1000;
	
	//==========================================================
	// Bird state fields
	//==========================================================
	
	public static final char STATE_ON_GROUND = 0; 
	public static final char STATE_IS_READY = STATE_ON_GROUND + 1;
	public static final char STATE_IS_GRABBED = STATE_IS_READY + 1;
	public static final char STATE_IS_FLYING = STATE_IS_GRABBED + 1;
	public static final char STATE_IS_DAMAGED = STATE_IS_FLYING + 1;
	
	public static final float DO_ACTION_RATIO = 6.0f / 100;
	
	//==========================================================
	// TexturePacker id fields
	//==========================================================
	
	public static final int ARBALEST_BEHIND_HAND_ID = 0;
	public static final int ARBALEST_FRONT_HAND_ID = 1;
	public static final int BLACK_BIRD_ID = 2;
	public static final int BLUE_BIRD_ID = 3;
	public static final int RED_BIRD_ID = 4;
	public static final int WHITE_BIRD_ID = 5;
	public static final int YELLOW_BIRD_ID = 6;
	
	//==========================================================
	// Physics fields
	//==========================================================
	
	public static final int DAMAGE_IMPULSE = 1000;
	public static final int BIRD_FORCE_MULTIFLIER = 10;
	public static final FixtureDef BIRD_FIXTURE_DEF = PhysicsFactory.createFixtureDef(100, 0.005f, 0.5f);
	
	//==========================================================
	// Menu jump bird fields
	//==========================================================
	
	public static final float MENU_JUMP_BIRD_MIN_SCALE = 0.25f;
	public static final float MENU_JUMP_BIRD_MAX_SCALE = 0.75f;
}
