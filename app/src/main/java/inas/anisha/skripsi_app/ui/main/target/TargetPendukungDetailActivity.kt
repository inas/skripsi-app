package inas.anisha.skripsi_app.ui.main.target

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.databinding.ActivityTargetPendukungDetailBinding
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.utils.ViewUtil.Companion.strikeThrough
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class TargetPendukungDetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityTargetPendukungDetailBinding
    private lateinit var mViewModel: TargetPendukungDetailViewModel
    private lateinit var observable: LiveData<TargetPendukungEntity>

    private var targetId: Long = 0L
    private var targetEntity: TargetPendukungEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_target_pendukung_detail)
        mViewModel = ViewModelProviders.of(this).get(TargetPendukungDetailViewModel::class.java)
        targetEntity = intent.getParcelableExtra(EXTRA_ENTITY)
        targetId = intent.getLongExtra(EXTRA_ID, 0L)
    }

    override fun onStart() {
        super.onStart()
        observable = mViewModel.getSupportingTarget(targetId)
        observable.observe(this, Observer { dbTarget ->
            var target = dbTarget
            targetEntity?.let {
                target = it
                mBinding.layoutButton.visibility = View.GONE
            }

            target?.let {
                mViewModel.target = it
                mBinding.viewModel = mViewModel.getSupportingTargetViewModel(it)
                mBinding.textviewTargetName.strikeThrough(it.isCompleted)
            }
        })

        mBinding.imageviewBack.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(EXTRA_ENTITY, mViewModel.target)
            setResult(RESULT_UPDATED, returnIntent)
            finish()
        }

        mBinding.buttonMarkAsComplete.setOnClickListener { mViewModel.setTargetAsComplete(true) }
        mBinding.buttonMarkAsIncomplete.setOnClickListener { mViewModel.setTargetAsComplete(false) }

        mBinding.buttonEdit.setOnClickListener {
            openModifySupportingTargetDialog(TargetPendukungViewModel().fromEntity(mViewModel.target))
        }

        mBinding.buttonHapus.setOnClickListener {
            if (targetEntity == null) {
                mViewModel.deleteSupportingTarget()
            } else {
                val returnIntent = Intent()
                setResult(RESULT_DELETED, returnIntent)
            }
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        observable.removeObservers(this)

        mBinding.imageviewBack.setOnClickListener(null)
        mBinding.buttonMarkAsComplete.setOnClickListener(null)
        mBinding.buttonMarkAsIncomplete.setOnClickListener(null)
        mBinding.buttonEdit.setOnClickListener(null)
        mBinding.buttonHapus.setOnClickListener(null)
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_ENTITY, mViewModel.target)
        setResult(RESULT_UPDATED, returnIntent)
        finish()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun openModifySupportingTargetDialog(target: TargetPendukungViewModel) {
        val tambahTargetDialog = TambahTargetPendukungDialog().apply {
            arguments = Bundle().apply {
                putParcelable(TambahTargetPendukungDialog.ARG_TARGET, target.toEntity())
            }
        }

        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetPendukungDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetPendukungViewModel) {
                if (targetEntity == null) {
                    mViewModel.updateSupportingTarget(target)
                } else {
                    mViewModel.target = target.toEntity()
                    mBinding.viewModel = target
                }
            }
        })

        tambahTargetDialog.show(supportFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_ENTITY = "EXTRA_ENTITY"

        const val RESULT_DELETED = 31
        const val RESULT_UPDATED = 32
    }
}