# VerticalLayoutTextView
竖排的文字控件，中英文混合时，中文正向，英文侧向

## 使用方法
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.wind.me.widget.VerticalLayoutTextView
        android:id="@+id/vertical_text1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:layout_marginRight="20dp"
        android:layout_marginLeft="20dp"
        app:textSize="14dp"
        app:lineSpace="20dp"
        app:normalCharPaddingTop="-3dp"
        app:customMaxHeight="216dp"
        app:maxLines="12"
        app:textColor="#FF423B2E"
        />

    <com.wind.me.widget.VerticalLayoutTextView
        android:id="@+id/vertical_text2"
        android:layout_below="@id/vertical_text1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="40dp"
        android:layout_marginRight="20dp"
        android:layout_marginLeft="20dp"
        app:textSize="14dp"
        app:needLeftLine="true"
        app:needSepLine="true"
        app:lineSpace="20dp"
        app:normalCharPaddingTop="-3dp"
        app:customMaxHeight="350dp"
        app:maxLines="12"
        app:textColor="#FF423B2E"
        />

</RelativeLayout>
```

## 实际效果
![device-2018-03-12-214431.png-256.2kB][1]


  [1]: http://static.zybuluo.com/Wind729/nyq641l48k6gbu998dl9iwb8/device-2018-03-12-214431.png
