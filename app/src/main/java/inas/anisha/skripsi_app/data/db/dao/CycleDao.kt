package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import inas.anisha.skripsi_app.data.db.entity.CycleEntity

@Dao
interface CycleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg cycle: CycleEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCycle(cycle: CycleEntity)

    @Query("DELETE FROM cycle")
    fun deleteAll()

    @Query("SELECT * FROM cycle")
    fun getAll(): LiveData<List<CycleEntity>>

    @Query("SELECT * FROM cycle ORDER BY id DESC LIMIT 1")
    fun getLatest(): CycleEntity

    @Query("WITH latest AS (SELECT id FROM cycle ORDER BY id DESC LIMIT 1) UPDATE cycle SET completion = :percentage WHERE id IN latest")
    fun updateCurrentCycleCompleteness(percentage: Int)

    @Query("SELECT COUNT(*) FROM cycle")
    fun getCount(): LiveData<Int>
}