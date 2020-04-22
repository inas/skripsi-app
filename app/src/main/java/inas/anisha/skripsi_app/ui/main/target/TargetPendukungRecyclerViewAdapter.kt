package inas.anisha.skripsi_app.ui.main.target

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ItemCardSmallBinding
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.utils.ViewUtil.Companion.strikeThrough

class TargetPendukungRecyclerViewAdapter :
    RecyclerView.Adapter<TargetPendukungRecyclerViewAdapter.ViewHolder>() {

    private var targets: List<TargetPendukungViewModel> = mutableListOf()
    private var listener: ItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCardSmallBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_card_small,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewModel = targets[position]
        holder.binding.viewModel = viewModel

        holder.binding.textviewTarget.strikeThrough(viewModel.isCompleted)
        holder.binding.textviewTargetTime.strikeThrough(viewModel.isCompleted)

        holder.binding.imageviewClock.visibility =
            if (viewModel.shouldShowTime()) View.VISIBLE else View.GONE
        holder.binding.textviewTargetTime.visibility =
            if (viewModel.shouldShowTime()) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener { listener?.onItemClick(viewModel) }
    }

    inner class ViewHolder(val binding: ItemCardSmallBinding) :
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
        notifyDataSetChanged()
    }

    interface ItemListener {
        fun onItemClick(viewModel: TargetPendukungViewModel)
    }
}