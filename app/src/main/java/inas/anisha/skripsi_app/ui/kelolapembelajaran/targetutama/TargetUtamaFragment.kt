package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentTargetUtamaBinding
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranIntroActivity
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranViewModel

class TargetUtamaFragment : Fragment() {

    private lateinit var mBinding: FragmentTargetUtamaBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(KelolaPembelajaranViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_target_utama, container, false)
        mBinding.viewModel = mViewModel

        initViews()
        setClickListener()

        return mBinding.root
    }

    fun initViews() {
        mBinding.layoutTargetRecommendation0.textviewTarget.text = "Melanjutkan pendidikan"
        mBinding.layoutTargetRecommendation0.textviewTargetDescription.text =
            "Setelah lulus saya ingin melanjutkan sekolah ke universitas impian"
        mBinding.layoutTargetRecommendation0.imageviewTarget.setBackgroundColor(resources.getColor(R.color.blue))

        mBinding.layoutTargetRecommendation0.textviewTarget.text = "Mewujudkan cita-cita saya"
        mBinding.layoutTargetRecommendation0.textviewTargetDescription.text =
            "Saya ingin memiliki pekerjaan yang saya impikan"
        mBinding.layoutTargetRecommendation0.imageviewTarget.setBackgroundColor(resources.getColor(R.color.yellow))
    }

    fun setClickListener() {
        mBinding.buttonAddTarget.setOnClickListener { openAddTargetActivity() }
        mBinding.layoutTargetAdded.layout.setOnClickListener { selectarget(true, false, false) }
        mBinding.layoutTargetRecommendation0.layout.setOnClickListener {
            selectarget(
                false,
                true,
                false
            )
        }
        mBinding.layoutTargetRecommendation1.layout.setOnClickListener {
            selectarget(
                false,
                false,
                true
            )
        }
    }

    fun openAddTargetActivity() {
        val intent = Intent(activity, KelolaPembelajaranIntroActivity::class.java)
        startActivity(intent)
    }

    fun selectarget(addedTarget: Boolean, firstRecTarget: Boolean, secondRecTarget: Boolean) {
        mBinding.addedTargetIsSelected = addedTarget
        mBinding.recTarget0IsSelected = firstRecTarget
        mBinding.recTarget1IsSelected = secondRecTarget
    }
}