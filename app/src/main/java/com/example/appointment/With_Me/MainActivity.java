package com.example.appointment.With_Me;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appointment.HttpUtil;
import com.example.appointment.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Button navButton;

    private ImageView bingPicImg;

    private TextView motto_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //左上方导航按钮打开滑动侧边栏
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //设置侧边栏点击事件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_with_me);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawers();
                return true;
            }
        });

        //添加必应每日一图
        bingPicImg = (ImageView) findViewById(R.id.pic_motto);
        //获取本地缓存实例
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString("bing_pic", null);
        if (HttpUtil.isNetworkAvailable(MainActivity.this)) {
            loadBingPic(); //联网时从网上请求
        } else {
            Glide.with(this).load(bingPic).into(bingPicImg);
            loadBingPic();// 未联网时从缓存中直接加载
        }

        //添加一言
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        motto_text = (TextView) findViewById(R.id.motto);
        String mottoGetSharedPreferences = preferences.getString("motto", null); //判断缓存
        if (mottoGetSharedPreferences != null) {
            motto_text.setText( mottoGetSharedPreferences );
        } else {
            motto_text.setText("不做无为之事，何以遣有涯之生。" + "\n" + "There is a long life to do without doing nothing.");
        }


        //修改一言
        motto_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder changeMotto = new AlertDialog.Builder(MainActivity.this);
                changeMotto.setTitle("一言");
                final EditText motto = new EditText(MainActivity.this);
                motto.setHint("  编辑您的一言...");
                motto.setHintTextColor(Color.parseColor("#D3D3D3"));
                motto.setBackground(null);
                changeMotto.setView(motto);
                changeMotto.setPositiveButton("存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        motto_text.setText("“ " + motto.getText() + " ”");
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                        editor.putString("motto", motto_text.getText().toString());
                        editor.apply();
                    }
                });
                changeMotto.setNegativeButton("退", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                changeMotto.show();
                return false;
            }
        });
    }


    //加载Bing每日一图
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBingPic = response.body().string();
                //添加到缓存
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putString("bing_pic", responseBingPic);
                editor.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(responseBingPic).into(bingPicImg);
                    }
                });
            }
        });
    }




}
