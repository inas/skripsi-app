package inas.anisha.skripsi_app.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ItemImportantScheduleBinding

class ImportantScheduleAdapter :
    RecyclerView.Adapter<ImportantScheduleAdapter.ViewHolder>() {

    private var mListener: ItemListener? = null

    private var viewModels: List<ImportantScheduleViewModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemImportantScheduleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_important_schedule,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.viewModel = viewModels[position]
    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    inner class ViewHolder(val binding: ItemImportantScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setItemListener(listener: ItemListener) {
        mListener = listener
    }


    fun setList(list: List<ImportantScheduleViewModel>) {
        viewModels = list
        notifyDataSetChanged()
    }

    interface ItemListener {
        fun onItemClick(position: Int)
    }
}
