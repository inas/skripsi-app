package inas.anisha.skripsi_app.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class OnboardingPagerViewModel(application: Application) : AndroidViewModel(application) {

    var title: String = ""
    var description: String = ""
    var pageNum: MutableLiveData<Int> = MutableLiveData(0)

    fun initViewModel(title: String, description: String, pageNum: Int) {
        this.title = title
        this.description = description
        this.pageNum.value = pageNum
    }
}