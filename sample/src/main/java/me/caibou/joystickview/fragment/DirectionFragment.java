package me.caibou.joystickview.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.caibou.joystickview.R;
import me.caibou.rockerview.DirectionView;

public class DirectionFragment extends Fragment {

    public static DirectionFragment newInstance() {

        Bundle args = new Bundle();

        DirectionFragment fragment = new DirectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_direction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final TextView tvDirection = view.findViewById(R.id.tv_direction);
        DirectionView directionView = view.findViewById(R.id.direction_control);
        directionView.setDirectionChangeListener(new DirectionView.DirectionChangeListener() {
            @Override
            public void onDirectChange(DirectionView.Direction direction) {
                tvDirection.setText(direction.toString());
            }
        });
    }
}
