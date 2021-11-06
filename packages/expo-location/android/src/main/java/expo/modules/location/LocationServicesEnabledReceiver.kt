package expo.modules.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import expo.modules.core.interfaces.services.EventEmitter
import expo.modules.location.LocationHelpers

class LocationServicesEnabledReceiver(private val eventEmitter: EventEmitter?) : BroadcastReceiver() {
  private val LOCATION_SERVICES_ENABLED_EVENT_NAME = "Expo.locationServicesEnabledDidChange"

  private fun onLocationServicesEnabledChange(Enabled: Boolean) {
    eventEmitter?.emit(
            LOCATION_SERVICES_ENABLED_EVENT_NAME,
            Bundle().apply {
              putBoolean("locationServicesEnabled", Enabled)
            }
    )
  }

  override fun onReceive(context: Context, intent: Intent) {
    val servicesEnabled: Boolean = LocationHelpers.isAnyProviderAvailable(context)

    onLocationServicesEnabledChange(servicesEnabled)
  }
}
