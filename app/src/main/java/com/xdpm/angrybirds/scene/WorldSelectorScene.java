package com.xdpm.angrybirds.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.common.MenuConstants;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SceneManager;

public class WorldSelectorScene extends BaseScene{
	
	private Entity mBacgroundEntity;
	private Entity mWorldSelectorButtonEntity;
	private Entity mOtherButtonEntity;

	private int mWorldId = 1;
	//==================================================
	// Constructors
	//==================================================
	public WorldSelectorScene(){
		super(true);
	}
	
	public WorldSelectorScene(boolean pIsUnloadedOnHidden){
		super(pIsUnloadedOnHidden);
	}
	
	public void setButtonSpritePositionAccordingFirstWorldSelector(){
		for(int i = 1; i < this.mWorldSelectorButtonEntity.getChildCount(); i++){
			this.mWorldSelectorButtonEntity.getChildByIndex(i).setPosition(this.mWorldSelectorButtonEntity.getChildByIndex(0).getX() 
					+ i * ResourceManager.getInstance().getOneLevelSelectorTextureRegion().getWidth() + 5, 
					this.mWorldSelectorButtonEntity.getChildByIndex(0).getY());
		}
	}
	
	public void moveAllButtonSpriteAcordingDirection(float direcMove){
		for(int count = 0; count < this.mWorldSelectorButtonEntity.getChildCount(); count++){
			ButtonSprite temp = (ButtonSprite)this.mWorldSelectorButtonEntity.getChildByIndex(count);
			temp.registerEntityModifier(new MoveModifier(0.25f, temp.getX(), temp.getX() + direcMove, temp.getY(), temp.getY()));
		}
	}
	
	private Vector2 startTouchPoint;
	private Vector2 oldButtonPos;
	private boolean isTouchDown = false;
	//private boolean isTouchMove = false;
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
	
		if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
			startTouchPoint = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			oldButtonPos = new Vector2(this.mWorldSelectorButtonEntity.getChildByIndex(0).getX(), 
					this.mWorldSelectorButtonEntity.getChildByIndex(0).getY());
			
