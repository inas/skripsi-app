package inas.anisha.skripsi_app.data

import android.content.Context
import android.content.SharedPreferences

class AppPreference(mContext: Context) {
    private val mSharedPreferences: SharedPreferences =
        mContext.getSharedPreferences("inas.anisha.skripsi_app", Context.MODE_PRIVATE)

    val shouldShowOnboarding: Boolean = mSharedPreferences.getBoolean(SHOULD_SHOW_ONBOARDING, true)
    fun setShouldNotShowOnboarding() = setBoolean(SHOULD_SHOW_ONBOARDING, false)

    fun removeKey(key: String) {
        this.mSharedPreferences.edit().remove(key).apply()
    }

    private fun setString(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    private fun setBoolean(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    companion object {
        private const val SHOULD_SHOW_ONBOARDING = "SHOULD_SHOW_ONBOARDING"

        @Volatile
        private var instance: AppPreference? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AppPreference(context.applicationContext)
            }

    }
}