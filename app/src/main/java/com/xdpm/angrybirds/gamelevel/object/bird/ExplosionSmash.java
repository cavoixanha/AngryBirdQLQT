package com.xdpm.angrybirds.gamelevel.object.bird;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.xdpm.angrybirds.gamelevel.object.PhysicsObject;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public class ExplosionSmash extends PhysicsObject<Rectangle> {

	private float mVelocity = 0;
	
	public ExplosionSmash(float pX, float pY) {
		super(pX, pY);
		this.mEntity = new Rectangle(pX, pY, 20, 20, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		this.mEntity.setAlpha(0);
		this.createBody(BodyType.DynamicBody, true, true);
	}

	public void setVelocity(float pVelocity) {
		this.mVelocity = pVelocity;
	}
	
	@Override
	public void createBody(BodyType pBodyType, boolean pUpdatePosition,
			boolean pUpdateRotation) {
		this.mBody = PhysicsFactory.createBoxBody(GameLevelManager.getInstance().getPhysicsWorld(), 
				this.mEntity, pBodyType, 
				PhysicsFactory.createFixtureDef(100f, 0.5f, 0.5f));
		this.mPhysicsConnector = new PhysicsConnector(this.mEntity, this.mBody, pUpdatePosition, pUpdateRotation);
		this.mBody.setUserData(this);
	}

	@Override
	public void onBeginContact(Contact contact) {

	}

	@Override
	public void onEndContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostSolve(float impulse) {
		
	}

	@Override
	public void onPreSolve(Contact contact, Manifold manifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAreaTouched(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
	}
	
	public void boom() {
		this.mBody.setLinearVelocity(this.mVelocity, this.mVelocity);
		
		GameLevelManager.getInstance().getMainGameScene().registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(pTimerHandler);
				destroy(false);
			}
		}));
	}

}
