package tkhub.project.sfa.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import tkhub.project.sfa.data.model.Districts
import tkhub.project.sfa.data.model.Towns
import tkhub.project.sfa.data.model.customers.Customer
import tkhub.project.sfa.data.model.customers.CustomerStatus
import tkhub.project.sfa.data.model.route.Route
import tkhub.project.sfa.services.utilities.DATABASE_NAME
import tkhub.project.sfa.services.utilities.InitDataDatabaseWorker


@Database(entities = [Customer::class,
    CustomerStatus::class,
    Route::class,
    Districts::class,
    Towns::class
                     ], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun customerDao(): CustomerDao
    abstract fun customerStatusDao(): CustomerStatusDao
    abstract fun routeDao(): RouteDao
    abstract fun districtsDao(): DistrictsDao
    abstract fun townsDao(): TownsDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }


        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            println("ggggggggggggggggggggggggggggggggggggg  onCreate")
                            val request = OneTimeWorkRequestBuilder<InitDataDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                            super.onCreate(db)

                        }
                    }
                )
                .build()
        }

    }
}