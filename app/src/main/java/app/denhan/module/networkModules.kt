package app.denhan.module

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.preference.PreferenceManager
import app.denhan.data.repos.AuthRepository
import app.denhan.data.api.WebService
import app.denhan.util.AppConstants
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import skycap.android.core.livedata.SingleEventLiveData
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


val networkModule = module {
    single<Gson> {
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setPrettyPrinting()
            .setLenient()
            .create()
    }

    single {
        ResourceProvider(get())
    }
    single {
        AuthRepository(get(), get())
    }
    single<SharedPreferences> {
        PreferenceManager.getDefaultSharedPreferences(get())
    }
    single {
        skycap.android.core.sharedprefs.SharedPrefsHelper(get())
    }

    single {
        SingleEventLiveData
    }

    single {
        okHttpClient(get())
    }

    single {
      AuthInterceptor(get())
    }

    single {
        createWebService<WebService>(get())
    }

}


fun okHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .addInterceptor(getHttpLoggingInterceptor())
        .hostnameVerifier(HostName())
        .addInterceptor(authInterceptor)
        .build()
}



class HostName : HostnameVerifier {
    @SuppressLint("BadHostnameVerifier")
    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return true
    }

}


inline fun <reified T> createWebService(okHttpClient: OkHttpClient): T {

    val retrofit = Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()
    return retrofit.create(T::class.java)
}


private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}