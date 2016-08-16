package de.rastapasta.android.xposed.pokemongo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Loader implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.nianticlabs.pokemongo"))
            return;

        final Class NiaNetClass = lpparam.classLoader.loadClass("com.nianticlabs.nia.network.NiaNet");
        final Class NianticTrustManagerClass = lpparam.classLoader.loadClass("com.nianticlabs.nia.network.NianticTrustManager");

        X509Certificate[] cert = new X509Certificate[0];

        findAndHookMethod(NianticTrustManagerClass, "checkServerTrusted", cert.getClass(), String.class,
                new CheckServerTrustedHook()
        );

        findAndHookMethod(NiaNetClass, "doSyncRequest", long.class, int.class, String.class, int.class, String.class, ByteBuffer.class, int.class, int.class,
                new DoSyncRequestHook()
        );
    }
}
