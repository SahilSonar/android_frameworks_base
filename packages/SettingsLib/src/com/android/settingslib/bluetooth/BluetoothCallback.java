/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settingslib.bluetooth;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_CONNECTING;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTING;
import static android.bluetooth.BluetoothAdapter.STATE_OFF;
import static android.bluetooth.BluetoothAdapter.STATE_ON;
import static android.bluetooth.BluetoothAdapter.STATE_TURNING_OFF;
import static android.bluetooth.BluetoothAdapter.STATE_TURNING_ON;

import android.annotation.IntDef;
import android.annotation.Nullable;

import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.bluetooth.BluetoothCodecStatus;

import java.util.UUID;

/**
 * BluetoothCallback provides a callback interface for the settings
 * UI to receive events from {@link BluetoothEventManager}.
 */
public interface BluetoothCallback {
    /**
     * It will be called when the state of the local Bluetooth adapter has been changed.
     * It is listening {@link android.bluetooth.BluetoothAdapter#ACTION_STATE_CHANGED}.
     * For example, Bluetooth has been turned on or off.
     *
     * @param bluetoothState the current Bluetooth state, the possible values are:
     * {@link android.bluetooth.BluetoothAdapter#STATE_OFF},
     * {@link android.bluetooth.BluetoothAdapter#STATE_TURNING_ON},
     * {@link android.bluetooth.BluetoothAdapter#STATE_ON},
     * {@link android.bluetooth.BluetoothAdapter#STATE_TURNING_OFF}.
     */
    default void onBluetoothStateChanged(@AdapterState int bluetoothState) {}

    /**
     * It will be called when the local Bluetooth adapter has started
     * or finished the remote device discovery process.
     * It is listening {@link android.bluetooth.BluetoothAdapter#ACTION_DISCOVERY_STARTED} and
     * {@link android.bluetooth.BluetoothAdapter#ACTION_DISCOVERY_FINISHED}.
     *
     * @param started indicate the current process is started or finished.
     */
    default void onScanningStateChanged(boolean started) {}

    /**
     * It will be called in following situations:
     * 1. In scanning mode, when a new device has been found.
     * 2. When a profile service is connected and existing connected devices has been found.
     * This API only invoked once for each device and all devices will be cached in
     * {@link CachedBluetoothDeviceManager}.
     *
     * @param cachedDevice the Bluetooth device.
     */
    default void onDeviceAdded(@NonNull CachedBluetoothDevice cachedDevice) {}

    /**
     * It will be called when requiring to remove a remote device from CachedBluetoothDevice list
     *
     * @param cachedDevice the Bluetooth device.
     */
    default void onDeviceDeleted(@NonNull CachedBluetoothDevice cachedDevice) {}

    /**
     * It will be called when bond state of a remote device is changed.
     * It is listening {@link android.bluetooth.BluetoothDevice#ACTION_BOND_STATE_CHANGED}
     *
     * @param cachedDevice the Bluetooth device.
     * @param bondState the Bluetooth device bond state, the possible values are:
     * {@link android.bluetooth.BluetoothDevice#BOND_NONE},
     * {@link android.bluetooth.BluetoothDevice#BOND_BONDING},
     * {@link android.bluetooth.BluetoothDevice#BOND_BONDED}.
     */
    default void onDeviceBondStateChanged(
            @NonNull CachedBluetoothDevice cachedDevice, int bondState) {}

    /**
     * It will be called in following situations:
     * 1. When the adapter is not connected to any profiles of any remote devices
     * and it attempts a connection to a profile.
     * 2. When the adapter disconnects from the last profile of the last device.
     * It is listening {@link android.bluetooth.BluetoothAdapter#ACTION_CONNECTION_STATE_CHANGED}
     *
     * @param cachedDevice the Bluetooth device.
     * @param state the Bluetooth device connection state, the possible values are:
     * {@link android.bluetooth.BluetoothAdapter#STATE_DISCONNECTED},
     * {@link android.bluetooth.BluetoothAdapter#STATE_CONNECTING},
     * {@link android.bluetooth.BluetoothAdapter#STATE_CONNECTED},
     * {@link android.bluetooth.BluetoothAdapter#STATE_DISCONNECTING}.
     */
    default void onConnectionStateChanged(
            @Nullable CachedBluetoothDevice cachedDevice,
            @ConnectionState int state) {}

    /**
     * It will be called when device been set as active for {@code bluetoothProfile}
     * It is listening in following intent:
     * {@link android.bluetooth.BluetoothA2dp#ACTION_ACTIVE_DEVICE_CHANGED}
     * {@link android.bluetooth.BluetoothHeadset#ACTION_ACTIVE_DEVICE_CHANGED}
     * {@link android.bluetooth.BluetoothHearingAid#ACTION_ACTIVE_DEVICE_CHANGED}
     *
     * @param activeDevice the active Bluetooth device.
     * @param bluetoothProfile the profile of active Bluetooth device.
     */
    default void onActiveDeviceChanged(
            @Nullable CachedBluetoothDevice activeDevice, int bluetoothProfile) {}

