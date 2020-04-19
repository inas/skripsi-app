package inas.anisha.skripsi_app.ui.main.target

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.databinding.ActivityTargetPendukungDetailBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class TargetPendukungDetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityTargetPendukungDetailBinding
    private lateinit var mViewModel: TargetPendukungDetailViewModel
    private lateinit var observable: LiveData<TargetPendukungEntity>

    private var targetId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_target_pendukung_detail)
        mViewModel = ViewModelProviders.of(this).get(TargetPendukungDetailViewModel::class.java)
        targetId = intent.getLongExtra(EXTRA_ID, 0L)
    }

    override fun onStart() {
        super.onStart()
        observable = mViewModel.getSupportingTarget(targetId)
        observable.observe(this, Observer {
            it?.let {
                mViewModel.target = it
                mBinding.viewModel = mViewModel.getSupportingTargetViewModel(it)
                mBinding.textviewTargetName.strikeThrough(it.isCompleted)
            }
        })

        mBinding.imageviewBack.setOnClickListener { finish() }
        mBinding.buttonHapus.setOnClickListener {
            mViewModel.deleteSupportingTarget()
            finish()
        }
        mBinding.buttonMarkAsComplete.setOnClickListener { mViewModel.setTargetAsComplete(true) }
        mBinding.buttonMarkAsIncomplete.setOnClickListener { mViewModel.setTargetAsComplete(false) }
    }

    override fun onStop() {
        super.onStop()
        observable.removeObservers(this)

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private fun TextView.strikeThrough(enable: Boolean) {
        paintFlags = if (enable) {
            (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}