package inas.anisha.skripsi_app.ui.main.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository

class SchedulePageViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository: Repository = Repository.getInstance(application)

}