package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.ui.common.tambahTarget.TargetUtamaViewModel

class KelolaPembelajaranViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)
    var mMainTarget: TargetUtamaViewModel? = null
    var page: MutableLiveData<Int> = MutableLiveData(0)

    fun setShouldNotShowKelolaPembelajaran() {
        mRepository.setShouldNotShowKelolaPembelajaran()
    }

    fun setMainTarget(target: TargetUtamaViewModel?) {
        mMainTarget = target
    }
}