package com.utn.nearly.utils

import android.location.Location
import android.widget.Toast
import java.util.regex.Matcher
import java.util.regex.Pattern

//abstract class Utils {
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
//}

fun isValidPassword(password: String): Boolean {
    val pattern: Pattern
    val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$&+=])(?=\\S+$).{4,}$"
    pattern = Pattern.compile(PASSWORD_PATTERN)
    val matcher: Matcher = pattern.matcher(password)
    return matcher.matches()
}

fun calculateDistance(lat1:Double, lon1: Double, lat2:Double, lon2:Double) : Double{
    val loc1 = Location("LOCACION1")
    loc1.latitude = lat1
    loc1.longitude = lon1

    val loc2 = Location("LOCACION1")
    loc2.latitude = lat2
    loc2.longitude = lon2

    val distancia = loc1.distanceTo(loc2)
    return distancia.toDouble()
}