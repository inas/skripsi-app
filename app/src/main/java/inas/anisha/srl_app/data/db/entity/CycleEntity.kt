package inas.anisha.srl_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "cycle")
@Parcelize
data class CycleEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "number") val number: Int = 0,
    @ColumnInfo(name = "completion") val completion: Int = 0,
    @ColumnInfo(name = "reflection") val reflection: String = "",
    @ColumnInfo(name = "start_date") val startDate: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "end_date") val endDate: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "total_task") val totalTask: Int = 0,
    @ColumnInfo(name = "completed_task") val completedTask: Int = 0,
    @ColumnInfo(name = "on_time_task") val onTimeTask: Int = 0,
    @ColumnInfo(name = "total_target") val totalTarget: Int = 0,
    @ColumnInfo(name = "completed_target") val completedTarget: Int = 0,
    @ColumnInfo(name = "completed_target_list") val completedTargetList: List<String> = mutableListOf(),
    @ColumnInfo(name = "frequency") val frequency: Int = 0,
    @ColumnInfo(name = "duration") val duration: Int = 0
) : Parcelable