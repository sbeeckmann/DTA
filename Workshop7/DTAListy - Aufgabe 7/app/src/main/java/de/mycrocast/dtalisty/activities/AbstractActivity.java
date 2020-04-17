package de.mycrocast.dtalisty.activities;

import androidx.appcompat.app.AppCompatActivity;

import de.mycrocast.dtalisty.application.DTAListApp;
import de.mycrocast.dtalisty.data.EntryHolderManager;
import de.mycrocast.dtalisty.messaging.RequestManager;

public abstract class AbstractActivity extends AppCompatActivity {

    protected RequestManager getRequestManager() {
        return ((DTAListApp) this.getApplication()).getRequestManager();
    }

    protected EntryHolderManager getEntryHolderManager() {
        return ((DTAListApp) this.getApplication()).getEntryHolderManager();
    }
}
