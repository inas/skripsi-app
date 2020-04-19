package inas.anisha.skripsi_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "school_class")
@Parcelize
data class SchoolClassEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "start_time") val startTime: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "end_time") val endTime: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "day") val day: Long = 0L
) : Parcelable