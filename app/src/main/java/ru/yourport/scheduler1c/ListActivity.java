package ru.yourport.scheduler1c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ListActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    final String ATTRIBUTE_NAME_TIME = "time";
    final String ATTRIBUTE_NAME_CHASSIS = "chassis";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final String ATTRIBUTE_NAME_ID = "id";

    // картинки для отображения динамики
    final int positive = android.R.drawable.arrow_up_float;
    final int negative = android.R.drawable.arrow_down_float;

    // массив имен атрибутов, из которых будут читаться данные
    String[] from = { ATTRIBUTE_NAME_TIME, ATTRIBUTE_NAME_CHASSIS, ATTRIBUTE_NAME_IMAGE,
            ATTRIBUTE_NAME_ID };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.tvTime, R.id.tvChassis, R.id.ivImg, R.id.tvID };

    TextView tvName;
    ListView lvMain;
    SimpleAdapter adapter;
    DataLoader dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        // получаем Intent, который вызывал это Activity
        Intent intent = getIntent();
        // читаем из него action
        String action = intent.getAction();
        String tvNameText = "";

        // в зависимости от action заполняем переменные
        //if (action.equals("ru.yourport.intent.action.showtime")) {
        if (action == null) {
            tvNameText = intent.getStringExtra("tvNameText");
            this.setTitle(intent.getStringExtra("Title"));
        }

        tvName = findViewById(R.id.tvName);
        tvName.setText(tvNameText);

        lvMain = findViewById(R.id.lvMain);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvTime = view.findViewById(R.id.tvTime);
                TextView tvID = view.findViewById(R.id.tvID);
                String info = "itemClick: position = " + position + ", id = " + id +
                        ", text = " + tvTime.getText().toString() +
                        ", id = " + tvID.getText().toString();
                Log.d(LOG_TAG, info);
                Toast.makeText(view.getContext(), info, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        dl = new DataLoader();
        dl.execute();
        //dl.execute(etLogin.getText().toString(), etPassword.getText().toString());

        showResult();
    }

    private void showResult() {
        if (dl == null) return;

        String[][] result;
        String ERROR = "";

        TextView tvError = findViewById(R.id.tvError);

        try {
            Log.d(LOG_TAG, "Try to get result");
            result = dl.get();
            Log.d(LOG_TAG, "get returns " + result.length);
            Toast.makeText(this, "Время выполнения " + dl.getTimeEnd(), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            ERROR = "InterruptedException: " + e.getMessage();
            tvError.setText(ERROR);
            e.printStackTrace();
            return;
        } catch (ExecutionException e) {
            ERROR = "ExecutionException: " + e.getMessage();
            tvError.setText(ERROR);
            e.printStackTrace();
            return;
        }

        ERROR = dl.getERROR();
        if (ERROR != "") {
            tvError.setText(ERROR);
            Toast.makeText(this, "Ошибка DataLoader: " + ERROR, Toast.LENGTH_LONG).show();
            return;
        }

        tvError.setVisibility(View.GONE);

        //ArrayList ss = new ArrayList();
        //ss.add("Тест1");
        //ss.add("Тест2");

        //adapter = new ArrayAdapter<>(adapter.getContext(),
        //        android.R.layout.simple_list_item_1, s); //или через ArrayList ss

        ArrayList<Map<String, Object>> data = new ArrayList<>(result.length);
        Map<String, Object> m;
        for (int i = 0; i < result.length; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_ID, result[i][0]);
            m.put(ATTRIBUTE_NAME_TIME, result[i][1]);
            m.put(ATTRIBUTE_NAME_CHASSIS, "");
            m.put(ATTRIBUTE_NAME_IMAGE, positive);
            data.add(m);
        }

        adapter = new MySimpleAdapter(this, data, R.layout.item, from, to);
        lvMain.setAdapter(adapter);
   }

    class MySimpleAdapter extends SimpleAdapter {

        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public void setViewText(TextView v, String text) {
            // метод супер-класса, который вставляет текст
            super.setViewText(v, text);
            // если нужный нам TextView, то разрисовываем
            //if (v.getId() == R.id.tvValue) {
            //    int i = Integer.parseInt(text);
            //    if (i < 0) v.setTextColor(Color.RED); else
            //    if (i > 0) v.setTextColor(Color.GREEN);
            //}
        }

        @Override
        public void setViewImage(ImageView v, int value) {
            // метод супер-класса
            super.setViewImage(v, value);
            // разрисовываем ImageView
            if (value == negative) v.setBackgroundColor(Color.RED);
            else
            if (value == positive) v.setBackgroundColor(Color.GREEN);
            v.setVisibility(View.GONE);
        }
    }
}
