package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.datamodel.TargetPendukungDataModel
import inas.anisha.skripsi_app.data.datamodel.TargetUtamaDataModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel

class KelolaPembelajaranViewModel(application: Application) : AndroidViewModel(application) {

    var mRepository: Repository = Repository.getInstance(application)

    var mainTarget: TargetUtamaViewModel = TargetUtamaViewModel()
    var cycleTime: Pair<Int, Int> = Pair(0, 0)
    var supportingTargets: MutableList<TargetPendukungViewModel> = mutableListOf()

    var page: MutableLiveData<Int> = MutableLiveData(0)

    fun saveDataToRepository() {
        val mainTargetDataModel = TargetUtamaDataModel().apply {
            name = mainTarget.name
            note = mainTarget.note
            date = mainTarget.date
        }

        val supportingTargetDataModels = mutableListOf<TargetPendukungDataModel>()
        supportingTargets.forEach {
            val dataModel = TargetPendukungDataModel().apply {
                name = it.name
                note = it.note
                time = it.time
            }
            supportingTargetDataModels.add(dataModel)
        }

        mainTargetDataModel
        supportingTargetDataModels
        cycleTime

    }

}