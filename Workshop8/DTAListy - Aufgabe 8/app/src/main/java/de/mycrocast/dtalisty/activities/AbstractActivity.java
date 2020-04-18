package de.mycrocast.dtalisty.activities;

import androidx.appcompat.app.AppCompatActivity;

import de.mycrocast.dtalisty.application.DTAListApp;
import de.mycrocast.dtalisty.data.EntryHolderManager;
import de.mycrocast.dtalisty.messaging.EntryRestCaller;
import de.mycrocast.dtalisty.messaging.UserRestCaller;

public abstract class AbstractActivity extends AppCompatActivity {

    protected EntryRestCaller getEntryRestCaller() {
        return ((DTAListApp) this.getApplication()).getEntryRestCaller();
    }

    protected UserRestCaller getUserRestCaller() {
        return ((DTAListApp) this.getApplication()).getUserRestCaller();
    }

    protected EntryHolderManager getEntryHolderManager() {
        return ((DTAListApp) this.getApplication()).getEntryHolderManager();
    }
}
