package inas.anisha.skripsi_app.ui.main.perjalanan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ItemTargetAccomplishedBinding

class CompletedTargetsAdapter :
    RecyclerView.Adapter<CompletedTargetsAdapter.ViewHolder>() {

    private var mListener: ItemListener? = null

    private var targets: List<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemTargetAccomplishedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_target_accomplished,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textviewName.text = targets[position]
    }

    override fun getItemCount(): Int {
        return targets.size
    }

    inner class ViewHolder(val binding: ItemTargetAccomplishedBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setItemListener(listener: ItemListener) {
        mListener = listener
    }


    fun setTargets(newTargets: List<String>) {
        targets = newTargets
        notifyDataSetChanged()
    }

    interface ItemListener {
        fun onItemClick(position: Int)
    }
}