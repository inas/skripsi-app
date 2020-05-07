package inas.anisha.skripsi_app.ui.evaluation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ItemExpandableEvaluationBinding
import inas.anisha.skripsi_app.databinding.ItemExpandableEvaluationHeaderBinding
import inas.anisha.skripsi_app.databinding.ItemExpandableEvaluationSeeMoreBinding


class EvaluationExpandableListAdapter(
    private val context: Context
) : BaseExpandableListAdapter() {

    private var listHeaderData: List<String> = mutableListOf()
    private var listChildData: HashMap<String, List<Pair<String, Int>>> = hashMapOf()
    private var mListener: ItemClickListener? = null

    override fun getChild(groupPosition: Int, childPosititon: Int): Pair<String, Int> {
        return listChildData[listHeaderData[groupPosition]]?.get(childPosititon) ?: Pair(
            "",
            R.drawable.ic_check_green_white
        )
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, view: View?, viewGroup: ViewGroup?
    ): View? {
        if (childPosition != MAX_CHILD) {
            val childBinding: ItemExpandableEvaluationBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_expandable_evaluation,
                viewGroup,
                false
            )
            val data = getChild(groupPosition, childPosition)
            childBinding.textviewItem.text = data.first
            childBinding.imageviewItem.setImageResource(data.second)
            view?.isClickable = false
            return childBinding.root
        } else {
            val seeMoreBinding: ItemExpandableEvaluationSeeMoreBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup?.context),
                R.layout.item_expandable_evaluation_see_more,
                viewGroup,
                false
            )

            seeMoreBinding.layout.setOnClickListener { mListener?.onClick() }
            return seeMoreBinding.root
        }
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listChildData[listHeaderData[groupPosition]]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): String {
        return listHeaderData[groupPosition]
    }

    override fun getGroupCount(): Int {
        return listHeaderData.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        view: View?, viewGroup: ViewGroup?
    ): View? {
        val parentBinding: ItemExpandableEvaluationHeaderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_expandable_evaluation_header,
            viewGroup,
            false
        )

        parentBinding.textviewItem.text = getGroup(groupPosition)
        parentBinding.imageviewChevron.visibility =
            if (getChildrenCount(groupPosition) == 0) View.INVISIBLE else View.VISIBLE

        if (isExpanded) {
            parentBinding.imageviewChevron.setImageResource(R.drawable.ic_chevron_up)
        } else {
            parentBinding.imageviewChevron.setImageResource(R.drawable.ic_chevron_down)
        }

        return parentBinding.root
    }

    fun setData(header: List<String>, child: HashMap<String, List<Pair<String, Int>>>) {
        listHeaderData = header
        listChildData = child
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    fun setItemClickListener(listener: ItemClickListener) {
        mListener = listener
    }

    interface ItemClickListener {
        fun onClick()
    }

    companion object {
        const val MAX_CHILD = 3
    }
}