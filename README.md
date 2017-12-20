# RockerView

## 快速开始

###JoystickView

<img src="art/JoystickView.gif" width=216/><img src="art/DirectionView.gif" width=216/>

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

![](/art/DirectionView.gif){:width="360px" height="640px"}

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



## License

```
Copyright 2017 drakeet.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```