

package com.example.kellychung.popularmovie;

        public class SettingsActivity extends android.preference.PreferenceActivity implements android.preference.Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_settings);
        addPreferencesFromResource(com.example.kellychung.popularmovie.R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference("sort"));

    }

    private void bindPreferenceSummaryToValue(android.preference.Preference preference){

        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference, android.preference.PreferenceManager
                .getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),""));



    }

    @Override
    public boolean onPreferenceChange(android.preference.Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof android.preference.ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            android.preference.ListPreference listPreference = (android.preference.ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }




    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
