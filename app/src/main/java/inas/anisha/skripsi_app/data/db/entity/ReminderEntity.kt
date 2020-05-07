package inas.anisha.skripsi_app.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "reminder", foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        entity = ScheduleEntity::class,
        parentColumns = ["id"],
        childColumns = ["schedule_id"]
    )]
)
@Parcelize
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "unit") val unit: Int,
    @ColumnInfo(name = "is_popup") val isPopup: Boolean,
    @ColumnInfo(name = "schedule_id") val scheduleId: Long
) : Parcelable