package inas.anisha.srl_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import inas.anisha.srl_app.utils.CalendarUtil.Companion.getMinuteOfDay
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "school_class")
@Parcelize
data class SchoolClassEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "start_time") val startTime: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "end_time") val endTime: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "day") val day: Int = 0,
    @ColumnInfo(name = "note") val note: String = "",
    @ColumnInfo(name = "start_minute_of_day") val startMinuteOfDay: Int = startTime.getMinuteOfDay(),
    @ColumnInfo(name = "end_minute_of_day") val endMinuteOfDay: Int = endTime.getMinuteOfDay()
) : Parcelable