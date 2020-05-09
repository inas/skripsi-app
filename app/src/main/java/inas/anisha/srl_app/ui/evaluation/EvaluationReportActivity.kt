package inas.anisha.srl_app.ui.evaluation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.srl_app.R
import inas.anisha.srl_app.data.db.entity.ScheduleEntity
import inas.anisha.srl_app.data.db.entity.TargetPendukungEntity
import inas.anisha.srl_app.databinding.ActivityEvaluationReportBinding
import inas.anisha.srl_app.ui.updatetarget.StartNewCycleActivity
import inas.anisha.srl_app.utils.CalendarUtil.Companion.toDateString
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*


class EvaluationReportActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityEvaluationReportBinding
    private lateinit var mViewModel: EvaluationReportViewModel
    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var evaluationListDialog: EvaluationListDialog

    private var supportingTargetObservable: LiveData<List<TargetPendukungEntity>>? = null
    private var currentCycleTasksObservable: LiveData<List<ScheduleEntity>>? = null

    private var taskExpandableListAdapter: EvaluationExpandableListAdapter? = null
    private var onTimeExpandableListAdapter: EvaluationExpandableListAdapter? = null
    private var targetExpandableListAdapter: EvaluationExpandableListAdapter? = null

    private var isInitial = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_evaluation_report)
        mViewModel = ViewModelProviders.of(this).get(EvaluationReportViewModel::class.java)
        initExpandableListView()
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()

        setDialog()
        setCycleData()
        setTaskData()
        setTargetData()

        mBinding.edittextReflection.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setRawInputType(InputType.TYPE_CLASS_TEXT)
        }
        mBinding.buttonSubmit.setOnClickListener {
            mViewModel.saveCycle(mBinding.edittextReflection.text.toString().trim())
            goToStartNewCycle()
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
        supportingTargetObservable?.removeObservers(this)
        currentCycleTasksObservable?.removeObservers(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun initExpandableListView() {
        if (taskExpandableListAdapter == null) {
            taskExpandableListAdapter = EvaluationExpandableListAdapter(this).apply {
                setItemClickListener(object : EvaluationExpandableListAdapter.ItemClickListener {
                    override fun onClick() {
                        showCompletedTaskDialog()
                    }
                })
            }

            mBinding.listviewTask.apply {
                setAdapter(taskExpandableListAdapter)
                setOnGroupClickListener { _, _, _, _ -> false }
            }
        }

        if (onTimeExpandableListAdapter == null) {
            onTimeExpandableListAdapter = EvaluationExpandableListAdapter(this).apply {
                setItemClickListener(object : EvaluationExpandableListAdapter.ItemClickListener {
                    override fun onClick() {
                        showOnTimeTaskDialog()
                    }
                })
            }

            mBinding.listviewOnTime.apply {
                setAdapter(onTimeExpandableListAdapter)
                setOnGroupClickListener { _, _, _, _ -> false }
            }
        }

        if (targetExpandableListAdapter == null) {
            targetExpandableListAdapter = EvaluationExpandableListAdapter(this).apply {
                setItemClickListener(object : EvaluationExpandableListAdapter.ItemClickListener {
                    override fun onClick() {
                        showTargetDialog()
                    }
                })
            }

            mBinding.listviewTarget.apply {
                setAdapter(targetExpandableListAdapter)
                setOnGroupClickListener { _, _, _, _ -> false }
            }
        }

        if (isInitial) Handler().postDelayed({
            if (targetExpandableListAdapter?.groupCount ?: 0 > 0) {
                mBinding.listviewTask.expandGroup(0, true)
                mBinding.cardViewTask.visibility = View.VISIBLE
            }

            if (targetExpandableListAdapter?.groupCount ?: 0 > 0) {
                mBinding.listviewOnTime.expandGroup(0, true)
                mBinding.cardViewOnTime.visibility = View.VISIBLE
            }

            if (targetExpandableListAdapter?.groupCount ?: 0 > 0) {
                mBinding.listviewTarget.expandGroup(0, true)
                mBinding.cardViewTarget.visibility = View.VISIBLE
            }

            isInitial = false
        }, 500)
    }

    fun setDialog() {
        evaluationListDialog = EvaluationListDialog().apply {
            setConfirmationDialogListener(object :
                EvaluationListDialog.ConfirmationDialogListener {
                override fun onConfirmed() {
                    dismiss()
                }
            })
        }
    }

    fun setCycleData() {
        val startDate = mViewModel.getCycleStartDate()
        val endDate = mViewModel.getCycleEvaluationDate()

        mBinding.textviewPeriod.text = startDate.toDateString() + " - " + endDate.toDateString()

        mViewModel.getCurrentCycle().observeOn(AndroidSchedulers.mainThread()).subscribe {
            mViewModel.cycleEntity = it
            mViewModel.id = it.id
            mViewModel.cycleNumber = it.number
            mBinding.textviewCycle.text = "Siklus " + it.number
        }.addTo(compositeDisposable)

        mBinding.textviewCycleDuration.text = mViewModel.getCycleTime()
    }

    fun setTaskData() {
        currentCycleTasksObservable = mViewModel.getCurrentCycleTasks().apply {
            observe(this@EvaluationReportActivity, Observer { tasks ->
                tasks?.let { mViewModel.processTasks(tasks) }

                val headerTextCompleted = mViewModel.getCompletedTaskString()
                val statusPairCompleted = mViewModel.taskNames.zip(mViewModel.taskCompletionIcons)
                val mapCompleted: HashMap<String, List<Pair<String, Int>>> = hashMapOf(
                    Pair(
                        headerTextCompleted,
                        statusPairCompleted.take(EvaluationExpandableListAdapter.MAX_CHILD + 1)
                    )
                )
                taskExpandableListAdapter?.setData(mutableListOf(headerTextCompleted), mapCompleted)

                val headerTextOnTime = mViewModel.getOnTimeTaskString()
                val statusPairOnTime = mViewModel.taskNames.zip(mViewModel.taskOnTimeIcons)
                val mapOnTime: HashMap<String, List<Pair<String, Int>>> = hashMapOf(
                    Pair(
                        headerTextOnTime,
                        statusPairOnTime.take(EvaluationExpandableListAdapter.MAX_CHILD + 1)
                    )
                )
                onTimeExpandableListAdapter?.setData(mutableListOf(headerTextOnTime), mapOnTime)
            })
        }
    }

    fun setTargetData() {
        supportingTargetObservable = mViewModel.getSupportingTargets().apply {
            observe(this@EvaluationReportActivity, Observer { targets ->
                targets?.let { mViewModel.processTargets(targets) }

                val headerText = mViewModel.getCompletedTargetString()
                val statusPairCompleted = mViewModel.targetNames.zip(mViewModel.targetIcons)
                val map: HashMap<String, List<Pair<String, Int>>> = hashMapOf(
                    Pair(
                        headerText,
                        statusPairCompleted.take(EvaluationExpandableListAdapter.MAX_CHILD + 1)
                    )
                )

                targetExpandableListAdapter?.setData(mutableListOf(headerText), map)
            })
        }
    }

    private fun showCompletedTaskDialog() {
        evaluationListDialog.apply {
            arguments = Bundle().apply {
                putString(EvaluationListDialog.ARG_TITLE, mViewModel.getCompletedTaskString())
                putStringArrayList(
                    EvaluationListDialog.ARG_ITEM,
                    mViewModel.taskNames as ArrayList<String>
                )
                putIntegerArrayList(
                    EvaluationListDialog.ARG_BOOLEAN,
                    mViewModel.taskCompletionIcons as ArrayList<Int>
                )
            }
        }.show(supportFragmentManager, EvaluationListDialog.TAG)
    }

    private fun showOnTimeTaskDialog() {
        evaluationListDialog.apply {
            arguments = Bundle().apply {
                putString(EvaluationListDialog.ARG_TITLE, mViewModel.getOnTimeTaskString())
                putStringArrayList(
                    EvaluationListDialog.ARG_ITEM,
                    mViewModel.taskNames as ArrayList<String>
                )
                putIntegerArrayList(
                    EvaluationListDialog.ARG_BOOLEAN,
                    mViewModel.taskOnTimeIcons as ArrayList<Int>
                )
            }
        }.show(supportFragmentManager, EvaluationListDialog.TAG)
    }

    private fun showTargetDialog() {
        evaluationListDialog.apply {
            arguments = Bundle().apply {
                putString(EvaluationListDialog.ARG_TITLE, mViewModel.getCompletedTargetString())
                putStringArrayList(
                    EvaluationListDialog.ARG_ITEM,
                    mViewModel.targetNames as ArrayList<String>
                )
                putIntegerArrayList(
                    EvaluationListDialog.ARG_BOOLEAN,
                    mViewModel.targetIcons as ArrayList<Int>
                )
            }
        }.show(supportFragmentManager, EvaluationListDialog.TAG)
    }

    private fun goToStartNewCycle() {
        val intent = Intent(this, StartNewCycleActivity::class.java)
        startActivity(intent)
    }
}