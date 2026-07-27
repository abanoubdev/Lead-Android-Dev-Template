package net.compose.leadandroiddevprep.composables

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
//
//@Composable
//fun CartReaderScreen(viewmodel: CardReaderViewModel = hiltViewModel()) {
//
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    val targetDeviceState by viewmodel.targetDevice.collectAsStateWithLifecycle()
//
//    val scope = rememberCoroutineScope()
//
//    DisposableEffect(targetDeviceState) {
//
//        val listener = object : HardwareListener {
//            override fun onDeviceConnected() {
//                scope.launch {
//                    snackbarHostState.showSnackbar(
//                        "Card Reader Connected!",
//                        duration = SnackbarDuration.Long,
//                        withDismissAction = true
//                    )
//                }
//            }
//
//            override fun onDeviceDisconnected() {
//                scope.launch {
//                    snackbarHostState.showSnackbar(
//                        "Card Reader Disconnected!",
//                        duration = SnackbarDuration.Long,
//                        withDismissAction = true
//                    )
//                }
//            }
//        }
//
//        if (targetDeviceState != null) {
//            HardwareSDK.registerListener(targetDeviceState, listener)
//        }
//
//        onDispose {
//            if (targetDeviceState != null) {
//                HardwareSDK.unregisterListener(targetDeviceState, listener)
//            }
//        }
//    }
//
//    SnackbarHost(snackbarHostState, modifier = Modifier)
//}
//
//@HiltViewModel
//class CardReaderViewModel @Inject constructor() : ViewModel() {
//
//    val _targetDevice = MutableStateFlow<BluetoothDevice?>(null)
//    val targetDevice = _targetDevice.asStateFlow()
//
//    init {
//        val device = BluetoothAdapter.getDefaultAdapter().bondedDevices.firstOrNull()
//
//        if (device != null) {
//            _targetDevice.value = device
//        }
//    }
//}
//
//interface HardwareListener {
//    fun onDeviceConnected()
//    fun onDeviceDisconnected()
//}