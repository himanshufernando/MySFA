package tkhub.project.sfa.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tkhub.project.sfa.data.model.Districts
import tkhub.project.sfa.data.model.Towns
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.data.model.customers.CustomerStatus
import tkhub.project.sfa.data.model.route.Route

@Dao
interface DistrictsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(vararg districts: Districts)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDistricts(districts: List<Districts>)

    @Query("SELECT * FROM districts WHERE district_active = 1")
    fun getDistrictsList(): List<Districts>



}