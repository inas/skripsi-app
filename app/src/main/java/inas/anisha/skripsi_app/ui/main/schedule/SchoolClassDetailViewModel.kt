package inas.anisha.skripsi_app.ui.main.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity

class SchoolClassDetailViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository = Repository.getInstance(application)
    var schoolClass: SchoolClassEntity = SchoolClassEntity(0)

    fun getSchoolClass(classId: Long): LiveData<SchoolClassEntity> =
        mRepository.getSchoolClass(classId)

    fun updateSchoolClass(schoolClass: SchoolClassViewModel) =
        mRepository.updateSchoolClass(schoolClass.toEntity())

    fun deleteSchoolClass() = mRepository.deleteSchoolClass(schoolClass.id)

}