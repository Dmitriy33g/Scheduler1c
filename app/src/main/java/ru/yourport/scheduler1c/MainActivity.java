package ru.yourport.scheduler1c;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    final String LOG_TAG = "myLogs";
    EditText etLogin, etPassword;
    Spinner spinner;

    String[] dataSpinner = {"Список организаций", "Список остатков номенклатуры"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        // адаптер
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapterSpinner);
        // заголовок
        spinner.setPrompt("Title");
        // выделяем элемент
        spinner.setSelection(1);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // показываем позиция нажатого элемента
                //Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

    public void onClickSoapHttp(View view) {
        String nameText = "";
        String title = "";
        String transport = "";
        String query = "";

        switch (view.getId()) {
            case R.id.btnSoap:
                transport = "SOAP";
                break;
            case R.id.btnHttp:
                transport = "HTTP";
                break;
        }

        switch (spinner.getSelectedItemPosition()) {
            case 0:
                nameText = "Список организаций";
                title = "Выберите организацию";
                query = "Organization";
                break;
            case 1:
                nameText = "Список остатков номенклатуры";
                title = "Выберите номенклатуру";
                query = "OstatkiNomenklatury";
                break;
        }

        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
        intent.putExtra("tvNameText", nameText);
        intent.putExtra("Title", title);
        intent.putExtra("transport", transport);
        intent.putExtra("query", query);
        startActivity(intent);
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

}