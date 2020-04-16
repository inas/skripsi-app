package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel

class KelolaPembelajaranViewModel(application: Application) : AndroidViewModel(application) {

    var mRepository: Repository = Repository.getInstance(application)

    var mainTarget: TargetUtamaViewModel = TargetUtamaViewModel()
    var cycleTime: Pair<Int, Int> = Pair(0, 0)
    var supportingTargets: MutableList<TargetPendukungViewModel> = mutableListOf()

    var page: MutableLiveData<Int> = MutableLiveData(0)

    fun saveDataToRepository() {
        val mainTargetDataModel =
            TargetUtamaEntity(0, mainTarget.name, mainTarget.note, mainTarget.date)

        val supportingTargetDataModels = mutableListOf<TargetPendukungEntity>()
        supportingTargets.forEach {
            supportingTargetDataModels.add(TargetPendukungEntity(0, it.name, it.note, it.time))
        }

        mRepository.setMainTarget(mainTargetDataModel)
        mRepository.setSupportingTargets(supportingTargetDataModels)
        mRepository.setCycleTime(cycleTime)
        mRepository.setShouldNotShowKelolaPembelajaran()
    }

}