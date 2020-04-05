package inas.anisha.skripsi_app.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = [], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var mInstance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val i = mInstance
            i?.let { return i }

            return synchronized(AppDatabase) {
                val i2 = mInstance
                if (i2 != null) {
                    i2
                } else {
                    val created = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "app_database"
                    ).build()
                    mInstance = created
                    created
                }
            }
        }
    }

}