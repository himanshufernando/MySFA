package tkhub.project.sfa.services.perfrence

import android.content.Context
import com.google.gson.Gson
import tkhub.project.sfa.R
import tkhub.project.sfa.SFA
import tkhub.project.sfa.data.model.BaseApiModal
import tkhub.project.sfa.data.model.user.User
import java.util.*
import java.util.regex.Pattern

object AppPrefs {


    //shard pref keys
    const val PREFNAME = "sfapref"
    const val KEY_PUSH_ID = "push_id"


    const val KEY_USER = "user"

    //info Dialog Type
    const val INFO_DIALOG_ERROR = 1
    const val INFO_DIALOG_SUCCESS = 2

    //Error Code
     const val ERROR_INTERNET = 404



    //Customer Error Code 10
    const val ERROR_CUSTOMER_EMPTY_NAME = 2001
    const val ERROR_CUSTOMER_EMPTY_ADDRESS = 2002
    const val ERROR_CUSTOMER_EMPTY_DISTRICT = 2003
    const val ERROR_CUSTOMER_EMPTY_AREA = 2004
    const val ERROR_CUSTOMER_EMPTY_TOWN = 2005
    const val ERROR_CUSTOMER_EMPTY_ROUTE= 2006
    const val ERROR_CUSTOMER_EMPTY_STATUS= 2007
    const val ERROR_CUSTOMER_EMPTY_CONTACT_NUMBER= 2008
    const val ERROR_CUSTOMER_INVALID_CONTACT_NUMBER= 2009
    const val ERROR_CUSTOMER_INVALID_EMAIL= 2010


    //User Error Code 10
    const val ERROR_USER_EMPTY_MOBILE = 1005
    const val ERROR_USER_INVALID_MOBILE = 1006






    fun setUserPrefs(value: User) {
        val sharedPref = SFA.applicationContext().getSharedPreferences(PREFNAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(KEY_USER, Gson().toJson(value))
            commit()
        }
    }

    fun getUserPrefs(): User {
        val sharedPref = SFA.applicationContext().getSharedPreferences(PREFNAME, Context.MODE_PRIVATE)
        var defUser = User()

        return if (sharedPref.getString(KEY_USER, null) == null) {
            defUser
        } else {
            Gson().fromJson(sharedPref.getString(KEY_USER, null), User::class.java)
        }

    }



    fun setPushIdPrefs(ctx: Context, id: String) {
        val sharedPref = ctx.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(KEY_PUSH_ID, id)
            commit()
        }
    }

    fun getPushIdPrefs(ctx: Context): String {
        val sharedPref = ctx.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_PUSH_ID, "")!!
    }

    fun errorNoInternet(): BaseApiModal {
        return BaseApiModal(
            true,
            SFA.applicationContext().resources.getString(R.string.no_internet),
            "",
            ERROR_INTERNET
        )
    }


    fun checkValidString(st: String): Boolean {
        if ((st.isNullOrEmpty()) || (st == "null")) {
            return true
        }
        return false
    }

    fun validateEmailAddress(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    fun validatePhoneNumber(number: String): Boolean {
        if(number.length != 10){
            return true
        }

        if (!(number.startsWith("0"))){
            return true
        }


        return false
    }

    fun getUUID():String{
        var uuid = ""
        uuid = UUID.randomUUID().toString()
        if(uuid.isEmpty()){
            val c: Calendar = Calendar.getInstance()
            uuid = c.get(Calendar.DATE).toString() +((1..10000).random()).toString()+ c.get(Calendar.HOUR).toString() + c.get(Calendar.MINUTE).toString() + c.get(Calendar.SECOND).toString() + c.get(Calendar.MILLISECOND).toString() + ((1..10000).random()).toString()

        }
       return uuid
    }


}