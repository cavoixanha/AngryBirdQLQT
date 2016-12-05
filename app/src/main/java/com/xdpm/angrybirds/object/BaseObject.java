package com.xdpm.angrybirds.object;

import org.andengine.input.touch.TouchEvent;
import com.badlogic.gdx.math.Vector2;
/**
 * All object in game should extend this class
 * 
 * @author ChuongNK
 * 
 */
public abstract class BaseObject {
	
	//==================================================
	// Properties
	//==================================================
		
	protected Vector2 mDesiredXY;
	
	//==================================================
	// Constructors
	//==================================================
	
	public BaseObject(float pX, float pY) {
		this.mDesiredXY = new Vector2(pX, pY);
	}
	
	//==================================================
	// Getters and Setters
	//==================================================
	
	public Vector2 getDesiredXY() {
		return this.mDesiredXY;
	}
	
	//==================================================
	// Abstract methods
	//==================================================
	
	public abstract void onAreaTouched(TouchEvent pSceneTouchEvent);
	
	public abstract void onSceneTouchEvent(TouchEvent pSceneTouchEvent);
}
