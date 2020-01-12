package com.shebang.dog.goo.service

import android.app.IntentService
import android.content.Intent
import android.location.Location
import com.google.android.gms.location.LocationResult
import com.shebang.dog.goo.data.model.Latitude
import com.shebang.dog.goo.data.model.Longitude
import com.shebang.dog.goo.util.DebugHelper
import com.shebang.dog.goo.util.LocationSharedPreferenceAccessor

class LocationService : IntentService(NAME) {
    override fun onHandleIntent(intent: Intent?) {
        DebugHelper.log("onHandleIntent")
        if (intent?.action == ACTION_PROCESS_UPDATES) {
            val locationResult = LocationResult.extractResult(intent)
            val location = convertAndroidLocation(locationResult.lastLocation)

            DebugHelper.log("onHandleIntent $location")
            LocationSharedPreferenceAccessor.setLocationResult(this, location)
        }
    }

    private fun convertAndroidLocation(location: Location): com.shebang.dog.goo.data.model.Location {
        return com.shebang.dog.goo.data.model.Location(
            Latitude(location.latitude),
            Longitude(location.longitude)
        )
    }

    companion object {
        const val NAME = "LocationService"

        const val ACTION_PROCESS_UPDATES =
            "com.shebang.dog.goo.service.LocationService" + "PROCESS_UPDATES"
    }
}