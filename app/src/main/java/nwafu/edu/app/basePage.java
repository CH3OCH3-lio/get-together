package nwafu.edu.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class basePage extends AppCompatActivity {

    private ViewPager view_page;

    //滑动页面的数量
    private List<View> tabViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_page);
        tabViews = new ArrayList<>();
        //初始化数据
        initData();
        view_page = (ViewPager) findViewById(R.id.view_pager);
        view_page.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return tabViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(tabViews.get(position));
                //滑到最后一个页面，显示button
                if(position == tabViews.size()-1)
                {
                    Button button= (Button) tabViews.get(position).findViewById(R.id.button);
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent =new Intent(basePage.this,mainpage.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                return tabViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(tabViews.get(position));
            }
        });
    }
    private void initData() {
        LayoutInflater tabs = LayoutInflater.from(basePage.this);
        View tab1 = tabs.inflate(R.layout.tab_01, null);
        tab1.setBackgroundResource(R.mipmap.splash);
        View tab2 = tabs.inflate(R.layout.tab_01, null);
        tab2.setBackgroundResource(R.mipmap.ic_launcher);
        View tab3 = tabs.inflate(R.layout.tab_01, null);

        tab3.setBackgroundResource(R.mipmap.splash);

        //添加到列表里
        tabViews.add(tab1);
        tabViews.add(tab2);
        tabViews.add(tab3);
    }
}
