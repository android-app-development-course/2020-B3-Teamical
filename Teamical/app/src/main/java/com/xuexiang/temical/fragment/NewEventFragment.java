package com.xuexiang.temical.fragment;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.xuexiang.temical.R;
import com.xuexiang.temical.activity.MainActivity;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.temical.utils.service.alarm;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.ActivityUtils;
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
    //    @BindView(R.id.stv_repeat)
//    SuperTextView remind_view;
    @BindView(R.id.super_remind)
    SuperTextView alarm_remind;
    @BindView(R.id.stv_start_time)
    SuperTextView start_time;
    @BindView(R.id.stv_end_time)
    SuperTextView end_time;
    @BindView(R.id.stv_alarm_remind)
    SuperTextView Is_alarm_remind;
    String[] mRemindOption = ResUtils.getStringArray(R.array.remind);
    private TimePickerView mStartTimePickerDialog;
    private TimePickerView mEndTimePickerDialog;

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
                if(Is_alarm_remind.getSwitchIsChecked())
                {
                    long start=DateUtils.string2Date(start_time.getRightString(),DateUtils.yyyyMMddHHmmss.get()).getTime();
                    long advance_time=0;
                    String temp_time=alarm_remind.getRightString();
                    switch (temp_time)
                    {
                        case "开始时":
                            break;
                        case "提前5分钟":
                            advance_time=5*60*1000;
                            break;
                        case "提前10分钟":
                            advance_time=10*60*1000;
                            break;
                        case "提前15分钟":
                            advance_time=15*60*1000;
                            break;
                        case "提前30分钟":
                            advance_time=30*60*1000;
                            break;
                        case "提前1小时":
                            advance_time=60*60*1000;
                            break;
                        case "提前1天":
                            advance_time=24*60*60*1000;
                            break;
                        case "提前2天":
                            advance_time=2*24*60*60*1000;
                            break;
                        case "提前1周":
                            advance_time=7*24*60*60*1000;
                            break;
                    }
                    long remind_start_time=start-advance_time;
                    long remind_end_time=DateUtils.string2Date(end_time.getRightString(),DateUtils.yyyyMMddHHmmss.get()).getTime();
                    String title=event_details.getCenterEditValue();
                    Intent i =new Intent(NewEventFragment.this.getContext(), alarm.class);
                    i.putExtra("start",remind_start_time);
                    i.putExtra("end",remind_end_time);
                    i.putExtra("title",title);
                    XUtil.getContext().startService(i);
                }
                ActivityUtils.startActivity(MainActivity.class);
            }
        });
        return titleBar;
    }

    @Override
    protected void initListeners() {

        team_event.setOnSuperTextViewClickListener(superTextView -> superTextView.setSwitchIsChecked(
                !superTextView.getSwitchIsChecked(), false)).setSwitchCheckedChangeListener(
                (buttonView, isChecked) -> mExpandableLayout.toggle());
//        remind_view.setOnSuperTextViewClickListener(superTextView -> {
//            OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
//                remind_view.setRightString(mRepeatOption[options1]);
//                return false;
//            }).setTitleText("提醒周期")
//                    .setSelectOptions(0)
//                    .build();
//            pvOptions.setPicker(mRepeatOption);
//            pvOptions.show();
//        });
        alarm_remind.setOnSuperTextViewClickListener(superTextView -> {
            OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
                alarm_remind.setRightString(mRemindOption[options1]);
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

                mStartTimePickerDialog = new TimePickerBuilder(getContext(), (date, v) -> {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    start_time.setRightString(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                })
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
            if (mEndTimePickerDialog == null) {
                Calendar calendar = Calendar.getInstance();

                mEndTimePickerDialog = new TimePickerBuilder(getContext(), (date, v) ->{
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    end_time.setRightString(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                })
                        .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                        .setType(TimePickerType.ALL)
                        .setTitleText("时间选择")
                        .isDialog(true)
                        .setOutSideCancelable(false)
                        .setDate(calendar)
                        .build();

            }
            mEndTimePickerDialog.show();

        });
    }

}
