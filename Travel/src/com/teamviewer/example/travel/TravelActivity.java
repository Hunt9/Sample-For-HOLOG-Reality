/*
 * Copyright 2013-2014 TeamViewer (www.teamviewer.com).  All rights reserved.
 *
 * Please refer to the end user license agreement (EULA), the app developer agreement and license 
 * information associated with this source code for terms and
 * conditions that govern your use of this software.
 */

package com.teamviewer.example.travel;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TravelActivity extends Activity implements ScreenSharingWrapper.RunningStateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupCustomActionBar();
        setupSimplePreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.travel_activity_actions, menu);
        
        MenuItem menuItem = menu.findItem(R.id.help);
        updateMenuItemState(menuItem);
        
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // ensure the correct reference is listening
        ScreenSharingWrapper.getInstance().setRunningStateListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // don't keep the reference when activity is destroyed
        ScreenSharingWrapper.getInstance().setRunningStateListener(null);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                ScreenSharingWrapper.getInstance().startTeamViewerSession(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRunningStateChange(boolean isRunning) {
        // saving the state is not necessary and
        // missed events between #onPause() and #onResume()
        // are intercepted by querying the session state in
        // #updateMenuItemState(MenuItem)
        invalidateOptionsMenu();
    }

    private void setupCustomActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_title);
        }
    }

    private void setupSimplePreferences() {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new TravelFragment())
                .commit();
    }

    private void updateMenuItemState(MenuItem menuItem) {
        boolean buttonEnabled = !ScreenSharingWrapper.getInstance().isSessionRunning();
        menuItem.setEnabled(buttonEnabled);
    }
}
