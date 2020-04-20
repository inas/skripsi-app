package inas.anisha.skripsi_app.ui.main.perjalanan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ItemPerjalananBinding

class PerjalananRecyclerViewAdapter :
    RecyclerView.Adapter<PerjalananRecyclerViewAdapter.ViewHolder>() {

    private var mListener: ItemListener? = null

    private var content: List<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPerjalananBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_perjalanan,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textviewContent.text = content[position]
        holder.binding.viewLineMiddle.visibility = if (position == 0) View.GONE else View.VISIBLE

        val isCurrentCycle = (position == itemCount - 1)
        if (!isCurrentCycle) {
            holder.binding.cardviewCycle.setOnClickListener { mListener?.onItemClick(position) }
        }

        holder.binding.isCurrentCycle = isCurrentCycle
    }

    override fun getItemCount(): Int {
        return content.size
    }

    inner class ViewHolder(val binding: ItemPerjalananBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setItemListener(listener: ItemListener) {
        mListener = listener
    }


    fun setContent(newContent: List<String>) {
//        content = mutableListOf("siklus 1", "siklus 2", "siklus 3")
        content = newContent
        notifyDataSetChanged()
    }

    interface ItemListener {
        fun onItemClick(position: Int)
    }
}