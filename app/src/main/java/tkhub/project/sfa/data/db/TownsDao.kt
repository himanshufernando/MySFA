package tkhub.project.sfa.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tkhub.project.sfa.data.model.Towns
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.data.model.customers.CustomerStatus
import tkhub.project.sfa.data.model.route.Route

@Dao
interface TownsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(vararg towns: Towns)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRoute(towns: List<Towns>)

    @Query("SELECT * FROM towns WHERE town_active = 1")
    fun getTownsList(): List<Towns>



}