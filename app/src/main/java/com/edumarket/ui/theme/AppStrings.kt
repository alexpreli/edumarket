package com.edumarket.ui.theme

import androidx.compose.runtime.compositionLocalOf

val LocalAppLanguage = compositionLocalOf { "en" }

object AppStrings {
    
    fun appName(lang: String) = if (lang == "ro") "EduMarket" else "EduMarket"
    fun appSubtitle(lang: String) = if (lang == "ro") "Platforma Ta de Învățare Digitală" else "Digital Learning Marketplace"
    
    
    fun navHome(lang: String) = if (lang == "ro") "Acasă" else "Home"
    fun navCart(lang: String) = if (lang == "ro") "Coș" else "Cart"
    fun navProfile(lang: String) = if (lang == "ro") "Profil" else "Profile"

    
    fun homeTitle(lang: String) = if (lang == "ro") "Cursurile Noastre" else "Our Courses"
    fun filterAll(lang: String) = if (lang == "ro") "TOATE" else "ALL"
    fun freeCourseDialogTitle(lang: String) = if (lang == "ro") "Curs Gratuit!" else "Free Course!"
    fun freeCourseDialogText(lang: String) = if (lang == "ro") "Ai cumpărat destule cursuri! Alege unul gratuit:" else "You've bought enough courses! Pick one for free:"
    fun cancel(lang: String) = if (lang == "ro") "Anulează" else "Cancel"
    fun confirm(lang: String) = if (lang == "ro") "Confirmă" else "Confirm"
    fun noFreeCoursesAvailable(lang: String) = if (lang == "ro") "Nu există cursuri gratuite disponibile momentan." else "No free courses available right now."
    fun addToCart(lang: String) = if (lang == "ro") "Adaugă în coș" else "Add to cart"
    fun inCart(lang: String) = if (lang == "ro") "În coș" else "In Cart"
    fun freeCourseBtn(lang: String) = if (lang == "ro") "Gratuit" else "Free"
    fun courseDuration(lang: String, duration: String) = if (lang == "ro") "Durată: $duration" else "Duration: $duration"
    fun courseTrainingCentre(lang: String, centre: String) = if (lang == "ro") "Centru: $centre" else "Centre: $centre"
    fun courseTeacher(lang: String, teacher: String) = if (lang == "ro") "Profesor: $teacher" else "Teacher: $teacher"
    fun loadingCourses(lang: String) = if (lang == "ro") "Se încarcă cursurile..." else "Loading courses..."

    
    fun booksTitle(lang: String) = if (lang == "ro") "Cărți Recomandate (OpenLibrary)" else "Recommended Books (OpenLibrary)"
    fun authorPrefix(lang: String) = if (lang == "ro") "De: " else "By: "
    fun loadingBooks(lang: String) = if (lang == "ro") "Se încarcă cărțile..." else "Loading books..."
    fun booksError(lang: String) = if (lang == "ro") "Cărțile nu au putut fi încărcate." else "Could not load books."

    
    fun cartTitle(lang: String) = if (lang == "ro") "Coșul Meu" else "My Cart"
    fun emptyCart(lang: String) = if (lang == "ro") "Coșul tău este gol." else "Your cart is empty."
    fun removeCourse(lang: String) = if (lang == "ro") "Elimină" else "Remove"
    fun checkout(lang: String) = if (lang == "ro") "Finalizează comanda" else "Checkout"
    fun cartOrderTitle(lang: String) = if (lang == "ro") "Plasează Comanda" else "Place Order"
    fun cartOrderContent(lang: String) = if (lang == "ro") "Ești sigur că vrei să finalizezi comanda pentru aceste cursuri?" else "Are you sure you want to checkout these courses?"
    fun cartOrderSuccess(lang: String) = if (lang == "ro") "Comanda a fost plasată cu succes!" else "Order placed successfully!"

    
    fun profileTitle(lang: String) = if (lang == "ro") "Profil Utilizator" else "User Profile"
    fun selectLanguage(lang: String) = if (lang == "ro") "Selectează Limba:" else "Select Language:"
    fun english(lang: String) = if (lang == "ro") "Engleză" else "English"
    fun romanian(lang: String) = if (lang == "ro") "Română" else "Romanian"
    fun logout(lang: String) = if (lang == "ro") "Deconectare" else "Logout"
    fun signedIn(lang: String) = if (lang == "ro") "Autentificat" else "Signed in"
    fun savedInDataStore(lang: String) = if (lang == "ro") "Salvat în DataStore – persistă la repornirea aplicației" else "Saved in DataStore – persists across app restarts"
    fun contactInfo(lang: String) = if (lang == "ro") "Informații de Contact" else "Contact Information"
    fun contactName(lang: String) = if (lang == "ro") "Nume" else "Name"
    fun contactPhone(lang: String) = if (lang == "ro") "Telefon" else "Phone"
    fun appInfoTitle(lang: String) = if (lang == "ro") "Informații Aplicație" else "App Info"
    fun appNameLabel(lang: String) = if (lang == "ro") "Nume Aplicație" else "App Name"
    fun appVersion(lang: String) = if (lang == "ro") "Versiune" else "Version"
    fun appPlatform(lang: String) = if (lang == "ro") "Platformă" else "Platform"

    
    fun loginTitle(lang: String) = if (lang == "ro") "Autentificare" else "Login"
    fun email(lang: String) = if (lang == "ro") "Email" else "Email"
    fun password(lang: String) = if (lang == "ro") "Parolă" else "Password"
    fun loginBtn(lang: String) = if (lang == "ro") "Intră în cont" else "Login"
    fun dontHaveAccount(lang: String) = if (lang == "ro") "Nu ai cont? Înregistrează-te" else "Don't have an account? Register"

    
    fun registerTitle(lang: String) = if (lang == "ro") "Înregistrare" else "Register"
    fun createAccount(lang: String) = if (lang == "ro") "Creează-ți contul" else "Create your account"
    fun fullName(lang: String) = if (lang == "ro") "Nume Complet" else "Full Name"
    fun registerBtn(lang: String) = if (lang == "ro") "Creează cont" else "Register"
    fun backToLogin(lang: String) = if (lang == "ro") "Înapoi la Autentificare" else "Back to Login"

    
    fun orderMessagePart1(lang: String) = if (lang == "ro") "Buna ziua. Doresc sa achizitionez urmatoarele cursuri:" else "Hello. I want to buy the following courses:"
    fun orderMessagePart2(lang: String) = if (lang == "ro") "Astept un raspuns." else "Waiting for a reply."
    fun supportMessage(lang: String) = if (lang == "ro") "Buna ziua! Am nevoie de ajutor!" else "Hello! I need help!"
    fun whatsappNotInstalled(lang: String) = if (lang == "ro") "WhatsApp nu este instalat" else "WhatsApp not installed"
    fun whatsappError(lang: String) = if (lang == "ro") "Eroare la deschiderea WhatsApp" else "Error opening WhatsApp"
}
