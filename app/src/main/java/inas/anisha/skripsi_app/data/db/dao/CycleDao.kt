package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import inas.anisha.skripsi_app.data.db.entity.CycleEntity

@Dao
interface CycleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(cycle: CycleEntity)

    @Query("DELETE FROM cycle")
    fun deleteAll()

    @Query("SELECT * FROM cycle")
    fun getAll(): LiveData<List<CycleEntity>>

    @Query("SELECT * FROM cycle ORDER BY id DESC LIMIT 1")
    fun getLatest(): LiveData<CycleEntity>
}