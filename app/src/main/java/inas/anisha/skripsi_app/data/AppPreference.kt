package inas.anisha.skripsi_app.data

import android.content.Context
import android.content.SharedPreferences

class AppPreference(mContext: Context) {
    private val mSharedPreferences: SharedPreferences =
        mContext.getSharedPreferences("inas.anisha.skripsi_app", Context.MODE_PRIVATE)

    fun shouldShowKelolaPembelajaran(): Boolean = getBoolean(SHOULD_SHOW_KELOLA_PEMBELAJARAN, true)
    fun setShouldNotShowKelolaPembelajaran() = setBoolean(SHOULD_SHOW_KELOLA_PEMBELAJARAN, false)

    fun getEvaluationDate() = getLong(EVALUATION_DATE, -1)
    fun setEvaluationDate(evaluationDate: Long) = setLong(EVALUATION_DATE, evaluationDate)

    fun getCycleTime(): Pair<Int, Int> {
        val frequency = getInt(CYCLE_FREQUENCY, -1)
        val duration = getInt(CYCLE_DURATION, -1)
        return Pair(frequency, duration)
    }

    fun setCycleTime(cycleTime: Pair<Int, Int>) {
        setInt(CYCLE_FREQUENCY, cycleTime.first)
        setInt(CYCLE_DURATION, cycleTime.second)
    }

    fun removeKey(key: String) {
        this.mSharedPreferences.edit().remove(key).apply()
    }

    private fun getString(key: String, defaultValue: String) =
        mSharedPreferences.getString(key, defaultValue)

    private fun setString(key: String, value: String) =
        mSharedPreferences.edit().putString(key, value).apply()

    private fun getInt(key: String, defaultValue: Int) =
        mSharedPreferences.getInt(key, defaultValue)

    private fun setInt(key: String, value: Int) =
        mSharedPreferences.edit().putInt(key, value).apply()

    private fun getBoolean(key: String, defaultValue: Boolean) =
        mSharedPreferences.getBoolean(key, defaultValue)

    private fun setBoolean(key: String, value: Boolean) =
        mSharedPreferences.edit().putBoolean(key, value).apply()

    private fun getLong(key: String, defaultValue: Long) =
        mSharedPreferences.getLong(key, defaultValue)

    private fun setLong(key: String, value: Long) =
        mSharedPreferences.edit().putLong(key, value).apply()

    companion object {
        private const val SHOULD_SHOW_KELOLA_PEMBELAJARAN = "SHOULD_SHOW_KELOLA_PEMBELAJARAN"
        private const val CYCLE_FREQUENCY = "CYCLE_FREQUENCY"
        private const val CYCLE_DURATION = "CYCLE_DURATION"
        private const val EVALUATION_DATE = "EVALUATION_DATE"

        @Volatile
        private var instance: AppPreference? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AppPreference(context.applicationContext)
            }

    }
}