package com.edumarket.data.remote.model

import com.google.gson.annotations.SerializedName



data class MultiLangText(
    @SerializedName("ro") val ro: String = "",
    @SerializedName("en") val en: String = ""
)

data class MultiLangList(
    @SerializedName("ro") val ro: List<String> = emptyList(),
    @SerializedName("en") val en: List<String> = emptyList()
)




data class CourseDto(
    @SerializedName("course_id")    val courseId: String       = "",
    @SerializedName("number")       val number: String         = "",
    @SerializedName("date")         val date: String           = "",
    @SerializedName("subject_id")   val subjectId: String      = "",
    @SerializedName("language")     val language: MultiLangText = MultiLangText(),
    @SerializedName("name")         val name: MultiLangText     = MultiLangText(),
    @SerializedName("description")  val description: MultiLangText = MultiLangText(),
    @SerializedName("subjects")     val subjects: MultiLangList = MultiLangList(),
    @SerializedName("background_src") val backgroundSrc: String = "",
    @SerializedName("duration")     val duration: MultiLangText = MultiLangText(),
    @SerializedName("training_centre") val trainingCentre: MultiLangText = MultiLangText(),
    @SerializedName("teacher_name") val teacherName: String = ""
)
