package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel

class KelolaPembelajaranViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)

    var mainTarget: TargetUtamaViewModel? = null
    var cycleTime: Pair<Int, Int>? = null

    var page: MutableLiveData<Int> = MutableLiveData(0)

}