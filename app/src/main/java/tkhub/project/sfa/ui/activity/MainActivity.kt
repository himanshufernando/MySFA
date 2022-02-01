package tkhub.project.sfa.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pl.aprilapps.easyphotopicker.*
import tkhub.project.sfa.R
import tkhub.project.sfa.SFA
import tkhub.project.sfa.data.model.MenuItems
import tkhub.project.sfa.services.listeners.CustomerPhotoListener
import androidx.activity.viewModels
import tkhub.project.sfa.services.listeners.LocationSettings
import tkhub.project.sfa.ui.fragment.customer.newcustomer.NewCustomerFragment.Companion.REQUEST_CHECK_SETTINGS

class MainActivity : FragmentActivity() {


    lateinit var navController: NavController
    lateinit var navView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var easyImage: EasyImage
    var menuItemsList = ArrayList<MenuItems>()
    private val adapterMenus = MenusAdapter()
    lateinit var locationSettings: LocationSettings


    //Listeners
    lateinit var customerPhotoListener: CustomerPhotoListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
        setContentView(R.layout.activity_main)

        navView = nav_view
        drawerLayout = drawer_layout

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)



        ezyImageInt()




        recyclerView_menus.adapter = adapterMenus
        adapterMenus.setOnItemClickListener(object : MenusAdapter.ClickListener {
            override fun onClick(selectedMenu: MenuItems, aView: View) {

                for (items in menuItemsList) {
                    items.isSelect = false
                }
                selectedMenu.isSelect = true
                adapterMenus.notifyDataSetChanged()



                when (selectedMenu.menuItemCode) {
                    "NCS" -> {
                        navController.navigate(R.id.fragmentTONewUser)
                    }
                    "CS" -> {
                        navController.navigate(R.id.fragmentToCustomers)
                    }

                    "ECS" ->{
                        navController.navigate(R.id.fragmentToEditCustomers)
                        
                    }

                }


            }
        })


    }

    private fun ezyImageInt(){
        easyImage = EasyImage.Builder(this)
            .setCopyImagesToPublicGalleryFolder(false)
            .setFolderName("EasyImage sample")
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(false)
            .build()

    }
    override fun onResume() {
        super.onResume()



      //  clearClickOnMenus()

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        println("ffffffffffffffffffffffffffffffff requestCode "+requestCode)

        when (requestCode) {
            34963 -> {
                easyImage.handleActivityResult(
                    requestCode,
                    resultCode,
                    data,
                    this,
                    object : DefaultCallback() {
                        override fun onMediaFilesPicked(
                            imageFiles: Array<MediaFile>,
                            source: MediaSource
                        ) {
                            customerPhotoListener = SFA.getCustomerPhotoListener()!!
                            for (item in imageFiles) {
                                customerPhotoListener.onCustomerPhotoUrlResponse(item.file)
                            }
                        }

                        override fun onImagePickerError(error: Throwable, source: MediaSource) {
                            customerPhotoListener.onCustomerPhotoUrlResponseError(error)
                        }

                        override fun onCanceled(source: MediaSource) {}
                    })
            }

            REQUEST_CHECK_SETTINGS ->{
                println("ffffffffffffffffffffffffffffffff 00 ")
                try {
                    locationSettings = SFA.getLocationSettingsListener()!!
                    locationSettings.onLocationSettingsResponse()
                } catch (ex: Exception) {
                    println("ffffffffffffffffffffffffffffffff 00 ")
                }

            }

        }

    }


    fun openImagePiker() {
        easyImage.openChooser(this)
    }


    private fun clearClickOnMenus() {
        for (items in menuItemsList) {
            items.isSelect = false
        }
        adapterMenus.notifyDataSetChanged()
    }

    fun lockDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun unLockDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun openDrawer() {
        hideKeyboard()
        drawerLayout.openDrawer(GravityCompat.START)
    }


    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }


    fun getMenus() {

        menuItemsList.clear()
        var item1 = MenuItems().apply {
            menuID = 1
            menuItemName = "Customer"
            menuItemID = 1
            menuItemCode = "CS"
            menuCompanyID = 1
        }

        var item2 = MenuItems().apply {
            menuID = 1
            menuItemName = "New Customer"
            menuItemID = 2
            menuItemCode = "NCS"
            menuCompanyID = 1
        }

        var item3 = MenuItems().apply {
            menuID = 1
            menuItemName = "Edit Customer"
            menuItemID = 3
            menuItemCode = "ECS"
            menuCompanyID = 1
        }

        menuItemsList.add(item1)
        menuItemsList.add(item2)
        menuItemsList.add(item3)


        adapterMenus.submitList(menuItemsList)
    }
}