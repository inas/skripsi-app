package inas.anisha.srl_app.ui.main.schedule.displayschool

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.ItemSchoolClassBinding
import inas.anisha.srl_app.ui.main.schedule.school.SchoolClassViewModel

class SchoolClassAdapter :
    RecyclerView.Adapter<SchoolClassAdapter.ViewHolder>() {

    private var mListener: ItemListener? = null

    private var viewModels: List<SchoolClassViewModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemSchoolClassBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_school_class,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vm = viewModels[position]
        holder.binding.viewModel = vm
        holder.binding.cardView.setOnClickListener { mListener?.onItemClick(vm.id) }
        holder.binding.viewBackground.setBackgroundResource(CARD_COLOUR[position % 4])

    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    inner class ViewHolder(val binding: ItemSchoolClassBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setItemListener(listener: ItemListener) {
        mListener = listener
    }


    fun setList(list: List<SchoolClassViewModel>) {
        viewModels = list
        notifyDataSetChanged()
    }

    interface ItemListener {
        fun onItemClick(id: Long)
    }

    companion object {
        private val CARD_COLOUR =
            mutableListOf(R.color.yellow, R.color.purple, R.color.pink_light, R.color.pink)
    }
}
