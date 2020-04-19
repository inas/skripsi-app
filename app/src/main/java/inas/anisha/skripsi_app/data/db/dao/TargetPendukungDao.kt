package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity

@Dao
interface TargetPendukungDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg targetEntity: TargetPendukungEntity)

    @Query("UPDATE target_pendukung SET name = :name, note = :note, time = :time, isCompleted = :isCompleted WHERE id = :targetId")
    fun update(targetId: Long, name: String, note: String, time: String, isCompleted: Boolean)

    @Query("DELETE FROM target_pendukung WHERE id = :targetId")
    fun delete(targetId: Long)

    @Query("DELETE FROM target_pendukung")
    fun deleteAll()

    @Query("SELECT * FROM target_pendukung WHERE id= :targetId ")
    fun get(targetId: Long): LiveData<TargetPendukungEntity>

    @Query("SELECT * from target_pendukung")
    fun getAll(): LiveData<List<TargetPendukungEntity>>
}