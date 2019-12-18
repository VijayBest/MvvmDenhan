package app.denhan.module

import app.denhan.util.AppConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import skycap.android.core.sharedprefs.SharedPrefsHelper


class AuthInterceptor (val sharedPrefsHelper: SharedPrefsHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header("accept", "application/json")
        builder.header("token",AppConstants.sessionToken)
         return chain.proceed(builder.build())

       /* var mainResponse = chain.proceed(buildRequest(chain))
        return mainResponse*/
    }


    private fun buildRequest(chain: Interceptor.Chain): Request {

        val original = chain.request()
        original.newBuilder()
            .method(original.method(), original.body())
            .header("Accept","application/json")
            .header("dtoken", "dtoken")
            .build()

        return original
    }


}