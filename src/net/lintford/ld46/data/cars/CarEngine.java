package net.lintford.ld46.data.cars;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.audio.AudioManager;
import net.lintford.library.core.audio.AudioSource;
import net.lintford.library.core.maths.MathHelper;

public class CarEngine {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private float mPitchEngine0 = 1.f;
	private float mPitchEngine1 = 1.f;

	private float mGainEngine0 = 1.f;
	private float mGainEngine1 = 1.f;

	private AudioSource mEngineSource0Low;
	private AudioSource mEngineSource0High;
	private AudioSource mEngineSource1Low;
	private AudioSource mEngineSource1High;

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	public void loadContent(ResourceManager pResourceManager) {

		mEngineSource0Low = pResourceManager.audioManager().getAudioSource(hashCode(), AudioManager.AUDIO_SOURCE_TYPE_SOUNDFX);
		mEngineSource0High = pResourceManager.audioManager().getAudioSource(hashCode(), AudioManager.AUDIO_SOURCE_TYPE_SOUNDFX);
		mEngineSource1Low = pResourceManager.audioManager().getAudioSource(hashCode(), AudioManager.AUDIO_SOURCE_TYPE_SOUNDFX);
		mEngineSource1High = pResourceManager.audioManager().getAudioSource(hashCode(), AudioManager.AUDIO_SOURCE_TYPE_SOUNDFX);

		final var lBuffer0 = pResourceManager.audioManager().getAudioDataBufferByName("SOUND_ENGINE_0_LOW");
		final var lBuffer1 = pResourceManager.audioManager().getAudioDataBufferByName("SOUND_ENGINE_0_HIGH");
		final var lBuffer3 = pResourceManager.audioManager().getAudioDataBufferByName("SOUND_ENGINE_1_LOW");
		final var lBuffer4 = pResourceManager.audioManager().getAudioDataBufferByName("SOUND_ENGINE_1_HIGH");

		mEngineSource0Low.setGain(0);
		mEngineSource1Low.setGain(0);

		mEngineSource0High.setGain(0);
		mEngineSource1High.setGain(0);

		mEngineSource0Low.play(lBuffer0.bufferID());
		mEngineSource0High.play(lBuffer1.bufferID());
		mEngineSource1Low.play(lBuffer3.bufferID());
		mEngineSource1High.play(lBuffer4.bufferID());

		mEngineSource0Low.setLooping(true);
		mEngineSource0High.setLooping(true);
		mEngineSource1Low.setLooping(true);
		mEngineSource1High.setLooping(true);

	}

	public void unloadContent() {
		mEngineSource0Low.unassign(hashCode());
		mEngineSource0High.unassign(hashCode());
		mEngineSource1Low.unassign(hashCode());
		mEngineSource1High.unassign(hashCode());

	}

	public void update(LintfordCore pCore, Car pCar) {
		final float lGameTimeModifer = pCore.time().getGameTimeModifier();
		final float lNormalizedSpeed = pCar.currentSpeedNormalized();

		if (mEngineSource0Low != null) {
			float lHN = lNormalizedSpeed * 1.5f * lNormalizedSpeed;
			mPitchEngine0 = lNormalizedSpeed * lGameTimeModifer;

			mGainEngine0 = (1.f - lNormalizedSpeed) * lGameTimeModifer;

			mEngineSource0Low.setGain(1.f - lNormalizedSpeed);
			mEngineSource0High.setGain(lNormalizedSpeed * 1.3f);

			float s = MathHelper.clamp(-1f + lNormalizedSpeed * 2.f, 0f, 1f);
			mEngineSource1Low.setGain(0.4f + s);
			mEngineSource1High.setGain(0);

			mEngineSource0Low.setPitch(lGameTimeModifer * (1.f + lHN));
			mEngineSource1Low.setPitch(lGameTimeModifer * (1.f + lHN));
			mEngineSource0High.setPitch(lGameTimeModifer * (1.f + lHN));
			mEngineSource1High.setPitch(lGameTimeModifer * (1.f + lHN));

			System.out.println(String.format("%.2f", s));

		}

	}

}
