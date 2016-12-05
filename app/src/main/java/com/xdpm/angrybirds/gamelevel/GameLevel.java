package com.xdpm.angrybirds.gamelevel;

import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.gamelevel.object.arbalest.Arbalest;
import com.xdpm.angrybirds.gamelevel.object.barrier.BaseBarrierObject;
import com.xdpm.angrybirds.gamelevel.object.barrier.BaseBarrierObject.IOnBarrierDestroyedListener;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject.IOnBirdDestroyedListener;
import com.xdpm.angrybirds.gamelevel.object.pig.BasePigObject;
import com.xdpm.angrybirds.gamelevel.object.pig.BasePigObject.IOnPigDestroyedListener;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.GameScoreManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.scene.MainGameScene;
import com.xdpm.angrybirds.utils.XMLLevelLoader;

public class GameLevel implements IOnBirdDestroyedListener, IOnPigDestroyedListener, IOnBarrierDestroyedListener {

	// =========================================================
	// Properties
	// =========================================================
	
	private MainGameScene mMainGameScene = GameLevelManager.getInstance().getMainGameScene();
	private PhysicsWorld mPhysicsWorld = GameLevelManager.getInstance().getPhysicsWorld();
	
	private int mWorldId;
	private int mStageId;
	private int mLevelId;
	
	private Entity mBehindLayer;
	private Entity mObjectLayer;
	private Entity mFrontLayer;
	
	private Rectangle mGround;
	private Body mGroundBody;
	
	private ArrayList<BaseBirdObject> mListOfBirds;
	private ArrayList<BasePigObject> mListOfPigs;
	private ArrayList<BaseBarrierObject> mListOfBarriers;
	private Arbalest mArbalest;
	
	private GameLevel mNextLevel;
	private boolean mIsHasNext;
	
	private XMLLevelLoader mLevelLoader;
	
	private boolean mIsLevelFinish = false;
	
	// =================================================================
	// Constructors
	// =================================================================
	
	public GameLevel(int pWorldId, int pStageId, int pLevelId) {
		this.mWorldId = pWorldId;
		this.mStageId = pStageId;
		this.mLevelId = pLevelId;
		
		this.mBehindLayer = new Entity();
		this.mObjectLayer = new Entity();
		this.mFrontLayer = new Entity();
		this.mLevelLoader = new XMLLevelLoader(this);
		
		//Level.setGameLevel(this);
	}
	
	// =================================================================
	// Getters & Setters
	// =================================================================
	
	public int getWorldId() {
		return this.mWorldId;
	}
	
	public int getStageId() {
		return this.mStageId;
	}
	
	public int getLevelId() {
		return this.mLevelId;
	}

	public GameLevel getNextLevel() {
		return this.mNextLevel;
	}
	
	public void setNextLevel(GameLevel pGameLevel) {
		this.mNextLevel = pGameLevel;
	}
	
	public boolean isHasNext() {
		return this.mIsHasNext;
	}
	
	public void setHasNext(boolean pHasNext) {
		this.mIsHasNext = pHasNext;
	}
	
	public Entity getBehindLayer() {
		return this.mBehindLayer;
	}
	
	public Entity getObjectLayer() {
		return this.mObjectLayer;
	}
	
	public Entity getFrontLayer() {
		return this.mFrontLayer;
	}

	// =================================================================
	// Methods
	// =================================================================
	
	@Override
	public void onBirdDestroyed(BaseBirdObject pBaseBirdObject) {
		if (this.mListOfBirds.contains(pBaseBirdObject)) {
			pBaseBirdObject.clearBirdDestroyedListener();
			pBaseBirdObject.clearBirdShotListener();
			this.mListOfBirds.remove(pBaseBirdObject);
			
			if (this.isLevelCleared() && !this.mIsLevelFinish) {
				this.mIsLevelFinish = true;
				this.onLevelCleared();
				GameLevelManager.getInstance().onLevelCleared();
				return;
			}
			
			if (this.isLevelFailed() && !this.mIsLevelFinish) {
				this.mIsLevelFinish = true;
				this.onLevelFailed();
				GameLevelManager.getInstance().onLevelFailed();
			}
		}
	}