			isTouchDown = true;
		}
		else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_MOVE){
			if(isTouchDown == true){
				//isTouchMove = true;
				Vector2 nowTouchPoint = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				Vector2 directionMove = new Vector2(nowTouchPoint.sub(startTouchPoint));
				this.mWorldSelectorButtonEntity.getChildByIndex(0).setPosition(oldButtonPos.x + directionMove.x, oldButtonPos.y);
	
				setButtonSpritePositionAccordingFirstWorldSelector();
			}
		}
		else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP){
			if(isTouchDown == true){
				float direcMove = 0;
				if(this.mWorldSelectorButtonEntity.getChildByIndex(0).getX() > Constants.DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON){
					direcMove = Constants.DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON - this.mWorldSelectorButtonEntity.getChildByIndex(0).getX();
				}
				ButtonSprite temp = (ButtonSprite)this.mWorldSelectorButtonEntity.getChildByIndex(0);
				if(this.mWorldSelectorButtonEntity.getChildByIndex(0).getX() < Constants.CAMERA_WIDTH - 
						Constants.DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON - 
						(this.mWorldSelectorButtonEntity.getChildCount()) * (temp.getWidth() + 5)){
					direcMove = Constants.CAMERA_WIDTH - Constants.DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON -
						(this.mWorldSelectorButtonEntity.getChildCount()) * (temp.getWidth() + 5) - 
						this.mWorldSelectorButtonEntity.getChildByIndex(0).getX();
				}
				
				if(direcMove != 0){
					moveAllButtonSpriteAcordingDirection(direcMove);
				}
				//set isTouchDown = false before we can return
				isTouchDown = false;
				Vector2 nowTouchPoint = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				Vector2 directionMove = new Vector2(nowTouchPoint.sub(startTouchPoint));
				
				if(Math.abs(directionMove.x) > 10){
					//isTouchMove = false;	//set isTouchMove = false before return
					return true;
				}else{
					//Do not thing to return super.onSceneTouchEvent
				}
			}
		}
		else{
			isTouchDown = false;
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}
	
	public void addWordSelector(){
		for(int i = 0; i < 11; i++){
			ButtonSprite worldSelector = new ButtonSprite(Constants.DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON,
					Constants.DISTANCE_TO_TOP_SCREEN_OF_LEVEL_BUTTON, 
					ResourceManager.getInstance().getmLeveSelectorSheetTexturePackTextureRegionLibrary().get(i),
					ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
			worldSelector.setScale(0.8f);
			
			TiledSprite ScorePanel = new TiledSprite(0, 0, 
					ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary().get(MenuConstants.WORLD_START_ID, 3, 1), 
					ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
			ScorePanel.setCurrentTileIndex(0);
			ScorePanel.setPosition((worldSelector.getWidth() - ScorePanel.getWidth())/2, worldSelector.getHeight() * 0.63f);
			
			TiledSprite StartsPanel = new TiledSprite(0, 0, 
					ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary().get(MenuConstants.WORLD_START_ID, 3, 1), 
					ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
			StartsPanel.setCurrentTileIndex(1);
			StartsPanel.setPosition((worldSelector.getWidth() - StartsPanel.getWidth())/2, ScorePanel.getY() + ScorePanel.getHeight() + 5);
			
			TiledSprite EndPanel = new TiledSprite(0, 0, 
					ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary().get(MenuConstants.WORLD_START_ID, 3, 1), 
					ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
			EndPanel.setCurrentTileIndex(2);
			EndPanel.setPosition((worldSelector.getWidth() - EndPanel.getWidth())/2, StartsPanel.getY() + StartsPanel.getHeight() + 5);
			
			worldSelector.attachChild(ScorePanel);
			worldSelector.attachChild(StartsPanel);
			worldSelector.attachChild(EndPanel);
			
			this.mWorldSelectorButtonEntity.attachChild(worldSelector);
		}
	}

	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack)
			throws Exception {
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		TexturePackTextureRegionLibrary menuTexturePackTextureRegionLibrary = ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary();
		
		this.setBackground(new Background(0, 0.75f, 1));
		this.setBackgroundEnabled(true);
		
		final Sprite backgroudGroupLevel = new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
				ResourceManager.getInstance().getGroundPaneTextureRegion(), 
				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());

//		final ButtonSprite levelOneSelector = new ButtonSprite(Constants.DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON,
//				Constants.DISTANCE_TO_TOP_SCREEN_OF_LEVEL_BUTTON, 
//				ResourceManager.getInstance().getOneLevelSelectorTextureRegion(),
//				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
//		levelOneSelector.setScale(0.8f);
//		
//		final ButtonSprite levelTwoSelector = new ButtonSprite(levelOneSelector.getX() + levelOneSelector.getWidth() + 5,
//				levelOneSelector.getY(), 
//				ResourceManager.getInstance().getTwoLevelSelectorTextureRegion(),
//				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
//		levelTwoSelector.setScale(0.8f);
//		
//		final ButtonSprite levelThreeSelector = new ButtonSprite(levelOneSelector.getX() + 2 * levelOneSelector.getWidth() + 5,
//				levelOneSelector.getY(), 
//				ResourceManager.getInstance().getThreeLevelSelectorTextureRegion(),
//				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
//		levelThreeSelector.setScale(0.8f);
//		
//		final ButtonSprite levelFourSelector = new ButtonSprite(levelOneSelector.getX() + 3 * levelOneSelector.getWidth() + 5,
//				levelOneSelector.getY(), 
//				ResourceManager.getInstance().getFourLevelSelectorTextureRegion(),
//				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
//		levelFourSelector.setScale(0.8f);
//		
//		final ButtonSprite levelFiveSelector = new ButtonSprite(levelOneSelector.getX() + 4 * levelOneSelector.getWidth() + 5,
//				levelOneSelector.getY(), 
//				ResourceManager.getInstance().getFiveLevelSelectorTextureRegion(),
//				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
//		levelFiveSelector.setScale(0.8f);
//		
//		final ButtonSprite levelSixSelector = new ButtonSprite(levelOneSelector.getX() + 5 * levelOneSelector.getWidth() + 5,
//				levelOneSelector.getY(), 
//				ResourceManager.getInstance().getSixLevelSelectorTextureRegion(),
//				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
//		levelSixSelector.setScale(0.8f);
//		
//		final ButtonSprite levelSevenSelector = new ButtonSprite(levelOneSelector.getX() + 6 * levelOneSelector.getWidth() + 5,
//				levelOneSelector.getY(), 
//				ResourceManager.getInstance().getSevenLevelSelectorTextureRegion(),
//				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
//		levelSevenSelector.setScale(0.8f);
		
		//back button sprite
		final ButtonSprite backButtonSelector = new ButtonSprite(0, Constants.CAMERA_HEIGHT*8.4f/10, 
				menuTexturePackTextureRegionLibrary.get(MenuConstants.BACK_SELECTOR_BUTTON_ID),
				vertexBufferObjectManager);
		backButtonSelector.setScale(0.8f);
		backButtonSelector.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(ButtonSprite arg0, float arg1, float arg2) {
					Debug.e("Show previous Scene");
					SceneManager.getInstance().showPreviousScene();
				}
			});
		this.registerTouchArea(backButtonSelector);
		
		//back world selector button
		final ButtonSprite backWorldSelector = new ButtonSprite(0, Constants.CAMERA_HEIGHT * 4 / 10, 
				menuTexturePackTextureRegionLibrary.get(MenuConstants.TURN_SELECTOR_BUTTON_ID, 2, 1),
				vertexBufferObjectManager);
		backWorldSelector.setScale(0.8f);
		backWorldSelector.setCurrentTileIndex(0);
		backWorldSelector.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(ButtonSprite arg0, float arg1, float arg2) {
					backWorldSelector.setCurrentTileIndex(0);
					moveAllButtonSpriteAcordingDirection(Constants.CAMERA_WIDTH/5f);
				}
			});
		this.registerTouchArea(backWorldSelector);
		
		//forward world selector button
		final ButtonSprite forwardWorldSelector = new ButtonSprite(Constants.CAMERA_WIDTH - 70, Constants.CAMERA_HEIGHT * 4 / 10, 
				menuTexturePackTextureRegionLibrary.get(MenuConstants.TURN_SELECTOR_BUTTON_ID, 2, 1),
				vertexBufferObjectManager);
		forwardWorldSelector.setScale(0.8f);
		forwardWorldSelector.setCurrentTileIndex(1);
		forwardWorldSelector.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(ButtonSprite arg0, float arg1, float arg2) {
					forwardWorldSelector.setCurrentTileIndex(1);
					moveAllButtonSpriteAcordingDirection(-Constants.CAMERA_WIDTH/5f);
				}
			});
		this.registerTouchArea(forwardWorldSelector);
		
		this.mBacgroundEntity = new Entity();
		this.mBacgroundEntity.attachChild(backgroudGroupLevel);
		
		this.mWorldSelectorButtonEntity = new Entity();
		addWordSelector();
		setButtonSpritePositionAccordingFirstWorldSelector();
