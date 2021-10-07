package tkhub.project.sfa

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import tkhub.project.sfa.services.listeners.CustomerPhotoListener
import tkhub.project.sfa.services.listeners.LocationSettings

class SFA : MultiDexApplication() {

    companion object {
        private var instance: SFA? = null

        //Listener
        private var customerPhotoListener: CustomerPhotoListener? = null
        private var locationSettings: LocationSettings? = null




        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun setLocationSettingsListener(locationsettings: LocationSettings) { locationSettings = locationsettings }
        fun getLocationSettingsListener(): LocationSettings? { return locationSettings }

        fun setCustomerPhotoListener(photolistener: CustomerPhotoListener) { customerPhotoListener = photolistener }
        fun getCustomerPhotoListener(): CustomerPhotoListener? { return customerPhotoListener }


    }
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = SFA.applicationContext()

    }

}