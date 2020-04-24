package de.mycrocast.dtalisty.application;

import android.app.Application;

import de.mycrocast.dtalisty.data.EntryHolderManager;
import de.mycrocast.dtalisty.messaging.RequestManager;
import de.mycrocast.dtalisty.messaging.ServerConfig;

public class DTAListApp extends Application {

    private RequestManager requestManager;
    private EntryHolderManager entryHolderManager;

    @Override
    public void onCreate() {
        super.onCreate();

        this.requestManager = new RequestManager(this.getApplicationContext(), new ServerConfig("192.168.2.114", "8080"));
        this.entryHolderManager = new EntryHolderManager();
    }

    public RequestManager getRequestManager() {
        return this.requestManager;
    }

    public EntryHolderManager getEntryHolderManager() {
        return this.entryHolderManager;
    }
}
