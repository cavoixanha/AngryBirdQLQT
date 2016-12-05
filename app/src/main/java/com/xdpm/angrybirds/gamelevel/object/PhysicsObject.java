package com.xdpm.angrybirds.gamelevel.object;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;
import com.xdpm.angrybirds.object.BaseObject;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public abstract class PhysicsObject<T extends IEntity> extends BaseObject implements IUpdateHandler  {

	protected T mEntity;
	protected Body mBody;
	protected PhysicsConnector mPhysicsConnector;
	
	protected boolean mIsDestroyed = false;
	
	protected float mMaxImpulse = 0;
	
	//==================================================
	// Constructors
	//==================================================
	
	public PhysicsObject(float pX, float pY) {
		super(pX, pY);
	}
	
	//==================================================
	// Getters and Setters
	//==================================================
	
	public Body getBody() {
		return this.mBody;
	}
	
	public void setBody(Body pBody) {
		this.mBody = pBody;
	}
	
	public PhysicsConnector getPhysicsConnector() {
		return this.mPhysicsConnector;
	}
	
	public void setPhysicsConnector(PhysicsConnector pPhysicsConnector) {
		this.mPhysicsConnector = pPhysicsConnector;
	}
	
	public T getEntity() {
		return this.mEntity;
	}
	
	//==================================================
	// Methods
	//==================================================
	public void beginContact(Contact contact) {
		this.onBeginContact(contact);
	}
	
	public void endContact(Contact contact) {
		this.onEndContact(contact);
	}
	
	public void preSolve(Contact contact, Manifold manifold) {
		this.onPreSolve(contact, manifold);
	}
	
	public void postSolve(Contact contact, ContactImpulse contactImpulse) {
		float impulse = contactImpulse.getNormalImpulses()[0];
		for (int i = 1; i < contactImpulse.getNormalImpulses().length; i++) {
			impulse = Math.max(impulse, contactImpulse.getNormalImpulses()[i]);
		}
		
		this.onPostSolve(impulse);
	}
	
	public void destroy(boolean pHasEffect) {
		if (!this.mIsDestroyed) {
			this.mIsDestroyed = true;
			
			if (pHasEffect) {
				this.onDestroy();
			}
			
			GameLevelManager.getInstance().getMainGameScene().unregisterUpdateHandler(this);
			
			ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
				
				@Override
				public void run() {
					GameLevelManager.getInstance().getPhysicsWorld().unregisterPhysicsConnector(mPhysicsConnector);
					GameLevelManager.getInstance().getPhysicsWorld().destroyBody(mBody);
					
					mEntity.detachSelf();
					mEntity.dispose();					
				}
			});
		}
	}
	
	//==================================================
	// Abstract methods
	//==================================================
	
	public abstract void createBody(BodyType pBodyType, boolean pUpdatePosition, boolean pUpdateRotation);
	
	public abstract void onBeginContact(Contact contact);
	
	public abstract void onEndContact(Contact contact);
	
	public abstract void onPostSolve(float impulse);
	
	public abstract void onPreSolve(Contact contact, Manifold manifold);
	
	public abstract void onDestroy();
	
	@Override
	public void onUpdate(float timeElapsed) {
	}
	
	@Override
	public void reset() {
		
	}
	
	//==================================================
	// Interfaces
	//==================================================
	
}
