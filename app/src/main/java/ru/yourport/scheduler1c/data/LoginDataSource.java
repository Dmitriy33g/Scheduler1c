package ru.yourport.scheduler1c.data;

import ru.yourport.scheduler1c.Firebase;
import ru.yourport.scheduler1c.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            Firebase firebase = new Firebase();
            Boolean result = firebase.signIn(username, password);
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
