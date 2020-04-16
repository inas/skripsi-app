package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ActivityKelolaPembelajaranBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class KelolaPembelajaranActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityKelolaPembelajaranBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kelola_pembelajaran)
        mViewModel = ViewModelProviders.of(this).get(KelolaPembelajaranViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        mBinding.viewpager.adapter = KelolaPembelajaranPagerAdapter(supportFragmentManager)
        mBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mViewModel.page.value = position
            }
        })

        mBinding.imageviewBack.setOnClickListener { onBackPressed() }
        mBinding.fabNext.setOnClickListener { nextPage() }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onBackPressed() {
        val currentItem = mBinding.viewpager.currentItem
        if (currentItem > 0) {
            mBinding.viewpager.setCurrentItem(currentItem - 1, true)
        } else {
            finish()
        }
    }

    fun nextPage() {
        val currentItem = mBinding.viewpager.currentItem
        if (currentItem + 1 == KelolaPembelajaranPagerAdapter.NUM_ITEMS) {
            (mBinding.viewpager.adapter as KelolaPembelajaranPagerAdapter).completeKelolaPembelajaran()
            completeKelolaPembelajaran()
        } else {
            mBinding.viewpager.setCurrentItem(mBinding.viewpager.currentItem + 1, true)
        }
    }

    private fun completeKelolaPembelajaran() {
        if (mViewModel.supportingTargets.size > 0) {
            mViewModel.saveDataToRepository()

//            val intent = Intent(this, MainActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            startActivity(intent)
        }
    }

}