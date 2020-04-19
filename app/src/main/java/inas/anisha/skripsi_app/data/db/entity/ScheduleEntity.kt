package inas.anisha.skripsi_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "schedule")
@Parcelize
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "type") val type: Long = 0L,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "deadline") val deadline: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "note") val note: String = "",
    @ColumnInfo(name = "priority") val priority: Int = 0,
    @ColumnInfo(name = "reward") val reward: String = "",
    @ColumnInfo(name = "execution_time") val executionTime: Calendar? = null,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "is_on_time") val isOnTime: Boolean = false
) : Parcelable