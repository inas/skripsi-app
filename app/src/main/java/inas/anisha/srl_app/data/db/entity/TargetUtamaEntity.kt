package inas.anisha.srl_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "target_utama")
@Parcelize
data class TargetUtamaEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "note") val note: String = "",
    @ColumnInfo(name = "date") val date: Calendar? = null
) : Parcelable