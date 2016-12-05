package com.xdpm.angrybirds.manager;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;

import com.xdpm.angrybirds.common.CloudConstants;
import com.xdpm.angrybirds.gamelevel.effect.BirdContactCloud;
import com.xdpm.angrybirds.gamelevel.effect.BirdFlyingCloud;
import com.xdpm.angrybirds.gamelevel.effect.ObjectDestroyBlackCloud;
import com.xdpm.angrybirds.gamelevel.effect.ObjectDestroyWhiteCloud;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject;
import com.xdpm.angrybirds.gamelevel.object.bird.BaseBirdObject.IOnBirdFlyingListener;
import com.xdpm.angrybirds.gamelevel.object.pig.BasePigObject;
import com.xdpm.angrybirds.scene.MainGameScene;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class EffectManager {
	//==================================================
	// Properties
	//==================================================
	
	private static ArrayList<BirdFlyingCloud> birdFlyingClouds = new ArrayList<BirdFlyingCloud>();
	
	//==================================================
	// Constructors
	//==================================================
	
	
	//==================================================
	// Getters & Setter
	//==================================================
	
	//==================================================
	// Methods
	//==================================================
	
	// Contact Effect

	public static void showBirdContactEffect(BaseBirdObject pBaseBirdObject) {
		BirdContactCloud cloud1 = new BirdContactCloud(0, 0);
		BirdContactCloud cloud2 = new BirdContactCloud(0, 0);
		BirdContactCloud cloud3 = new BirdContactCloud(0, 0);
		BirdContactCloud cloud4 = new BirdContactCloud(0, 0);
		BirdContactCloud cloud5 = new BirdContactCloud(0, 0);
		
		cloud1.getEntity().setPosition(pBaseBirdObject.getEntity().getX() - cloud1.getEntity().getWidth() * 0.5f, 
										pBaseBirdObject.getEntity().getY() - cloud1.getEntity().getHeight() * 0.5f);
		cloud2.getEntity().setPosition(pBaseBirdObject.getEntity().getX() + pBaseBirdObject.getEntity().getWidth() - cloud2.getEntity().getWidth() * 0.5f, 
										pBaseBirdObject.getEntity().getY() - cloud2.getEntity().getHeight() * 0.5f);
		cloud3.getEntity().setPosition(pBaseBirdObject.getEntity().getX() + pBaseBirdObject.getEntity().getWidth() - cloud3.getEntity().getWidth() * 0.5f,
										pBaseBirdObject.getEntity().getY() + pBaseBirdObject.getEntity().getHeight() - cloud3.getEntity().getHeight() * 0.5f);
		cloud4.getEntity().setPosition(pBaseBirdObject.getEntity().getX() - cloud4.getEntity().getWidth() * 0.5f, 
										pBaseBirdObject.getEntity().getY() + pBaseBirdObject.getEntity().getHeight() - cloud4.getEntity().getHeight() * 0.5f);
		cloud5.getEntity().setPosition(pBaseBirdObject.getEntity().getX() + Math.abs(pBaseBirdObject.getEntity().getWidth() - cloud5.getEntity().getWidth()) * 0.5f, 
										pBaseBirdObject.getEntity().getY() - Math.abs(pBaseBirdObject.getEntity().getHeight() - cloud5.getEntity().getHeight()) * 0.5f);
		
		Entity frontLayer = GameLevelManager.getInstance().getCurrentGameLevel().getFrontLayer();
		MainGameScene mainGameScene = GameLevelManager.getInstance().getMainGameScene();
		
		frontLayer.attachChild(cloud1.getEntity());
		frontLayer.attachChild(cloud2.getEntity());
		frontLayer.attachChild(cloud3.getEntity());
		frontLayer.attachChild(cloud4.getEntity());
		frontLayer.attachChild(cloud5.getEntity());
		
		mainGameScene.registerUpdateHandler(cloud1);
		mainGameScene.registerUpdateHandler(cloud2);
		mainGameScene.registerUpdateHandler(cloud3);
		mainGameScene.registerUpdateHandler(cloud4);
		mainGameScene.registerUpdateHandler(cloud5);
	}
	
	// Destroy Effect
	
	public static void showBirdDestroyEffect(BaseBirdObject pBaseBirdObject) {
		final ObjectDestroyWhiteCloud cloud = new ObjectDestroyWhiteCloud(0, 0);
		cloud.getEntity().setX(pBaseBirdObject.getEntity().getX() - Math.abs(cloud.getEntity().getWidth() - pBaseBirdObject.getEntity().getWidth()) * 0.5f);
		cloud.getEntity().setY(pBaseBirdObject.getEntity().getY() - Math.abs(cloud.getEntity().getHeight() - pBaseBirdObject.getEntity().getHeight()) * 0.5f);
		
		GameLevelManager.getInstance().getCurrentGameLevel().getFrontLayer().attachChild(cloud.getEntity());
		cloud.startEffect();
		
		GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(1.0f * CloudConstants.DESTROY_CLOUD_ANIMATION_DURATION / 1000, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHanlder) {
				GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(pTimerHanlder);
				ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						cloud.getEntity().detachSelf();
						cloud.getEntity().dispose();
					}
				});
			}
		}));
	}
	
	public static void showPigDestroyEffect(BasePigObject pBaseBirdObject) {
		final ObjectDestroyBlackCloud cloud = new ObjectDestroyBlackCloud(0, 0);
		cloud.getEntity().setX(pBaseBirdObject.getEntity().getX() - Math.abs(cloud.getEntity().getWidth() - pBaseBirdObject.getEntity().getWidth()) * 0.5f);
		cloud.getEntity().setY(pBaseBirdObject.getEntity().getY() - Math.abs(cloud.getEntity().getHeight() - pBaseBirdObject.getEntity().getHeight()) * 0.5f);
		
		GameLevelManager.getInstance().getCurrentGameLevel().getFrontLayer().attachChild(cloud.getEntity());
		cloud.startEffect();
		
		GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHanlder) {
				GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(pTimerHanlder);
				ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						cloud.getEntity().detachSelf();
						cloud.getEntity().dispose();
					}
				});
			}
		}));
	}
	
	// Misc
	public static void showBirdFlyingEffect(BaseBirdObject pBaseBirdObject) {
		BirdFlyingCloud cloud;
		if (birdFlyingClouds.size() % 2 == 0) {
			cloud = new BirdFlyingCloud(0, 0, 2);
		} else {
			cloud = new BirdFlyingCloud(0, 0, 3);
		}
		
		cloud.getEntity().setX(pBaseBirdObject.getEntity().getX() + Math.abs(cloud.getEntity().getWidth() - pBaseBirdObject.getEntity().getWidth()) * 0.5f);
		cloud.getEntity().setY(pBaseBirdObject.getEntity().getY() + Math.abs(cloud.getEntity().getHeight() - pBaseBirdObject.getEntity().getHeight()) * 0.5f);
		
		birdFlyingClouds.add(cloud);
		
		GameLevelManager.getInstance().getCurrentGameLevel().getBehindLayer().attachChild(cloud.getEntity());
	}
	
	public static void hideBirdFlyingEffect() {		
		for (final BirdFlyingCloud cloud : birdFlyingClouds) {
			GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(0.005f, new ITimerCallback() {				
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
						
						@Override
						public void run() {
							cloud.getEntity().detachSelf();
							cloud.getEntity().dispose();	
						}
					});
					GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(pTimerHandler);
				}
			}));
		}
		birdFlyingClouds.clear();
	}
}
