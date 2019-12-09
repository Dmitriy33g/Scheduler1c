package ru.yourport.scheduler1c;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    final String LOG_TAG = "myLogs";
    EditText etLogin, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        Button btnSoap = findViewById(R.id.btnSoap);
        btnSoap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("tvNameText", "Список организаций");
                intent.putExtra("Title", "Выберите организацию");
                startActivity(intent);
            }
        });

    }

    public void clickTest(View view) {


        Toast toast = Toast.makeText(this, "Тест", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        toastContainer.addView(imageView, 0);
        toast.show();

        Log.d(LOG_TAG, "clickTest");
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        //lvMain.setAdapter(adapter);
        //adapter.remove();

        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void onClick2(View view) {

        DialogFragment fireMissiles = new FireMissilesDialogFragment();
        fireMissiles.show(getSupportFragmentManager(), "fireMissiles");
    }

    public void onClick3(View view) {
        Log.d("myLogs","OK");
        Retrofit retrofit = null;

//        OkHttpClient client = new OkHttpClient.Builder()
//                .readTimeout(300, TimeUnit.SECONDS)
//                .addInterceptor(new BasicAuthInterceptor("wsChangeServis", "Service2018"))
//                .build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://kamaz.ddns.net:10100/testut/hs/ExchangeTFK/query")
//                .client(client)
//                .build();

        //SSLConnection.allowAllSSL();
//        OkHttpClient client = new OkHttpClient();
//        //RequestBody formBody = new FormBody.Builder()
//        //        .build();
//        try {
//            //.url("https://kamaz.ddns.net:10100/testut/hs/ExchangeTFK/query")
//            Request request = new Request.Builder()
//                    .url("https://publicobject.com/helloworld.txt")
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            String serverAnswer = response.body().string();
//            Log.d("myLogs","Result: " + serverAnswer);
//        } catch (IOException e) {
//            Log.d("myLogs","Error: " + e.getMessage());
//            e.printStackTrace();
//        }
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
//                            new AsyncTask<Void, String, String>() {
//                                @Override
//                                protected String doInBackground(Void... voids) {
//                                    String s = "";
//                                    try {
//                                        s = doGet("https://kamaz.ddns.net:10100/testut/hs/ExchangeTFK/query");
//                                    } catch (Exception e) {
//                                        Log.d("myLogs","Error: " + e.getMessage());
//                                        e.printStackTrace();
//                                    }
//                                    return s;
//                                }
//
//                                @Override
//                                protected void onPostExecute(final String result) {
//
//                                }
//                            }.execute();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Toast.makeText(this,
                "Today is " + day + "." + (month + 1) + "." + year, Toast.LENGTH_LONG).show();
    }

    public static class FireMissilesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Вопрос?")//R.string.dialog_fire_missiles
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                         }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public void run() throws Exception {
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                //System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            Log.d("myLogs","Result: " + response.body().string());
            //System.out.println(response.body().string());
        } catch (IOException e) {
            Log.d("myLogs","Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    class BasicAuthInterceptor implements Interceptor {
        private String credentials;

        public BasicAuthInterceptor(String user, String password) {
            credentials = Credentials.basic(user, password);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder()
                    .addHeader("Authorization", credentials)
                    .build();
            return chain.proceed(authenticatedRequest);
        }

    }
    public static String doGet(String url) throws Exception {

        SSLConnection.allowAllSSL();

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputline;
        StringBuffer responce = new StringBuffer();

        while ((inputline = bufferedReader.readLine()) != null) {
            responce.append(inputline);
        }
        bufferedReader.close();

        Log.d("myLogs","Response string: " + responce.toString());

        return responce.toString();
    }
}