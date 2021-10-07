package tkhub.project.sfa.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.data.model.customers.CustomerStatus
import tkhub.project.sfa.data.model.route.Route

@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(vararg route: Route)

    @Query("SELECT * FROM customerroute WHERE customer_route_active = 1")
    fun getRoutesList(): List<Route>



}