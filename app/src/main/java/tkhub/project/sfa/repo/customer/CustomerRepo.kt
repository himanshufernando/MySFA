package tkhub.project.sfa.repo.customer

import tkhub.project.sfa.R
import tkhub.project.sfa.SFA
import tkhub.project.sfa.data.db.*
import tkhub.project.sfa.data.model.*
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.data.model.customers.CustomerBase
import tkhub.project.sfa.data.model.customers.CustomerStatus
import tkhub.project.sfa.data.model.customers.CustomerStatusBase
import tkhub.project.sfa.data.model.route.Route
import tkhub.project.sfa.data.model.route.RouteBase
import tkhub.project.sfa.services.network.api.APIInterface
import tkhub.project.sfa.services.network.internet.InternetConnection

import tkhub.project.sfa.services.perfrence.AppPrefs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomerRepo(private val customerDao: CustomerDao,
                   private val customerStatusDao: CustomerStatusDao,
                   private val routeDao: RouteDao,
                   private val districtsDao: DistrictsDao,
                   private val townsDao: TownsDao,
                   var client: APIInterface) {

    var appPref = AppPrefs
    var user = appPref.getUserPrefs()
    val simpleDateFormat= SimpleDateFormat("dd-MM-yyyy HH:MM:SS")


    suspend fun saveNewCustomers(customer: Customer): CustomerBase {
        val customerBase = CustomerBase()
        when {

            appPref.checkValidString(customer.customer_name) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_NAME
                customerBase.message = "Please enter customer name !"
                return customerBase
            }
            appPref.checkValidString(customer.customer_address) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_ADDRESS
                customerBase.message = "Please enter customer address !"
                return customerBase
            }
            appPref.checkValidString(customer.customer_district) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_DISTRICT
                customerBase.message = "Please select customer district !"
                return customerBase
            }
            customer.customer_district_id == 0 -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_DISTRICT
                customerBase.message = "Please select customer district again!"
                return customerBase
            }

            appPref.checkValidString(customer.customer_area) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_AREA
                customerBase.message = "Please select customer area !"
                return customerBase
            }

            appPref.checkValidString(customer.customer_town) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_TOWN
                customerBase.message = "Please select customer town !"
                return customerBase
            }

            customer.customer_town_id == 0L -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_DISTRICT
                customerBase.message = "Please select customer town again!"
                return customerBase
            }

            appPref.checkValidString(customer.customer_route) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_ROUTE
                customerBase.message = "Please select customer route !"
                return customerBase
            }

            customer.customer_route_id == 0L -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_DISTRICT
                customerBase.message = "Please select customer route again!"
                return customerBase
            }

            appPref.checkValidString(customer.customer_status) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_STATUS
                customerBase.message = "Please select customer status !"
                return customerBase
            }

            customer.customer_status_id == 0L -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_DISTRICT
                customerBase.message = "Please select customer status again!"
                return customerBase
            }


            appPref.checkValidString(customer.customer_contact_number) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_EMPTY_CONTACT_NUMBER
                customerBase.message = "Please enter customer contact number !"
                return customerBase
            }

            appPref.validatePhoneNumber(customer.customer_contact_number) -> {
                customerBase.code = appPref.ERROR_CUSTOMER_INVALID_CONTACT_NUMBER
                customerBase.message = "Please enter valid customer contact number !"
                return customerBase
            }
            ((!appPref.checkValidString(customer.customer_email)) && (!appPref.validateEmailAddress(customer.customer_email))) ->{
                customerBase.code = appPref.ERROR_CUSTOMER_INVALID_CONTACT_NUMBER
                customerBase.message = "Please enter valid email address !"
                return customerBase
            }

            (!appPref.checkValidString(customer.customer_owners_contact_number) && appPref.validatePhoneNumber(customer.customer_owners_contact_number)) ->{
                customerBase.code = appPref.ERROR_CUSTOMER_INVALID_CONTACT_NUMBER
                customerBase.message = "Please enter valid owner's contact number !"
                return customerBase
            }

            else -> {
                customer.customer_app_id = user.user_id.toString()+appPref.getUUID()
                customer.customer_user =  user.user_id
                customer.customer_app_date =simpleDateFormat.format(Date())

                customerDao.insertNewCustomer(customer)

                if(InternetConnection.checkInternetConnection()){
                    customerBase.status = true
                    customerBase.message = SFA.applicationContext().resources.getString(R.string.no_internet)
                    return customerBase

                }else{
                    customerBase.status = true
                    customerBase.message = SFA.applicationContext().resources.getString(R.string.save_customer_locally)
                    return customerBase
                }


            }
        }

    }


    suspend fun getCustomers(): CustomerBase {

        var list = ArrayList<Customer>()


        return CustomerBase().apply {
            status = true
            message = ""
            data = list
        }

    }





    suspend fun getRoute(): RouteBase {
        var routeBase = RouteBase()
        var dataFromDB = routeDao.getRoutesList()
        return if(dataFromDB.isNullOrEmpty()){
            var result = client.getCustomerRoutes()
            if(!result.error){
                for(item in result.data){
                    routeDao.insertRoute(item)
                }
            }
            result
        }else{
            routeBase.apply {
                error = false
                data = dataFromDB as ArrayList<Route>
            }
            routeBase
        }
    }





    suspend fun getCustomerStatus(): CustomerStatusBase {

        var customerStatusBase = CustomerStatusBase()

        var dataFromDB = customerStatusDao.getCustomersStatusList()
        return if(dataFromDB.isNullOrEmpty()){
            var result = client.getCustomerStatus()
            if(!result.error){
                customerStatusDao.insertAllCustomerStatus(result.data)
            }
            result
        }else{
            customerStatusBase.apply {
                error = false
                data = dataFromDB as ArrayList<CustomerStatus>
            }
            customerStatusBase
        }

    }


    suspend fun getAllDistricts(): ArrayList<Districts> {
        return districtsDao.getDistrictsList() as ArrayList<Districts>
    }

    suspend fun getAllTowns(): ArrayList<Towns> {
        return townsDao.getTownsList() as ArrayList<Towns>
    }


}