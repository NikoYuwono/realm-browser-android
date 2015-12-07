package com.nikoyuwono.realmbrowser;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by nyuwono on 12/7/15.
 */
public class RealmBrowser {

    private static final String TAG = "RealmBrowser";

    private static final int DEFAULT_PORT = 8765;
    private RealmBrowserHTTPD server;

    public void start() {
        start(DEFAULT_PORT);
    }

    public void start(int port) {
        try {
            if (server == null) {
                server = new RealmBrowserHTTPD(port);
            }
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    public void showServerAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        Log.d(TAG, "Please access! http://" + formatedIpAddress + ":" + server.getListeningPort());
    }

    private class RealmBrowserHTTPD extends NanoHTTPD {

        public RealmBrowserHTTPD(int port) throws IOException {
            super(port);
        }

        @Override
        public Response serve(IHTTPSession session) {
            Method method = session.getMethod();
            String uri = session.getUri();
            Log.d(TAG, method + " '" + uri + "' ");
            Realm realm = Realm.getDefaultInstance();
            DynamicRealm dynamicRealm = DynamicRealm.getInstance(realm.getConfiguration());
            Set<Class<? extends RealmObject>> modelClasses =
                    realm.getConfiguration().getRealmObjectClasses();
            Map<String, String> params = session.getParms();
            String className = params.get("class_name");
            String selectedView = params.get("selected_view");

            HtmlBuilder htmlBuilder = new HtmlBuilder();
            htmlBuilder.start();
            // Sidebar
            htmlBuilder.showSidebar(modelClasses);

            // Main Display
            htmlBuilder.startMainDisplay();
            if (className != null) {
                try {
                    Class clazz = Class.forName(className);
                    if (RealmObject.class.isAssignableFrom(clazz)) {
                        htmlBuilder.setSelectedTableName(clazz);
                        htmlBuilder.startMainDisplayNavigation(selectedView);
                        if (selectedView != null && selectedView.equals(HtmlBuilder.CONTENT_VIEW)) {
                            String fieldName = params.get("field_name");
                            String queryValue = params.get("query_value");
                            HashMap<String, String> queryMap = new HashMap<>();
                            if (fieldName != null && queryValue != null) {
                                queryMap.put(fieldName, queryValue);
                            }
                            htmlBuilder.showTableContent(dynamicRealm, queryMap);
                        } else {
                            htmlBuilder.showTableStructure(dynamicRealm);
                        }
                        htmlBuilder.closeMainDisplayNavigation();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                htmlBuilder.showEmptyView();
            }
            htmlBuilder.closeMainDisplay();

            realm.close();
            return newFixedLengthResponse(htmlBuilder.finish());
        }
    }
}
