package tkhub.project.sfa.services.network.api

import com.google.gson.JsonObject
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import tkhub.project.sfa.BuildConfig.API_BASE_URL
import tkhub.project.sfa.data.model.BaseApiModal
import tkhub.project.sfa.data.model.customers.CustomerStatusBase
import tkhub.project.sfa.data.model.route.RouteBase
import tkhub.project.sfa.data.model.user.UserBase
import java.util.concurrent.TimeUnit

interface APIInterface {


    @GET("users/login")
    suspend fun userLogin(
        @Query("user_mobile") user_mobile: String,
        @Query("user_password") user_password: String
    ): UserBase


    @PUT("users/pushtokenupdate/{id}")
    suspend fun pushtokenupdate(@Path("id") id: Long, @Body userInfo: JsonObject): BaseApiModal


    @GET("customer/status")
    suspend fun getCustomerStatus(): CustomerStatusBase


    @GET("customer/route")
    suspend fun getCustomerRoutes(): RouteBase










    companion object {
        fun create(): APIInterface = create(API_BASE_URL.toHttpUrlOrNull()!!)
        fun create(httpUrl: HttpUrl): APIInterface {
            val client = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIInterface::class.java)

            // CODE FOR CASH  retrieved DATA USING RETROFIT CASH
            /* var cacheSize = 10 * 1024 * 1024
             var httpCacheDirectory = File(app.cacheDir, "responses")
             var cache = Cache(httpCacheDirectory, cacheSize.toLong())

             var client = OkHttpClient.Builder()
                 .connectTimeout(100, TimeUnit.SECONDS)
                 .readTimeout(100, TimeUnit.SECONDS)
                 .cache(cache).addNetworkInterceptor { chain->
                     val response = chain.proceed(chain.request())
                     val maxAge = 0 // read from cache for 60 seconds even if there is internet connection
                     response.newBuilder()
                         .header("Cache-Control", "public, max-age=$maxAge")
                         .removeHeader("Pragma")
                         .build() }.addInterceptor { chain->
                     var request = chain.request()
                     if (!InternetConnection.checkInternetConnection()) {
                         val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
                         request = request.newBuilder()
                             .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                             .removeHeader("Pragma")
                             .build()
                     }
                     chain.proceed(request) }.build()*/
        }


    }

}