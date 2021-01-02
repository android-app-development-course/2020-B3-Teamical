/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.temical.fragment;

import android.view.View;
import android.widget.TextView;

import com.xuexiang.rxutil2.rxjava.RxJavaUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.xuexiang.temical.adapter.entity.NewInfo;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.temical.DemoDataProvider;
import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.NewsCardViewListAdapter;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.Utils;
import com.xuexiang.xui.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019-06-28 12:40
 */
@Page(name = "calendar")
public class ComplexCalendarFragment extends BaseFragment implements CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener {

    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;

    // 弹出进度框
    LoadingDialog mLoadingDialog;

//    @BindView(R.id.fab_recycler_view)
//    FloatingActionButton fab;

    private int mYear;
    private List<NewInfo> itemList = new ArrayList<>();
    private NewsCardViewListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complex_calendar;
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initViews() {
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        mCalendarView.setFixMode();

        initCalendarView();

        initRecyclerView();

        // 对话框
        mLoadingDialog = WidgetUtils.getLoadingDialog(getContext())
                .setIconScale(0.4F)
                .setLoadingSpeed(8);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        // 生成一些demo数据
        itemList = DemoDataProvider.getDemoNewInfos();
        mAdapter.refresh(itemList);
        // 监听点击事件
        mAdapter.setOnItemClickListener((itemView, item, position) -> Utils.goWeb(getContext(), item.getDetailUrl()));
    }

    @Override
    protected void initListeners() {
//        fab.setOnClickListener(v -> {XToastUtils.toast("添加日程");});
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
    }

    private void initCalendarView() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "记").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "记"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "假").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "假"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "记"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "假").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "假"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "多").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "多"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @SingleClick
    @OnClick({R.id.tv_month_day, R.id.fl_current})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_month_day:
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
                break;
            case R.id.fl_current:
                mCalendarView.scrollToCurrent();
                break;
            default:
                break;
        }
    }

    // 为浮动按钮绑定事件
    @SingleClick
    @OnClick({R.id.fab_quick_add, R.id.fab_simple_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_quick_add:
                quickAdd();
                break;
            case R.id.fab_simple_add:
                simpleAdd();
                break;
            default:
                break;
        }
        mFloatingActionMenu.toggle(false);
    }

    /**
     * 快速添加
     */
    protected void quickAdd() {
//        XToastUtils.toast("快速添加日程");
        mLoadingDialog.show();
        RxJavaUtils.delay(2, aLong -> {
            mLoadingDialog.dismiss();
            XToastUtils.success("添加成功");
        });

        // 添加一个示例进来
        itemList.add(new NewInfo("个人日程", "青阳公司场品发布会")
                .setSummary("会议初始身份证件，并且全程手机保持静音。")
                .setPraise("20:00 p.m. 20 Nov")
                .setComment("华融大厦五楼会议大厅"));
        mAdapter.refresh(itemList);
    }

    /**
     * 普通添加
     */
    protected void simpleAdd() {
        XToastUtils.toast("普通添加日程");
        openNewPage(NewEventFragment.class);
    }
}
