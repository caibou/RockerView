package me.caibou.joystickview.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.caibou.joystickview.R;
import me.caibou.rockerview.JoystickView;

public class JoystickFragment extends Fragment {

    public static JoystickFragment newInstance() {

        Bundle args = new Bundle();

        JoystickFragment fragment = new JoystickFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_joystick, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final TextView tvAngle = view.findViewById(R.id.tv_angle);
        JoystickView joystickView = view.findViewById(R.id.joystick_control);
        joystickView.setAngleUpdateListener(new JoystickView.OnAngleUpdateListener() {
            @Override
            public void onAngleUpdate(double angle, int action) {
                if (action == JoystickView.ACTION_RELEASE){
                    tvAngle.setText("");
                } else {
                    tvAngle.setText(getString(R.string.angle, angle));
                }
            }
        });
    }
}
