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
        holder.binding.isCurrentCycle = (position == itemCount - 1)
        holder.binding.viewLineMiddle.visibility = if (position == 0) View.GONE else View.VISIBLE
    }

    inner class ViewHolder(val binding: ItemPerjalananBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return content.size
    }

    fun setContent(newContent: List<String>) {
//        content = mutableListOf("siklus 1", "siklus 2", "siklus 3")
        content = newContent
        notifyDataSetChanged()
    }

}