package tkhub.project.sfa.services.utilities

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope
import tkhub.project.sfa.data.db.AppDatabase
import tkhub.project.sfa.data.model.Districts
import tkhub.project.sfa.data.model.Towns

class InitDataDatabaseWorker (context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val database = AppDatabase.getInstance(applicationContext)
    override suspend fun doWork(): Result = coroutineScope {
        try {
            println("ggggggggggggggggggggggggggggggggggggg  InitDataDatabaseWorker")

            applicationContext.assets.open(TOWNS_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val towns = object : TypeToken<List<Towns>>() {}.type
                    val townsList: List<Towns> = Gson().fromJson(jsonReader, towns)
                    database.townsDao().insertAllRoute(townsList)
                }
            }
            applicationContext.assets.open(DISTRICTS_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->

                    val districts = object : TypeToken<List<Districts>>() {}.type
                    val districtsList: List<Districts> = Gson().fromJson(jsonReader, districts)
                    database.districtsDao().insertAllDistricts(districtsList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            println("ggggggggggggggggggggggggggggggggggggg  Error seeding database "+ex)
            Log.e(TAG, " Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private val TAG = InitDataDatabaseWorker::class.java.simpleName
    }
}