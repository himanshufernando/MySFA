package tkhub.project.sfa.ui.fragment.customer.newcustomer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import tkhub.project.sfa.R
import tkhub.project.sfa.SFA
import tkhub.project.sfa.services.listeners.CustomerPhotoListener
import tkhub.project.sfa.ui.activity.MainActivity
import tkhub.project.sfa.ui.dialog.InfoDialog
import tkhub.project.sfa.viewmodels.customer.CustomerViewModel
import java.io.File
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_new_customer.view.*
import tkhub.project.sfa.data.model.customers.CustomerStatus
import tkhub.project.sfa.data.model.Districts
import tkhub.project.sfa.data.model.route.Route
import tkhub.project.sfa.data.model.SfaViewModelResult
import tkhub.project.sfa.data.model.Towns
import tkhub.project.sfa.databinding.FragmentNewCustomerBinding
import tkhub.project.sfa.services.listeners.LocationSettings
import tkhub.project.sfa.services.perfrence.AppPrefs

class NewCustomerFragment : Fragment(), View.OnClickListener, CustomerPhotoListener,
    InfoDialog.InfoDialogListener,
    LocationSettings {


    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentNewCustomerBinding
    private val adapterDistricts = DistrictsAdapter()
    private val adapterCustomerStatus = CustomerStatusAdapter()
    private val adapterRoutes = RoutesAdapter()

    var appPrefs = AppPrefs

    private val viewmodel: CustomerViewModel by viewModels { CustomerViewModel.LiveDataVMFactory }


    var isDistrictListRequest = false
    var isCustomerStatusListRequest = false
    var isRouteListRequest = false

    lateinit var filePath: File
    lateinit var mStorage: StorageReference

    private val WRITE_REQUEST_CODE: Int = 222


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

    companion object {
        private const val REQUEST_PERMISSIONS_LOCATION = 34
        const val REQUEST_CHECK_SETTINGS = 35
        const val AUTOCOMPLETE_REQUEST_CODE = 65
    }


    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult != null) {
                setandSaveLocation(locationResult.lastLocation)

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_customer, container, false)
        binding.userViewModel = viewmodel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SFA.setCustomerPhotoListener(this)
        SFA.setLocationSettingsListener(this)

        binding.root.cl_navigation.setOnClickListener(this)
        binding.root.cl_customer_image.setOnClickListener(this)
        binding.root.cl_district.setOnClickListener(this)
        binding.root.cl_customer_status.setOnClickListener(this)
        binding.root.cl_routs.setOnClickListener(this)
        binding.root.txt_user_save.setOnClickListener(this)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            checkCurrentLocationSetting()
        }

        getCustomerStatus()
        getRoute()
        getDistricts()
        getTowns()

    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        mainActivity = (activity as MainActivity)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onResume() {
        super.onResume()


        mainActivity.unLockDrawer()
        isDistrictListRequest = false
        isCustomerStatusListRequest = false
        isRouteListRequest = false




        Glide.with(requireContext())
            .load(viewmodel.customerProfile.value)
            .error(R.drawable.ic_user)
            .into(binding.root.im_user_profile)




    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cl_navigation -> mainActivity.openDrawer()
            R.id.cl_customer_image -> {
                callImagePiker()
            }
            R.id.cl_district -> {
                isDistrictListRequest = true
                if (viewmodel.districtsList.value!!.isNullOrEmpty()) {
                    getDistricts()
                } else {
                    if (binding.root.cl_district_list.isVisible) {
                        binding.root.cl_district_list.visibility = View.GONE
                    } else {
                        binding.root.cl_district_list.visibility = View.VISIBLE
                    }

                }
            }

            R.id.cl_customer_status -> {
                isCustomerStatusListRequest = true
                if (viewmodel.customerStatusList.value!!.isNullOrEmpty()) {
                    getCustomerStatus()
                } else {
                    if (binding.root.cl_customer_status_list.isVisible) {
                        binding.root.cl_customer_status_list.visibility = View.GONE
                    } else {
                        binding.root.cl_customer_status_list.visibility = View.VISIBLE
                    }

                }
            }


            R.id.cl_routs -> {
                isRouteListRequest = true
                if (viewmodel.routesList.value!!.isNullOrEmpty()) {
                    getRoute()
                } else {
                    if (binding.root.cl_customer_route_list.isVisible) {
                        binding.root.cl_customer_route_list.visibility = View.GONE
                    } else {
                        binding.root.cl_customer_route_list.visibility = View.VISIBLE
                    }

                }

            }

            R.id.txt_user_save -> addNewCustomer()


        }

    }


    private fun addNewCustomer(){
        viewmodel.saveNewCustomer().observe(this, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {
                    Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                }
                is SfaViewModelResult.ExceptionError.ExError -> {
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT).show()

                }
                is SfaViewModelResult.LogicalError.LogError -> {
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT).show()
                }

            }

        })
    }
    private fun checkPermissions() = PermissionChecker.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PermissionChecker.PERMISSION_GRANTED

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_LOCATION
        )
    }

    private fun checkCurrentLocationSetting() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(context as Activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        builder.setAlwaysShow(true)

        task.addOnSuccessListener {
            getLastLocation()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener(context as Activity) { task ->
            if (location == null) {
                requestNewLocationData()
            } else {
                setandSaveLocation(task.result!!)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context as Activity)
        fusedLocationClient.requestLocationUpdates(
            locationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    fun setandSaveLocation(loc: Location) {
        location = loc
        viewmodel.currentLocation.value = location


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSIONS_LOCATION -> {
                checkCurrentLocationSetting()
            }
            else -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    mainActivity.openImagePiker()
                }
            }
        }

        return
    }


    private fun getCustomerStatus() {
        binding.root.recyclerView_customer_status.adapter = adapterCustomerStatus
        adapterCustomerStatus.setOnItemClickListener(object : CustomerStatusAdapter.ClickListener {
            override fun onClick(customerStatus: CustomerStatus, aView: View) {
                binding.root.cl_customer_status_list.visibility = View.GONE
                binding.root.txt_selected_customer_status.text = customerStatus.customer_status
                viewmodel.selectedCustomerStatus.value = customerStatus
            }
        })
        viewmodel.getAllCustomerStatus().observe(viewLifecycleOwner, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {
                    viewmodel.customerStatusList.value = it.data.data
                    adapterCustomerStatus.submitList(viewmodel.customerStatusList.value)
                    if (isCustomerStatusListRequest) {
                        binding.root.cl_customer_status_list.visibility = View.VISIBLE
                    }

                }
                is SfaViewModelResult.ExceptionError.ExError -> {

                }
                is SfaViewModelResult.LogicalError.LogError -> {

                }

            }

        })
    }

    private fun getRoute() {
        binding.root.recyclerView_route.adapter = adapterRoutes
        adapterRoutes.setOnItemClickListener(object : RoutesAdapter.ClickListener {
            override fun onClick(route: Route, aView: View) {
                binding.root.cl_customer_route_list.visibility = View.GONE
                binding.root.txt_selected_routs.text = route.customer_route
                viewmodel.selectedRoutes.value = route
            }
        })


        viewmodel.getAllRoutes().observe(viewLifecycleOwner, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {
                    viewmodel.routesList.value = it.data.data
                    adapterRoutes.submitList(viewmodel.routesList.value)

                    if (isDistrictListRequest) {
                        binding.root.cl_customer_route_list.visibility = View.VISIBLE
                    }

                }
                is SfaViewModelResult.ExceptionError.ExError -> {

                }
                is SfaViewModelResult.LogicalError.LogError -> {

                }

            }

        })


    }

    private fun getDistricts() {
        binding.root.recyclerView_districts.adapter = adapterDistricts
        adapterDistricts.setOnItemClickListener(object : DistrictsAdapter.ClickListener {
            override fun onClick(districts: Districts, aView: View) {
                binding.root.cl_district_list.visibility = View.GONE
                binding.root.txt_selected_district.text = districts.district_name
                viewmodel.selectedDistrict.value = districts
            }
        })


        viewmodel.getAllDistricts().observe(viewLifecycleOwner, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {
                    viewmodel.districtsList.value = it.data
                    adapterDistricts.submitList(viewmodel.districtsList.value)
                    if (isDistrictListRequest) {
                        binding.root.cl_district_list.visibility = View.VISIBLE
                    }
                }
                is SfaViewModelResult.ExceptionError.ExError -> {

                }
                is SfaViewModelResult.LogicalError.LogError -> {

                }

            }

        })


    }



    private fun getTowns() {
        viewmodel.getAllTowns().observe(viewLifecycleOwner, Observer {
            when (it) {
                is SfaViewModelResult.Success -> {
                    var autoCompleteTownAdapter = TownAdapter(requireContext(), R.layout.fragment_new_customer, R.id.lbl_name, it.data)
                    binding.root.edt_town.setAdapter(autoCompleteTownAdapter)
                }
                is SfaViewModelResult.ExceptionError.ExError -> {

                }
                is SfaViewModelResult.LogicalError.LogError -> {

                }

            }

        })

        binding.root.edt_town.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            var selectedPro : Towns = parent.getItemAtPosition(position) as Towns
            viewmodel.selectedTown.value = selectedPro
        }
    }



    private fun callImagePiker() {
        val permission = activity?.let {
            PermissionChecker.checkSelfPermission(
                it,
                Manifest.permission.CAMERA
            )
        }
        val permissionStorage = activity?.let {
            PermissionChecker.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        when {
            permission != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), WRITE_REQUEST_CODE
                )
            }
            permissionStorage != PackageManager.PERMISSION_GRANTED -> {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), WRITE_REQUEST_CODE
                )
            }
            else -> {
                mainActivity.openImagePiker()

            }
        }

    }

    private fun showInfoDialog(message: String, code: Int) {
        val dialogInfoFragment = InfoDialog.newInstance(message, code, appPrefs.INFO_DIALOG_SUCCESS)
        dialogInfoFragment.setListener(this)
        dialogInfoFragment.show(activity?.supportFragmentManager!!, "Success")
    }


    override fun onInfoDialogPositive(code: Int) {

    }

    override fun onCustomerPhotoUrlResponse(file: File) {

        if (file != null) {
            binding.root.anim_customer_image.visibility = View.VISIBLE
            mStorage = FirebaseStorage.getInstance().reference
            var file = Uri.fromFile(file)
            val riversRef = mStorage.child("Customers/${file.lastPathSegment}")
            var uploadTask = riversRef.putFile(file)


            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                riversRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewmodel.customerProfile.value = task.result
                    Glide.with(requireContext())
                        .load(viewmodel.customerProfile.value)
                        .error(R.drawable.ic_user)
                        .into(binding.root.im_user_profile)

                } else {
                    binding.root.anim_customer_image.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Profile pictures uploading fail due to network failure,Please try again !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }


    }

    override fun onCustomerPhotoUrlResponseError(error: Throwable) {
        showInfoDialog("Error in selecting Customer Photo,Please try again !", 0)
    }

    override fun onLocationSettingsResponse() {
        getLastLocation()
    }


}