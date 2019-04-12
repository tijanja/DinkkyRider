package advancenji.teetech.com.dankkyrider2.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import advancenji.teetech.com.dankkyrider2.R;
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

    public static AlertDialog alert(Context ctx, String message, String tittle,String buttonName ,View.OnClickListener negative, View.OnClickListener positive) {
        //if (ctx == null || TextUtils.isEmpty(message) || ((Activity) ctx).isFinishing()) return;

        AlertDialog.Builder db = new AlertDialog.Builder(ctx);
        db.setTitle(tittle).setMessage(message).setPositiveButton(buttonName, null).setNegativeButton("No", null);

        d = db.create();
        //d.setIcon(ctx.getResources().getDrawable(R.drawable.ic_alert));
        d.setCanceledOnTouchOutside(false);
        d.show();

        Button positiveButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#a475f6"));
        positiveButton.setOnClickListener(positive);

        Button negativeButton = d.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#a475f6"));
        negativeButton.setOnClickListener(negative);
        return d;
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

    public static int startButtonAnim(Activity activity, final FrameLayout next)
    {

        ValueAnimator anim = ValueAnimator.ofInt(next.getMeasuredWidth(),next.getMeasuredHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = next.getLayoutParams();
                layoutParams.width = val;
                next.requestLayout();
                next.setEnabled(true);

            }
        });

        next.invalidate();

        anim.setDuration(250);
        anim.start();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                next.findViewById(R.id.nextText).animate().setDuration(250).alpha(0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        super.onAnimationEnd(animation);
                        next.findViewById(R.id.progress).setVisibility(View.VISIBLE);

                    }
                }).start();
            }
        });

        return next.getMeasuredWidth();

    }


    public static void stopButtonAnim(Activity activity,final FrameLayout next,int measuredWidth) {
        Log.e("amin-1", "width--" + measuredWidth);
        final ValueAnimator anim = ValueAnimator.ofInt(next.getMeasuredWidth(), measuredWidth);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = next.getLayoutParams();
                layoutParams.width = val;
                next.requestLayout();
            }
        });

        anim.setDuration(250);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                anim.start();
                next.findViewById(R.id.progress).setVisibility(View.GONE);
                next.findViewById(R.id.progress).invalidate();

                next.findViewById(R.id.nextText).animate().setDuration(250).alpha(1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        next.setEnabled(true);
                        next.setVisibility(View.VISIBLE);
                        next.invalidate();


                    }
                }).start();
                next.findViewById(R.id.nextText).invalidate();
            }
        });
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
