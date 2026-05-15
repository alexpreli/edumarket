package com.edumarket.data.remote.model

import com.google.gson.annotations.SerializedName


data class OpenLibraryResponse(
    @SerializedName("work_count") val workCount: Int = 0,
    @SerializedName("works")      val works: List<BookWork> = emptyList()
)

data class BookWork(
    @SerializedName("title")   val title: String = "Unknown Title",
    @SerializedName("authors") val authors: List<BookAuthor> = emptyList()
)

data class BookAuthor(
    @SerializedName("name") val name: String = "Unknown Author"
)
