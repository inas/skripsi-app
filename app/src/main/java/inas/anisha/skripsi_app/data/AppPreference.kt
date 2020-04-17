package inas.anisha.skripsi_app.data

import android.content.Context
import android.content.SharedPreferences

class AppPreference(mContext: Context) {
    private val mSharedPreferences: SharedPreferences =
        mContext.getSharedPreferences("inas.anisha.skripsi_app", Context.MODE_PRIVATE)

    fun shouldShowKelolaPembelajaran(): Boolean =
        mSharedPreferences.getBoolean(SHOULD_SHOW_KELOLA_PEMBELAJARAN, true)

    fun setShouldNotShowKelolaPembelajaran(): Boolean = mSharedPreferences.edit().putBoolean(
        SHOULD_SHOW_KELOLA_PEMBELAJARAN, false
    ).commit()

    fun getCycleTime(): Pair<Int, Int> {
        val frequency = mSharedPreferences.getInt(CYCLE_FREQUENCY, -1)
        val duration = mSharedPreferences.getInt(CYCLE_DURATION, -1)
        return Pair(frequency, duration)
    }

    fun setCycleTime(cycleTime: Pair<Int, Int>) {
        setInt(CYCLE_FREQUENCY, cycleTime.first)
        setInt(CYCLE_DURATION, cycleTime.second)
    }

    fun removeKey(key: String) {
        this.mSharedPreferences.edit().remove(key).apply()
    }

    private fun setString(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    private fun setInt(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).apply()
    }

    private fun setBoolean(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    companion object {
        private const val SHOULD_SHOW_KELOLA_PEMBELAJARAN = "SHOULD_SHOW_KELOLA_PEMBELAJARAN"
        private const val CYCLE_FREQUENCY = "CYCLE_FREQUENCY"
        private const val CYCLE_DURATION = "CYCLE_DURATION"

        @Volatile
        private var instance: AppPreference? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AppPreference(context.applicationContext)
            }

    }
}