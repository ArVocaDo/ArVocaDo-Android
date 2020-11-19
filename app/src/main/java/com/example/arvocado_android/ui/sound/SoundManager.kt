package com.example.arvocado_android.ui.sound

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool

public class SoundManager(context : Context, mSoundPool: SoundPool ) {
    private var mSoundPoolMap = HashMap<Integer,Integer>()
    private val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE)

    public fun addSound(index :Int , soundId:Int) {

    }

}