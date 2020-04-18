package de.mycrocast.dtalisty.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import de.mycrocast.dtalisty.data.EntryHolderManager;
import de.mycrocast.dtalisty.messaging.EntryRestCaller;
import de.mycrocast.dtalisty.messaging.ServerConfig;
import de.mycrocast.dtalisty.messaging.UserRestCaller;

public class DTAListApp extends Application {
    private final static String IP = "192.168.2.114";
    private final static String PORT = "8080";

    private EntryHolderManager entryHolderManager;

    private EntryRestCaller entryRestCaller;
    private UserRestCaller userRestCaller;

    @Override
    public void onCreate() {
        super.onCreate();

        this.entryHolderManager = new EntryHolderManager();

        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        ServerConfig serverConfig = new ServerConfig(IP, PORT);
        this.entryRestCaller = new EntryRestCaller(requestQueue, serverConfig);
        this.userRestCaller = new UserRestCaller(requestQueue, serverConfig);
    }

    public EntryRestCaller getEntryRestCaller() {
        return this.entryRestCaller;
    }

    public UserRestCaller getUserRestCaller() {
        return this.userRestCaller;
    }

    public EntryHolderManager getEntryHolderManager() {
        return this.entryHolderManager;
    }
}
