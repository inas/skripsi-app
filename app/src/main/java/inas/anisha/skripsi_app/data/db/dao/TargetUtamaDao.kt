package inas.anisha.skripsi_app.data.db.dao

import androidx.room.*
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity

@Dao
interface TargetUtamaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(targetEntity: TargetUtamaEntity)

    @Delete
    fun delete(targetEntity: TargetUtamaEntity)

    @Query("DELETE FROM target_utama")
    fun deleteOldTarget()

    @Query("SELECT * from target_utama")
    fun getTarget(): TargetUtamaEntity
}