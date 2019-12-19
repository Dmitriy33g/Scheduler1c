package ru.yourport.scheduler1c;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.Executor;

public class Firebase {

    private final String LOG_TAG = "myLogs";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    boolean result;

    public boolean signIn (String email, String password) {

        result = false;

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "Авторизация пройдена");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //Toast.makeText(MainActivity.this,
                            //        "Авторизация пройдена", Toast.LENGTH_SHORT).show();
                            result = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(LOG_TAG, "Авторизация не пройдена", task.getException());
                            //Toast.makeText(MainActivity.this,
                            //        "Авторизация не пройдена", Toast.LENGTH_SHORT).show();
                            result = false;
                        }
                    }
                });

        return result;
    }
}
