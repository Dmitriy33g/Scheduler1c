package ru.yourport.scheduler1c;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.yourport.scheduler1c.ui.login.LoginActivity;

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
                //Boolean bol = dataSnapshot.hasChild("1c");
                String value = dataSnapshot.child("master1c").child("log").getValue(String.class);
                Log.d(LOG_TAG, value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null ) {
                    firebaseUser.reload();
                    String token = firebaseUser.getIdToken(false).toString();
                    Log.d("myLogs",
                            "Пользователь вошел в систему как " + firebaseUser.getEmail() +
                                    (firebaseUser.isEmailVerified() ?
                            ", и адрес электронной почты подтвержден." :
                            ". Адрес электронной почты не подтвержден!") + " TokenID=" + token);
                } else {
                    Log.d("myLogs", "onAuthStateChanged:signed_out");
                }
            }
        });

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        //reauthenticate(etLogin.getText().toString(), etPassword.getText().toString());

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

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
//        DialogFragment dialog = new MessageFragment();
//        Bundle args = new Bundle();
//        args.putInt("layout", R.layout.dialogsignin);
//        args.putString("namePositive", "ОК");
//        args.putString("nameNeutral", "Регистрация");
//        args.putString("nameNegative", "Отмена");
//        dialog.setArguments(args);
//        dialog.setCancelable(false);
//        dialog.show(getSupportFragmentManager(), "dialogsignin");
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
        args.putString("title", "Мой заголовок!");
        args.putString("message", "Привет!");
        args.putString("namePositive", "ОК");
        dialog.setArguments(args);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog");
        ((MessageFragment) dialog).dismissDialog((MessageFragment) dialog, 8000);
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

    public void onClickAvto(View view) {

        final String login = etLogin.getText().toString();
        final String password = etPassword.getText().toString();

        int index = login.indexOf("@");
        String logstart = login.substring(0, index > 0 ? index-1 : 0);
        String logfinish = login.substring(index > 0 ? index+1 : 0);

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введите Email и Пароль", Toast.LENGTH_LONG).show();
            return;
        } else if (index == -1 || logstart.isEmpty() || logfinish.isEmpty() ) {
            Toast.makeText(this, "Не верно введен Email", Toast.LENGTH_LONG).show();
            return;
        }

        switch (view.getId()) {
            case R.id.btnSingIn:
                signIn(login, password);
                break;
            case R.id.btnReg:
                registration(login, password);
                break;
        }
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
                //dialog.cancel();
                /*DialogFragment newdialog = new MessageFragment();
                Bundle args = new Bundle();
                args.putInt("layout", R.layout.dialogsignin);
                newdialog.setArguments(args);
                newdialog.setCancelable(false);
                newdialog.show(getSupportFragmentManager(), "dialogsignin");*/
                Log.d(LOG_TAG, "Dialog BUTTON_POSITIVE");
                break;
            case Dialog.BUTTON_NEGATIVE:
                Log.d(LOG_TAG, "Dialog BUTTON_NEGATIVE");
                finish();
                break;
            case Dialog.BUTTON_NEUTRAL:
                Log.d(LOG_TAG, "Dialog BUTTON_NEUTRAL");
                //dialog.getClass().getName();
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
                            Log.d(LOG_TAG, "Авторизация пройдена");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this,
                                    "Авторизация пройдена", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(LOG_TAG, "Авторизация не пройдена", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Авторизация не пройдена", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void registration (final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "Пользователь успешно зарегистрирован");
                            //Toast.makeText(MainActivity.this,
                            //        "Пользователь успешно зарегистрирован",
                            //        Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            sendVerificationEmail();
                            User user = new User(mAuth.getUid(), "", "");//email
                            mDatabase.child("users").child(mAuth.getUid()).setValue(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "Не прошла регистрация", task.getException());
                            Toast.makeText(MainActivity.this, "Регистрация не прошла",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,
                                        "Регистрация прошла успешно. Письмо с подтверждением отправлено",
                                        Toast.LENGTH_SHORT).show();
                                Log.w(LOG_TAG, "Регистрация прошла успешно. Письмо с подтверждением отправлено", task.getException());
                            } else
                                Log.w(LOG_TAG, "Не удалось отправить письмо", task.getException());
                        }
                    });
        }
    }

    public void reauthenticate(final String email, final String password) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && (email.isEmpty() || password.isEmpty())) {
            mAuth.signOut();
            return;
        } else if (email.isEmpty() || password.isEmpty()) return;

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Переавторизация прошла успешно", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Переавторизация прошла успешно", task.getException());
                        } else {
                            Log.d(LOG_TAG, "Переавторизация не пройдена", task.getException());
                        }

                    }
                });
    }

    public class User {

        public String username;
        public String email;
        public String id;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

    }
}