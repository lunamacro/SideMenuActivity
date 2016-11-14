package com.luna.sidemenuactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

import java.lang.reflect.Field;

public class SideMenuActivity extends AppCompatActivity {
    DrawerLayout activity_side_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        initView();
        initEvent();


    }


    //初始化界面控件
    void initView() {
        activity_side_menu = (DrawerLayout) findViewById(R.id.activity_side_menu);
        activity_side_menu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        setDrawerLeftEdgeSize(this, activity_side_menu, 0.6f);      //设置屏幕左边的60%区域为可滑动区域
    }


    //初始化抽屉层监听
    private void initEvent() {
        //setDrawerListener已经废弃，请使用addDrawerListener
        activity_side_menu.addDrawerListener(new myDrawListener());
    }

    /**
     * 抽屉滑动范围控制
     */
    private void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null)
            return;
        try {
            // 通过反射获取到DrawerLayout的mLeftDragger属性
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            // 找到 edgesize 并设置为 accessible
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            // 重新设置 edgesize ，以屏幕宽度为比例
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
            Log.e("NoSuchFieldException", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("IllegalArgumentExp", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("IllegalAccessException", e.getMessage());
        }
    }


    public void openLeftSideMenu(View view) {
        activity_side_menu.openDrawer(GravityCompat.START);
    }

    public void openRightSideMenu(View view) {
        activity_side_menu.openDrawer(GravityCompat.END);
        activity_side_menu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
    }

    //自定义抽屉监听器
    class myDrawListener implements DrawerLayout.DrawerListener {

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            View mContent = activity_side_menu.getChildAt(0);
            float Scale = 0.5f * (1.0f + slideOffset);

            if (drawerView.getTag().equals("LEFT")) {
                drawerView.setAlpha(Scale);
                ViewHelper.setTranslationX(mContent, drawerView.getMeasuredWidth() * slideOffset);
                mContent.invalidate();
            } else {
                drawerView.setAlpha(Scale);
                ViewHelper.setTranslationX(mContent, -drawerView.getMeasuredWidth() * slideOffset);
                mContent.invalidate();
            }
        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {
            activity_side_menu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }


}
