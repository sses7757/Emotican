package com.emotican.emotican;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.emotican.API.LocalAPI;
import com.emotican.customview.MainElement;
import com.emotican.util.CommonMethods;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.emotican.emotican.R.id.container;

public class MainActivity extends Activity {
    private static final int NUMBER_PAGES=3; //3个标签
    private static final int DURATION=300; //300ms动画时间

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    /**
     * 顺序：new,net,local
     */
    private List<ViewPagerFragment> pages = new ArrayList<ViewPagerFragment>(NUMBER_PAGES);
    private List<View> tabs = new ArrayList<View>(NUMBER_PAGES);
    private ImageView tabBar;

    private int pWidth; // 图片宽度
    private int section; // 图片偏移量
    private int currentPage;

    private RelativeLayout.LayoutParams lp_rel;

    private static void logD(String msg) {
        Log.d("MainActivity", msg);
    }
    private static void logE(String msg) {
        Log.e("MainActivity", msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalAPI.startQueryData();

        initData();
        initPages();
        initViewer();

        setRGBLightBlue();

        View floating = findViewById(R.id.floatingActionButton);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DrawActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int screenWidth = dm.widthPixels;
        pWidth = (int) getResources().getDimension(R.dimen.strip_width);
        section = (screenWidth - pWidth * 3) / 2 + pWidth;
    }

    /**
     * 初始化控件
     */
    private void initViewer() {
        viewPagerAdapter = new ViewPagerAdapter(pages);
        viewPager = (ViewPager) findViewById(container);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(viewPagerAdapter);
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), new LinearInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(DURATION);
        } catch (Exception e) {
            logE(e.toString());
        }
        lp_rel.setMargins(0, 0, 0, 0);
        tabBar.setLayoutParams(lp_rel);
    }

    /**
     * 初始化标签和页面
     */
    private void initPages() {
        Point size = new Point(0, 0);
        getWindowManager().getDefaultDisplay().getSize(size);
        pages.add(ViewPagerFragment.newInstance(0, size.x, this));
        pages.add(ViewPagerFragment.newInstance(1, size.x, this));
        pages.add(ViewPagerFragment.newInstance(2, size.x, this));

        tabs.add(findViewById(R.id.newEmoji));
        tabs.add(findViewById(R.id.netEmoji));
        tabs.add(findViewById(R.id.localEmoji));
        tabs.get(0).setOnClickListener(listener);
        tabs.get(1).setOnClickListener(listener);
        tabs.get(2).setOnClickListener(listener);

        lp_rel = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.strip_width),
                (int) getResources().getDimension(R.dimen.strip_height));
        tabBar = (ImageView) findViewById(R.id.cursor);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.newEmoji:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.netEmoji:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.localEmoji:
                    viewPager.setCurrentItem(2);
                    break;
            }
        }
    };

    public static class ViewPagerFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_WINDOW_WIDTH = "window_width";
        private MainActivity father;
        private View root;

        public ViewPagerFragment() {
        }

        @Override
        public View getView() {
            return  root;
        }

        public static ViewPagerFragment newInstance(int index, int windowWidth, MainActivity father)  {
            ViewPagerFragment fragment = new ViewPagerFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, index);
            args.putInt(ARG_WINDOW_WIDTH, windowWidth);
            fragment.setArguments(args);
            fragment.father = father;

            LayoutInflater inflater = father.getLayoutInflater();
            View root = fragment.onCreateView(inflater, null, null);
            fragment.root = root;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = null;
            switch ((int) this.getArguments().get(ARG_SECTION_NUMBER)) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_new, container, container != null);
                    //initNewPage(rootView, (int) this.getArguments().get(ARG_WINDOW_WIDTH), father);
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_net, container, container != null);
                    //initNetPage(rootView, (int) this.getArguments().get(ARG_WINDOW_WIDTH), father);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_local, container, container != null);
                    //initLocalPage(rootView, (int) this.getArguments().get(ARG_WINDOW_WIDTH), father);
                    break;
            }
            return rootView;
        }

        private void initNewPage(View view, int width, Activity father) {
            GridLayout grid = (GridLayout) view.findViewById(R.id.grid_new);
            Map<String, Bitmap> res = LocalAPI.getTemples();
            String[] keys = res.keySet().toArray(new String[0]);
            for (int i=1; i<=res.size(); ++i) {
                LinearLayout element = new MainElement(father, getContext(), new Point(i % 2, i / 2),
                        res.get(keys[i - 1]), CommonMethods.removeID(keys[i - 1]), width, MainElement.TYPE_NEW);
                grid.addView(element);
            }
        }
        private void initNetPage(View view, int width, Activity father) {
            GridLayout grid = (GridLayout) view.findViewById(R.id.grid_net);
            Map<String, Bitmap> res = LocalAPI.getNetworkRes();
            String[] keys = res.keySet().toArray(new String[0]);
            for (int i=1; i<=res.size(); ++i) {
                LinearLayout element = new MainElement(father, getContext(), new Point(i % 2, i / 2),
                        res.get(keys[i - 1]), CommonMethods.removeID(keys[i - 1]), width, MainElement.TYPE_NET);
                grid.addView(element);
            }
        }
        private void initLocalPage(View view, int width, Activity father) {
            GridLayout grid = (GridLayout) view.findViewById(R.id.grid_local);
            Map<String, Bitmap> res = LocalAPI.getLocalExist();
            String[] keys = res.keySet().toArray(new String[0]);
            for (int i=1; i<=res.size(); ++i) {
                LinearLayout element = new MainElement(father, getContext(), new Point(i % 2, i / 2),
                        res.get(keys[i - 1]), CommonMethods.removeID(keys[i - 1]), width, MainElement.TYPE_LOCAL);
                grid.addView(element);
            }
        }
    }

    private int[] rgb = new int[3];

    private void setRGBLightBlue() {
        int blue_int = ContextCompat.getColor(getApplicationContext(), R.color.colorLightBlue);
        int red = (blue_int & 0xff0000) >> 16;
        int green = (blue_int & 0x00ff00) >> 8;
        int blue = (blue_int & 0x0000ff);
        this.rgb = new int[]{red, green, blue};
    }

    public class ViewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
        private List<View> views;

        public ViewPagerAdapter(List<ViewPagerFragment> fragments) {
            this.views = new ArrayList<View>(fragments.size());
            for (int i=0; i<fragments.size(); ++i) {
                this.views.add(fragments.get(i).getView());
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position)); // 把被点击的图片放入缓存中
            return views.get(position); // 返回被点击图片对象
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                currentPage = viewPager.getCurrentItem();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            lp_rel.setMargins(section * position + (int) (positionOffset * section), 0, 0, 0);
            tabBar.setLayoutParams(lp_rel);

            ImageView pre;
            ImageView to;
            if (viewPager.getCurrentItem() == position && position < 2) { //1->0
                pre = (ImageView) tabs.get(position);
                to = (ImageView) tabs.get(position + 1);
                if (position == 0 && currentPage == 2) { //2->0
                    pre = (ImageView) tabs.get(0);
                    to = (ImageView) tabs.get(2);
                }
            }
            else if (viewPager.getCurrentItem() > position) { //0->1, 1->2
                pre = (ImageView) tabs.get(currentPage);
                to = (ImageView) tabs.get(viewPager.getCurrentItem());
                if (currentPage == 0 && viewPager.getCurrentItem() == 2) { //0->2
                    if (position == 0)
                        positionOffset /= 2;
                    else
                        positionOffset = 0.5f + positionOffset / 2;
                }
            }
            else {
                return;
            }
            logD("pos:"+position+"  cur:"+viewPager.getCurrentItem()+"  pix:"+positionOffset);
            int[] rgb_to = new int[]{(int) (rgb[0] * positionOffset), (int) (rgb[1] * positionOffset), (int) (rgb[2] * positionOffset)};
            int[] rgb_pre = new int[]{rgb[0] - rgb_to[0], rgb[1] - rgb_to[1], rgb[2] - rgb_to[2]};
            to.setColorFilter(Color.rgb(rgb_to[0], rgb_to[1], rgb_to[2]));
            pre.setColorFilter(Color.rgb(rgb_pre[0], rgb_pre[1], rgb_pre[2]));
        }

        @Override
        public void onPageSelected(int position) {
//            for (int i=0; i<NUMBER_PAGES; ++i) {
//                if (i != position)
//                    ((ImageView) tabs.get(i)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
//            }
//            ((ImageView) tabs.get(position)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorLightBlue));
        }
    }


    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 1500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, LinearInterpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }
}
