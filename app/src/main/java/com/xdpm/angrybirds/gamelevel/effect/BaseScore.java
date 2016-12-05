package com.xdpm.angrybirds.gamelevel.effect;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.util.modifier.IModifier;

import com.xdpm.angrybirds.manager.GameLevelManager;
import com.xdpm.angrybirds.manager.ResourceManager;

public abstract class BaseScore<T extends IEntity> {
	protected T mEntity;
	
	public T getEntity() {
		return this.mEntity;
	}
	
	public void showScore() {
		ParallelEntityModifier entityModifier = new ParallelEntityModifier(
				new IEntityModifier.IEntityModifierListener() {					
					@Override
					public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1) {

					}
					
					@Override
					public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1) {
						ResourceManager.getInstance().getBaseGameActivity().getEngine().runOnUpdateThread(new Runnable() {
							
							@Override
							public void run() {
								mEntity.detachSelf();
								mEntity.dispose();
							}
						});
					}
				},
				new ScaleModifier(1f, 0.25f, 1),
				new MoveYModifier(1f, this.mEntity.getY(), this.mEntity.getY() - 32)
				);
		entityModifier.setAutoUnregisterWhenFinished(true);
		
		this.mEntity.registerEntityModifier(entityModifier);
		GameLevelManager.getInstance().getCurrentGameLevel().getFrontLayer().attachChild(this.mEntity);
	}
}
