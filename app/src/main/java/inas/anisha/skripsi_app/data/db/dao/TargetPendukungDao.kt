package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity

@Dao
interface TargetPendukungDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg targetEntity: TargetPendukungEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(targetEntity: TargetPendukungEntity)

    @Query("DELETE FROM target_pendukung WHERE id = :targetId")
    fun delete(targetId: Long)

    @Query("DELETE FROM target_pendukung")
    fun deleteAll()

    @Query("SELECT * FROM target_pendukung WHERE id = :targetId ")
    fun get(targetId: Long): LiveData<TargetPendukungEntity>

    @Query("SELECT * from target_pendukung")
    fun getAll(): LiveData<List<TargetPendukungEntity>>

    @Query("SELECT * from target_pendukung WHERE isCompleted = :isCompleted")
    fun getByCompleteness(isCompleted: Boolean): LiveData<List<TargetPendukungEntity>>
}