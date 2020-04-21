package inas.anisha.skripsi_app.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ActivityMainBinding
import inas.anisha.skripsi_app.ui.main.perjalanan.PerjalananFragment
import inas.anisha.skripsi_app.ui.main.target.TargetFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel

    private val targetFragment: TargetFragment = TargetFragment()
    private val perjalananFragment: PerjalananFragment = PerjalananFragment()
    lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this // todo remove?

        supportFragmentManager.beginTransaction()
            .add(R.id.layout_fragment_container, perjalananFragment, "3").hide(perjalananFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.layout_fragment_container, targetFragment, "2").commit()

        activeFragment = targetFragment
        mBinding.bottomnavigation.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        mViewModel.prepopulate()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    //                        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment1).commit()
                    //                        activeFragment = fragment1
                }
                R.id.action_jadwal -> {
                    //                        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment2).commit()
                    //                        activeFragment = fragment2
                }
                R.id.action_target -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(targetFragment.apply { reInitData() }).commit()
                    activeFragment = targetFragment
                }
                R.id.action_perjalanan -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(perjalananFragment.apply { reInitData() }).commit()
                    activeFragment = perjalananFragment
                }
            }
            true
        }
}