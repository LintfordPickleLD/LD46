package net.lintford.ld46.screens;

import org.lwjgl.opengl.GL11;

import net.lintford.ld46.controllers.CameraCarChaseController;
import net.lintford.ld46.controllers.CarController;
import net.lintford.ld46.controllers.GameStateController;
import net.lintford.ld46.controllers.TrackController;
import net.lintford.ld46.data.GameWorld;
import net.lintford.ld46.renderers.GameStateRenderer;
import net.lintford.ld46.renderers.TrackRenderer;
import net.lintford.library.controllers.box2d.Box2dWorldController;
import net.lintford.library.controllers.camera.CameraZoomController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.renderers.debug.DebugBox2dDrawer;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	// Data
	private GameWorld mGameWorld;

	// Controllers
	private Box2dWorldController mBox2dWorldController;
	private CameraCarChaseController mCameraChaseControler;
	private CarController mCarController;
	private TrackController mTrackController;
	private GameStateController mGameStateController;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public GameScreen(ScreenManager pScreenManager) {
		super(pScreenManager);

		mGameWorld = new GameWorld();

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {
		super.initialize();

		createControllers();

		mGameStateController.startNewGame();

	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		createRenderers();

	}

	@Override
	public void handleInput(LintfordCore pCore, boolean pAcceptMouse, boolean pAcceptKeyboard) {
		super.handleInput(pCore, pAcceptMouse, pAcceptKeyboard);

	}

	@Override
	public void update(LintfordCore pCore, boolean pOtherScreenHasFocus, boolean pCoveredByOtherScreen) {
		super.update(pCore, pOtherScreenHasFocus, pCoveredByOtherScreen);

		if (mGameStateController.getEndConditionFlag() != GameStateController.END_CONDITION_NOT_SET) {
			switch (mGameStateController.getEndConditionFlag()) {
			case GameStateController.END_CONDITION_DESTROYED:
			case GameStateController.END_CONDITION_LOST:
			case GameStateController.END_CONDITION_WON_FIGHTING:
			case GameStateController.END_CONDITION_WON_RACING:
				mScreenManager.exitGame();

			}

		}

	}

	@Override
	public void draw(LintfordCore pCore) {
		GL11.glClearColor(0.03f, 0.37f, 0.13f, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		super.draw(pCore);

		final var lFontUnit = mRendererManager.textFont();
		final var lZoomText = "Camera Zoom: " + pCore.gameCamera().getZoomFactor();
		final var lHudBoundingBox = pCore.HUD().boundingRectangle();
		final var lZoomTextWidth = lFontUnit.bitmap().getStringWidth(lZoomText);

		lFontUnit.begin(pCore.HUD());
		lFontUnit.draw(lZoomText, lHudBoundingBox.w() * .5f - 5.f - lZoomTextWidth, -lHudBoundingBox.h() * 0.5f, 1f);
		lFontUnit.end();

		{ // DEBUG Draw Chase Camera
//			float lCamPosX = mCameraChaseControler.mPosition.x;
//			float lCamPosY = mCameraChaseControler.mPosition.y;
//
//			float lCamDesiredPosX = mCameraChaseControler.mDesiredPosition.x;
//			float lCamDesiredPosY = mCameraChaseControler.mDesiredPosition.y;
//
//			float lCamLookPosX = mCameraChaseControler.mLookAhead.x * 60f;
//			float lCamLookPosY = mCameraChaseControler.mLookAhead.y * 60f;
//
//			Debug.debugManager().drawers().drawPointImmediate(pCore.gameCamera(), lCamPosX, lCamPosY, -0.01f, 1f, 1f, 0f, 1f);
//			Debug.debugManager().drawers().drawPointImmediate(pCore.gameCamera(), lCamDesiredPosX, lCamDesiredPosY, -0.01f, 0f, 0f, 1f, 1f);
//
//			Debug.debugManager().drawers().drawLineImmediate(pCore.gameCamera(), lCamPosX, lCamPosY, lCamPosX + lCamLookPosX, lCamPosY + lCamLookPosY);

		}

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void createControllers() {
		final var lCore = mScreenManager.core();
		final var lControllerManager = lCore.controllerManager();
		final var lGameCamera = lCore.gameCamera();

		mBox2dWorldController = new Box2dWorldController(lControllerManager, mGameWorld.box2dWorld(), entityGroupID());
		mBox2dWorldController.initialize(lCore);

		final var lZoomController = new CameraZoomController(lControllerManager, lGameCamera, entityGroupID());
		lZoomController.setZoomConstraints(0.025f, 50.0f);

		mTrackController = new TrackController(lControllerManager, mGameWorld.trackManager(), entityGroupID());
		mTrackController.initialize(lCore);

		mCarController = new CarController(lControllerManager, mGameWorld.carManager(), entityGroupID());
		mCarController.initialize(lCore);

		// Needs to be called after the carcontroller is initialized
		final var lPlayerCar = mGameWorld.carManager().playerCar();

		mCameraChaseControler = new CameraCarChaseController(lControllerManager, lGameCamera, lPlayerCar, entityGroupID());
		mCameraChaseControler.initialize(lCore);

		// mCameraFollowController = new CameraFollowController(lControllerManager, lGameCamera, lPlayerCar, entityGroupID());
		// mCameraFollowController.initialize(lCore);

		mGameStateController = new GameStateController(lControllerManager, mGameWorld, entityGroupID());
		mGameStateController.initialize(lCore);

	}

	private void createRenderers() {
		final var lCore = mScreenManager.core();

		new TrackRenderer(mRendererManager, entityGroupID()).initialize(lCore);
		new GameStateRenderer(mRendererManager, entityGroupID()).initialize(lCore);
		new DebugBox2dDrawer(mRendererManager, mGameWorld.box2dWorld(), entityGroupID()).initialize(lCore);

	}

}
