package com.shebang.dog.goo.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.shebang.dog.goo.data.model.location.Latitude
import com.shebang.dog.goo.data.model.location.Location
import com.shebang.dog.goo.data.model.location.Longitude

object LocationSharedPreferenceAccessor {
    private const val KEY_LOCATION_RESULT = "location result"

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
                    val latitude =
                        Latitude((string.takeWhile { it != delimiter }).toDouble())
                    val longitude =
                        Longitude((string.takeLastWhile { it != delimiter }).toDouble())
                    Location(
                        latitude,
                        longitude
                    )
                }
            }
        }

        val result = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_LOCATION_RESULT, "")

        return parseLocation(result)
    }
}