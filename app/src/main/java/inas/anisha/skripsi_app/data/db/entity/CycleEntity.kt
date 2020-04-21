package inas.anisha.skripsi_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "cycle")
@Parcelize
data class CycleEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "number") val number: Int = 0,
    @ColumnInfo(name = "completion") val completion: Int = 0,
    @ColumnInfo(name = "reflection") val reflection: String = ""
) : Parcelable