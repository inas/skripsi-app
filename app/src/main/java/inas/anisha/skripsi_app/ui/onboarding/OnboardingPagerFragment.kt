package inas.anisha.skripsi_app.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentOnboardingPagerBinding

class OnboardingPagerFragment : Fragment() {

    private lateinit var mBinding: FragmentOnboardingPagerBinding
    private lateinit var mViewModel: OnboardingPagerViewModel
    private lateinit var mClickListener: ButtonClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(OnboardingPagerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding_pager, container, false)
        mBinding.viewModel = mViewModel

        arguments?.let {
            val pageNum = it.getInt("page", 0)
            mViewModel.initViewModel(
                it.getString("title", "title"),
                it.getString("description", "description"),
                pageNum
            )
            when (pageNum) {
                0 -> {
                    mBinding.imageviewOnboarding.setImageResource(
                        it.getInt(
                            "image",
                            R.drawable.img_onboarding_0
                        )
                    )
                    mBinding.imageviewProgress0.setImageResource(R.drawable.ic_indicator_on)
                }
                1 -> {
                    mBinding.imageviewOnboarding.setImageResource(
                        it.getInt(
                            "image",
                            R.drawable.img_onboarding_1
                        )
                    )
                    mBinding.imageviewProgress1.setImageResource(R.drawable.ic_indicator_on)
                }
                2 -> {
                    mBinding.imageviewOnboarding.setImageResource(
                        it.getInt(
                            "image",
                            R.drawable.img_onboarding_2
                        )
                    )
                    mBinding.imageviewProgress2.setImageResource(R.drawable.ic_indicator_on)
                    mBinding.buttonStart.visibility = View.VISIBLE
                }
            }
        }

        mBinding.buttonSkip.setOnClickListener { mClickListener.onButtonClick(0) }
        mBinding.buttonNext.setOnClickListener { mClickListener.onButtonClick(1) }
        mBinding.buttonStart.setOnClickListener { mClickListener.onButtonClick(2) }

        return mBinding.root
    }

    interface ButtonClickListener {
        fun onButtonClick(buttonType: Int)
    }

    companion object {
        const val CLICK_SKIP = 0
        const val CLICK_NEXT = 1
        const val CLICK_START = 2

        fun newInstance(
            title: String,
            description: String,
            page: Int,
            image: Int,
            listener: ButtonClickListener
        ): OnboardingPagerFragment {
            val fragment = OnboardingPagerFragment()
            fragment.arguments = Bundle().apply {
                putString("title", title)
                putString("description", description)
                putInt("page", page)
                putInt("image", image)
            }
            fragment.mClickListener = listener
            return fragment
        }
    }
}