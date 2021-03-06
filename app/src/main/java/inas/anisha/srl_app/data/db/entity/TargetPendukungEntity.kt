package inas.anisha.srl_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "target_pendukung")
@Parcelize
data class TargetPendukungEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "note") val note: String = "",
    @ColumnInfo(name = "time") val time: String = "",
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean = false
) : Parcelable