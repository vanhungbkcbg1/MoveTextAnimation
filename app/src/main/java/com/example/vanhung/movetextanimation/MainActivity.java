package com.example.vanhung.movetextanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RelativeLayout layout_root;
    private boolean is_click_again = false;
    private float current_x = 0;
    private float current_y = 0;
    private int maxScreenWidth;
    private List<View>controls=new ArrayList<>();
    private int left;
    private int top;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout_root = (RelativeLayout) findViewById(R.id.layout_root);
        maxScreenWidth = getMaxScreenWidth();
        addViewToRoot(20, 400, 6);

    }

    private int getMaxScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    void rebuildPosition(View v,Position position)
    {
        for (int i=0;i<controls.size();i++)
        {
            View current_control=controls.get(i);
            if (i==0)
            {
                ObjectAnimator animX = ObjectAnimator.ofFloat(current_control, "x", 10);
                ObjectAnimator animY = ObjectAnimator.ofFloat(current_control, "y", 5);
                AnimatorSet animSetXY = new AnimatorSet();
                animSetXY.playTogether(animX, animY);
                animSetXY.start();
                left = 10;
                top = 5;
                width = current_control.getWidth();
                height = current_control.getHeight();
            }else
            {
                int actual_width = current_control.getWidth();
                left = left + width + 10;
                if (left + actual_width <= maxScreenWidth) {
                    ObjectAnimator animX = ObjectAnimator.ofFloat(current_control, "x", left);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(current_control, "y", top);
                    AnimatorSet animSetXY = new AnimatorSet();
                    animSetXY.playTogether(animX, animY);
                    animSetXY.start();
                } else {
                    left = 10;
                    top = top + height + 5;
                    ObjectAnimator animX = ObjectAnimator.ofFloat(current_control, "x", left);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(current_control, "y", top);
                    AnimatorSet animSetXY = new AnimatorSet();
                    animSetXY.playTogether(animX, animY);
                    animSetXY.start();
                }
            }
            if (v!=null) {
                position.setClick_count(1);
                v.setTag(position);
            }
        }
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Position position=(Position)v.getTag();
            if (position.getClick_count()==0) {
                controls.add(v);
                rebuildPosition(v,position);
            }
            else
            {
                ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", position.getLeft());
                ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", position.getTop());
                AnimatorSet animSetXY = new AnimatorSet();
                animSetXY.playTogether(animX, animY);
                animSetXY.start();
                //v.setTag(1,0);
                position.setClick_count(0);
                v.setTag(position);
                controls.remove(v);
                rebuildPosition(null,null);
            }
        }
    };

    void addViewToRoot(int min_left, int min_top, int number_view) {
        int width = 0;
        int height = 0;
        int left = 0;
        int top = 0;
        for (int i = 0; i < number_view; i++) {
            if (width == 0 && height == 0) {
                Button btn = new Button(this);
                btn.setText("Answer" + String.valueOf(i));
                btn.setOnClickListener(onClickListener);
                btn.setTextColor(Color.WHITE);
                btn.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                width = btn.getMeasuredWidth();
                height = btn.getMeasuredHeight();

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(min_left, min_top, 0, 0);
                Position position=new Position();
                position.setLeft(min_left);
                position.setTop(min_top);
                position.setClick_count(0);
                btn.setTag( position);
                //btn.setTag(1,0);
                layout_root.addView(btn, params);
                top = min_top;
                left = min_left;

            } else {

                Button btn = new Button(this);
                btn.setText("Answer" + String.valueOf(i));
                btn.setTextColor(Color.WHITE);
                btn.setOnClickListener(onClickListener);
                btn.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Position position=new Position();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(left + width + 10, top, 0, 0);
                position.setLeft(left + width + 10);
                position.setTop(top);
                position.setClick_count(0);
                int actual_width = btn.getMeasuredWidth();
                int actual_height = btn.getMeasuredHeight();
                left = left + width + 10;
                if (left + actual_width <= maxScreenWidth) {

                    btn.setTag(position);
                    //btn.setTag(1,0);
                    layout_root.addView(btn, params)   ;
                } else {
                    left = min_left;
                    top = top + height + 10;
                    params.setMargins(left, top, 0, 0);
                    position.setTop(top);
                    position.setLeft(left);
                    position.setClick_count(0);
                    btn.setTag(position);
                    //btn.setTag(1,0);
                    layout_root.addView(btn, params);

                }
                width = actual_width;
                height = actual_height;

            }

        }
    }

}
