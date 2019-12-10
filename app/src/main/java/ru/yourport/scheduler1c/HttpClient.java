package ru.yourport.scheduler1c;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class HttpClient extends AsyncTask<String, Integer, String[][]> {

    private long TimeStart = 0, TimeEnd = 0;
    private String ERROR = "";
    private final String URL = "https://kamaz.ddns.net:10100/testut/hs/ExchangeTFK/query";
    private final String LOG_TAG = "myLogs";

    private OkHttpClient client = new OkHttpClient();

    public String getERROR() {
        return ERROR;
    }

    public long getTimeEnd() {
        return TimeEnd;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Date date = new Date();
        TimeStart = date.getTime();
    }

    @Override
    protected String[][] doInBackground(String... strings) {
        String[][] resultString = new String[0][0];
        String LOGIN = "wsChangeServis";//strings[0]
        String PASSWORD = "Service2018";//strings[1]

        try {
            Authenticate(LOGIN, PASSWORD);

            JSONObject jsonObject = new JSONObject(run());
            JSONArray ja = jsonObject.getJSONArray("МассивОрганизаций");

            resultString = new String[ja.length()][3];

            //resultString[0][1] = jsonObject.getString("Текст");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject joOrg = ja.getJSONObject(i);
                String id = joOrg.getString("ID");
                String name = joOrg.getString("Наименование");
                int idfb = joOrg.getInt("IDFb");
                Log.d(LOG_TAG, "ID = " + id + ", Наименование = " + name +
                        ", IDFb = " + idfb);

                resultString[i][0] = id;
                resultString[i][1] = name;
            }

        } catch (Exception e) {
            Log.d(LOG_TAG,"Error HttpClient: " + e.getMessage());
            e.printStackTrace();
        }

        Date date = new Date();
        TimeEnd = date.getTime() - TimeStart;

        Log.d(LOG_TAG, "Результат времени в милисекундах: " + TimeEnd);

        return resultString;
    }

    private SSLSocketFactory sslSocket() {
        X509TrustManager trustManager;
        TrustManager[] trustManagers;
        SSLSocketFactory sslSocketFactory;
        try {
            trustManagers = new TrustManager[]{new SSLConnection._FakeX509TrustManager()};
            trustManager = (X509TrustManager) trustManagers[0];

            //if (trustManagers == null) {
            //    trustManagers = new TrustManager[]{new SSLConnection._FakeX509TrustManager()};
            //}
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return sslSocketFactory;
    }

    private void Authenticate(final String login, final String password) {
        client = new OkHttpClient.Builder()
                .readTimeout(300, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslSocket())
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        if (response.request().header("Authorization") != null) {
                            return null; // Give up, we've already attempted to authenticate.
                        }

                        //System.out.println("Authenticating for response: " + response);
                        //System.out.println("Challenges: " + response.challenges());
                        String credential = Credentials.basic(login, password);
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }
                })
                .build();
    }

    private String run() {

        RequestBody formBody = new FormBody.Builder()
                .add("List", "Organization")
                .add("g", "test")
                .build();

        Request request = new Request.Builder()
                .url(URL)
                //.addHeader("gg", "test2")
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                Log.d("myLogs",responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            String result = response.body().string();
            Log.d("myLogs","Result: " + result);
            return result;
        } catch (IOException e) {
            ERROR = "Error HttpClient run: " + e.getMessage();
            Log.d("myLogs", ERROR);
            e.printStackTrace();
        }

        return "";
    }
}