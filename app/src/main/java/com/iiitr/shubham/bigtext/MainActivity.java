package com.iiitr.shubham.bigtext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iiitr.shubham.bigtext.Adapters.TextRecyclerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextRecyclerAdapter adapter;
    private ArrayList<String> mList;
    private EditText editText;
    private final int maxChars = 40;
    private String MY_LIST = "MY_LIST";
    private boolean isLongPressed = false;
    private float initTextSize = 15;
    private float endTextSize = 100;
    private long animationDuration = 5000;
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = findViewById(R.id.text);
        editText.setTextSize(initTextSize);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(editText.getTextSize());
            }
        });
        fab.setLongClickable(true);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPressed = true;
                onLongPress();
                return true;
            }
        });
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(isLongPressed) {
                        onLongPressStopped();
                        sendMessage(editText.getTextSize());
                        isLongPressed = false;
                    }
                }
                return false;
            }
        });
    }

    private void initRecyclerView(ArrayList<String> mList, float tSize) {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new TextRecyclerAdapter(mList, this, tSize);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
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

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor sp = this.getPreferences(Context.MODE_PRIVATE).edit();
        sp.putString(MY_LIST, editText.getText().toString());
        sp.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
        String res = sp.getString(MY_LIST, "");
        editText.setText(res);
    }
    public void onLongPress() {
        final float start = initTextSize;
        float end = endTextSize;
        animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                editText.setTextSize(animatedValue);
            }
        });
        animator.start();
    }
    public void onLongPressStopped() {
        if(animator != null) {
            animator.cancel();
        }
    }

    public void sendMessage(float tSize) {
        String text = editText.getText().toString();
        editText.setText("");
        editText.setTextSize(initTextSize);
        mList = TextProcess.process(text, maxChars);
        initRecyclerView(mList, tSize);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mList.size() - 1);
    }
}