	@Override
	public void onBarrierDestroyed(BaseBarrierObject pBaseBarrierObject) {
		if (this.mListOfBarriers.contains(pBaseBarrierObject)) {
			pBaseBarrierObject.clearBarrierDestroyedListener();
			this.mListOfBarriers.remove(pBaseBarrierObject);
		}
	}

	@Override
	public void onPigDestroyed(BasePigObject pBasePigObject) {
		if (this.mListOfPigs.contains(pBasePigObject)) {
			pBasePigObject.clearPigDestroyedListener();
			this.mListOfPigs.remove(pBasePigObject);
			
			if (this.isLevelCleared() && !this.mIsLevelFinish) {
				this.mIsLevelFinish = true;
				this.onLevelCleared();
				GameLevelManager.getInstance().onLevelCleared();
				return;
			}
			
			if (this.isLevelFailed() && !this.mIsLevelFinish) {
				this.mIsLevelFinish = true;
				this.onLevelFailed();
				GameLevelManager.getInstance().onLevelFailed();
			}
		}
	}
	
	public void onLoadGameLevel() {
		this.mLevelLoader.load();
		
		this.mIsLevelFinish = false;
		
		this.mGround = new Rectangle(- Constants.CAMERA_BOUND_WIDTH * 0.5f, 
				Constants.CAMERA_BOUND_HEIGHT
				- ResourceManager.getInstance().getGroundParallelTextureRegion().getHeight(),
				Constants.CAMERA_BOUND_WIDTH * 2, 1, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		this.mGround.setAlpha(0);
		this.mGround.setZIndex(-1000);
		
		this.mGroundBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, this.mGround, BodyType.StaticBody, Constants.GROUND_FIXTURE_DEF);
		PhysicsConnector groundPhysicsConnector = new PhysicsConnector(this.mGround, this.mGroundBody, false, false);
		this.mPhysicsWorld.registerPhysicsConnector(groundPhysicsConnector);
		this.mBehindLayer.attachChild(this.mGround);
		
		this.mArbalest = this.mLevelLoader.getArbalest();
		this.mListOfBarriers = this.mLevelLoader.getBarrierList();
		this.mListOfBirds = this.mLevelLoader.getBirdList();
		this.mListOfPigs = this.mLevelLoader.getPigList();
		
		if (null != this.mArbalest) {
			this.mBehindLayer.attachChild(this.mArbalest.getBehindLayer());
			this.mFrontLayer.attachChild(this.mArbalest.getFrontLayer());
		}
		
		if (null != this.mListOfBirds && null != this.mListOfPigs && null != this.mListOfBarriers) {
			for (BaseBirdObject bird : this.mListOfBirds) {
				bird.registryBirdDestroyedListener(this);
				this.mObjectLayer.attachChild(bird.getEntity());
				this.mMainGameScene.registerUpdateHandler(bird);
				this.mPhysicsWorld.registerPhysicsConnector(bird.getPhysicsConnector());
			}
			for (BasePigObject pig : this.mListOfPigs) {
				pig.registryPigDestroyedListener(this);
				this.mObjectLayer.attachChild(pig.getEntity());
				this.mMainGameScene.registerUpdateHandler(pig);
				this.mPhysicsWorld.registerPhysicsConnector(pig.getPhysicsConnector());
			}
			for (BaseBarrierObject barrier : this.mListOfBarriers) {
				barrier.registryBarrierDestroyedListener(this);
				this.mObjectLayer.attachChild(barrier.getEntity());
				this.mPhysicsWorld.registerPhysicsConnector(barrier.getPhysicsConnector());
			}
		}
		
		
	}
	
	public void onShowGameLevel() {
		
		this.mMainGameScene.attachChild(this.mBehindLayer);
		this.mMainGameScene.attachChild(this.mObjectLayer);
		this.mMainGameScene.attachChild(this.mFrontLayer);
		
		this.mArbalest.equipBirds(this.mListOfBirds);
		ResourceManager.getInstance().getCamera().setArbalest(this.mArbalest);
		GameScoreManager.getInstance().onBegineCalculating();
	}
	
	public void onHideGameLevel() {

	}
	
