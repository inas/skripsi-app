package inas.anisha.skripsi_app.ui.main.perjalanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentPagePerjalananBinding
import kotlinx.android.synthetic.main.item_perjalanan.view.*


class PerjalananFragment : Fragment() {

    lateinit var mBinding: FragmentPagePerjalananBinding
    lateinit var mViewModel: PerjalananViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(PerjalananViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_perjalanan, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserData()

        mBinding.layoutMainTarget.view_line_middle.visibility = View.GONE
        mBinding.layoutMainTarget.imageview_item.setImageDrawable(
            resources.getDrawable(
                R.drawable.bg_main_target,
                null
            )
        )
        mViewModel.getMainTarget().observe(this, Observer {
            mBinding.layoutMainTarget.textview_content.text = it.name
        })
    }

    private fun initUserData() {
        mBinding.textviewName.text = mViewModel.getUserName()
        mBinding.textviewGradeAndStudy.text =
            mViewModel.getUserGrade() + " " + mViewModel.getUserStudy()
    }

    fun reInitData() {
        initUserData()
    }

    companion object {
        const val TAG = "PERJALANAN_FRAGMENT"
    }
}