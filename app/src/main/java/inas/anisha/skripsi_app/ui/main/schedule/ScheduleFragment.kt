package inas.anisha.skripsi_app.ui.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentPageScheduleBinding
import inas.anisha.skripsi_app.ui.main.schedule.displaytask.DisplayTaskFragment

class ScheduleFragment : Fragment() {
    lateinit var mBinding: FragmentPageScheduleBinding
    lateinit var mViewModel: SchedulePageViewModel

    lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(SchedulePageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_schedule, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activeFragment = DisplayTaskFragment()
        childFragmentManager.beginTransaction()
            .add(R.id.layout_placeholder, activeFragment, DisplayTaskFragment.TAG).commit()
    }

    companion object {
        val DISPLAY = mutableListOf("Pekan", "Hari", "Tugas", "Sekolah")
    }
}