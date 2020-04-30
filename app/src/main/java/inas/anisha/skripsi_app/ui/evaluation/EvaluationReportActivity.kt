package inas.anisha.skripsi_app.ui.evaluation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.databinding.ActivityEvaluationReportBinding
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*


class EvaluationReportActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityEvaluationReportBinding
    private lateinit var mViewModel: EvaluationReportViewModel
    private lateinit var compositeDisposable: CompositeDisposable

    private var supportingTargetObservable: LiveData<List<TargetPendukungEntity>>? = null
    private var taskExpandableListAdapter: EvaluationExpandableListAdapter? = null
    private var onTimeExpandableListAdapter: EvaluationExpandableListAdapter? = null
    private var targetExpandableListAdapter: EvaluationExpandableListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_evaluation_report)
        mViewModel = ViewModelProviders.of(this).get(EvaluationReportViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()

        initExpandableListView()
        setCycleData()
        setTargetData()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
        supportingTargetObservable?.removeObservers(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun initExpandableListView() {
        if (taskExpandableListAdapter == null) taskExpandableListAdapter =
            EvaluationExpandableListAdapter(this)
        mBinding.listviewTask.setAdapter(taskExpandableListAdapter)
        mBinding.listviewTask.setOnGroupClickListener { parent, v, groupPosition, id ->
            parent.expandGroup(groupPosition)
            false
        }

        if (onTimeExpandableListAdapter == null) onTimeExpandableListAdapter =
            EvaluationExpandableListAdapter(this)
        mBinding.listviewOnTime.setAdapter(onTimeExpandableListAdapter)
        mBinding.listviewOnTime.setOnGroupClickListener { parent, v, groupPosition, id ->
            parent.expandGroup(groupPosition)
            if (parent.isGroupExpanded(groupPosition)) {
                v.findViewById<ImageView>(R.id.imageview_chevron)
                    .setImageDrawable(resources.getDrawable(R.drawable.ic_chevron_up, null))
            } else {
                v.findViewById<ImageView>(R.id.imageview_chevron)
                    .setImageDrawable(resources.getDrawable(R.drawable.ic_chevron_down, null))
            }
            false
        }

        if (targetExpandableListAdapter == null) targetExpandableListAdapter =
            EvaluationExpandableListAdapter(this)
        mBinding.listviewTaskTarget.apply {
            setAdapter(targetExpandableListAdapter)
            setOnGroupClickListener { parent, v, groupPosition, id -> false }
        }

        Handler().postDelayed({ mBinding.listviewTaskTarget.expandGroup(0, true) }, 500)
    }

    fun setCycleData() {
        val startDate =
            Calendar.getInstance().apply { timeInMillis = mViewModel.getCycleStartDate() }
        val endDate = Calendar.getInstance().apply {
            timeInMillis = mViewModel.getCycleEndDate()
            add(Calendar.DAY_OF_MONTH, -1)
        }

        mBinding.textviewPeriod.text = startDate.toDateString() + " - " + endDate.toDateString()

        mViewModel.getCurrentCycle().observeOn(AndroidSchedulers.mainThread()).subscribe {
            mBinding.textviewCycle.text = "Siklus " + it.number
        }.addTo(compositeDisposable)

        val cycleTime = mViewModel.getCycleTime()
        val frequency = when (cycleTime.first) {
            SkripsiConstant.CYCLE_FREQUENCY_DAILY -> "Harian"
            SkripsiConstant.CYCLE_FREQUENCY_WEEKLY -> "Mingguan"
            else -> "Bulanan"
        }

        mBinding.textviewCycleDuration.text =
            "" + (if (cycleTime.second == 1) "" else (" " + cycleTime.second + " ")) + frequency
    }

    fun setTargetData() {
        supportingTargetObservable = mViewModel.getSupportingTargets().apply {
            observe(this@EvaluationReportActivity, Observer { targets ->

                var completedTarget = 0
                val statusPair = mutableListOf<Pair<String, Int>>()
                targets.forEach {
                    if (it.isCompleted) {
                        statusPair.add(Pair(it.name, R.drawable.ic_check_green_white))
                        completedTarget++
                    } else {
                        statusPair.add(Pair(it.name, R.drawable.ic_cross))
                    }
                }

                val headerText = "" + completedTarget + "/" + targets.size + " target tercapai"
                val map = hashMapOf(
                    Pair(
                        headerText,
                        statusPair.take(EvaluationExpandableListAdapter.MAX_CHILD + 1)
                    )
                )
                targetExpandableListAdapter?.setData(mutableListOf(headerText), map)
            })
        }
    }

    private fun goToStartNewCycle() {
//        val intent = Intent(this, StartNewCycleActivity::class.java)
//        startActivity(intent)
    }
}