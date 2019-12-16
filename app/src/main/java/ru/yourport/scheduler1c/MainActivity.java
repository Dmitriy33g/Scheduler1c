package ru.yourport.scheduler1c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        DialogInterface.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    final String LOG_TAG = "myLogs";
    EditText etLogin, etPassword;
    Spinner spinner;

    String[] dataSpinner = {"Список организаций", "Список остатков номенклатуры"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean bol = dataSnapshot.hasChild("1c");
                String value = dataSnapshot.child("master1c").child("log").getValue(String.class);
                Log.d(LOG_TAG, value + "\n1c=" + bol);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        // адаптер
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
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

        DialogFragment dialog = new MessageFragment();
        Bundle args = new Bundle();
        args.putString("message", "Привет!");
        args.putBoolean("isPositive", true);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
        //dialog.dismissDialog(dialog, 8000);
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

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                //i = R.string.yes;
                Log.d(LOG_TAG, "Dialog BUTTON_POSITIVE");
                break;
            case Dialog.BUTTON_NEGATIVE:
                Log.d(LOG_TAG, "Dialog BUTTON_NEGATIVE");
                break;
            case Dialog.BUTTON_NEUTRAL:
                Log.d(LOG_TAG, "Dialog BUTTON_NEUTRAL");
                break;
        }
    }

    public void signIn (String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    public void registration (String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}