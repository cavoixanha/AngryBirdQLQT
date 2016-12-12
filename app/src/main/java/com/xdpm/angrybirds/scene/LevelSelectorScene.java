package com.xdpm.angrybirds.scene;

import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.texturepack.TexturePackTextureRegionLibrary;

import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.common.MenuConstants;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.manager.SceneManager;
import com.xdpm.angrybirds.object.button.LevelSelectorButton;

public class LevelSelectorScene extends BaseScene{

	private Entity mBacgroundEntity;
	private Entity mStageChooseButtonEntity;
	private Entity mOtherButtonEntity;

	private int mWorldId = 1;
	private int mStageId = 1;
	private ArrayList<Color> backgroundColorList;

	private int iStage = 1; // so khung canh chua cac man nho
	private int iRow = 2; // so dong cua button cua man choi nho
	private int iCol = 7; // so cot cua button chon man choi nho
	//==================================================
	// Constructors
	//==================================================
	public LevelSelectorScene(){
		super(true);
	}
	
//	public LevelSelectorScene(int pWorld){
//		super(true);
//		mWorldId = pWorld;
//	}
	
	public LevelSelectorScene(boolean pIsUnloadedOnHidden){
		super(pIsUnloadedOnHidden);
	}
	
	public void addStageChoosers() {
		int countStage = 0; // so luong man choi ban dau la 0
		TexturePackTextureRegionLibrary menuTexturePackTextureRegionLibrary = ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary();
		for(int stage = 0; stage < iStage; stage++){ //need to change
			for(int i = 0; i < iRow; i++){	//need to change
				for(int j = 0; j < iCol; j++){ //need to change
					// du 10 man choi nho thi dung
					if(countStage == 10){
						return;
					}
					countStage++;
					TiledSprite levelChooser = new TiledSprite(stage * Constants.CAMERA_WIDTH + 
							j * Constants.CAMERA_WIDTH/8 + Constants.CAMERA_WIDTH/16,
							i * Constants.CAMERA_HEIGHT/4 + Constants.CAMERA_HEIGHT/12, 
							menuTexturePackTextureRegionLibrary.get(MenuConstants.LEVEL_SELECTOR_ID, 7, 1), 
							ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
					LevelSelectorButton mbsLevelChooser = new LevelSelectorButton(levelChooser.getX(), 
							levelChooser.getY(), 1, 7 * i + j + 1, levelChooser);	//contruct and set level
					mbsLevelChooser.getEntity().setScale(0.8f);
					mbsLevelChooser.setStageId(stage + 1);	//set stage
					mbsLevelChooser.setWorldId(mWorldId);	//set world
					
					this.mStageChooseButtonEntity.attachChild(mbsLevelChooser.getEntity());
					// chi cho phep cham vao nhung man choi da mo khoa
					if(i == 0 && j == 0){// day la man dau tien
						this.registerTouchArea(mbsLevelChooser.getEntity());
					}
				}
			}
		}
	}
	
	public void setStageButtonPositionAccordingFistStageButton(){
		Sprite firstStage = (Sprite)this.mStageChooseButtonEntity.getChildByIndex(0);
		for(int count = 1; count < this.mStageChooseButtonEntity.getChildCount(); count++){
			Sprite temp = (Sprite)this.mStageChooseButtonEntity.getChildByIndex(count);
			int world = count / (iCol * iRow);
			int stage = count % (iCol * iRow);
			int row = stage / iCol;
			int col = stage % iCol;
			temp.setPosition(firstStage.getX() + world * Constants.CAMERA_WIDTH + col * Constants.CAMERA_WIDTH/8,
							row * Constants.CAMERA_HEIGHT/4 + Constants.CAMERA_HEIGHT/12);
		}
	}
	
	public void moveAllStageButtonPosition(float direcMove){
		for(int count = 0; count < this.mStageChooseButtonEntity.getChildCount(); count++){
			Sprite temp = (Sprite)this.mStageChooseButtonEntity.getChildByIndex(count);
			temp.registerEntityModifier(new MoveModifier(0.25f, temp.getX(), temp.getX() + direcMove, temp.getY(), temp.getY()));
		}
	}
	// ham de load ngoi sao va so thu tu man nho nhu la: 1,2,3....
	public void loadAllLevelStatus(){
		int iUnlock = 1; // so luong man da mo khoa
		for(int count = 0; count < iUnlock; count++){
			TiledSprite temp = (TiledSprite)this.mStageChooseButtonEntity.getChildByIndex(count);
			temp.setCurrentTileIndex(mWorldId % 6 + 1);
			// button cai dat ngoi sao
			TiledSprite starts = new TiledSprite(5, temp.getHeight() - 25, 
					ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary().get(MenuConstants.START_SCORE_ID, 1, 3), 
					ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
			// neu count % 3 bang 0 thi 3 sao, bang 1 thi 2 sao, bang 2 thi 1 sao
			starts.setCurrentTileIndex(count % 3);
			
			Text LevelNum = new Text(0, 0, ResourceManager.getInstance().getAngrybirdFont48(), (count + 1) + "", ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
			LevelNum.setPosition(temp.getWidth()/2 - LevelNum.getWidth()/2, temp.getHeight()*0.4f - LevelNum.getHeight()/2);

			// khoa lai vi khi chay lan dau tien khong co ngoi sao
			//temp.attachChild(starts);
			temp.attachChild(LevelNum);
		}
	}

	private Vector2 startTouchPoint;
	private Vector2 oldButtonPos;
	boolean isTouchDown = false;
	boolean isTouchMove = false;
	// ham cham de thay doi khung canh
	// khoa lai vi co 1 khung canh nen su thay doi nay la khong can thiet
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		
		/*if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN){
			startTouchPoint = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			oldButtonPos = new Vector2(this.mStageChooseButtonEntity.getChildByIndex(0).getX(),
					this.mStageChooseButtonEntity.getChildByIndex(0).getY());

			isTouchDown = true;
		}
		else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_MOVE){
			if(isTouchDown == true){
				isTouchMove = true;
				Vector2 nowTouchPoint = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				Vector2 directionMove = new Vector2(nowTouchPoint.sub(startTouchPoint));
				if(directionMove.len() > 25){ 	//25 la khoang cach de di chuyen
					this.mStageChooseButtonEntity.getChildByIndex(0).setPosition(oldButtonPos.x + directionMove.x, oldButtonPos.y);
					setStageButtonPositionAccordingFistStageButton();
				}
				return true;
			}
			return false;
		}
		else if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP){
			if(isTouchDown == true){
				Vector2 nowTouchPoint = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				Vector2 directionMove = new Vector2(nowTouchPoint.sub(startTouchPoint));
				Sprite firstStage = (Sprite)this.mStageChooseButtonEntity.getChildByIndex(0);
				if(directionMove.len() > 25){	//25 la khoang cach de di chuyen
					float direcMove;
					if(firstStage.getX() > 0){
						if(directionMove.x > 0){
							direcMove = -directionMove.x;
						}
						else{
							direcMove = -Constants.CAMERA_WIDTH - firstStage.getX();
							mStageId++;
						}
					}
					else{
						if(firstStage.getX() < -Constants.CAMERA_WIDTH * (3 - 1)){ //need to change 3
							direcMove = -directionMove.x;
						}
						else{
							int firstStageX = (int) (firstStage.getX() % Constants.CAMERA_WIDTH);
							if(directionMove.x > 0){
								direcMove = 0 + Constants.DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON - firstStageX;
								mStageId--;
							}
							else{
								direcMove = -Constants.CAMERA_WIDTH + Constants.DISTANCE_TO_LEFT_SCREEN_OF_LEVEL_BUTTON - firstStageX;
								mStageId ++;
							}
						}
					}
					Debug.e(mStageId + "");
					moveAllStageButtonPosition(direcMove);
					ChangeBackground();
					return true;
				}
				else{
					
				}
				
				isTouchDown = false;
				
//				if(isTouchMove == true){
//					isTouchMove = false;	//set isTouchMove = false before return
//					return true;
//				}else{
//					//Do not thing to return super.onSceneTouchEvent
//				}
			}
			else{
				return false;
			}
		}*/
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}
	
	public int getLevelID(){
		return mWorldId;
	}
	
	public void ChangeBackground(){
		int indexColor = (mWorldId - 1) * 3 + mStageId - 1;
		indexColor = indexColor % backgroundColorList.size();
		if(indexColor < 0){
			indexColor = 0;
		}
		this.setBackground(new Background(backgroundColorList.get(indexColor)));
		Debug.e("Index color: " + mWorldId + " " + mStageId + " " + indexColor);
	}

	@Override
	public void onLoadScene(OnLoadSceneCallBack pOnLoadSceneCallBack)
			throws Exception {
		Debug.e("onLoadScene");
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager();
		TexturePackTextureRegionLibrary menuTexturePackTextureRegionLibrary = ResourceManager.getInstance().getMenuSpriteSheetTexturePackTextureRegionLibrary();
		

		mWorldId = ResourceManager.getInstance().getBaseGameActivity()
				.getSharedPreferences(Constants.SHARED_PREF_LEVELSELECTOR, 0).getInt("WorldId", 0);
		Debug.e("World ID is " + mWorldId);
		
		//reset mStageId when reload
		mStageId = 1;
		ContructBackgroundColorList();
		//Call this method to set background color
		this.ChangeBackground();
		this.setBackgroundEnabled(true);
		
		final Sprite backgroudGroupLevel = new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
				ResourceManager.getInstance().getGroundPaneTextureRegion(), 
				ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		
		final ButtonSprite backButtonSelector = new ButtonSprite(0, Constants.CAMERA_HEIGHT*8.4f/10,
				menuTexturePackTextureRegionLibrary.get(MenuConstants.BACK_SELECTOR_BUTTON_ID),
				vertexBufferObjectManager);
		backButtonSelector.setScale(0.8f);
		backButtonSelector.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(ButtonSprite arg0, float arg1, float arg2) {
					Debug.e("Show previous Scene");
					
					//ChangeBackground();
					SceneManager.getInstance().showPreviousScene();
				}
			});
		this.registerTouchArea(backButtonSelector);

		this.mBacgroundEntity = new Entity();
		this.mBacgroundEntity.attachChild(backgroudGroupLevel);


		this.mStageChooseButtonEntity = new Entity();
		addStageChoosers();
		
		loadAllLevelStatus();
		
		this.mOtherButtonEntity = new Entity();
		this.mOtherButtonEntity.attachChild(backButtonSelector);
		
		pOnLoadSceneCallBack.onLoadSceneFinish();
	}

