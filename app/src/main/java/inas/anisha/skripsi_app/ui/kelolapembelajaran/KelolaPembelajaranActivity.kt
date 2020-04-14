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
//                mBinding.fabNext.visibility = if (position < KelolaPembelajaranPagerAdapter.NUM_ITEMS - 1) View.VISIBLE else View.GONE
                mViewModel.page.value = position
                updateBreadCrumbColor(position)
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

    fun nextPage() = mBinding.viewpager.setCurrentItem(mBinding.viewpager.currentItem + 1, true)

    fun updateBreadCrumbColor(position: Int) {
        when (position) {
//            0 -> updateBreadCrumbColor()
        }
    }

}