//		this.mWorldSelectorButtonEntity.attachChild(levelOneSelector);
//		this.mWorldSelectorButtonEntity.attachChild(levelTwoSelector);
//		this.mWorldSelectorButtonEntity.attachChild(levelThreeSelector);
//		this.mWorldSelectorButtonEntity.attachChild(levelFourSelector);
//		this.mWorldSelectorButtonEntity.attachChild(levelFiveSelector);
//		this.mWorldSelectorButtonEntity.attachChild(levelSixSelector);
//		this.mWorldSelectorButtonEntity.attachChild(levelSevenSelector);

		this.mOtherButtonEntity = new Entity();
		this.mOtherButtonEntity.attachChild(backButtonSelector);
		this.mOtherButtonEntity.attachChild(backWorldSelector);
		this.mOtherButtonEntity.attachChild(forwardWorldSelector);
		
		for(int i = 0; i < this.mWorldSelectorButtonEntity.getChildCount(); i++){
			ButtonSprite btnSprite = (ButtonSprite) this.mWorldSelectorButtonEntity.getChildByIndex(i);
			final int temp = i;
			btnSprite.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(ButtonSprite arg0, float arg1, float arg2) {
					mWorldId = temp + 1;
					Debug.e("show level " + mWorldId);
					ResourceManager.getInstance().getBaseGameActivity()
					.getSharedPreferences(Constants.SHARED_PREF_LEVELSELECTOR, 0).edit()
					.putInt("WorldId", mWorldId).apply();
					
					SceneManager.getInstance().showLevelSelectorScene();
				}
			});
			this.registerTouchArea(btnSprite);
		}
		
		pOnLoadSceneCallBack.onLoadSceneFinish();
	}

	@Override
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {
		// TODO Auto-generated method stub
		ResourceManager.getInstance().getCamera().setSelectorCamera();
		/*
		 * Register onclick again when reload
		 */
//		for(int i = 0; i < this.mWorldSelectorButtonEntity.getChildCount(); i++){
//			ButtonSprite btnSprite = (ButtonSprite) this.mWorldSelectorButtonEntity.getChildByIndex(i);
//			
//			this.registerTouchArea(btnSprite);
//		}
		
		/*
		 * AttackChild in there to right mean
		 */
		this.attachChild(mBacgroundEntity);
		this.attachChild(mWorldSelectorButtonEntity);
		this.attachChild(mOtherButtonEntity);
		
		
		pOnShowSceneCallBack.onShowSceneFinish();
	}

	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		// TODO Auto-generated method stub
		/*
		 * DeAttackChild here for save memory
		 */
		//this.detachChildren();
		//this.clearTouchAreas();
		
		pOnHideSceneCallBack.onHideSceneFinish();
	}

	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack)
			throws Exception {
		this.detachChildren();
		this.clearChildScene();
		this.clearUpdateHandlers();
		this.clearEntityModifiers();
		this.clearTouchAreas();
		
		pOnUnLoadSceneCallBack.onUnLoadSceneFinish();
	}
}
