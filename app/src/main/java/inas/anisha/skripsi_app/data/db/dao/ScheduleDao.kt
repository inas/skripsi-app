package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import java.util.*

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg schedule: ScheduleEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(schedule: ScheduleEntity)

    @Query("DELETE FROM schedule WHERE id = :scheduleId")
    fun delete(scheduleId: Int)

    @Query("DELETE FROM schedule")
    fun deleteAll()

    @Query("SELECT * FROM schedule WHERE id = :scheduleId")
    fun get(scheduleId: Int): LiveData<ScheduleEntity>

    @Query("SELECT * from schedule")
    fun getAll(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE type = :type")
    fun getAll(type: Int): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE type = :type AND end_date >= :dateLimit")
    fun getAll(type: Int, dateLimit: Calendar): LiveData<List<ScheduleEntity>>
}