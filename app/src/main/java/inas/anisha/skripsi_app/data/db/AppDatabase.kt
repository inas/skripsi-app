package inas.anisha.skripsi_app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import inas.anisha.skripsi_app.data.db.converter.CalendarConverter
import inas.anisha.skripsi_app.data.db.dao.TargetPendukungDao
import inas.anisha.skripsi_app.data.db.dao.TargetUtamaDao
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity

@Database(entities = [TargetUtamaEntity::class, TargetPendukungEntity::class], version = 1)
@TypeConverters(CalendarConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun targetUtamaDao(): TargetUtamaDao
    abstract fun targetPendukungDao(): TargetPendukungDao

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