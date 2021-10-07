package tkhub.project.sfa.viewmodels.user

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import tkhub.project.sfa.SFA
import tkhub.project.sfa.data.model.*
import tkhub.project.sfa.data.model.user.User
import tkhub.project.sfa.repo.user.UserRepo
import tkhub.project.sfa.services.network.api.APIInterface
import tkhub.project.sfa.services.perfrence.AppPrefs

class UserViewModel(client: APIInterface) : ViewModel()  {


    var userRepo = UserRepo(client)



    // login fragment
    var editTextLoginPhone = ObservableField<String>()
    var editTextLoginPassword = ObservableField<String>()
    var layoutLoginLoading = ObservableField<Boolean>()



    init {
        layoutLoginLoading.set(false)
    }



    // login user
    private val _callUserLogin = MutableLiveData<User>()
    private val callUserLogin: LiveData<User> = _callUserLogin

    fun userLogin(){
        layoutLoginLoading.set(true)
        var user = User().apply {
            user_mobile = editTextLoginPhone.get().toString().trim()
            user_password = editTextLoginPassword.get().toString().trim()
        }
        _callUserLogin.value = user
    }

    val getUserLoginResponse = callUserLogin.switchMap { it ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                var respond = userRepo.userLogin(it)
                layoutLoginLoading.set(false)
                if(!respond.error){
                    emit(SfaViewModelResult.Success(respond))
                    AppPrefs.setUserPrefs(respond.data)
                }else{
                    var baseApiModal = BaseApiModal().apply {
                        code = respond.code
                        error = respond.error
                        message = respond.message
                    }
                    emit(SfaViewModelResult.LogicalError.LogError(baseApiModal))
                }
            }
            catch (exception: Exception) {
                layoutLoginLoading.set(false)
                emit(SfaViewModelResult.ExceptionError.ExError(exception))
            }
        }
    }




    //  user push update
    private val _callUserPushTokenUpdate = MutableLiveData<Long>()
    private val callUserPushTokenUpdate: LiveData<Long> = _callUserPushTokenUpdate

    fun pushTokenUpdate(userid : Long){
        _callUserPushTokenUpdate.value = userid
    }

    val pushTokenUpdateResponse = callUserPushTokenUpdate.switchMap { it ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                var respond = userRepo.pushTokenUpdate(it)
                if(!respond.error){
                    emit(SfaViewModelResult.Success(respond))
                }else{
                    var baseApiModal = BaseApiModal().apply {
                        code = respond.code
                        error = respond.error
                        message = respond.message
                    }
                    emit(SfaViewModelResult.LogicalError.LogError(baseApiModal))
                }
            }
            catch (exception: Exception) {
                emit(SfaViewModelResult.ExceptionError.ExError(exception))
            }
        }
    }




    object LiveDataVMFactory : ViewModelProvider.Factory {
        var app: Context = SFA.applicationContext()
        private val dataSource = APIInterface.create()
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(dataSource) as T

        }
    }

}