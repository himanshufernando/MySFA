package tkhub.project.sfa.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tkhub.project.sfa.data.model.customers.Customer


@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCustomer(vararg customer: Customer)

    @Query("SELECT * FROM customertb")
    fun getCustomersList(): List<Customer>


}