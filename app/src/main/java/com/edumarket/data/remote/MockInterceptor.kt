package com.edumarket.data.remote

import com.edumarket.EduMarketApp
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.toString()

        return if (url.contains("/courses")) {
            val json = EduMarketApp.appContext.assets
                .open("courses.json")
                .bufferedReader()
                .use { it.readText() }

            Response.Builder()
                .code(200)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .message("OK")
                .body(json.toResponseBody("application/json".toMediaType()))
                .build()
        } else {
            
            chain.proceed(chain.request())
        }
    }
}
