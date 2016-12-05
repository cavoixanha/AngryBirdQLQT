package com.xdpm.angrybirds.manager;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import com.xdpm.angrybirds.common.BarrierConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.common.ScoreConstants;
import com.xdpm.angrybirds.scene.MainGameScene;

public class GameScoreManager {

	// =================================================================
	// Properties
	// =================================================================
	private static GameScoreManager mGameGoalManager;
	
	private MainGameScene mMainGameScene;
	
	private int mTotalScore = 0;
	
	private Rectangle mScoreLayer;
	private Text mScoreLabel;
	private Text mScoreText;
	private Text mHighScoreLabel;
	private Text mHighScoreText;
	
	// =================================================================
	// Constructors
	// =================================================================
	
	private GameScoreManager() {
		
	}
	
	// =================================================================
	// Getters & Setters
	// =================================================================
	
	public static GameScoreManager getInstance() {
		if (null == mGameGoalManager) {
			mGameGoalManager = new GameScoreManager();
		}
		return mGameGoalManager;
	}
	
	public void setMainGameScene(MainGameScene pMainGameScene) {
		this.mMainGameScene = pMainGameScene;
	}
	
	public int getTotalScore() {
		return this.mTotalScore;
	}
	
	// =================================================================
	// Methods from/for SuperClass/Interface
	// =================================================================

	public void onBegineCalculating() {
		
		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager(); 
		
		this.mScoreLayer = new Rectangle(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT, 
				vertexBufferObjectManager);
		this.mScoreLayer.setAlpha(0);
		
		this.mScoreLabel = new Text(0, 0, ResourceManager.getInstance().getAngrybirdFont48(), "SCORE",
				vertexBufferObjectManager);
		this.mScoreLabel.setPosition(this.mScoreLayer.getWidth() - this.mScoreLabel.getWidth(), 0);
		
		this.mScoreText = new Text(0, 0, ResourceManager.getInstance().getAngrybirdFont48(), "0", 9,
				vertexBufferObjectManager);
		this.mScoreText.setPosition(this.mScoreLayer.getWidth() - this.mScoreText.getWidth(), this.mScoreLabel.getHeight() + 10);
		this.mScoreText.setTextOptions(new TextOptions(HorizontalAlign.RIGHT));
		
		this.mScoreLayer.attachChild(this.mScoreLabel);
		this.mScoreLayer.attachChild(this.mScoreText);
		
		ResourceManager.getInstance().getHUD().attachChild(this.mScoreLayer);
	}
	
	public void onEndCalculating() {
		this.mTotalScore = 0;
		
		this.mScoreLabel.detachSelf();
		this.mScoreLabel.dispose();
		
		this.mScoreText.detachSelf();
		this.mScoreText.dispose();
		
		this.mScoreLayer.detachSelf();
		this.mScoreLayer.dispose();
	}
	
	public void increaseScoreForDestroyPig() {
		this.mTotalScore += ScoreConstants.DESTROY_PIG_SCORE;
		this.drawScore();
	}
	
	public int increaseScoreForDamage(float impulse) {
		int integerImpulse = Math.round(impulse);
		int score = integerImpulse / ScoreConstants.MIN_DAMAGE * ScoreConstants.UNIT_SCORE;
		if (score >= ScoreConstants.UNIT_SCORE) {
			this.mTotalScore += score;
			this.drawScore();
			return score;
		}
		return 0;
	}
	
	public int getScoreByDamage(float impulse) {
		int integerImpulse = Math.round(impulse);
		return integerImpulse / ScoreConstants.MIN_DAMAGE * ScoreConstants.UNIT_SCORE;
	}
	
	public void increaseScoreForBirdsRemain() {
		this.mTotalScore += ScoreConstants.BIRD_REMAIN_SCORE;
		this.drawScore();
	}
	
	public void resetScore() {
		this.mTotalScore = 0;
		this.drawScore();
	}
	
	private void drawScore() {
		ResourceManager.getInstance().getBaseGameActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mScoreText.setText(Integer.toString(mTotalScore));
				mScoreText.setPosition(mScoreLayer.getWidth() - mScoreText.getWidth(), mScoreText.getY());
			}
		});
	}
}
