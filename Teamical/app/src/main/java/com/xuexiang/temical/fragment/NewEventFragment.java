package com.xuexiang.temical.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.textview.badge.Badge;
import com.xuexiang.xui.widget.textview.badge.BadgeView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.temical.R;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xutil.data.DateUtils;

import java.util.Calendar;

import butterknife.BindView;

@Page(name = "NewEvent")
public class NewEventFragment extends BaseFragment {
    @BindView(R.id.stv_detail)
    SuperTextView event_details;
    @BindView(R.id.super_team_event)
    SuperTextView team_event;
    @BindView(R.id.expandable_layout)
    ExpandableLayout mExpandableLayout;
    @BindView(R.id.stv_repeat)
    SuperTextView remind_view;
    @BindView(R.id.super_remind)
    SuperTextView alarm_remind;
    @BindView(R.id.stv_start_time)
    SuperTextView start_time;
    @BindView(R.id.stv_end_time)
    SuperTextView end_time;


    private Badge mBadge;
    private TimePickerView mStartTimePickerDialog;
    private TimePickerView mendTimePickerDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_event;
    }

    @Override
    protected void initViews() {
        //设置空字符串用于占位
//        event_details.setDefaultDrawable(null,null,null,0,0,0);
//        event_details.setleft

    }


    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        titleBar.setTitle("新建日程");
        titleBar.setHeight(200);

        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_close));
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_ok) {
            @Override
            public void performAction(View view) {

            }
        });
        return titleBar;
    }

    @Override
    protected void initListeners() {
        String[] mRepeatOption = ResUtils.getStringArray(R.array.repeat);
        String[] mRemindOption = ResUtils.getStringArray(R.array.remind);
        team_event.setOnSuperTextViewClickListener(superTextView -> superTextView.setSwitchIsChecked(!superTextView.getSwitchIsChecked(), false)).setSwitchCheckedChangeListener((buttonView, isChecked) -> mExpandableLayout.toggle());
        remind_view.setOnSuperTextViewClickListener(superTextView -> {
            OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {

                return false;
            }).setTitleText("提醒周期")
                    .setSelectOptions(0)
                    .build();
            pvOptions.setPicker(mRepeatOption);
            pvOptions.show();
        });
        alarm_remind.setOnSuperTextViewClickListener(superTextView -> {
            OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {

                return false;
            }).setTitleText("提醒周期")
                    .setSelectOptions(0)
                    .build();
            pvOptions.setPicker(mRemindOption);
            pvOptions.show();
        });
        start_time.setOnSuperTextViewClickListener(superTextView -> {
            if (mStartTimePickerDialog == null) {
                Calendar calendar = Calendar.getInstance();

                mStartTimePickerDialog = new TimePickerBuilder(getContext(), (date, v) -> XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get())))
                        .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                        .setType(TimePickerType.ALL)
                        .setTitleText("时间选择")
                        .isDialog(true)
                        .setOutSideCancelable(false)
                        .setDate(calendar)
                        .build();
            }
            mStartTimePickerDialog.show();
        });

        end_time.setOnSuperTextViewClickListener(superTextView -> {
            if (mStartTimePickerDialog == null) {
                Calendar calendar = Calendar.getInstance();

                mStartTimePickerDialog = new TimePickerBuilder(getContext(), (date, v) -> XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get())))
                        .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                        .setType(TimePickerType.ALL)
                        .setTitleText("时间选择")
                        .isDialog(true)
                        .setOutSideCancelable(false)
                        .setDate(calendar)
                        .build();
            }
            mStartTimePickerDialog.show();
        });
    }

}
