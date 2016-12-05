package com.xdpm.angrybirds.common;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface PigConstants {
	
	public static final FixtureDef PIG_FIXTURE_DEF = PhysicsFactory.createFixtureDef(100, 0.005f, 0.5f);
	
	public static final int HELMET_PIG_ID = 0;
	public static final int KING_PIG_ID = 1;
	public static final int NORMAL_PIG_ID = 2;
	public static final int OLD_PIG_ID = 3;
	public static final int SMALL_PIG_ID = 4;
	
	public static final char ACTION_NORMAL = 0;
	public static final char ACTION_WINK = ACTION_NORMAL + 1;
	public static final char ACTION_LAUGH = ACTION_WINK + 1;
	
	public static final long ACTION_NORMAL_ANIMATE_DURATION = 0;
	public static final long ACTION_WINK_ANIMATE_DURATION = 1000;
	public static final long ACTION_LAUGH_ANIMATE_DURATION = 5000;
	
	public static final char NORMAL = 0;
	public static final char DAMAGE_1 = NORMAL + 1;
	public static final char DAMAGE_2 = DAMAGE_1 + 1;
	
	public static final float DAMAGE_1_IMPULSE = 1000;
	public static final float DAMAGE_2_IMPULSE = 1500;
	
	public static final int NORMAL_TILED_INDEX = 0;
	public static final int DAMAGE_1_TILED_INDEX = 3;
	public static final int DAMAGE_2_TILED_INDEX = 6;
}
