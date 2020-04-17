package inas.anisha.skripsi_app.ui.main.target

import android.util.Log
import android.view.LayoutInflater
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
        holder.binding.viewModel = targets[position]
        holder.binding.imageviewDelete.setOnClickListener { listener?.onItemDeleted(targets[position]) }
        holder.itemView.setOnClickListener { listener?.onItemClick(targets[position]) }
    }

    inner class ViewHolder(val binding: ItemTargetPendukungBinding) :
        RecyclerView.ViewHolder(binding.root)

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