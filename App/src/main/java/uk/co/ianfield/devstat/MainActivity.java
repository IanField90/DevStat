package uk.co.ianfield.devstat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {
    private static final int SCREEN_METRICS = 0;
    private static final int SOFTWARE = 1;
    private static final int HARDWARE = 2;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.title_screen_metrics),
                                getString(R.string.title_software),
                                getString(R.string.title_hardware)
                        }),
                this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }


    @OptionsItem(R.id.action_about)
    void actionAboutClick() {
        Intent intent = new Intent(this, AboutActivity_.class);
        startActivity(intent);
    }

    @OptionsItem(R.id.action_developer)
    void actionDeveloperClick() {
        startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        if (position == SCREEN_METRICS) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ScreenStatsFragment_())
                    .commit();
        }
        else if (position == SOFTWARE) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SoftwareStatsFragment_())
                    .commit();
        }
        else if (position == HARDWARE) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HardwareStatsFragment_())
                    .commit();
        }
        return true;
    }

}
