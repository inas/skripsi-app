package inas.anisha.skripsi_app.ui.evaluation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
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

    private lateinit var taskDialog: EvaluationListDialog
    private lateinit var onTimeDialog: EvaluationListDialog
    private lateinit var targetDialog: EvaluationListDialog

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
                        taskDialog.show(supportFragmentManager, EvaluationListDialog.TAG)
                    }
                })
            }

            mBinding.listviewTask.apply {
                setAdapter(taskExpandableListAdapter)
                setOnGroupClickListener { parent, v, groupPosition, id -> false }
            }
        }

        if (onTimeExpandableListAdapter == null) {
            onTimeExpandableListAdapter = EvaluationExpandableListAdapter(this).apply {
                setItemClickListener(object : EvaluationExpandableListAdapter.ItemClickListener {
                    override fun onClick() {
                        onTimeDialog.show(supportFragmentManager, EvaluationListDialog.TAG)
                    }
                })
            }

            mBinding.listviewOnTime.apply {
                setAdapter(onTimeExpandableListAdapter)
                setOnGroupClickListener { parent, v, groupPosition, id -> false }
            }
        }

        if (targetExpandableListAdapter == null) {
            targetExpandableListAdapter = EvaluationExpandableListAdapter(this).apply {
                setItemClickListener(object : EvaluationExpandableListAdapter.ItemClickListener {
                    override fun onClick() {
                        targetDialog.show(supportFragmentManager, EvaluationListDialog.TAG)
                    }
                })
            }

            mBinding.listviewTarget.apply {
                setAdapter(targetExpandableListAdapter)
                setOnGroupClickListener { parent, v, groupPosition, id -> false }
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

    fun setTaskData() {
        currentCycleTasksObservable = mViewModel.getCurrentCycleTasks().apply {
            observe(this@EvaluationReportActivity, Observer { tasks ->
                var completedTask = 0
                var onTimeTask = 0

                val taskNames = mutableListOf<String>()
                val taskCompletionIcons = mutableListOf<Int>()
                val taskOnTimeIcons = mutableListOf<Int>()

                val statusPairCompleted = mutableListOf<Pair<String, Int>>()
                val statusPairOnTime = mutableListOf<Pair<String, Int>>()
                tasks.forEach {
                    taskNames.add(it.name)

                    if (it.isCompleted) {
                        statusPairCompleted.add(Pair(it.name, R.drawable.ic_check_green_white))
                        taskCompletionIcons.add(R.drawable.ic_check_green_white)
                        completedTask++
                    } else {
                        statusPairCompleted.add(Pair(it.name, R.drawable.ic_cross))
                        taskCompletionIcons.add(R.drawable.ic_cross)
                    }

                    if (it.isOnTime) {
                        statusPairOnTime.add(Pair(it.name, R.drawable.ic_check_green_white))
                        taskOnTimeIcons.add(R.drawable.ic_check_green_white)
                        onTimeTask++
                    } else {
                        statusPairOnTime.add(Pair(it.name, R.drawable.ic_cross))
                        taskOnTimeIcons.add(R.drawable.ic_cross)
                    }
                }

                val headerTextCompleted = "" + completedTask + "/" + tasks.size + " tugas selesai"
                val mapCompleted = hashMapOf(
                    Pair(
                        headerTextCompleted,
                        statusPairCompleted.take(EvaluationExpandableListAdapter.MAX_CHILD + 1)
                    )
                )
                taskExpandableListAdapter?.setData(mutableListOf(headerTextCompleted), mapCompleted)
                taskDialog.apply {
                    arguments = Bundle().apply {
                        putString(EvaluationListDialog.ARG_TITLE, headerTextCompleted)
                        putStringArrayList(
                            EvaluationListDialog.ARG_ITEM,
                            taskNames as ArrayList<String>
                        )
                        putIntegerArrayList(
                            EvaluationListDialog.ARG_BOOLEAN,
                            taskCompletionIcons as ArrayList<Int>
                        )
                    }
                }

                val headerTextOnTime =
                    "" + onTimeTask + "/" + tasks.size + " tugas dikerjakan tepat waktu"
                val mapOnTime = hashMapOf(
                    Pair(
                        headerTextOnTime,
                        statusPairOnTime.take(EvaluationExpandableListAdapter.MAX_CHILD + 1)
                    )
                )
                onTimeExpandableListAdapter?.setData(mutableListOf(headerTextOnTime), mapOnTime)
                onTimeDialog.apply {
                    arguments = Bundle().apply {
                        putString(EvaluationListDialog.ARG_TITLE, headerTextOnTime)
                        putStringArrayList(
                            EvaluationListDialog.ARG_ITEM,
                            taskNames as ArrayList<String>
                        )
                        putIntegerArrayList(
                            EvaluationListDialog.ARG_BOOLEAN,
                            taskOnTimeIcons as ArrayList<Int>
                        )
                    }
                }
            })
        }
    }

    fun setDialog() {
        taskDialog = EvaluationListDialog().apply {
            setConfirmationDialogListener(object :
                EvaluationListDialog.ConfirmationDialogListener {
                override fun onConfirmed() {
                    dismiss()
                }
            })
        }

        onTimeDialog = EvaluationListDialog().apply {
            setConfirmationDialogListener(object :
                EvaluationListDialog.ConfirmationDialogListener {
                override fun onConfirmed() {
                    dismiss()
                }
            })
        }

        targetDialog = EvaluationListDialog().apply {
            setConfirmationDialogListener(object :
                EvaluationListDialog.ConfirmationDialogListener {
                override fun onConfirmed() {
                    dismiss()
                }
            })
        }
    }

    fun setTargetData() {
        supportingTargetObservable = mViewModel.getSupportingTargets().apply {
            observe(this@EvaluationReportActivity, Observer { targets ->

                var completedTarget = 0
                val statusPair = mutableListOf<Pair<String, Int>>()
                val itemList = mutableListOf<String>()
                val icList = mutableListOf<Int>()
                targets.forEach {
                    itemList.add(it.name)
                    if (it.isCompleted) {
                        statusPair.add(Pair(it.name, R.drawable.ic_check_green_white))
                        icList.add(R.drawable.ic_check_green_white)
                        completedTarget++
                    } else {
                        statusPair.add(Pair(it.name, R.drawable.ic_cross))
                        icList.add(R.drawable.ic_cross)
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

                targetDialog.apply {
                    arguments = Bundle().apply {
                        putString(EvaluationListDialog.ARG_TITLE, headerText)
                        putStringArrayList(
                            EvaluationListDialog.ARG_ITEM,
                            itemList as ArrayList<String>
                        )
                        putIntegerArrayList(
                            EvaluationListDialog.ARG_BOOLEAN,
                            icList as ArrayList<Int>
                        )
                    }
                }
            })
        }
    }

    private fun goToStartNewCycle() {
//        val intent = Intent(this, StartNewCycleActivity::class.java)
//        startActivity(intent)
    }
}