	public void onUnLoadGameLevel() {	
		
		this.mArbalest.getBehindLayer().detachChildren();
		this.mArbalest.getBehindLayer().detachSelf();
		this.mArbalest.getBehindLayer().dispose();
		
		this.mArbalest.getFrontLayer().detachChildren();
		this.mArbalest.getFrontLayer().detachSelf();
		this.mArbalest.getFrontLayer().dispose();
		
		this.mPhysicsWorld.destroyBody(this.mGroundBody);
		this.mGround.detachSelf();
		this.mGround.dispose();
		
		for (int i = 0; i < this.mListOfBirds.size(); i++) {
			BaseBirdObject bird = this.mListOfBirds.get(i);
			bird.clearBirdDestroyedListener();
			bird.clearBirdShotListener();
			
			this.mMainGameScene.unregisterUpdateHandler(bird);
			if (null != bird.getBody()) {
				this.mPhysicsWorld.destroyBody(bird.getBody());
			}
			
			if (null != bird.getPhysicsConnector()) {
				this.mPhysicsWorld.unregisterPhysicsConnector(bird.getPhysicsConnector());
			}
			
			bird.getEntity().detachSelf();
			bird.getEntity().dispose();
		}
		
		for (int i = 0; i < this.mListOfPigs.size(); i++) {
			BasePigObject pig = this.mListOfPigs.get(i);
			pig.clearPigDestroyedListener();
			
			this.mMainGameScene.unregisterUpdateHandler(pig);
			if (null != pig.getBody()) {
				this.mPhysicsWorld.destroyBody(pig.getBody());
			}
			
			if (null != pig.getPhysicsConnector()) {
				this.mPhysicsWorld.unregisterPhysicsConnector(pig.getPhysicsConnector());
			}
	
			pig.getEntity().detachSelf();
			pig.getEntity().dispose();
		}
		
		for (int i = 0; i < this.mListOfBarriers.size(); i++) {
			BaseBarrierObject barrier = this.mListOfBarriers.get(i);
			barrier.clearBarrierDestroyedListener();
			
			if (null != barrier.getBody()) {
				this.mPhysicsWorld.destroyBody(barrier.getBody());
			}
			
			if (null != barrier.getPhysicsConnector()) {
				this.mPhysicsWorld.unregisterPhysicsConnector(barrier.getPhysicsConnector());
			}
			
			barrier.getEntity().detachSelf();
			barrier.getEntity().dispose();
		}
		
		this.mBehindLayer.detachChildren();
		this.mObjectLayer.detachChildren();
		this.mFrontLayer.detachChildren();

		this.mBehindLayer.detachSelf();
		this.mObjectLayer.detachSelf();
		this.mFrontLayer.detachSelf();
		
		this.mBehindLayer.dispose();
		this.mObjectLayer.dispose();
		this.mFrontLayer.dispose();
		
		this.mListOfBarriers.clear();
		this.mListOfBirds.clear();
		this.mListOfPigs.clear();
		
		this.mBehindLayer = null;
		this.mObjectLayer = null;
		this.mFrontLayer = null;
		
		this.mListOfBarriers = null;
		this.mListOfBirds = null;
		this.mListOfPigs = null;
		
		this.mLevelLoader = null;
		
		ResourceManager.getInstance().getCamera().removeChasedObject();
		GameScoreManager.getInstance().onEndCalculating();
	}
	
	public void onLevelCleared() {
		for (int i = 0; i < this.mListOfBirds.size(); i++) {
			this.mListOfBirds.get(i).showRemainScore();
			GameScoreManager.getInstance().increaseScoreForBirdsRemain();
		}
	}
	
	public void onLevelFailed() {
		
	}
	
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		for (BaseBirdObject bird : this.mListOfBirds) {
			bird.onSceneTouchEvent(pSceneTouchEvent);
		}
	}
	
	public int getRemainBirds() {
		return this.mListOfBirds.size();
	}
	
	public boolean isLevelCleared() {
		return this.mListOfBirds.size() > 0 && this.mListOfPigs.size() <= 0;
	}
	
	public boolean isLevelFailed() {
		return this.mListOfBirds.size() <= 0 && this.mListOfPigs.size() > 0;
	}
}