package com.xdpm.angrybirds.gamelevel.object.barrier;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.xdpm.angrybirds.gamelevel.object.PhysicsObject;
import com.xdpm.angrybirds.manager.ResourceManager;

public abstract class BaseBarrierSmash extends PhysicsObject<TiledSprite> {

	public BaseBarrierSmash(float pX, float pY, ITiledTextureRegion pTiledTextureRegion) {
		super(pX, pY);
		this.mEntity = new TiledSprite(pX, pY, pTiledTextureRegion, ResourceManager.getInstance().getBaseGameActivity().getVertexBufferObjectManager());
		
		int tile = MathUtils.random(0, 2);
		this.mEntity.setCurrentTileIndex(tile);
		
		//this.createBody(BodyType.DynamicBody, true, true);
	}

	@Override
	public void onBeginContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostSolve(float impulse) {
		// TODO Auto-generated method stub
		
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

}
