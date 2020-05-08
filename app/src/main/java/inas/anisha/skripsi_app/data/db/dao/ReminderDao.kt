package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import inas.anisha.skripsi_app.data.db.entity.ReminderEntity

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(reminder: ReminderEntity)

    @Query("DELETE FROM reminder WHERE schedule_id = :scheduleId")
    fun delete(scheduleId: Long): Int

    @Query("DELETE FROM reminder")
    fun deleteAll()

    @Query("SELECT * FROM reminder WHERE schedule_id = :scheduleId ")
    fun get(scheduleId: Long): LiveData<ReminderEntity>

    @Query("SELECT * from reminder")
    fun getAll(): List<ReminderEntity>
}