    /**
     * It will be called in following situations:
     * 1. When the call state on the device is changed.
     * 2. When the audio connection state of the A2DP profile is changed.
     * It is listening in following intent:
     * {@link android.bluetooth.BluetoothHeadset#ACTION_AUDIO_STATE_CHANGED}
     * {@link android.telephony.TelephonyManager#ACTION_PHONE_STATE_CHANGED}
     */
    default void onAudioModeChanged() {}

    /**
     * It will be called when one of the bluetooth device profile connection state is changed.
     *
     * @param cachedDevice the active Bluetooth device.
     * @param state the BluetoothProfile connection state, the possible values are:
     * {@link android.bluetooth.BluetoothProfile#STATE_CONNECTED},
     * {@link android.bluetooth.BluetoothProfile#STATE_CONNECTING},
     * {@link android.bluetooth.BluetoothProfile#STATE_DISCONNECTED},
     * {@link android.bluetooth.BluetoothProfile#STATE_DISCONNECTING}.
     * @param bluetoothProfile the BluetoothProfile id.
     */
    default void onProfileConnectionStateChanged(
            @NonNull CachedBluetoothDevice cachedDevice,
            @ConnectionState int state,
            int bluetoothProfile) {
    }

    /**
     * Called when ACL connection state is changed. It listens to
     * {@link android.bluetooth.BluetoothDevice#ACTION_ACL_CONNECTED} and {@link
     * android.bluetooth.BluetoothDevice#ACTION_ACL_DISCONNECTED}
     *
     * @param cachedDevice Bluetooth device that changed
     * @param state        the Bluetooth device connection state, the possible values are:
     *                     {@link android.bluetooth.BluetoothAdapter#STATE_DISCONNECTED},
     *                     {@link android.bluetooth.BluetoothAdapter#STATE_CONNECTED}
     */
    default void onAclConnectionStateChanged(
            @NonNull CachedBluetoothDevice cachedDevice, int state) {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(prefix = { "STATE_" }, value = {
            STATE_DISCONNECTED,
            STATE_CONNECTING,
            STATE_CONNECTED,
            STATE_DISCONNECTING,
    })
    @interface ConnectionState {}

    @IntDef(prefix = { "STATE_" }, value = {
            STATE_OFF,
            STATE_TURNING_ON,
            STATE_ON,
            STATE_TURNING_OFF,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface AdapterState {}

    /**
     * Called when a2dp codec config is changed. It listens to
     * {@link android.bluetooth.BluetoothA2dp#ACTION_CODEC_CONFIG_CHANGED}.
     *
     * @param cachedDevice Bluetooth device that changed
     * @param codecStatus  the current codec status of the a2dp profile
     */
    default void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedDevice,
            BluetoothCodecStatus codecStatus) {
    }

    /**
     * Called when new device group has been identified with the bonded remote device
     *
     * @param cachedDevice Bluetooth device with which device group has been found.
     * @param groupId Identifier of the device group.
     * @param setPrimaryServiceUuid Primary service with which this Device Group
     *                              is associated.
     */
    default void onNewGroupFound(CachedBluetoothDevice cachedDevice, int groupId,
            UUID setPrimaryServiceUuid) {
    }

    /**
     * Called when Group Discovery status has been changed.
     *
     * @param groupId Identifier of the coordinated set.
     * @param status Status of the group discovery procedure.
     * @param reason Reason for the change in status of discovery.
     */
    default void onGroupDiscoveryStatusChanged (int groupId, int status, int reason) {
    }

    /**
     * Called when Broadcast state is changed. It listens to
     * {@link android.bluetooth.BluetoothBroadcast#ACTION_BROADCAST_STATE_CHANGED}
     *
     * @param state        the Bluetooth device connection state, the possible values are:
     *                     {@link android.bluetooth.BluetoothBroadcast#STATE_DISABLED},
     *                     {@link android.bluetooth.BluetoothBroadcast#STATE_ENABLING},
     *                     {@link android.bluetooth.BluetoothBroadcast#STATE_ENABLED},
     *                     {@link android.bluetooth.BluetoothBroadcast#STATE_DISABLING},
     *                     {@link android.bluetooth.BluetoothBroadcast#STATE_STREAMING}
     */
    default void onBroadcastStateChanged(int state) {
    }

    /**
     * Called when Broadcast key is changed. It listens to
     * {@link android.bluetooth.BluetoothBroadcast#ACTION_BROADCAST_ENCRYPTION_KEY_GENERATED}
     *
     */
    default void onBroadcastKeyGenerated() {
    }
}
