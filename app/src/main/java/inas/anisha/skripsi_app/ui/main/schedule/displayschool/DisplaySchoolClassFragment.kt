package inas.anisha.skripsi_app.ui.main.schedule.displayschool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentDisplaySchoolClassBinding
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassViewModel
import java.util.*

class DisplaySchoolClassFragment : Fragment() {

    lateinit var mBinding: FragmentDisplaySchoolClassBinding
    lateinit var mViewModel: DisplaySchoolClassViewModel
    lateinit var mAdapter: SchoolClassListPagerAdapter

    private var mondayObservable: LiveData<List<SchoolClassViewModel>>? = null
    private var tuesdayObservable: LiveData<List<SchoolClassViewModel>>? = null
    private var wednesdayObservable: LiveData<List<SchoolClassViewModel>>? = null
    private var thursdayObservable: LiveData<List<SchoolClassViewModel>>? = null
    private var fridayObservable: LiveData<List<SchoolClassViewModel>>? = null
    private var saturdayObservable: LiveData<List<SchoolClassViewModel>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(DisplaySchoolClassViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_display_school_class,
                container,
                false
            )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = SchoolClassListPagerAdapter(childFragmentManager)
        mBinding.viewPager.adapter = mAdapter
        mBinding.tabLayout.apply {
            setupWithViewPager(mBinding.viewPager)
            getTabAt(0)?.text = "Senin"
            getTabAt(1)?.text = "Selasa"
            getTabAt(2)?.text = "Rabu"
            getTabAt(3)?.text = "Kamis"
            getTabAt(4)?.text = "Jumat"
            getTabAt(5)?.text = "Sabtu"
            selectTab(getTabAt(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2))
        }

        observeSchoolClasses()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mondayObservable?.removeObservers(this)
        tuesdayObservable?.removeObservers(this)
        wednesdayObservable?.removeObservers(this)
        thursdayObservable?.removeObservers(this)
        fridayObservable?.removeObservers(this)
        saturdayObservable?.removeObservers(this)
    }

    fun observeSchoolClasses() {
        mondayObservable = mViewModel.getSchoolClasses(Calendar.MONDAY).apply {
            observe(this@DisplaySchoolClassFragment, Observer { mAdapter.setItems(it, 0) })
        }

        tuesdayObservable = mViewModel.getSchoolClasses(Calendar.TUESDAY).apply {
            observe(this@DisplaySchoolClassFragment, Observer { mAdapter.setItems(it, 1) })
        }

        wednesdayObservable = mViewModel.getSchoolClasses(Calendar.WEDNESDAY).apply {
            observe(this@DisplaySchoolClassFragment, Observer { mAdapter.setItems(it, 2) })
        }

        thursdayObservable = mViewModel.getSchoolClasses(Calendar.THURSDAY).apply {
            observe(this@DisplaySchoolClassFragment, Observer { mAdapter.setItems(it, 3) })
        }

        fridayObservable = mViewModel.getSchoolClasses(Calendar.FRIDAY).apply {
            observe(this@DisplaySchoolClassFragment, Observer { mAdapter.setItems(it, 4) })
        }

        saturdayObservable = mViewModel.getSchoolClasses(Calendar.SATURDAY).apply {
            observe(this@DisplaySchoolClassFragment, Observer { mAdapter.setItems(it, 5) })
        }
    }

    companion object {
        const val TAG = "DISPLAY_SCHOOL_CLASS_FRAGMENT"
    }
}