package tkhub.project.sfa.repo.user

import com.google.gson.JsonObject
import tkhub.project.sfa.R
import tkhub.project.sfa.SFA
import tkhub.project.sfa.data.model.*
import tkhub.project.sfa.data.model.user.User
import tkhub.project.sfa.data.model.user.UserBase
import tkhub.project.sfa.services.network.api.APIInterface
import tkhub.project.sfa.services.network.internet.InternetConnection
import tkhub.project.sfa.services.perfrence.AppPrefs

class UserRepo(var client: APIInterface) {

    var appPref = AppPrefs




    suspend fun userLogin(user: User): UserBase {
        var userBase = UserBase()
        when {
            (!InternetConnection.checkInternetConnection()) -> {
                userBase.code = appPref.ERROR_INTERNET
                userBase.message = SFA.applicationContext().resources.getString(R.string.no_internet)
                return userBase
            }
            appPref.checkValidString(user.user_mobile) -> {
                userBase.code = appPref.ERROR_USER_EMPTY_MOBILE
                userBase.message = "Please enter your mobile number !"
                return userBase
            }
            user.user_mobile.length != 10 -> {
                userBase.code = appPref.ERROR_USER_INVALID_MOBILE
                userBase.message = "Please enter valid mobile number !"
                return userBase
            }
            appPref.checkValidString(user.user_password) -> {
                userBase.code = appPref.ERROR_USER_EMPTY_MOBILE
                userBase.message = "Please enter your password !"
                return userBase
            }
            else ->{
                return client.userLogin(user.user_mobile,user.user_password)
            }

        }
        return userBase
    }



    suspend fun pushTokenUpdate(userID: Long): BaseApiModal {
        var userBase = BaseApiModal()
        var pushToken = appPref.getPushIdPrefs(SFA.applicationContext())
        when {
            (!InternetConnection.checkInternetConnection()) -> {
                userBase.code = appPref.ERROR_INTERNET
                userBase.message = SFA.applicationContext().resources.getString(R.string.no_internet)
                return userBase
            }
            appPref.checkValidString(pushToken) -> {
                return userBase
            }
            else ->{
                var userPushJson = JsonObject()

                userPushJson.addProperty("user_push_token", pushToken)

                return client.pushtokenupdate(userID,userPushJson)
            }

        }
        return userBase
    }


}