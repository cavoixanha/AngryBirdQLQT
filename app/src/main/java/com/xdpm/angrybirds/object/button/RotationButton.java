package com.xdpm.angrybirds.object.button;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

import com.xdpm.angrybirds.common.ButtonConstants;
import com.xdpm.angrybirds.common.Constants;
import com.xdpm.angrybirds.manager.ResourceManager;

public class RotationButton extends BaseButton<Sprite> {

	// =======================================================
	// Properties
	// =======================================================

	public enum Orientation {
		Vertical, Horizontal
	}

	protected Rectangle mMenuSprite;
	private Sprite mForegroundSprite;

	private MoveModifier mExpandMenuMoveModifier;
	private MoveModifier mShrinkMenuMoveModifier;

	private Orientation mOrientation;

	protected Entity mMenuItems;

	// =======================================================
	// Constructors
	// =======================================================

	public RotationButton(float pX, float pY, float pScale,
			ITextureRegion pTextureRegion, Orientation pOrientation) {
		super(pX, pY, pScale);

		VertexBufferObjectManager vertexBufferObjectManager = ResourceManager
				.getInstance().getBaseGameActivity()
				.getVertexBufferObjectManager();
		this.mOrientation = pOrientation;

		// Background
		TextureRegion backgroundTextureRegion = ResourceManager.getInstance().getButtonSpriteSheetTexturePackTextureRegionLibrary().get(ButtonConstants.MENU_BACKGROUND_BUTTON_ID);
		this.mEntity = new Sprite(pX, pY, backgroundTextureRegion,
				vertexBufferObjectManager);

		// Background Menu
		this.mMenuSprite = new Rectangle(4, 0, this.mEntity.getWidth() - 8, this.mEntity.getHeight(), vertexBufferObjectManager);
		this.mMenuSprite.setColor(Color.BLACK);
		this.mMenuSprite.setAlpha(0.4f);
		this.mMenuSprite.setZIndex(-100);
		this.mMenuSprite.setVisible(false);

		if (Orientation.Vertical == pOrientation) {
			this.mMenuSprite.setRotation(0);
		} else {
			this.mMenuSprite.setRotation(90);
		}

		// Foreground
		this.mForegroundSprite = new Sprite(
				(backgroundTextureRegion.getWidth() - pTextureRegion.getWidth()) / 2,
				(backgroundTextureRegion.getHeight() - pTextureRegion.getHeight()) / 2, pTextureRegion, vertexBufferObjectManager);

		this.mForegroundSprite.setUserData(this);
		ResourceManager.getInstance().getHUD().registerTouchArea(this.mForegroundSprite);

		this.mEntity.attachChild(this.mMenuSprite);
		this.mEntity.attachChild(this.mForegroundSprite);
	}

	// =======================================================
	// Methods from/for SuperClass/Interface
	// =======================================================

	@Override
	public void onButtonClick() {
		if (!this.mIsClicked) {
			this.mForegroundSprite.registerEntityModifier(new RotationModifier(
					Constants.MENU_ANIMATION_DURATION, 0, 180));
			this.showMenu();
		} else {
			this.mForegroundSprite.registerEntityModifier(new RotationModifier(
					Constants.MENU_ANIMATION_DURATION, 180, 0));
			this.hideMenu();
		}
		this.mIsClicked = !this.mIsClicked;
	}

	private void showMenu() {
		this.mMenuSprite.setVisible(true);
		if (Orientation.Vertical == this.mOrientation) {
			this.mExpandMenuMoveModifier = new MoveModifier(
					Constants.MENU_ANIMATION_DURATION, this.mMenuSprite.getX(),
					this.mMenuSprite.getX(), this.mEntity.getHeight() * 0.5f,
					- this.mMenuSprite.getHeight() + this.mEntity.getHeight() * 0.5f);
		} else {
			this.mExpandMenuMoveModifier = new MoveModifier(
					Constants.MENU_ANIMATION_DURATION,
					this.mEntity.getWidth() * 0.5f,
					this.mMenuSprite.getHeight(), this.mMenuSprite.getY(),
					this.mMenuSprite.getY());
		}

		this.mExpandMenuMoveModifier.setAutoUnregisterWhenFinished(true);
		this.mMenuSprite.registerEntityModifier(this.mExpandMenuMoveModifier);
	}

	private void hideMenu() {
		if (Orientation.Vertical == this.mOrientation) {
			this.mShrinkMenuMoveModifier = new MoveModifier(
					Constants.MENU_ANIMATION_DURATION, this.mMenuSprite.getX(),
					this.mMenuSprite.getX(), -this.mMenuSprite.getHeight() + this.mEntity.getHeight() * 0.5f,
					this.mEntity.getHeight() * 0.5f,
					new IEntityModifier.IEntityModifierListener() {

						@Override
						public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1) {
							mMenuSprite.setVisible(false);
						}

						@Override
						public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1) {

						}
					});
		} else {
			this.mShrinkMenuMoveModifier = new MoveModifier(
					Constants.MENU_ANIMATION_DURATION,
					this.mMenuSprite.getHeight(),
					this.mEntity.getWidth() * 0.5f, this.mMenuSprite.getY(),
					this.mMenuSprite.getY(),
					new IEntityModifier.IEntityModifierListener() {

						@Override
						public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1) {
						}

						@Override
						public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1) {
							mMenuSprite.setVisible(false);
						}
					});
		}

		this.mShrinkMenuMoveModifier.setAutoUnregisterWhenFinished(true);
		this.mMenuSprite.registerEntityModifier(this.mShrinkMenuMoveModifier);
	}
}
