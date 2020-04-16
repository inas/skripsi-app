package inas.anisha.skripsi_app.data.db.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity

@Dao
interface TargetPendukungDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg targetEntity: TargetPendukungEntity)

    @Delete
    fun delete(targetEntity: TargetPendukungEntity)

    @Query("DELETE FROM target_pendukung")
    fun deleteAll()

    @Query("SELECT * from target_pendukung")
    fun getAll(): MutableLiveData<MutableList<TargetPendukungEntity>>
}