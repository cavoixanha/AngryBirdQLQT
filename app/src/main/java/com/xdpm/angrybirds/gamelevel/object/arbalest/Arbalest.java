package com.xdpm.angrybirds.gamelevel.object.arbalest;

import java.util.ArrayList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.xdpm.angrybirds.common.BirdConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject.IOnBirdChangePositionListener;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject.IOnBirdShotListener;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.object.BaseObject;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class Arbalest extends BaseObject implements IOnBirdChangePositionListener, IOnBirdShotListener {

	//==============================================================
	// Properties
	//==============================================================
	
	private Vector2 mCenterArbalestXY;
	
	private Entity mBehindLayer;
	private Sprite mBehindHandSprite;
	private Rectangle mBehindRopeRectangle;
	
	private Entity mFrontLayer;
	private Sprite mFrontHandSprite;
	private Rectangle mFrontRopeRectangle;
	
	private ArrayList<BaseBirdObject> mListOfBirdObjects;
	private BaseBirdObject mCurrentBirdObject;
	private int mCurrentBirdInddex = -1;
	private int mNumOfBird = 0;
	
	//==============================================================
	// Constructors
	//==============================================================
	public Arbalest(float pX, float pY) {
		super(pX, pY);
		
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		
		this.mBehindLayer = new Entity();
		this.mFrontLayer = new Entity();

		this.mBehindHandSprite = new Sprite(pX + 27, pY + 6, ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.ARBALEST_BEHIND_HAND_ID), vertexBufferObjectManager);
		this.mBehindRopeRectangle = new Rectangle(pX + 50, pY + 26, 8, 18, vertexBufferObjectManager);
		this.mBehindRopeRectangle.setColor(0.51f, 0.17f, 0);
		
		this.mFrontHandSprite = new Sprite(pX, pY, ResourceManager.getInstance().getBirdSpriteSheetTexturePackTextureRegionLibrary().get(BirdConstants.ARBALEST_FRONT_HAND_ID), vertexBufferObjectManager);
		this.mFrontRopeRectangle = new Rectangle(pX + 8, pY + 26, 8, 18, vertexBufferObjectManager);
		this.mFrontRopeRectangle.setColor(0.51f, 0.17f, 0);
		
		this.mCenterArbalestXY = new Vector2(this.mFrontRopeRectangle.getX() + Math.abs(this.mFrontRopeRectangle.getX() - this.mBehindRopeRectangle.getX()) / 2,
											this.mFrontRopeRectangle.getY() + this.mFrontRopeRectangle.getHeight() / 2);
		
		this.mBehindLayer.attachChild(this.mBehindHandSprite);
		this.mBehindLayer.attachChild(this.mBehindRopeRectangle);
		
		this.mFrontLayer.attachChild(this.mFrontRopeRectangle);
		//his.mFrontLayer.attachChild(this.mHolderRectangle);
		this.mFrontLayer.attachChild(this.mFrontHandSprite);
	}
	
	//==============================================================
	// Getters & Setters
	//==============================================================
	
	public Entity getBehindLayer() {
		return this.mBehindLayer;
	}
	
	public Entity getFrontLayer() {
		return this.mFrontLayer;
	}
	
	public BaseBirdObject getCurrentBird() {
		return this.mCurrentBirdObject;
	}
	
	//==============================================================
	// Methods for/from SuperClass/Interfaces
	//==============================================================

	@Override
	public void onAreaTouched(TouchEvent pSceneTouchEvent) {

	}
	
	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_UP :
		case TouchEvent.ACTION_CANCEL:
			this.shrinkTheRopes();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBirdChangedPosition(float pX, float pY) {
		if (pX == this.mCurrentBirdObject.getDesiredXY().x && pY == this.mCurrentBirdObject.getDesiredXY().y) {
			this.shrinkTheRopes();
		} else {
			this.moveTheRopes(pX, pY);
		}
	}
	
	@Override
	public void onBirdShot() {
		this.shrinkTheRopes();
		this.nextBird();
	}
	
	//==============================================================
	// Methods
	//==============================================================
	
		//=================================
		// Private Methods
		//=================================
	
	private void moveTheRopes(float pX, float pY) {

		// Birds properties
		float w = this.mCurrentBirdObject.getEntity().getWidth();
		float h = this.mCurrentBirdObject.getEntity().getHeight();
		float r = (float) Math.sqrt((w / 2) * (w / 2) + (h / 2) * (h / 2))-5;
		
		
		// Calculating delta length of ropes and holder
		
		float behindRopeDelta = MathUtils.distance(this.mBehindRopeRectangle.getX(), this.mBehindRopeRectangle.getY() + this.mBehindRopeRectangle.getHeight() / 2, 
													pX + w / 2, pY + h / 2);
		float frontRopeDelta = MathUtils.distance(this.mFrontRopeRectangle.getX(), this.mFrontRopeRectangle.getY() + this.mFrontRopeRectangle.getHeight() / 2, 
													pX + w / 2, pY + h / 2);
		
		// Set width of ropes and position of holder
		
		this.mBehindRopeRectangle.setWidth(behindRopeDelta + r);
		this.mFrontRopeRectangle.setWidth(frontRopeDelta + r);
		
		// Calculating angle of ropes
		int i = 7;
		float hw = - (Math.abs(pX-this.mFrontHandSprite.getX())) / (w/2);
		if (pX < this.mFrontHandSprite.getX())
			hw = -hw;
		float hh = - (Math.abs(pY-this.mFrontHandSprite.getY()))/(h/2);
		if (pY < this.mFrontHandSprite.getY())
			hh = -hh;
		float behindRopeAngle = (float) Math.atan2(pY + h / 2 - hh*i - (this.mBehindRopeRectangle.getY() - this.mBehindRopeRectangle.getHeight() / 2), 
													pX + w / 2 - hw*i - this.mBehindRopeRectangle.getX());
		float frontRopeAngle = (float) Math.atan2(pY + h / 2 - hh*i - this.mFrontRopeRectangle.getY(), 
													pX + w / 2 - hw*i - this.mFrontRopeRectangle.getX());
		
		// Set ropes scale
		this.mBehindRopeRectangle.setScaleY(Constants.ARBALEST_MAX_DELTA_LENGTH / (Constants.ARBALEST_MAX_DELTA_LENGTH + behindRopeDelta));
		this.mFrontRopeRectangle.setScaleY(Constants.ARBALEST_MAX_DELTA_LENGTH / (Constants.ARBALEST_MAX_DELTA_LENGTH + frontRopeDelta));
		
		// Set ropes rotation
		this.mBehindRopeRectangle.setRotation(behindRopeAngle * 180 /(float) Math.PI);
		this.mFrontRopeRectangle.setRotation(frontRopeAngle * 180 / (float) Math.PI);
	}
	
	
	private void shrinkTheRopes() {
		this.mBehindRopeRectangle.setWidth(8);
		this.mBehindRopeRectangle.setHeight(18);
		this.mBehindRopeRectangle.setRotation(0 );
		this.mBehindRopeRectangle.setScale(1);
		
		this.mFrontRopeRectangle.setWidth(8);
		this.mFrontRopeRectangle.setHeight(18);
		this.mFrontRopeRectangle.setRotation(0);
		this.mFrontRopeRectangle.setScale(1);
	}
	
		//=================================
		// Public Methods
		//=================================
	public void equipBirds(ArrayList<BaseBirdObject> pListOfBirdObjects) {
		this.mListOfBirdObjects = new ArrayList<BaseBirdObject>();
		this.mListOfBirdObjects.addAll(pListOfBirdObjects);
		this.mCurrentBirdInddex = 0;
		this.mNumOfBird = pListOfBirdObjects.size();
		
		this.mCurrentBirdObject = this.mListOfBirdObjects.get(this.mCurrentBirdInddex);
		this.mCurrentBirdObject.jumpToArbalest(this.mCenterArbalestXY);
		this.mCurrentBirdObject.setBirdChangedPositionListener(this);
		this.mCurrentBirdObject.registryBirdShotListener(this);
	}
	
	public void nextBird() {
		ResourceManager.getInstance().getBaseGameActivity().getEngine().registerUpdateHandler(new TimerHandler(1, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mCurrentBirdInddex++;
				if (mCurrentBirdInddex >= mNumOfBird) {
					mCurrentBirdObject = null;
					
					ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(pTimerHandler);
					return;
				}
				
				mCurrentBirdObject = mListOfBirdObjects.get(mCurrentBirdInddex);
				mCurrentBirdObject.jumpToArbalest(mCenterArbalestXY);
				mCurrentBirdObject.setBirdChangedPositionListener(Arbalest.this);
				mCurrentBirdObject.registryBirdShotListener(Arbalest.this);
				
				ResourceManager.getInstance().getBaseGameActivity().getEngine().unregisterUpdateHandler(pTimerHandler);
			}
		}));
		
	}
}
