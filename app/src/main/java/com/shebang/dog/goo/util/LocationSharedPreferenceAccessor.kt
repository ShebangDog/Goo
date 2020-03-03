package com.shebang.dog.goo.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.shebang.dog.goo.data.model.Latitude
import com.shebang.dog.goo.data.model.Location
import com.shebang.dog.goo.data.model.Longitude

object LocationSharedPreferenceAccessor {
    const val KEY_LOCATION_RESULT = "location result"

    fun setLocationResult(context: Context, location: Location) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(KEY_LOCATION_RESULT, location.toString())
            .apply()
    }

    fun getLocationResult(context: Context): Location? {
        fun parseLocation(value: String?): Location? {
            val string = value?.takeIf { it.isNotBlank() }
            return when (string != null) {
                false -> null
                true -> {
                    val delimiter = Location.delimiter.single()
                    val latitude = Latitude((string.takeWhile { it != delimiter }).toDouble())
                    val longitude = Longitude((string.takeLastWhile { it != delimiter }).toDouble())
                    Location(latitude, longitude)
                }
            }
        }

        val result = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_LOCATION_RESULT, "")

        return parseLocation(result)
    }

    fun registerOnSharedPreferenceChangeListener(
        context: Context,
        onSharedPreferenceChange: (key: String) -> Unit
    ) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .registerOnSharedPreferenceChangeListener { _, string: String ->
                onSharedPreferenceChange.invoke(string)
            }
    }
}