package ru.yourport.scheduler1c;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.yourport.scheduler1c.ui.fragment.MyFragment;

public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MyFragment.newInstance())
                    .commitNow();
        }
    }
}
