package com.artybyte.test.whacamole

import android.content.SharedPreferences
import android.media.MediaPlayer
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
            proceedMainFragment()
        }
    }

    fun getGameRecordScore(): Int {
        return GameScoreRecord
    }

    fun getGameScore(): Int {
        return GameScoreCurrent
    }

    fun setGameRecordScore(recordScore: Int) {
        GameScoreRecord = recordScore
    }

    fun setGameScore(currentScore: Int) {
        GameScoreCurrent = currentScore
    }

    fun writeAndSaveGameRecord(){
        SharedPrefs.edit().putInt("score_record", GameScoreRecord).apply()
    }

    fun writeAndSaveGameCurrentScore(score: Int){
        GameScoreCurrent = score
        SharedPrefs.edit().putInt("score_current", score).apply()
    }

    private fun loadGameScore(){
        if (SharedPrefs.contains("score_record")) GameScoreRecord =
            SharedPrefs.getInt("score_record", 0)

        if (SharedPrefs.contains("score_current")) GameScoreCurrent =
            SharedPrefs.getInt("score_current", 0)
    }

    private fun proceedMainFragment(){
        sendUpdateEvent()
    }
}