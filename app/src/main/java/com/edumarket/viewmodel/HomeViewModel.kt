package com.edumarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edumarket.data.local.AppDatabase
import com.edumarket.data.local.entity.CartItemEntity
import com.edumarket.data.local.entity.CourseEntity
import com.edumarket.data.preferences.UserPreferences
import com.edumarket.data.remote.RetrofitClient
import com.edumarket.data.remote.model.CourseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



data class CourseUiModel(
    val id: Int,
    val name: String,
    val description: String,
    val subjects: List<String>,
    val language: String,        
    val date: String,
    val backgroundSrc: String,
    val duration: String,
    val trainingCentre: String,
    val teacherName: String,
    val isSelected: Boolean = false, 
    val isFree: Boolean = false      
)

data class BookUiModel(
    val title: String,
    val author: String,
    val bookUrl: String = "",
    val coverUrl: String? = null
)

data class HomeUiState(
    val courses: List<CourseUiModel> = emptyList(),
    val books: List<BookUiModel> = emptyList(),
    val allSubjects: List<String> = emptyList(),
    val selectedSubject: String = "ALL",
    val boughtCount: Int = 0,         
    val showFreeDialog: Boolean = false,
    val showOrderDialog: Boolean = false,
    val isLoading: Boolean = true,
    val booksLoading: Boolean = true,
    val error: String? = null,
    val language: String = "en"
)



class HomeViewModel : ViewModel() {

    private val db             = AppDatabase.getInstance()
    private val courseDao      = db.courseDao()
    private val cartDao        = db.cartDao()
    private val userPrefs      = UserPreferences()
    private val api            = RetrofitClient.apiService

    companion object {
        const val OPEN_LIBRARY_URL =
            "https://openlibrary.org/subjects/programming.json?limit=10"
        const val COURSES_UNTIL_GIFT = 3
    }

    
    private val _boughtCount       = MutableStateFlow(0)
    private val _showFreeDialog    = MutableStateFlow(false)
    private val _showOrderDialog   = MutableStateFlow(false)
    private val _selectedSubject   = MutableStateFlow("ALL")
    private val _books             = MutableStateFlow<List<BookUiModel>>(emptyList())
    private val _booksLoading      = MutableStateFlow(true)
    private val _coursesLoading    = MutableStateFlow(true)
    private val _error             = MutableStateFlow<String?>(null)

    
    val uiState: StateFlow<HomeUiState> = combine(
        courseDao.getAllCourses(),
        cartDao.getAllCartItems(),
        userPrefs.language,
        _selectedSubject,
        _books,
        _boughtCount,
        _showFreeDialog,
        _showOrderDialog,
        _coursesLoading,
        _booksLoading,
        _error
    ) { args ->
        @Suppress("UNCHECKED_CAST")
        val courses        = args[0] as List<CourseEntity>
        @Suppress("UNCHECKED_CAST")
        val cartItems      = args[1] as List<CartItemEntity>
        val lang           = args[2] as String
        val subject        = args[3] as String
        @Suppress("UNCHECKED_CAST")
        val books          = args[4] as List<BookUiModel>
        val bought         = args[5] as Int
        val freeDialog     = args[6] as Boolean
        val orderDialog    = args[7] as Boolean
        val cLoading       = args[8] as Boolean
        val bLoading       = args[9] as Boolean
        val err            = args[10] as String?

        val cartIds      = cartItems.map { it.courseId }.toSet()
        val freeIds      = cartItems.filter { it.isFree }.map { it.courseId }.toSet()

        val uiCourses = courses.map { entity ->
            entity.toUiModel(lang, entity.id in cartIds, entity.id in freeIds)
        }

        val subjects = listOf("ALL") + uiCourses
            .flatMap { it.subjects }
            .distinct()
            .sorted()

        val filtered = if (subject == "ALL") uiCourses
        else uiCourses.filter { subject in it.subjects }

        HomeUiState(
            courses         = filtered,
            books           = books,
            allSubjects     = subjects,
            selectedSubject = subject,
            boughtCount     = bought,
            showFreeDialog  = freeDialog,
            showOrderDialog = orderDialog,
            isLoading       = cLoading,
            booksLoading    = bLoading,
            error           = err,
            language        = lang
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        loadCourses()
        loadBooks()
    }

    

    private fun loadCourses() {
        viewModelScope.launch {
            _coursesLoading.value = true
            runCatching {
                val dtos = api.getCourses()
                val entities = dtos.mapNotNull { dto ->
                    val id = dto.courseId.toIntOrNull() ?: return@mapNotNull null
                    dto.toEntity(id)
                }
                courseDao.deleteAll()
                courseDao.insertAll(entities)
            }.onFailure { e ->
                _error.value = "Could not load courses: ${e.message}"
            }
            _coursesLoading.value = false
        }
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _booksLoading.value = true
            runCatching {
                val response = api.getProgrammingBooks(OPEN_LIBRARY_URL)
                _books.value = response.works.take(10).map { work ->
                    val bookUrl = if (work.key != null) "https://openlibrary.org${work.key}" else ""
                    val coverUrl = if (work.coverId != null) "https://covers.openlibrary.org/b/id/${work.coverId}-M.jpg" else null
                    BookUiModel(
                        title    = work.title,
                        author   = work.authors.firstOrNull()?.name ?: "Unknown",
                        bookUrl  = bookUrl,
                        coverUrl = coverUrl
                    )
                }
            }.onFailure {
                
                _books.value = emptyList()
            }
            _booksLoading.value = false
        }
    }

    

