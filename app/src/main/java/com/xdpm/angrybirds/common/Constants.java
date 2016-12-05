package com.xdpm.angrybirds.common;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Put all constants value here
 * 
 * @author ChuongNK
 * 
 */
public class Constants {

	// ===========================================================
	// Camera value
	// ===========================================================
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	public static final int CAMERA_BOUND_WIDTH = CAMERA_WIDTH * 2;
	public static final int CAMERA_BOUND_HEIGHT = CAMERA_HEIGHT * 2;
	public static final float CAMERA_CENTER_X = CAMERA_BOUND_WIDTH * 0.5f;
	public static final float CAMERA_CENTER_Y = CAMERA_BOUND_HEIGHT * 0.5f;
	public static final float CAMERA_MAX_ZOOM_FACTOR = 1.0f;
	public static final float CAMERA_MIN_ZOOM_FACTOR = CAMERA_MAX_ZOOM_FACTOR * 0.5f;
	public static final int CAMERA_MAX_ZOOM_FACTOR_CHANGE = 50;
	public static final int CAMERA_MAX_VELOCITY_X = 500;
	public static final int CAMERA_MAX_VELOCITY_Y = CAMERA_MAX_VELOCITY_X * CAMERA_BOUND_HEIGHT / CAMERA_BOUND_WIDTH;
	public static final int CAMERA_OFFSET_X_MULTIFILY = 50;

	// ===========================================================
	// Asset base folder path
	// ===========================================================
	public static final String DEFAULT_ASSET_BASE_IMAGE = "images/";
	public static final String DEFAULT_ASSET_BASE_SOUND = "sounds/";
	public static final String DEFAULT_ASSET_BASE_MUSIC = "music/";
	public static final String DEFAULT_ASSET_BASE_FONT = "font/";

	// ===========================================================
	// Layout margin
	// ===========================================================
	
	public static final int MENU_MARGIN_LEFT = 5;
	public static final int MENU_MARGIN_RIGHT = 5;
	public static final int MENU_MARGIN_TOP = 15;
	public static final int MENU_MARGIN_BOTTOM = 10;
	
	public static float MENU_ANIMATION_DURATION = 0.2f;
	
	public static float MENU_GROUND_MOVING_SPEED = 10; // second
	public static float MENU_TREE_MOVING_SPEED = 40; // second
	public static float MENU_BIRD_JUMP_DURATION = 5; // second
	// ===========================================================
	// Object FixtureDef
	// ===========================================================
	
	public static final FixtureDef GROUND_FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0, 5000.0f);
	
	public static final float ARBALEST_MAX_DELTA_LENGTH = 150;
	public static final float ARBALEST_MIN_DELTA_LENGTH = 90;

	
	// ===========================================================
	// Game Fields
	// ===========================================================
	
	public static final String PREF_WORLD_ID = "pref_world_id";
	public static final String PREF_STAGE_ID = "pref_state_id";
	public static final String PREF_LEVEL_ID = "pref_level_id";
	public final static String PREF_SOUND_SETTING = "pref_sound_setting";
	
	// ===========================================================
	// Layer Size
	// ===========================================================
	public static final int MENU_RESULT_WIDTH = 380;
	public static final int MENU_RESULT_ELEMENT_MARGIN = 10;
	public static final int MENU_RESULT_MARGIN_TOP = 50;
	public static final int MENU_RESULT_MARGIN_BOTTOM = 20;
	public static final int MENU_RESULT_LINE_HEIGHT = 20;
	
	// ===========================================================
	// LevelSelectorScene
	// ===========================================================
	
	public static final char WORLD_POACHED_EGGS_ID = 1;
	public static final char WORLD_MIGHTY_HOAX_ID = WORLD_POACHED_EGGS_ID + 1;
	public static final char WORLD_DANGER_ABOVE_ID = WORLD_MIGHTY_HOAX_ID + 1;
	public static final char WORLD_THE_BIG_SETUP_ID = WORLD_DANGER_ABOVE_ID + 1;
	public static final char WORLD_HAMEM_HIGH_ID = WORLD_THE_BIG_SETUP_ID + 1;
	public static final char WORLD_MINE_AND_DINE_ID = WORLD_HAMEM_HIGH_ID + 1;
	public static final char WORLD_BIRTHDAY_PARTY_ID = WORLD_MINE_AND_DINE_ID + 1;
	
	
	
	public static final char DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON = 50;
	public static final char DISTANCE_TO_TOP_SCREEN_OF_LEVEL_BUTTON = 20;
	
	public static final String SHARED_PREF_LEVELSELECTOR = "LevelSelector";
}
