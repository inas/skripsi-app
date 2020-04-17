package inas.anisha.skripsi_app.ui.main.target

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ItemTargetPendukungBinding
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel

class TargetPendukungRecyclerViewAdapter :
    RecyclerView.Adapter<TargetPendukungRecyclerViewAdapter.ViewHolder>() {

    private var targets: List<TargetPendukungViewModel> = mutableListOf()
    private var listener: ItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemTargetPendukungBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_target_pendukung,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewModel = targets[position]
        holder.binding.viewModel = viewModel
//        holder.binding.textviewTarget.text = viewModel.name
//        holder.binding.textviewTargetNote.text = viewModel.note
//        holder.binding.textviewTargetTime.text = viewModel.time
//
        holder.binding.imageviewClock.visibility =
            if (viewModel.shouldShowTime()) View.VISIBLE else View.GONE
        holder.binding.textviewTargetTime.visibility =
            if (viewModel.shouldShowTime()) View.VISIBLE else View.GONE
        holder.binding.imageviewDelete.visibility =
            if (viewModel.isRemovable) View.VISIBLE else View.GONE
        holder.binding.checkbox.visibility =
            if (viewModel.shouldShowSelection) View.VISIBLE else View.GONE
        holder.binding.checkbox.isChecked = viewModel.isSelected.value ?: false

        holder.binding.imageviewDelete.setOnClickListener { listener?.onItemDeleted(viewModel) }
        holder.itemView.setOnClickListener { listener?.onItemClick(viewModel) }
    }

    inner class ViewHolder(val binding: ItemTargetPendukungBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemId(position: Int): Long {
        return targets[position].id
    }

    override fun getItemCount(): Int {
        return targets.size
    }

    fun setItemListener(listener: ItemListener) {
        this.listener = listener
    }

    fun setTargets(newTargets: List<TargetPendukungViewModel>) {
        targets = newTargets
        Log.d("debugskripsi", "should update")
        notifyDataSetChanged()
    }

    interface ItemListener {
        fun onItemClick(viewModel: TargetPendukungViewModel)
        fun onItemDeleted(viewModel: TargetPendukungViewModel)
    }
}