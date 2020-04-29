package inas.anisha.skripsi_app.ui.main.schedule.displayschool

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassViewModel

class DisplaySchoolClassViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository: Repository = Repository.getInstance(application)

    fun getSchoolClasses(dayOfWeek: Int): LiveData<List<SchoolClassViewModel>> {
        return Transformations.map(mRepository.getSchoolClasses(dayOfWeek)) { data ->
            data.map { SchoolClassViewModel().fromEntity(it) }
        }
    }
}