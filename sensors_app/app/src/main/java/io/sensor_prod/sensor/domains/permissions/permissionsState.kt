@file:OptIn(ExperimentalPermissionsApi::class)

package io.sensor_prod.sensor.domains.permissions

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

interface PermissionsState {
    val isGranted: Boolean
    fun requestManually(): Unit

}

class MutablePermissionState(
    private val permissionsRequest: PermissionsRequest, private val multiplePermissionsState: MultiplePermissionsState?

): PermissionsState {
    override val isGranted: Boolean by derivedStateOf {
        multiplePermissionsState?.allPermissionsGranted ?: false
    }


//        multiplePermissionsState?.allPermissionsGranted ?: false




    override fun requestManually() {
        if(multiplePermissionsState == null){
            Log.d("MutablePermissionState: ", "requestManually 2")

        }
        multiplePermissionsState?.launchMultiplePermissionRequest()
    }

}
