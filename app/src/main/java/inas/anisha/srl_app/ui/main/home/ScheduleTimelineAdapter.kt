package inas.anisha.srl_app.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import inas.anisha.srl_app.R
import inas.anisha.srl_app.constant.SkripsiConstant
import inas.anisha.srl_app.databinding.ItemScheduleTimelineBinding

class ScheduleTimelineAdapter :
    RecyclerView.Adapter<ScheduleTimelineAdapter.ViewHolder>() {

    private var mListener: ItemListener? = null

    private var viewModels: List<ScheduleTimelineViewModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemScheduleTimelineBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_schedule_timeline,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vm = viewModels[position]
        holder.binding.viewModel = vm
        holder.binding.viewJadwal.setOnClickListener { mListener?.onItemClick(vm.type, vm.id) }

        if (vm.type == SkripsiConstant.SCHEDULE_TIMELINE_TYPE_ACTIVITY) {
            holder.binding.viewJadwal.setBackgroundResource(R.drawable.bg_schedule_activity)
        } else {
            holder.binding.viewJadwal.setBackgroundResource(R.drawable.bg_schedule_class)
        }
    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    inner class ViewHolder(val binding: ItemScheduleTimelineBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setItemListener(listener: ItemListener) {
        mListener = listener
    }


    fun setList(list: List<ScheduleTimelineViewModel>) {
        viewModels = list
        notifyDataSetChanged()
    }

    interface ItemListener {
        fun onItemClick(type: Int, id: Long)
    }
}
