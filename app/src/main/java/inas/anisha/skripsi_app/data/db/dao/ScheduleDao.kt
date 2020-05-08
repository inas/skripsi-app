package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import java.util.*

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg schedule: ScheduleEntity): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(schedule: ScheduleEntity)

    @Query("UPDATE schedule SET is_on_time = :isOntime WHERE id = :scheduleId")
    fun updateOnTimeStatus(scheduleId: Long, isOntime: Boolean)

    @Query("UPDATE schedule SET is_completed = :isCompleted WHERE id = :scheduleId")
    fun updateCompleteness(scheduleId: Long, isCompleted: Boolean)

    @Query("DELETE FROM schedule WHERE id = :scheduleId")
    fun delete(scheduleId: Long)

    @Query("DELETE FROM schedule")
    fun deleteAll()

    @Query("SELECT * FROM schedule WHERE id = :scheduleId")
    fun get(scheduleId: Long): LiveData<ScheduleEntity>

    @Query("SELECT * from schedule")
    fun getAll(): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE type = :type ORDER BY end_date")
    fun getAll(type: Int): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE type = :type AND end_date < :dateLimit")
    fun getAllBeforeDate(type: Int, dateLimit: Calendar): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE type = :type AND end_date >= :dateLimit")
    fun getAllAfterDate(type: Int, dateLimit: Calendar): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE end_date BETWEEN :start AND :end")
    fun getAll(start: Calendar, end: Calendar): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE type = :type AND end_date BETWEEN :start AND :end ORDER BY end_date")
    fun getAll(start: Calendar, end: Calendar, type: Int): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE type = :type ORDER BY start_date")
    fun getAllSorted(type: Int): LiveData<List<ScheduleEntity>>

    @Query("SELECT * from schedule WHERE end_date BETWEEN :start AND :end ORDER BY start_minute_of_day")
    fun getAllSorted(start: Calendar, end: Calendar): LiveData<List<ScheduleEntity>>

    @Query("SELECT COUNT(*) FROM schedule WHERE end_date BETWEEN :start AND :end AND type = :type")
    fun getCount(start: Calendar, end: Calendar, type: Int): LiveData<Int>

    @Query("SELECT * FROM schedule WHERE type = :type AND start_date < :end AND end_date > :start AND id != :scheduleId")
    fun getOverlappingEntity(
        type: Int,
        start: Calendar,
        end: Calendar,
        scheduleId: Long
    ): List<ScheduleEntity>
}