package com.artybyte.test.whacamole

import android.content.SharedPreferences
import com.artybyte.test.whacamole.observe.IPreferencesLoadedEventObservable
import com.artybyte.test.whacamole.observe.PreferencesLoadedEventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameState(sharedPrefsName: String,
                sharedPrefs: SharedPreferences,
                gameScoreRecord: Int=0,
                gameScoreCurrent: Int=0,
                override val preferencesLoadedEventObservers: ArrayList<PreferencesLoadedEventObserver> = arrayListOf()
): IPreferencesLoadedEventObservable {

    private var GameScoreRecord = 0;
    private var GameScoreCurrent = 0;
    private val SharedPrefs: SharedPreferences
    private val SharedPrefsName = sharedPrefsName

    init {
        GameScoreRecord = gameScoreRecord
        GameScoreCurrent = gameScoreCurrent
        SharedPrefs = sharedPrefs

        CoroutineScope(Dispatchers.Default).launch {
            loadGameScore()

            delay(1500L)
            proceedMainFragment()
        }
    }

    private fun loadGameScore(){
        if (SharedPrefs.contains("score_record")) GameScoreRecord =
            ( SharedPrefs.getString(SharedPrefsName, "score_record")!! ).toInt()

        if (SharedPrefs.contains("score_current")) GameScoreCurrent =
            ( SharedPrefs.getString(SharedPrefsName, "score_current")!! ).toInt()
    }

    private fun proceedMainFragment(){
        sendUpdateEvent()
    }
}