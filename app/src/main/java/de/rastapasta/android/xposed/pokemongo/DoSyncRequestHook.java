package de.rastapasta.android.xposed.pokemongo;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class DoSyncRequestHook extends XC_MethodHook {

    private static final String DEFAULT_URL = "pgorelease.nianticlabs.com";

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        /** arg[0] = object long
         arg[1] = request id integer
         arg[2] = url string
         arg[3] = method integer
         arg[4] = headers string
         arg[5] = bytebuffer
         arg[6] = bytebuffer body offset int
         arg[7] = bytebuffer body size int **/

        XSharedPreferences pref = new XSharedPreferences(DoSyncRequestHook.class.getPackage().getName(), "preferences");
        pref.makeWorldReadable();
        pref.reload();

        if (pref.getBoolean("enable_endpoint", false)) {
            final String url = (String) param.args[2];
            final String[] parts = url.split(DEFAULT_URL);
            if (parts.length == 2) {
                XposedBridge.log("Intercepted request to " + url);

                final String scheme = pref.getBoolean("enable_ssl", false) ? "https://" : "http://";
                final String endpoint = pref.getString("endpoint", DEFAULT_URL);
                param.args[2] = scheme + endpoint + parts[1];

                XposedBridge.log("Rewriting URL to " + param.args[2]);
            }
        }
    }
}
