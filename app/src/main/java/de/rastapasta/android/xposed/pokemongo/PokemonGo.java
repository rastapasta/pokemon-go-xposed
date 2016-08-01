package de.rastapasta.android.xposed.pokemongo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodHook;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import java.security.cert.X509Certificate;
import org.apache.commons.lang3.SerializationUtils;
import android.util.Base64;

public class PokemonGo implements IXposedHookLoadPackage {
    // Contains the original certificate chain, serialized and Base64 encoded 
    private static String originalChain =
            "rO0ABXVyACVbTGphdmEuc2VjdXJpdHkuY2VydC5YNTA5Q2VydGlmaWNhdGU7V79uQD0B25oCAAB4"+
            "cAAAAAJzcgAtamF2YS5zZWN1cml0eS5jZXJ0LkNlcnRpZmljYXRlJENlcnRpZmljYXRlUmVwiSdq"+
            "ncmuPAwCAAJbAARkYXRhdAACW0JMAAR0eXBldAASTGphdmEvbGFuZy9TdHJpbmc7eHB1cgACW0Ks"+
            "8xf4BghU4AIAAHhwAAAF/zCCBfswggTjoAMCAQICED+/UHcTKhDwnz2ZGQZ91X0wDQYJKoZIhvcN"+
            "AQELBQAwRDELMAkGA1UEBhMCVVMxFjAUBgNVBAoTDUdlb1RydXN0IEluYy4xHTAbBgNVBAMTFEdl"+
            "b1RydXN0IFNTTCBDQSAtIEczMB4XDTE2MDEyMjAwMDAwMFoXDTE3MDEyMTIzNTk1OVowbjELMAkG"+
            "A1UEBhMCVVMxEzARBgNVBAgMCkNhbGlmb3JuaWExFjAUBgNVBAcMDVNhbiBGcmFuY2lzY28xFjAU"+
            "BgNVBAoMDU5pYW50aWMsIEluYy4xGjAYBgNVBAMMESoubmlhbnRpY2xhYnMuY29tMIIBIjANBgkq"+
            "hkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtEEv/+epdmou6uuHYZUfj8I6ScLhSXwle0YsGIrch9Tp"+
            "cAJ5AJNqjSxAu4b+V615a7Ussb+lif4k5SmX3tr06BjVmFITaQ7vLif6j8/X9nzEOu9U3PKH3EwU"+
            "50d12b25UggJTxhFNp8BlWTFMuyxRaiQ6rN/GtxW1ItJq1WK3UtCBMH52ag1/I0vmzkPKdP2v59h"+
            "jtHzFifjjN1zgAIfFEVSpf2zvSZpUVNfaBAc3mHTTm8xLpim1lwP3qOvcs58b80GcmJpwZDitoK+"+
            "dxJzVwpFq0zYGJQ/v61yhTCa7xR4Poa61Tw+qXDhgA0YewVdOvUws/6MrSeo5DcMUMIZ8wIDAQAB"+
            "o4ICvTCCArkwLQYDVR0RBCYwJIIRKi5uaWFudGljbGFicy5jb22CD25pYW50aWNsYWJzLmNvbTAJ"+
            "BgNVHRMEAjAAMA4GA1UdDwEB/wQEAwIFoDArBgNVHR8EJDAiMCCgHqAchhpodHRwOi8vZ24uc3lt"+
            "Y2IuY29tL2duLmNybDCBnQYDVR0gBIGVMIGSMIGPBgZngQwBAgIwgYQwPwYIKwYBBQUHAgEWM2h0"+
            "dHBzOi8vd3d3Lmdlb3RydXN0LmNvbS9yZXNvdXJjZXMvcmVwb3NpdG9yeS9sZWdhbDBBBggrBgEF"+
            "BQcCAjA1DDNodHRwczovL3d3dy5nZW90cnVzdC5jb20vcmVzb3VyY2VzL3JlcG9zaXRvcnkvbGVn"+
            "YWwwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMB8GA1UdIwQYMBaAFNJv95b0hT9yPDB9"+
            "I9qFeJujfFp8MFcGCCsGAQUFBwEBBEswSTAfBggrBgEFBQcwAYYTaHR0cDovL2duLnN5bWNkLmNv"+
            "bTAmBggrBgEFBQcwAoYaaHR0cDovL2duLnN5bWNiLmNvbS9nbi5jcnQwggEFBgorBgEEAdZ5AgQC"+
            "BIH2BIHzAPEAdgDd6x0reg1PpiCLga2BaHB+Lo6dAdVciI09EcTNtuy+zAAAAVJqPh97AAAEAwBH"+
            "MEUCIFl7qCsjXg4KXb/LBVTO4/TY205KL7Iq7mxUoP4aFnUVAiEAqwliPLt8gXhZgw+cfcWNr0wz"+
            "WQenuoN5YoH13ZOhnmkAdwCkuQmQtBhYFIe7E6LMZ3AKPDWYBPkb37jjd80OyA3cEAAAAVJqPh+k"+
            "AAAEAwBIMEYCIQCme0KT+jn8lQKSPG0CVGgKys0Su9Xoaozu7CfR2sgXqQIhAIEYG8Z7OQ0HOsdn"+
            "zUixhdjdsH/TgUWKi8XIOFngqYS9MA0GCSqGSIb3DQEBCwUAA4IBAQDG9raLxSzfkdUf4+Amtf4l"+
            "R1vGXB/XsHNX9IksgNMckh10wtjRzN3V/gaGRN3c/YaKZ6nPqA8udksbigsgWmjjja0TFkF1psJ/"+
            "E1WyMZ9ZLdxNEiL96K3vBuM1KpmpAsW8sTlavuhZFk0KeHFELz+agfBna0atiNd9xkLb72mvC8k0"+
            "LX6WHqF6U3pxce2ke+nvSu0D7aXrYNTMGCPFu7pR4vd8j1G83YWA3yzNZg5KD254Bet3aZkro5h3"+
            "96OCV+Jp/CFRT5MuQgV6WQ7q5RzoT5oY5mheM7Wf9XOEsmhZX3jNiGkJ6tgX+UQt9ESErIlBL477"+
            "KRX09KJi4JP7c6pldAAFWC41MDlzcQB+AAJ1cQB+AAYAAARTMIIETzCCAzegAwIBAgIDAjpvMA0G"+
            "CSqGSIb3DQEBCwUAMEIxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1HZW9UcnVzdCBJbmMuMRswGQYD"+
            "VQQDExJHZW9UcnVzdCBHbG9iYWwgQ0EwHhcNMTMxMTA1MjEzNjUwWhcNMjIwNTIwMjEzNjUwWjBE"+
            "MQswCQYDVQQGEwJVUzEWMBQGA1UEChMNR2VvVHJ1c3QgSW5jLjEdMBsGA1UEAxMUR2VvVHJ1c3Qg"+
            "U1NMIENBIC0gRzMwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDjvn4KhqPPa209K6GX"+
            "rUkkTdd3uTR5CKWeop7eRxKSPX7qGYax6E89X/fQp3eaWx8KA7UZU9ulIZRpY51qTJEMEEe+Efps"+
            "hiW3qwRoQjgJZfAU2hme+msLq2LvjafvY3AjqK+B89FuiGdT7BKkKXWKp/JXPaKDmJfyCn3U50Nu"+
            "MHhiIllZuHEnRaoPZsZVP/oyFysxj0ag+mkUfJ2fWuLrM04QprPtd2PYw5703d95mnrU7t7dmszD"+
            "t6ldzBE6B7tvl6QBI0eVH6N3+liSxsfQvc+TGEK3fveeZerVO8rtrMVwof7UEJrwEgRErBpbeFBF"+
            "V0xvvYDLgVwts7x2oR5lAgMBAAGjggFKMIIBRjAfBgNVHSMEGDAWgBTAephojYn7qwVkDBF9qn1l"+
            "uMrMTjAdBgNVHQ4EFgQU0m/3lvSFP3I8MH0j2oV4m6N8WnwwEgYDVR0TAQH/BAgwBgEB/wIBADAO"+
            "BgNVHQ8BAf8EBAMCAQYwNgYDVR0fBC8wLTAroCmgJ4YlaHR0cDovL2cxLnN5bWNiLmNvbS9jcmxz"+
            "L2d0Z2xvYmFsLmNybDAvBggrBgEFBQcBAQQjMCEwHwYIKwYBBQUHMAGGE2h0dHA6Ly9nMi5zeW1j"+
            "Yi5jb20wTAYDVR0gBEUwQzBBBgpghkgBhvhFAQc2MDMwMQYIKwYBBQUHAgEWJWh0dHA6Ly93d3cu"+
            "Z2VvdHJ1c3QuY29tL3Jlc291cmNlcy9jcHMwKQYDVR0RBCIwIKQeMBwxGjAYBgNVBAMTEVN5bWFu"+
            "dGVjUEtJLTEtNTM5MA0GCSqGSIb3DQEBCwUAA4IBAQCg1Pcs+3QLf2TxzUNqn2JTHAJ8mJCi7k9o"+
            "1CAacxI+d7NQ63K87oi+fxfqd4+DYZVPhKHLMk9sIb7SaZZ9Y73cK6gf0BOEcP72NZWJ+aZ3sEbI"+
            "u7cT9clgadZM/tKO79NgwYCA4ef7i28heUrg3Kkbwbf7w0lZXLV3B0TUl/xJAIlvBk4BcBmsLxHA"+
            "4uYPL4ZLjXvDuacu9PGsFj45SVGeF0tPEDpbpaiSb/361gsDTUdWVxnzy2v189bPsPX1oxHSIFMT"+
            "NDcFLENaY9+NQNaFHlHpURceA1bJ8TCt55sRornQMYGbaLHZ6PPmlH7HrhMvh+3QJbBo+d4IWvMp"+
            "zNSScQB+AAg=";

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        // Check if we got the Pokemon App
        if (!lpparam.packageName.equals("com.nianticlabs.pokemongo"))
            return;

        // Prepare the hook parameters
        X509Certificate[] cert = new X509Certificate[0];

        // Hook it up! Intercept any calls to the checkServerTrusted
        findAndHookMethod("com.nianticlabs.nia.network.NianticTrustManager", lpparam.classLoader, "checkServerTrusted", cert.getClass(), String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("Replacing Pokemon Go's chain of trust");

                // Decode the stored original chain
                byte[] buffer = Base64.decode(originalChain, Base64.DEFAULT);

                // Restore the original chain object and inject it into the call arguments
                param.args[0] = (X509Certificate[])SerializationUtils.deserialize(buffer);
            }
        });
    }

}
