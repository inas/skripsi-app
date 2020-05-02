package inas.anisha.skripsi_app.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentOnboardingPagerBinding

class OnboardingPagerFragment : Fragment() {

    private lateinit var mBinding: FragmentOnboardingPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding_pager, container, false)

        arguments?.let {
            mBinding.textviewHeading.text = it.getString(ARG_TITLE, "title")
            mBinding.textviewDescription.text = it.getString(ARG_DESCRIPTION, "description")
            mBinding.imageviewOnboarding.setImageResource(it.getInt(ARG_ILLUSTRATION))
        }

        return mBinding.root
    }

    interface ButtonClickListener {
        fun onButtonClick(buttonType: Int)
    }

    companion object {
        const val ARG_TITLE = "ARG_TITLE"
        const val ARG_DESCRIPTION = "ARG_DESCRIPTION"
        const val ARG_ILLUSTRATION = "ARG_ILLUSTRATION"

        fun newInstance(
            title: String,
            description: String,
            image: Int
        ): OnboardingPagerFragment {
            val fragment = OnboardingPagerFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_DESCRIPTION, description)
                putInt(ARG_ILLUSTRATION, image)
            }
            return fragment
        }
    }
}