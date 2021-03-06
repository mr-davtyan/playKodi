package net.davtyan.playKODI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static net.davtyan.playKODI.MyActivity.haveToCloseApp;

public class Settings extends AppCompatActivity {

    static final String APP_PREFERENCES = "MySettings";

    static final String APP_PREFERENCES_HOST = "Host"; //
    static final String APP_PREFERENCES_PORT = "Port"; //
    static final String APP_PREFERENCES_LOGIN = "Login"; //
    static final String APP_PREFERENCES_PASS = "Pass"; //
    static final String APP_PREFERENCES_FIRST_RUN = "Run"; //
    static final String APP_PREFERENCES_THEME_DARK = "Theme"; //
    static final String APP_PREFERENCES_THEME_DARK_AUTO = "AutoTheme"; //
    static final String APP_PREFERENCES_COPY_LINKS = "CopyLinks"; //
    static final String APP_PREFERENCES_PREVIEW_LINKS = "Show Link after sending"; //
    static String basicAuth;
    private static SharedPreferences mSettings;
    private EditText appPreferencesHostText;
    private EditText appPreferencesPortText;
    private EditText appPreferencesLoginText;
    private EditText appPreferencesPassText;
    private CheckBox checkBoxLightTheme;
    private CheckBox checkBoxFollowSystemDark;
    private CheckBox checkBoxCopyToClipboard;
    private CheckBox checkBoxPreviewLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.getBoolean(APP_PREFERENCES_THEME_DARK_AUTO, false)) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.AppThemeDark);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    setTheme(R.style.AppTheme);
                    break;
            }
        } else {
            if (mSettings.getBoolean(APP_PREFERENCES_THEME_DARK, true)) { //checking the theme
                setTheme(R.style.AppTheme);
            } else {
                setTheme(R.style.AppThemeDark);
            }
        }

        setContentView(R.layout.settings);

        checkBoxLightTheme = findViewById(R.id.checkBoxLightTheme); //set the theme
        checkBoxFollowSystemDark = findViewById(R.id.checkBoxFollowSystemDark); //set the theme auto
        checkBoxCopyToClipboard = findViewById(R.id.checkBoxCopyToClipboard); //bind copy to clipboard
        checkBoxPreviewLink = findViewById(R.id.checkBoxPreviewLink); //bind copy to clipboard

        appPreferencesHostText = findViewById(R.id.editTextHost);
        appPreferencesPortText = findViewById(R.id.editTextPort);
        appPreferencesLoginText = findViewById(R.id.editTextLogin);
        appPreferencesPassText = findViewById(R.id.editTextPass);

        appPreferencesHostText.setText(mSettings.getString(APP_PREFERENCES_HOST, ""));
        appPreferencesPortText.setText(mSettings.getString(APP_PREFERENCES_PORT, ""));
        appPreferencesLoginText.setText(mSettings.getString(APP_PREFERENCES_LOGIN, ""));
        appPreferencesPassText.setText(mSettings.getString(APP_PREFERENCES_PASS, ""));

        checkBoxLightTheme.setChecked(mSettings.getBoolean(APP_PREFERENCES_THEME_DARK, true));

        if (mSettings.getBoolean(APP_PREFERENCES_THEME_DARK_AUTO, true)) {
            checkBoxFollowSystemDark.setChecked(true);
            checkBoxLightTheme.setEnabled(false);
        } else {
            checkBoxFollowSystemDark.setChecked(false);
            checkBoxLightTheme.setEnabled(true);
        }

        checkBoxCopyToClipboard.setChecked(mSettings.getBoolean(APP_PREFERENCES_COPY_LINKS, false));
        checkBoxPreviewLink.setChecked(mSettings.getBoolean(APP_PREFERENCES_PREVIEW_LINKS, false));

        checkBoxFollowSystemDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                checkBoxLightTheme.setEnabled(!isChecked);
            }
        });

    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonSaveSettings) {  // save button

            String strHost = appPreferencesHostText.getText().toString(); // text from field - Host
            String strPort = appPreferencesPortText.getText().toString(); // text from field - Port
            String strLogin = appPreferencesLoginText.getText().toString(); // text from field - Login
            String strPass = appPreferencesPassText.getText().toString(); // text from field - Pass

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_HOST, strHost);
            editor.putString(APP_PREFERENCES_PORT, strPort);
            editor.putString(APP_PREFERENCES_LOGIN, strLogin);
            editor.putString(APP_PREFERENCES_PASS, strPass);
            editor.putBoolean(APP_PREFERENCES_FIRST_RUN, false);
            editor.putBoolean(APP_PREFERENCES_THEME_DARK, checkBoxLightTheme.isChecked());
            editor.putBoolean(APP_PREFERENCES_THEME_DARK_AUTO, checkBoxFollowSystemDark.isChecked());
            editor.putBoolean(APP_PREFERENCES_COPY_LINKS, checkBoxCopyToClipboard.isChecked());
            editor.putBoolean(APP_PREFERENCES_PREVIEW_LINKS, checkBoxPreviewLink.isChecked());

            editor.apply();

            String userPass = mSettings.getString(APP_PREFERENCES_LOGIN, "Login") + //crate authentication string
                    ":" +
                    mSettings.getString(APP_PREFERENCES_PASS, "Pass");
            basicAuth = "Basic " + Base64.encodeToString(userPass.getBytes(), Base64.NO_WRAP);

            Intent intentMyActivity1 = new Intent(Settings.this, MyActivity.class);
            startActivity(intentMyActivity1);
            finish();
        } else if (id == R.id.buttonCloseSettings) {  // exit button
            if (!mSettings.getBoolean(APP_PREFERENCES_FIRST_RUN, true)) {
                Intent intentMyActivity2 = new Intent(Settings.this, MyActivity.class);
                startActivity(intentMyActivity2);
            } else {
                haveToCloseApp = true;
            }
            finish();
        }
    }

    public void onBackPressed() { // Back button
        if (!mSettings.getBoolean(APP_PREFERENCES_FIRST_RUN, true)) {
            Intent intentMyActivity2 = new Intent(Settings.this, MyActivity.class);
            startActivity(intentMyActivity2);
        } else {
            haveToCloseApp = true;
        }
        finish();
    }

}