    fun selectSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun addToCart(course: CourseUiModel) {
        if (course.isSelected || course.isFree) return
        viewModelScope.launch {
            cartDao.insert(
                CartItemEntity(
                    courseId     = course.id,
                    courseName   = course.name,
                    courseNumber = course.backgroundSrc, 
                    isFree       = false
                )
            )
            _boughtCount.value = _boughtCount.value + 1
            if (_boughtCount.value % COURSES_UNTIL_GIFT == 0) {
                _showFreeDialog.value = true
            }
        }
    }

    fun removeFromCart(courseId: Int, isFree: Boolean) {
        viewModelScope.launch {
            cartDao.deleteById(courseId)
            if (!isFree) {
                _boughtCount.value = maxOf(0, _boughtCount.value - 1)
            }
        }
    }

    fun chooseFreeCourse(course: CourseUiModel) {
        viewModelScope.launch {
            cartDao.insert(
                CartItemEntity(
                    courseId     = course.id,
                    courseName   = course.name,
                    courseNumber = course.backgroundSrc,
                    isFree       = true
                )
            )
            _showFreeDialog.value = false
        }
    }

    fun declineFreeCourse() {
        _showFreeDialog.value = false
    }

    fun showOrderDialog() {
        _showOrderDialog.value = true
    }

    fun dismissOrderDialog() {
        _showOrderDialog.value = false
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
            _boughtCount.value = 0
            _showOrderDialog.value = false
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch { userPrefs.setLanguage(lang) }
    }
}



private fun CourseEntity.toUiModel(
    lang: String,
    isSelected: Boolean,
    isFree: Boolean
): CourseUiModel = CourseUiModel(
    id            = id,
    name          = if (lang == "en") nameEn else nameRo,
    description   = if (lang == "en") descriptionEn else descriptionRo,
    subjects      = if (lang == "en") subjectsEnList() else subjectsRoList(),
    language      = if (lang == "en") languageEn else languageRo,
    date          = date,
    backgroundSrc = backgroundSrc,
    duration      = if (lang == "en") durationEn else durationRo,
    trainingCentre= if (lang == "en") trainingCentreEn else trainingCentreRo,
    teacherName   = teacherName,
    isSelected    = isSelected,
    isFree        = isFree
)

private fun CourseEntity.subjectsEnList(): List<String> =
    subjectsEn.split(",").map { it.trim() }.filter { it.isNotEmpty() }

private fun CourseEntity.subjectsRoList(): List<String> =
    subjectsRo.split(",").map { it.trim() }.filter { it.isNotEmpty() }

private fun CourseDto.toEntity(id: Int): CourseEntity = CourseEntity(
    id            = id,
    number        = number,
    date          = date,
    nameEn        = name.en,
    nameRo        = name.ro,
    descriptionEn = description.en,
    descriptionRo = description.ro,
    subjectsEn    = subjects.en.joinToString(", "),
    subjectsRo    = subjects.ro.joinToString(", "),
    languageEn    = language.en,
    languageRo    = language.ro,
    backgroundSrc = backgroundSrc,
    durationEn    = duration.en,
    durationRo    = duration.ro,
    trainingCentreEn = trainingCentre.en,
    trainingCentreRo = trainingCentre.ro,
    teacherName   = teacherName
)
