package com.edumarket.data.remote

import com.edumarket.data.remote.model.CourseDto
import com.edumarket.data.remote.model.OpenLibraryResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    
    @GET("courses")
    suspend fun getCourses(): List<CourseDto>

    
    @GET
    suspend fun getProgrammingBooks(@Url url: String): OpenLibraryResponse
}
