package inas.anisha.srl_app.ui.main.schedule.displaytask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import inas.anisha.srl_app.R
import inas.anisha.srl_app.ui.main.home.ImportantScheduleViewModel


class TaskListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var taskListItems: List<TaskListItem> = mutableListOf()
    private var mListener: ItemClickListener? = null

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeader: TextView = itemView.findViewById(R.id.textview_header)
    }

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: MaterialCardView = itemView.findViewById(R.id.cardview)
        val tvTitle: TextView = itemView.findViewById(R.id.textview_jadwal)
        val tvTime: TextView = itemView.findViewById(R.id.textview_duedate)
        val tvPriority: AppCompatRatingBar = itemView.findViewById(R.id.rating)
        val tvStatus: TextView = itemView.findViewById(R.id.textview_status)
    }

    override fun getItemViewType(position: Int): Int =
        when (taskListItems[position]) {
            is ImportantScheduleViewModel -> TaskListItem.TYPE_CONTENT
            else -> TaskListItem.TYPE_HEADER
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TaskListItem.TYPE_CONTENT -> ContentViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_important_schedule, parent, false)
            )
            else -> HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_schedule_header, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TaskListItem.TYPE_CONTENT -> onBindContent(
                holder,
                taskListItems[position] as ImportantScheduleViewModel
            )
            else -> onBindHeader(holder, taskListItems[position] as TaskDateItem)
        }

    override fun getItemCount(): Int {
        return taskListItems.size
    }

    private fun onBindHeader(holder: RecyclerView.ViewHolder, item: TaskDateItem) {
        val header = holder as HeaderViewHolder
        header.tvHeader.text = item.dateString
    }

    private fun onBindContent(holder: RecyclerView.ViewHolder, item: ImportantScheduleViewModel) {
        val content = holder as ContentViewHolder
        content.tvTitle.text = item.name
        content.tvTime.text = item.time
        content.tvPriority.rating = item.rating.toFloat()
        content.tvStatus.visibility = if (item.isCompleted) View.VISIBLE else View.GONE

        content.cardView.setOnClickListener {
            mListener?.onClick(item.id)
        }
    }

    fun setItems(items: List<TaskListItem>) {
        taskListItems = items
        notifyDataSetChanged()
    }

    fun setListener(listener: ItemClickListener) {
        mListener = listener
    }

    interface ItemClickListener {
        fun onClick(itemId: Long)
    }
}