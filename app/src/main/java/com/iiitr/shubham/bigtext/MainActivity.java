package com.iiitr.shubham.bigtext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
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
    private float endTextSize = 200;
    private long animationDuration = 5000;
    private ArrayList<ValueAnimator> animators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = findViewById(R.id.text);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                editText.setText("");
                mList = TextProcess.process(text, maxChars);
                initRecyclerView(mList);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mList.size() - 1);
            }
        });
        fab.setLongClickable(true);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPressed = true;
                animators = new ArrayList<>();
                for(int i = 0; i < recyclerView.getChildCount(); i++) {
                    View view = recyclerView.getChildAt(i);
                    TextView tv = view.findViewById(R.id.text);
                    onLongPress(tv);
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(isLongPressed) {
                        for(int i = 0; i < recyclerView.getChildCount(); i++) {
                            View view = recyclerView.getChildAt(i);
                            TextView tv = view.findViewById(R.id.text);
                            onLongPressStopped(i);
                        }
                        isLongPressed = false;
                    }
                }
                return false;
            }
        });
    }

    private void initRecyclerView(ArrayList<String> mList) {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new TextRecyclerAdapter(mList, this, initTextSize);
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
        Gson gson = new Gson();
        sp.putString(MY_LIST, gson.toJson(mList));
        sp.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String res = sp.getString(MY_LIST, "");
        if(res.equals("")) {
            return;
        }
        String[]  vals = gson.fromJson(res, String[].class);
        mList = new ArrayList<>();
        for(String s : vals) {
            mList.add(s);
        }
        initRecyclerView(mList);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mList.size() - 1);
    }
    public void onLongPress(final TextView text) {
        final float start = initTextSize;
        float end = endTextSize;
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                text.setTextSize(animatedValue);
            }
        });
        animator.start();
        animators.add(animator);
    }
    public void onLongPressStopped(int index) {
        if(animators.get(index) != null) {
            animators.get(index).cancel();
        }
    }
}
