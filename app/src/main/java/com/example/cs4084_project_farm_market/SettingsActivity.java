package com.example.cs4084_project_farm_market;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
public class SettingsActivity extends AppCompatActivity {

    SwitchCompat switch_1, switch_2;
    boolean stateSwitch1, stateSwitch2;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        stateSwitch1 = preferences.getBoolean("switch1", false);
        stateSwitch2 = preferences.getBoolean("switch2", false);
        switch_1= (SwitchCompat) findViewById(R.id.switch_1);
        switch_2= (SwitchCompat) findViewById(R.id.switch_2);
        switch_1.setChecked(stateSwitch1);
        switch_2.setChecked(stateSwitch2);
        switch_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch1 = !stateSwitch1;
                switch_1.setChecked(stateSwitch1);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch1", stateSwitch1);
                editor.apply();
            }
        });
        switch_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch2 = !stateSwitch1;
                switch_2.setChecked(stateSwitch1);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch2", stateSwitch2);
                editor.apply();
            }
        });
    }
}