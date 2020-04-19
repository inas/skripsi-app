package inas.anisha.skripsi_app.ui.main.target

import android.content.Context
import android.os.Bundle
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
            }
        })

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

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}