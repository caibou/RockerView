package me.caibou.joystickview;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import me.caibou.joystickview.fragment.DirectionFragment;
import me.caibou.joystickview.fragment.JoystickFragment;
import me.caibou.rockerview.DirectionView;
import me.caibou.rockerview.JoystickView;

public class MainActivity extends AppCompatActivity {

    private Fragment[] fragments = {JoystickFragment.newInstance(), DirectionFragment.newInstance()};
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show(fragments[0]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_joystick:
                show(fragments[0]);
                break;
            case R.id.menu_direction:
                show(fragments[1]);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void show(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (currentFragment != null){
            ft.hide(currentFragment);
        }
        if (fragment.isAdded()){
            ft.show(fragment);
        } else {
            ft.add(R.id.fl_contnet, fragment);
        }
        ft.commit();
        currentFragment = fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
