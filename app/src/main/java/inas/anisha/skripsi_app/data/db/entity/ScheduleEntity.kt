package inas.anisha.skripsi_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toMinuteOfDay
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "schedule")
@Parcelize
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "type") val type: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "start_date") val startDate: Calendar? = null,
    @ColumnInfo(name = "end_date") val endDate: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "note") val note: String = "",
    @ColumnInfo(name = "priority") val priority: Int = 0,
    @ColumnInfo(name = "reward") val reward: String = "",
    @ColumnInfo(name = "execution_time") val executionTime: Calendar? = null,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "is_on_time") val isOnTime: Boolean = false,
    @ColumnInfo(name = "week_of_year") val weekOfYear: Int = endDate.get(Calendar.WEEK_OF_YEAR),
    @ColumnInfo(name = "year") val year: Int = endDate.get(Calendar.YEAR),
    @ColumnInfo(name = "start_minute_of_day") val startMinuteOfDay: Int = (startDate?.toMinuteOfDay()
        ?: endDate.toMinuteOfDay()),
    @ColumnInfo(name = "end_minute_of_day") val endMinuteOfDay: Int = endDate.toMinuteOfDay()
) : Parcelable