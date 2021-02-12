/*
 * Copyright (C) 2018 Havoc-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cesium.config.center.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import android.widget.Toast;

public class NavigationFragment extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "NavigationFragment";

    private static final String NAV_BAR_LAYOUT = "nav_bar_layout";
    private static final String SYSUI_NAV_BAR = "sysui_nav_bar";
    private static final String KEY_BUTTON_BACKLIGHT = "button_backlight";
    private static final String TORCH_POWER_BUTTON_GESTURE = "torch_power_button_gesture";

    private ListPreference mNavBarLayout;
    private ContentResolver mResolver;
    private SwitchPreference mTorchPowerButton;
    private Preference mButtonBacklight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.config_center_navigation_category);
        mResolver = getActivity().getContentResolver();

        mButtonBacklight = (ListPreference) findPreference(KEY_BUTTON_BACKLIGHT);
        mNavBarLayout = (ListPreference) findPreference(NAV_BAR_LAYOUT);
        mNavBarLayout.setOnPreferenceChangeListener(this);
        String navBarLayoutValue = Settings.Secure.getString(mResolver, SYSUI_NAV_BAR);
        if (navBarLayoutValue != null) {
            mNavBarLayout.setValue(navBarLayoutValue);
        } else {
            mNavBarLayout.setValueIndex(0);
       boolean buttonBacklightSupported = getResources().getBoolean(
                com.android.internal.R.bool.config_button_brightness_support);
        }
        // screen off torch
        mTorchPowerButton = (SwitchPreference) findPreference(TORCH_POWER_BUTTON_GESTURE);
        Boolean mTorchPowerButtonValue = Settings.Secure.getInt(mResolver,
                Settings.Secure.TORCH_POWER_BUTTON_GESTURE, 0) != 0;
        mTorchPowerButton.setOnPreferenceChangeListener(this);
        mTorchPowerButton.setChecked(mTorchPowerButtonValue);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNavBarLayout) {
            Settings.Secure.putString(mResolver, SYSUI_NAV_BAR, (String) newValue);
            return true;
        } 
        if (preference == mTorchPowerButton) {
            boolean mTorchPowerButtonValue = (Boolean) newValue;
            if (mTorchPowerButtonValue) {
                Settings.Secure.putInt(mResolver, Settings.Secure.TORCH_POWER_BUTTON_GESTURE,
                2);
            } else {
                Settings.Secure.putInt(mResolver, Settings.Secure.TORCH_POWER_BUTTON_GESTURE,
                0);
            }
            return true;
        }
        return false;
    }

   @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CUSTOM_SETTINGS;
    }
}

