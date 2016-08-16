package de.rastapasta.android.xposed.pokemongo;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class DoSyncRequestHook extends XC_MethodHook {
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

        XposedBridge.log("Intercepted request to "+(String)param.args[2]);

        XSharedPreferences pref = new XSharedPreferences(DoSyncRequestHook.class.getPackage().getName(), "preferences");
        pref.makeWorldReadable();
        pref.reload();

        String request = ((String)param.args[2]).substring(34);
        XposedBridge.log("URL part: "+request);

        if (pref.getBoolean("enable_endpoint", false)) {
            String endpoint = pref.getString("endpoint", "pgorelease.nianticlabs.com");
            String url = pref.getBoolean("enable_ssl", false) ? "https://" : "http://";

            url += endpoint + request;
            XposedBridge.log("Rewriting URL to "+url);

            param.args[2] = url;
        }
    }
}
