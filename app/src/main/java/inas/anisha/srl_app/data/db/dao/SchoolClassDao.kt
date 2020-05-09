package inas.anisha.srl_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import inas.anisha.srl_app.data.db.entity.SchoolClassEntity

@Dao
interface SchoolClassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg schoolClass: SchoolClassEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(schoolClass: SchoolClassEntity)

    @Query("DELETE FROM school_class WHERE id = :classId")
    fun delete(classId: Long)

    @Query("DELETE FROM school_class")
    fun deleteAll()

    @Query("SELECT * FROM school_class WHERE id= :classId ")
    fun get(classId: Long): LiveData<SchoolClassEntity>

    @Query("SELECT * from school_class")
    fun getAll(): LiveData<List<SchoolClassEntity>>

    @Query("SELECT * from school_class WHERE day = :dayOfWeek")
    fun getAll(dayOfWeek: Int): LiveData<List<SchoolClassEntity>>

    @Query("SELECT * from school_class WHERE day = :dayOfWeek ORDER BY start_minute_of_day")
    fun getAllSorted(dayOfWeek: Int): LiveData<List<SchoolClassEntity>>

    @Query("SELECT COUNT(*) FROM school_class WHERE day = :dayOfWeek")
    fun getCount(dayOfWeek: Int): LiveData<Int>

    @Query("SELECT * FROM school_class WHERE day = :dayOfWeek AND start_minute_of_day < :end AND end_minute_of_day > :start AND id != :classId")
    fun getOverlappingEntity(
        dayOfWeek: Int,
        start: Int,
        end: Int,
        classId: Long
    ): List<SchoolClassEntity>
}