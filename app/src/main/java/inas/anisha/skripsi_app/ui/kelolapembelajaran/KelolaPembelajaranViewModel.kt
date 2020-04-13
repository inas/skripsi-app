package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.ui.common.addTarget.TargetUtamaViewModel

class KelolaPembelajaranViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)
    var mMainTarget: TargetUtamaViewModel? = null

    fun setShouldNotShowKelolaPembelajaran() {
        mRepository.setShouldNotShowKelolaPembelajaran()
    }

    fun setMainTarget(target: TargetUtamaViewModel?) {
        mMainTarget = target
    }
}