package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository

class KelolaPembelajaranViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)

    fun setShouldNotShowKelolaPembelajaran() {
        mRepository.setShouldNotShowKelolaPembelajaran()
    }
}