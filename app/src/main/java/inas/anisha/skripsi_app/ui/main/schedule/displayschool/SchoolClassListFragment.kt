package inas.anisha.skripsi_app.ui.main.schedule.displayschool

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentSchoolClassListBinding
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassDetailActivity
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassViewModel

class SchoolClassListFragment : Fragment() {

    lateinit var mBinding: FragmentSchoolClassListBinding

    private var adapter: SchoolClassAdapter? = null
    private var schoolClassVms: List<SchoolClassViewModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_school_class_list, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SchoolClassAdapter().apply {
            setList(schoolClassVms)
            setItemListener(object : SchoolClassAdapter.ItemListener {
                override fun onItemClick(id: Long) {
                    openSchoolClassDetail(id)
                }
            })
        }

        mBinding.recyclerView.adapter = adapter
        mBinding.textviewEmpty.visibility =
            if (schoolClassVms.isEmpty()) View.VISIBLE else View.GONE
    }

    fun setItems(items: List<SchoolClassViewModel>) {
        if (::mBinding.isInitialized) {
            mBinding.textviewEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }
        adapter?.setList(items)
        schoolClassVms = items
    }

    fun openSchoolClassDetail(schoolClassId: Long) {
        val intent = Intent(activity, SchoolClassDetailActivity::class.java).apply {
            putExtra(
                SchoolClassDetailActivity.EXTRA_ID,
                schoolClassId
            )
        }
        startActivity(intent)
    }
}