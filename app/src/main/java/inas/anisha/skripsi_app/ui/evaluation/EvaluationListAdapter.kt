package inas.anisha.skripsi_app.ui.evaluation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ItemExpandableEvaluationBinding

class EvaluationListAdapter :
    RecyclerView.Adapter<EvaluationListAdapter.ViewHolder>() {

    private var itemPairs: List<Pair<String, Int>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemExpandableEvaluationBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_expandable_evaluation,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemPairs[position]
        holder.binding.textviewItem.text = data.first
        holder.binding.imageviewItem.setImageResource(data.second)
    }

    override fun getItemCount(): Int {
        return itemPairs.size
    }

    inner class ViewHolder(val binding: ItemExpandableEvaluationBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setList(list: List<Pair<String, Int>>) {
        itemPairs = list
        notifyDataSetChanged()
    }
}
