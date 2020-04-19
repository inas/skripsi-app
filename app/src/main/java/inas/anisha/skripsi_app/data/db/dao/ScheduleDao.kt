package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(schedule: ScheduleEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(schedule: ScheduleEntity)

    @Query("DELETE FROM schedule WHERE id = :scheduleId")
    fun delete(scheduleId: Long)

    @Query("DELETE FROM schedule")
    fun deleteAll()

    @Query("SELECT * FROM schedule WHERE id= :scheduleId")
    fun get(scheduleId: Long): LiveData<ScheduleEntity>

    @Query("SELECT * from schedule")
    fun getAll(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE type= :type")
    fun getAllOfType(type: Long): LiveData<List<ScheduleEntity>>
}