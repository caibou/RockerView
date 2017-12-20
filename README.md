# RockerView

## 快速开始

###JoystickView

<img src="/art/JoystickView.gif" style="zoom:30%" />

```xml
<me.caibou.rockerview.JoystickView
        android:id="@+id/joystick_control"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
                               
        app:edge_radius="65dp"
        app:stick_color="#f52504"
        />
```

其中edge_radius表示外边框的半径，stick_color表示摇杆的颜色





### DirectionView

<img src="/art/DirectionView.gif" style="zoom:30%" />

```xml
<me.caibou.rockerview.DirectionView
        android:id="@+id/direct_control"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        
		app:edge_radius="65dp"
        app:button_outside_circle_radius="60dp"
        app:button_side_width="40dp"
		app:indicator_color="#f52504"
		/>
```

其中edge_radius表示外边框的半径，button_outside_circle_radius是方向按钮外切圆的半径，button_side_width是方向按钮的边长，indicator_color是手指按下之后指示器的颜色。