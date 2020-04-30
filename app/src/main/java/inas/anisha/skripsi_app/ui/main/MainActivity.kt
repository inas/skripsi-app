package inas.anisha.skripsi_app.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ActivityMainBinding
import inas.anisha.skripsi_app.ui.common.ConfirmationWithImageDialog
import inas.anisha.skripsi_app.ui.evaluation.EvaluationReportIntroActivity
import inas.anisha.skripsi_app.ui.main.home.HomeFragment
import inas.anisha.skripsi_app.ui.main.perjalanan.PerjalananFragment
import inas.anisha.skripsi_app.ui.main.schedule.ScheduleFragment
import inas.anisha.skripsi_app.ui.main.target.TargetFragment
import inas.anisha.skripsi_app.utils.CalendarUtil
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.getPreviousMidnight
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel

    private val homeFragment: HomeFragment = HomeFragment()
    private val scheduleFragment: ScheduleFragment = ScheduleFragment()
    private val targetFragment: TargetFragment = TargetFragment()
    private val perjalananFragment: PerjalananFragment = PerjalananFragment()
    lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .add(R.id.layout_fragment_container, scheduleFragment, "1").hide(scheduleFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.layout_fragment_container, targetFragment, "2").hide(targetFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.layout_fragment_container, perjalananFragment, "3").hide(perjalananFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.layout_fragment_container, homeFragment, "0").commit()

        activeFragment = homeFragment
        mBinding.bottomnavigation.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        if (mViewModel.shouldShowSchoolScheduleDialog()) showAddSchoolClassDialog()
//        mViewModel.prepopulate()
    }

    override fun onStart() {
        super.onStart()

        val evaluationDate = mViewModel.getEvaluationDate()
        if (evaluationDate.getPreviousMidnight() < Calendar.getInstance() && mViewModel.shouldShowEvaluationReport()) {
            val intent = Intent(this, EvaluationReportIntroActivity::class.java)
            startActivity(intent)
            finish()
        } else if (CalendarUtil.isSameDay(
                evaluationDate.apply { add(Calendar.DAY_OF_MONTH, -1) },
                Calendar.getInstance()
            ) && mViewModel.shouldShowEndOfCycleWarning()
        ) {
            showEndOfCycleWarningDialog()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(homeFragment.apply { reInitData() }).commit()
                    activeFragment = homeFragment
                }

                R.id.action_jadwal -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(scheduleFragment).commit()
                    activeFragment = scheduleFragment
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

    private fun showAddSchoolClassDialog() {
        ConfirmationWithImageDialog().apply {
            arguments = Bundle().apply {
                putString(
                    ConfirmationWithImageDialog.ARG_TITLE,
                    "Selamat! Kamu telah berhasil melakukan langkah pertama!"
                )
                putString(
                    ConfirmationWithImageDialog.ARG_MESSAGE,
                    "Selanjutnya, masukkan jadwal sekolahmu dan kegiatanmu untuk mengatur jadwal belajarmu!"
                )
                putString(ConfirmationWithImageDialog.ARG_BUTTON, "Masukkan jadwal")
                putInt(ConfirmationWithImageDialog.ARG_BACKGROUND, R.drawable.bg_dialog_add_school)
            }
            
            setConfirmationDialogListener(object :
                ConfirmationWithImageDialog.ConfirmationDialogListener {
                override fun onConfirmed() {
                    mBinding.bottomnavigation.selectedItemId = R.id.action_jadwal
                    scheduleFragment.openSchoolScheduleDisplay()
                    dismiss()
                }
            })
        }.show(supportFragmentManager, ConfirmationWithImageDialog.TAG)

        mViewModel.setShouldNotShowSchoolScheduleDialog()
    }

    fun showEndOfCycleWarningDialog() {
        ConfirmationWithImageDialog().apply {
            arguments = Bundle().apply {
                putString(ConfirmationWithImageDialog.ARG_TITLE, "Besok masa siklusmu akan habis")
                putString(
                    ConfirmationWithImageDialog.ARG_MESSAGE,
                    "Pastikan kamu telah mengganti status task-task yang telah selesai untuk masuk dalam evaluasi"
                )
                putString(ConfirmationWithImageDialog.ARG_BUTTON, "OK, mengerti")
                putInt(
                    ConfirmationWithImageDialog.ARG_BACKGROUND,
                    R.drawable.bg_dialog_end_of_cycle_warning
                )
            }

            setConfirmationDialogListener(object :
                ConfirmationWithImageDialog.ConfirmationDialogListener {
                override fun onConfirmed() {
                    dismiss()
                }
            })
        }.show(supportFragmentManager, ConfirmationWithImageDialog.TAG)

        mViewModel.setShouldShowEndOfCycleWarning(false)
    }
}