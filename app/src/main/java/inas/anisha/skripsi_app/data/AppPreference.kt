package inas.anisha.skripsi_app.data

import android.content.Context
import android.content.SharedPreferences

class AppPreference(mContext: Context) {
    private val mSharedPreferences: SharedPreferences =
        mContext.getSharedPreferences("inas.anisha.skripsi_app", Context.MODE_PRIVATE)

    fun shouldShowKelolaPembelajaran(): Boolean = getBoolean(SHOULD_SHOW_KELOLA_PEMBELAJARAN, true)
    fun setShouldNotShowKelolaPembelajaran() = setBoolean(SHOULD_SHOW_KELOLA_PEMBELAJARAN, false)

    fun shouldShowSchoolScheduleDialog(): Boolean =
        getBoolean(SHOULD_SHOW_ADD_SCHOOL_SCHEDULE_DIALOG, true)

    fun setShouldNotShowSchoolScheduleDialog() =
        setBoolean(SHOULD_SHOW_ADD_SCHOOL_SCHEDULE_DIALOG, false)

    fun shouldShowEndOfCycleWarning(): Boolean = getBoolean(SHOULD_SHOW_END_OF_CYCLE_WARNING, true)
    fun setShouldShowEndOfCycleWarning(shouldShow: Boolean) =
        setBoolean(SHOULD_SHOW_END_OF_CYCLE_WARNING, shouldShow)

    fun shouldShowEvaluationReport(): Boolean = getBoolean(SHOULD_SHOW_EVALUATION_REPORT, true)
    fun setShouldShowEvaluationReport(shouldShow: Boolean) =
        setBoolean(SHOULD_SHOW_EVALUATION_REPORT, shouldShow)

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

    fun getUserName(): String = getString(USER_NAME, "")
    fun setUserName(name: String) = setString(USER_NAME, name)

    fun getUserGrade(): String = getString(USER_GRADE, "")
    fun setUserGrade(grade: String) = setString(USER_GRADE, grade)

    fun getUserStudy(): String = getString(USER_STUDY, "")
    fun setUserStudy(study: String) = setString(USER_STUDY, study)

    fun removeKey(key: String) {
        this.mSharedPreferences.edit().remove(key).apply()
    }

    private fun getString(key: String, defaultValue: String): String =
        mSharedPreferences.getString(key, defaultValue) ?: ""

    private fun setString(key: String, value: String) =
        mSharedPreferences.edit().putString(key, value).apply()

    private fun getInt(key: String, defaultValue: Int): Int =
        mSharedPreferences.getInt(key, defaultValue)

    private fun setInt(key: String, value: Int) =
        mSharedPreferences.edit().putInt(key, value).apply()

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        mSharedPreferences.getBoolean(key, defaultValue)

    private fun setBoolean(key: String, value: Boolean) =
        mSharedPreferences.edit().putBoolean(key, value).apply()

    private fun getLong(key: String, defaultValue: Long): Long =
        mSharedPreferences.getLong(key, defaultValue)

    private fun setLong(key: String, value: Long) =
        mSharedPreferences.edit().putLong(key, value).apply()

    companion object {
        private const val SHOULD_SHOW_KELOLA_PEMBELAJARAN = "SHOULD_SHOW_KELOLA_PEMBELAJARAN"
        private const val SHOULD_SHOW_ADD_SCHOOL_SCHEDULE_DIALOG =
            "SHOULD_SHOW_ADD_SCHOOL_SCHEDULE_DIALOG"
        private const val SHOULD_SHOW_END_OF_CYCLE_WARNING = "SHOULD_SHOW_END_OF_CYCLE_WARNING"
        private const val SHOULD_SHOW_EVALUATION_REPORT = "SHOULD_SHOW_EVALUATION_REPORT"

        private const val CYCLE_FREQUENCY = "CYCLE_FREQUENCY"
        private const val CYCLE_DURATION = "CYCLE_DURATION"
        private const val EVALUATION_DATE = "EVALUATION_DATE"

        private const val USER_NAME = "USER_NAME"
        private const val USER_GRADE = "USER_GRADE"
        private const val USER_STUDY = "USER_STUDY"

        @Volatile
        private var instance: AppPreference? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AppPreference(context.applicationContext)
            }

    }
}