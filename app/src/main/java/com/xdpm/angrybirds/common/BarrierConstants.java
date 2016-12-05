package com.xdpm.angrybirds.common;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public interface BarrierConstants {
	public static final char NORMAL = 0;
	public static final char DAMAGE_1 = NORMAL + 1;
	public static final char DAMAGE_2 = DAMAGE_1 + 1;
	public static final char DAMAGE_3 = DAMAGE_2 + 1;
	
	public static final float DAMAGE_1_IMPULSE = 1000;
	public static final float DAMAGE_2_IMPULSE = 2000;
	public static final float DAMAGE_3_IMPULSE = 3000;
	
	public static final int DAMAGE_1_TILED_INDEX = 1;
	public static final int DAMAGE_2_TILED_INDEX = 2;
	public static final int DAMAGE_3_TILED_INDEX = 3;
	
	public static final char CLASS_STRAIGHT_01 = 1;
	public static final char CLASS_STRAIGHT_02 = 2;
	public static final char CLASS_STRAIGHT_03 = 3;
	public static final char CLASS_STRAIGHT_04 = 4;
	
	public static final char CLASS_SQUARE_01 = 5;
	public static final char CLASS_SQUARE_02 = 6;
	public static final char CLASS_SQUARE_03 = 7;
	public static final char CLASS_SQUARE_HOLLOW = 8;
	
	public static final char CLASS_TRIANGLE_FILLED = 9;
	public static final char CLASS_TRIANGLE_HOLLOW = 10;
	
	public static final char CLASS_CIRCLE_BIG = 11;
	public static final char CLASS_CIRCLE_SMALL = 12;
	
	public static final int CIRCLE_ICE_BIG_ID = 0;
	public static final int CIRCLE_ICE_SMALL_ID = 1;
	public static final int SMASH_ICE_ID = 2;
	public static final int SQUARE_ICE_01_ID = 3;
	public static final int SQUARE_ICE_02_ID = 4;
	public static final int SQUARE_ICE_HOLLOW_ID = 5;
	public static final int STRAIGHT_ICE_01_ID = 6;
	public static final int STRAIGHT_ICE_02_ID = 7;
	public static final int STRAIGHT_ICE_03_ID = 8;
	public static final int STRAIGHT_ICE_04_ID = 9;
	public static final int TRIANGLE_ICE_FILLED_ID = 10;
	public static final int TRIANGLE_ICE_HOLLOW_ID = 11;
	
	public static final int CIRCLE_STONE_BIG_ID = 0;
	public static final int CIRCLE_STONE_SMALL_ID = 1;
	public static final int SMASH_STONE_ID = 2;
	public static final int SQUARE_STONE_01_ID = 3;
	public static final int SQUARE_STONE_02_ID = 4;
	public static final int SQUARE_STONE_03_ID = 5;
	public static final int SQUARE_STONE_HOLLOW_ID = 6;
	public static final int STRAIGHT_STONE_01_ID = 7;
	public static final int STRAIGHT_STONE_02_ID = 8;
	public static final int STRAIGHT_STONE_03_ID = 9;
	public static final int STRAIGHT_STONE_04_ID = 10;
	public static final int TRIANGLE_STONE_FILLED_ID = 11;
	public static final int TRIANGLE_STONE_HOLLOW_ID = 12;
	
	public static final int CIRCLE_WOOD_BIG_ID = 0;
	public static final int CIRCLE_WOOD_SMALL_ID = 1;
	public static final int SMASH_WOOD_ID = 2;
	public static final int SQUARE_WOOD_01_ID = 3;
	public static final int SQUARE_WOOD_02_ID = 4;
	public static final int SQUARE_WOOD_03_ID = 5;
	public static final int SQUARE_WOOD_HOLLOW_ID = 6;
	public static final int STRAIGHT_WOOD_01_ID = 7;
	public static final int STRAIGHT_WOOD_02_ID = 8;
	public static final int STRAIGHT_WOOD_03_ID = 9;
	public static final int STRAIGHT_WOOD_04_ID = 10;
	public static final int TRIANGLE_WOOD_FILLED_ID = 11;
	public static final int TRIANGLE_WOOD_HOLLOW_ID = 12;
	
	public static final FixtureDef ICE_BARRIER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(100, 0.03f, 0.5f);
	public static final FixtureDef WOOD_BARRIER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(200, 0.01f, 0.5f);
	public static final FixtureDef STONE_BARRIER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(400, 0.005f, 0.5f);
}
