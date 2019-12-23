package ru.yourport.scheduler1c.data;

import android.util.Log;

import ru.yourport.scheduler1c.Firebase;
import ru.yourport.scheduler1c.data.model.LoggedInUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    Boolean result;
    Firebase firebase;
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            firebase = new Firebase();
            firebase.signIn(username, password);
            result = firebase.getResult();
            /*for (int i = 0; result==null && i <= 10; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    result = firebase.getResult();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("myLogs","for="+i);
            }*/
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; result == null && i <= 10; i++) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                            result = firebase.getResult();
                            //Log.d(LOG_TAG, "for=" + i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
            if (!result) {
                return new Result.Error(new IOException("Авторизация не пройдена"));
            }
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
