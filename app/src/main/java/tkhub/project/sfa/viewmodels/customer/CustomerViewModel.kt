package tkhub.project.sfa.viewmodels.customer

import android.content.Context
import android.location.Location
import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import tkhub.project.sfa.SFA
import tkhub.project.sfa.data.db.*
import tkhub.project.sfa.data.model.*
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.data.model.customers.CustomerStatus
import tkhub.project.sfa.data.model.route.Route
import tkhub.project.sfa.repo.customer.CustomerRepo
import tkhub.project.sfa.services.network.api.APIInterface

class CustomerViewModel(
    customerDao: CustomerDao,
    customerStatusDao: CustomerStatusDao,
    routeDao: RouteDao,
    districtsDao: DistrictsDao,
    townsDao: TownsDao,
    client: APIInterface
) : ViewModel() {


    var custRepo = CustomerRepo(customerDao, customerStatusDao, routeDao, districtsDao, townsDao, client)

    //

    var edtCustomerName = ObservableField<String>()
    var edtAddress = ObservableField<String>()
    var edtArea = ObservableField<String>()
    var edtContactNumber = ObservableField<String>()
    var edtEmail = ObservableField<String>()
    var edtBR = ObservableField<String>()
    var edtOwnersName = ObservableField<String>()
    var edtOwnersContactNumber = ObservableField<String>()
    var edtRegNumber = ObservableField<String>()


    //districts list[New Customer]
    val districtsList: MutableLiveData<ArrayList<Districts>> by lazy {
        MutableLiveData<ArrayList<Districts>>()
    }
    val selectedDistrict: MutableLiveData<Districts> by lazy {
        MutableLiveData<Districts>()
    }

    //Customer Status list[New Customer]
    val customerStatusList: MutableLiveData<ArrayList<CustomerStatus>> by lazy {
        MutableLiveData<ArrayList<CustomerStatus>>()
    }
    val selectedCustomerStatus: MutableLiveData<CustomerStatus> by lazy {
        MutableLiveData<CustomerStatus>()
    }


    //routes list[New Customer]
    val routesList: MutableLiveData<ArrayList<Route>> by lazy {
        MutableLiveData<ArrayList<Route>>()
    }
    val selectedRoutes: MutableLiveData<Route> by lazy {
        MutableLiveData<Route>()
    }

    //town [New Customer]
    val selectedTown: MutableLiveData<Towns> by lazy {
        MutableLiveData<Towns>()
    }

    //location [New Customer]
    val currentLocation: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>()
    }



    // save Customer[Customer]
    private val _callGetCustomer = MutableLiveData<Int>()
    val callGetCustomer: LiveData<Int> = _callGetCustomer


    // [Customer]
    val customerList: MutableLiveData<ArrayList<Customer>> by lazy {
        MutableLiveData<ArrayList<Customer>>()
    }


    // [Customer]
    val customerProfile: MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>()
    }

    init {
        customerProfile.value = Uri.EMPTY
        customerList.value = ArrayList<Customer>()

    }



    fun getCustomer() = liveData(Dispatchers.IO) {
        try {
            emit(SfaViewModelResult.Success(custRepo.getCustomers()))
        } catch (exception: Exception) {
            emit(SfaViewModelResult.ExceptionError.ExError(exception))
        }

    }



    // save Customer[New Customer]

    fun saveNewCustomer() = liveData(Dispatchers.IO) {
        try {

            var customer = Customer().apply {
                customer_name = edtCustomerName.get().toString().trim()
                customer_address = edtAddress.get().toString().trim()
                customer_district = selectedDistrict.value!!.district_name
                customer_district_id = selectedDistrict.value!!.district_id
                customer_area = edtArea.get().toString().trim()
                customer_town = selectedTown.value!!.name_en
                customer_town_id = selectedTown.value!!.id
                customer_route = selectedRoutes.value!!.customer_route
                customer_route_id = selectedRoutes.value!!.customer_route_id
                customer_status = selectedCustomerStatus.value!!.customer_status
                customer_status_id = selectedCustomerStatus.value!!.customer_status_id
                customer_contact_number = edtContactNumber.get().toString().trim()
                customer_email = edtEmail.get().toString().trim()
                customer_br_number = edtBR.get().toString().trim()
                customer_owners_name = edtOwnersName.get().toString().trim()
                customer_owners_contact_number = edtOwnersContactNumber.get().toString().trim()
                customer_reg_number = edtRegNumber.get().toString().trim()
                customer_image = customerProfile.value.toString()

                if(currentLocation.value == null){
                    customer_latitude = 0.0
                    customer_longitude = 0.0
                }else{
                    customer_latitude = currentLocation.value?.latitude!!
                    customer_longitude = currentLocation.value?.longitude!!
                }

            }

            val respond = custRepo.saveNewCustomers(customer)
            if (respond.status) {
                emit(SfaViewModelResult.Success(respond))
            } else {
                val baseApiModal = BaseApiModal().apply {
                    code = respond.code
                    error = respond.status
                    message = respond.message
                }
                emit(SfaViewModelResult.LogicalError.LogError(baseApiModal))
            }
        } catch (exception: Exception) {
           //emit(AudiciResult.ExceptionError.ExError(exception))
        }
    }




    // get all district[New Customer]

    fun getAllDistricts() = liveData(Dispatchers.IO) {
        try {
            emit(SfaViewModelResult.Success(custRepo.getAllDistricts()))
        } catch (exception: Exception) {
            emit(SfaViewModelResult.ExceptionError.ExError(exception))
        }

    }



    // get all towns[New Customer]

    fun getAllTowns() = liveData(Dispatchers.IO) {
        try {
            var respond = custRepo.getAllTowns()
            emit(SfaViewModelResult.Success(respond))

        } catch (exception: Exception) {
            emit(SfaViewModelResult.ExceptionError.ExError(exception))
        }

    }



    // get all Customer Status[New Customer]


    fun getAllCustomerStatus() = liveData(Dispatchers.IO) {
        try {
            var respond = custRepo.getCustomerStatus()
            emit(SfaViewModelResult.Success(respond))
        } catch (exception: Exception) {
            emit(SfaViewModelResult.ExceptionError.ExError(exception))
        }

    }



    // get all route[New Customer]
    fun getAllRoutes() = liveData(Dispatchers.IO) {
        try {
            var respond = custRepo.getRoute()
            emit(SfaViewModelResult.Success(respond))
        } catch (exception: Exception) {
            emit(SfaViewModelResult.ExceptionError.ExError(exception))
        }

    }

    object LiveDataVMFactory : ViewModelProvider.Factory {
        var app: Context = SFA.applicationContext()
        private val customerDAO = AppDatabase.getInstance(app).customerDao()
        private val customerStatusDao = AppDatabase.getInstance(app).customerStatusDao()
        private val routeDao = AppDatabase.getInstance(app).routeDao()
        private val districtsDao = AppDatabase.getInstance(app).districtsDao()
        private val townsDao = AppDatabase.getInstance(app).townsDao()
        private val dataSource = APIInterface.create()
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CustomerViewModel(
                customerDAO,
                customerStatusDao,
                routeDao,
                districtsDao,
                townsDao,
                dataSource
            ) as T
        }
    }

}