	private void ContructBackgroundColorList() {
		backgroundColorList = new ArrayList<Color>();
		backgroundColorList.add(new Color(0.50f, 0.75f, 0.50f));
		backgroundColorList.add(new Color(0.75f, 0.75f, 0.50f));
		backgroundColorList.add(new Color(0.25f, 0.50f, 0.50f));
		backgroundColorList.add(new Color(0.50f, 0.50f, 0.75f));
		backgroundColorList.add(new Color(0.75f, 0.25f, 0.75f));
		backgroundColorList.add(new Color(0.25f, 0.25f, 0.75f));
		backgroundColorList.add(new Color(0.50f, 0.75f, 0.25f));
		backgroundColorList.add(new Color(0.75f, 0.75f, 0.25f));
	}

	@Override
	public void onShowScene(OnShowSceneCallBack pOnShowSceneCallBack)
			throws Exception {
		// TODO Auto-generated method stub
		Debug.e("onShowScene");
		
		ResourceManager.getInstance().getCamera().setSelectorCamera();
		
		/*
		 * Attack Child
		 */
		this.attachChild(mBacgroundEntity);
		this.attachChild(mStageChooseButtonEntity);
		this.attachChild(mOtherButtonEntity);
		
		pOnShowSceneCallBack.onShowSceneFinish();
	}

	@Override
	public void onHideScene(OnHideSceneCallBack pOnHideSceneCallBack)
			throws Exception {
		// TODO Auto-generated method stub
		Debug.e("onHideScene");
		
		//this.detachChildren();
		//this.clearTouchAreas();
		pOnHideSceneCallBack.onHideSceneFinish();
	}

	@Override
	public void onUnLoadScene(OnUnLoadSceneCallBack pOnUnLoadSceneCallBack)
			throws Exception {
		// TODO Auto-generated method stub
		
		Debug.e("onUnLoadScene");
		
		this.detachChildren();
		this.clearChildScene();
		this.clearUpdateHandlers();
		this.clearEntityModifiers();
		this.clearTouchAreas();
		
		pOnUnLoadSceneCallBack.onUnLoadSceneFinish();
	}
}
