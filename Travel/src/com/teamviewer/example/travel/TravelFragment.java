/*
 * Copyright 2013-2014 TeamViewer (www.teamviewer.com).  All rights reserved.
 *
 * Please refer to the end user license agreement (EULA), the app developer agreement and license 
 * information associated with this source code for terms and
 * conditions that govern your use of this software.
 */

package com.teamviewer.example.travel;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class TravelFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        bindPreferenceSummaryToValue(findPreference("planning_trip"));
        bindPreferenceSummaryToValue(findPreference("planning_destination"));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
        } else {
            preference.setSummary(stringValue);
        }
        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // update the preference's summary on value change
        preference.setOnPreferenceChangeListener(this);

        // initialize the summary at binding time
        String currentValue = PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), "");
        onPreferenceChange(preference, currentValue);
    }
}
