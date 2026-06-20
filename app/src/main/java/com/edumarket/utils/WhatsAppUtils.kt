package com.edumarket.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.edumarket.ui.theme.AppStrings

object WhatsAppUtils {

    fun openWhatsApp(context: Context, phoneNumber: String, message: String, lang: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")
            
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, AppStrings.whatsappNotInstalled(lang), Toast.LENGTH_SHORT).show()
                Log.w("WhatsAppUtils", "WhatsApp not installed")
            }
        } catch (e: Exception) {
            Toast.makeText(context, AppStrings.whatsappError(lang), Toast.LENGTH_SHORT).show()
            Log.e("WhatsAppUtils", "Error opening WhatsApp", e)
        }
    }
}
