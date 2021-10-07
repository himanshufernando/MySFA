package tkhub.project.sfa.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tkhub.project.sfa.data.model.customers.CustomerStatus

@Dao
interface CustomerStatusDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomerStatus(vararg customer: CustomerStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCustomerStatus(districts: List<CustomerStatus>)


    @Query("SELECT * FROM customerstatus WHERE customer_status_active = 1")
    fun getCustomersStatusList(): List<CustomerStatus>


}