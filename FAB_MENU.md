![Fusion](https://github.com/blackbeared/fusion/blob/master/logo.png)

## Fusion By BlackBeared  [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![](https://jitpack.io/v/blackbeared/fusion.svg)](https://jitpack.io/#blackbeared/fusion)

_An Easy-to-use **Kotlin** based Customizable Library with Material Layouts_ by [@blackbeared](http://www.linkedin.com/er-sandip-savaliya).

##  Fully customizable FABs with FAB-Menu.

![Fusion](https://github.com/blackbeared/fusion/blob/master/fab_menu.gif)

## Sample Usage
FAB and FAB-MENU provides a functionality to add a floating actions menu with various functionalities.


```gradle

   <com.awesome.fabs.AwesomeFabMenu
        android:id="@+id/fab_menu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="right|bottom"
        android:layout_marginBottom="10dp"
        android:layout_marginLeft="10dp"
        android:layout_marginRight="10dp"
        app:menu_animationDelayPerItem="50"
        app:menu_backgroundColor="@android:color/transparent"
        app:menu_buttonSpacing="0dp"
        app:menu_colorNormal="#DA4336"
        app:menu_colorPressed="#E75043"
        app:menu_colorRipple="#99FFFFFF"
        app:menu_fab_label=""
        app:menu_fab_size="normal"
        app:menu_icon="@drawable/fab_add"
        app:menu_labels_colorNormal="#333333"
        app:menu_labels_colorPressed="#444444"
        app:menu_labels_colorRipple="#66FFFFFF"
        app:menu_labels_cornerRadius="3dp"
        app:menu_labels_ellipsize="none"
        app:menu_labels_hideAnimation="@anim/fab_slide_out_to_right"
        app:menu_labels_margin="0dp"
        app:menu_labels_maxLines="-1"
        app:menu_labels_padding="8dp"
        app:menu_labels_paddingBottom="4dp"
        app:menu_labels_paddingLeft="8dp"
        app:menu_labels_paddingRight="8dp"
        app:menu_labels_paddingTop="4dp"
        app:menu_labels_position="left"
        app:menu_labels_showAnimation="@anim/fab_slide_in_from_right"
        app:menu_labels_showShadow="true"
        app:menu_labels_singleLine="false"
        app:menu_labels_textColor="#FFFFFF"
        app:menu_labels_textSize="14sp"
        app:menu_openDirection="up"
        app:menu_shadowColor="#66000000"
        app:menu_shadowRadius="4dp"
        app:menu_shadowXOffset="1dp"
        app:menu_shadowYOffset="3dp"
        app:menu_showShadow="true">

        <com.awesome.fabs.AwesomeFab
            android:id="@+id/menu_item"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_history"
            app:fab_colorNormal="@color/color_green"
            app:fab_colorPressed="@color/color_green"
            app:fab_colorRipple="@color/color_light_green"
            app:fab_label="History"
            app:fab_size="mini" />

        <com.awesome.fabs.AwesomeFab
            android:id="@+id/menu_item2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_headphones"
            app:fab_colorNormal="@color/color_blue"
            app:fab_colorPressed="@color/color_light_blue"
            app:fab_colorRipple="@color/color_blue"
            app:fab_label="Music"
            app:fab_size="mini" />

    </com.awesome.fabs.AwesomeFabMenu>
```

## Description

Add this xml code in your **view.xml** file to get Fab menu. You can set the menu expand directions, various show-hide animations on FAB, and you can also set custom FAB item lables.
