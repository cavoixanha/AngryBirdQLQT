package com.xdpm.angrybirds.gamelevel.object;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * 
 * @author Nguyen Anh Tuan
 *
 */
public class ObjectContactListener implements ContactListener{
	
	@Override
	public void preSolve(Contact contact, Manifold manifold) {
		PhysicsObject objectA = (PhysicsObject) contact.getFixtureA().getBody().getUserData();
		PhysicsObject objectB = (PhysicsObject) contact.getFixtureB().getBody().getUserData();
		if (null != objectA) {
			objectA.preSolve(contact, manifold);
		}
		if (null != objectB) {
			objectB.preSolve(contact, manifold);
		}
	}
	
	@Override
	public void postSolve(Contact contact, ContactImpulse contactImpulse) {
		PhysicsObject objectA = (PhysicsObject) contact.getFixtureA().getBody().getUserData();
		PhysicsObject objectB = (PhysicsObject) contact.getFixtureB().getBody().getUserData();
		if (null != objectA) {
			objectA.postSolve(contact, contactImpulse);
		}
		if (null != objectB) {
			objectB.postSolve(contact, contactImpulse);
		}
	}
	
	@Override
	public void endContact(Contact contact) {
		PhysicsObject objectA = (PhysicsObject) contact.getFixtureA().getBody().getUserData();
		PhysicsObject objectB = (PhysicsObject) contact.getFixtureB().getBody().getUserData();
		if (null != objectA) {
			objectA.endContact(contact);
		}
		if (null != objectB) {
			objectB.endContact(contact);
		}
	}
	
	@Override
	public void beginContact(Contact contact) {
		PhysicsObject objectA = (PhysicsObject) contact.getFixtureA().getBody().getUserData();
		PhysicsObject objectB = (PhysicsObject) contact.getFixtureB().getBody().getUserData();
		if (null != objectA) {
			objectA.beginContact(contact);
		}
		if (null != objectB) {
			objectB.beginContact(contact);
		}
	}
}
