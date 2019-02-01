package advancenji.teetech.com.dankkyrider2.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {

    public static AlertDialog d;

    public static final String NAIRA_SYMBOL = "\u20A6";

    public static void httpPostConnection( String url, RequestBody requestBody, final HttpResponse httpResponse)
    {
        Map<String, String> header = new HashMap<>();
        header.put("connection", "keep-alive");
        header.put("accept-language", "en-us");
        header.put("content-type","application/json");


        Headers headers = Headers.of(header);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //client.sslSocketFactory(bankConParams.getSSLSocketFactory());
        client.hostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        Request.Builder builder = new Request.Builder();
        Request request = null;


        request = builder.url(url)
                .headers(headers)
                .post(requestBody)
                .build();



        if (request != null) {
            client.build().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    httpResponse.fail(e.getMessage());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String res = response.body().string().toString();
                    httpResponse.success(res);

                }
            });
        }
    }

    public static void httpGetConnection(String url, final HttpResponse httpResponse)
    {
        //Headers headers = Headers.of(bankConParams.getHeader());
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //client.sslSocketFactory(bankConParams.getSSLSocketFactory());
        client.hostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        Request.Builder builder = new Request.Builder();
        Request request = null;

        request = builder.url(url)
                //.headers(headers)
                .get()
                .build();


        if (request != null) {
            client.build().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    httpResponse.fail(e.getMessage());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String res = response.body().string().toString();
                    httpResponse.success(res);

                }
            });
        }
    }

    public static boolean isAlertShown()
    {
        boolean  c = false;
        if(d!=null&&d.isShowing())
        {
            d.dismiss();
            c = true;
        }

        return c;
    }

    public static void alert(Context ctx, String message) {

        if (ctx == null || TextUtils.isEmpty(message) || ((Activity) ctx).isFinishing()) return;

        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setTitle(null).setMessage(message).setPositiveButton("OK", null);
        d = db.create();
        d.setCanceledOnTouchOutside(false);
        d.show();
        Button positiveButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#a475f6"));
    }

    public static void closeAlert() {
        d.dismiss();
    }

    public static String formatPrice(String price) {
        String amount = "";
        if (price.equalsIgnoreCase("")) {
            return "";
        } else {
            try {
                amount = NumberFormat.getCurrencyInstance(Locale.US).format(Double.parseDouble(price));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            return Utils.NAIRA_SYMBOL+amount.replace("$", "");
        }
    }
}
