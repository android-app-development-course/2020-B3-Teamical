/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.xuexiang.temical.R;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.SettingUtils;
import com.xuexiang.temical.utils.Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutQuad;


/**
 * 统计页面
 */
@Page(name = "用户数据统计")
public class StatisticsFragment extends BaseFragment implements OnChartValueSelectedListener{
    @BindView(R.id.toolbar_recycler_view)
    Toolbar toolbar;

    @BindView(R.id.chart1)
    PieChart pieChart;

    @BindView(R.id.chart2)
    RadarChart radarChart;

    protected final String[] parties = new String[]{
            "工作", "学习", "娱乐", "健身",
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_statistics;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");

        titleBar.setActionTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
        return titleBar;
    }

    @Override
    protected void initViews() {
        //隐私政策弹窗
        if (!SettingUtils.isAgreePrivacy()) {
            Utils.showPrivacyDialog(getContext(), (dialog, which) -> {
                dialog.dismiss();
                SettingUtils.setIsAgreePrivacy(true);
            });
        }

        // 饼图
        initPieChartStyle();
        initPieChartLabel();
        setPieChartData(4, 10);
        pieChart.animateY(1400, EaseInOutQuad);
        pieChart.setOnChartValueSelectedListener(this);

        // 雷达图
        initRadarChartStyle();
        initRadarChartLabel();
        setRadarChartData(5, 80);

        // 设置雷达图显示的动画
        radarChart.animateXY(1400, 1400);
    }

    /**
     * 初始化图表的样式
     */
    protected void initRadarChartStyle() {
        // 设置雷达图的背景颜色
        radarChart.setBackgroundColor(Color.rgb(60, 65, 82));
        // 禁止图表旋转
        radarChart.setRotationEnabled(false);

        //设置雷达图网格的样式
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);

        // 设置标识雷达图上各点的数字控件
        MarkerView mv = new RadarMarkerView(getContext(), R.layout.marker_view_radar);
        mv.setChartView(radarChart);
        radarChart.setMarker(mv);

        initXYAxisStyle();
    }

    private void initXYAxisStyle() {
        //设置X轴（雷达图的项目点）的样式
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
//        xAxis.setValueFormatter(new ValueFormatter() {
//            private final String[] mActivities = new String[]{"Burger", "Steak", "Salad", "Pasta", "Pizza"};
//
//            public String getFormattedValue(float value) {
//                return mActivities[(int) value % mActivities.length];
//            }
//        });
        xAxis.setTextColor(Color.WHITE);

        //设置Y轴（雷达图的分值）的样式
        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        //最小分值
        yAxis.setAxisMinimum(0f);
        //最大分值
        yAxis.setAxisMaximum(80f);
        //是否画出分值
        yAxis.setDrawLabels(false);
    }

    /**
     * 初始化图表的 标题 样式
     */
    protected void initRadarChartLabel() {
        //设置图表数据 标题 的样式
        Legend l = radarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
    }

    /**
     * 设置图表数据
     *
     * @param count 一组数据的数量
     * @param range
     */
    protected void setRadarChartData(int count, float range) {
        float min = 20;

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();
        //雷达图的数据一般都有最大值，数据在一定范围内
        for (int i = 0; i < count; i++) {
            float val1 = (float) (Math.random() * range) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * range) + min;
            entries2.add(new RadarEntry(val2));
        }

        //设置两组数据的表现样式
        RadarDataSet set1 = new RadarDataSet(entries1, "上星期");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "本星期");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        //最终将两组数据填充进图表中
        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        radarChart.setData(data);
        radarChart.invalidate();
    }

    private void showBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .addItem(getResources().getString(R.string.chart_toggle_values))
                .addItem(getResources().getString(R.string.chart_toggle_x_values))
                .addItem(getResources().getString(R.string.chart_toggle_y_values))
                .addItem(getResources().getString(R.string.chart_animate_x))
                .addItem(getResources().getString(R.string.chart_animate_y))
                .addItem(getResources().getString(R.string.chart_animate_xy))
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            for (IDataSet<?> set : radarChart.getData().getDataSets()) {
                                set.setDrawValues(!set.isDrawValuesEnabled());
                            }
                            radarChart.invalidate();
                            break;
                        case 1:
                            radarChart.getXAxis().setEnabled(!radarChart.getXAxis().isEnabled());
                            radarChart.invalidate();
                            break;
                        case 2:
                            radarChart.getYAxis().setEnabled(!radarChart.getYAxis().isEnabled());
                            radarChart.invalidate();
                            break;
                        case 3:
                            radarChart.animateX(1400);
                            break;
                        case 4:
                            radarChart.animateY(1400);
                            break;
                        case 5:
                            radarChart.animateXY(1400, 1400);
                            break;
                        default:
                            break;
                    }
                })
                .build()
                .show();
    }

    /**
     * 设置图表数据
     *
     * @param count 柱状图中柱的数量
     * @param range
     */
    protected void setPieChartData(int count, float range) {
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            //设置数据源
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5), parties[i % parties.length], getResources().getDrawable(R.drawable.ic_star_green)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Users Results");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        List<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    /**
     * 初始化图表的样式
     */
    protected void initPieChartStyle() {
        //使用百分百显示
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        //设置拖拽的阻尼，0为立即停止
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        //设置图标中心文字
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawCenterText(true);
        //设置图标中心空白，空心
        pieChart.setDrawHoleEnabled(true);
        //设置空心圆的弧度百分比，最大100
        pieChart.setHoleRadius(58f);
        pieChart.setHoleColor(Color.WHITE);
        //设置透明弧的样式
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setTransparentCircleRadius(61f);

        //设置可以旋转
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
    }

    /**
     * 初始化图表的 标题
     */
    protected void initPieChartLabel() {
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
    }

    @Override
    protected void initListeners() {
        // 返回
        toolbar.setNavigationOnClickListener(v -> popToBack());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    /**
     * 生成饼图中间的文字
     *
     * @return
     */
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("时间统计");
//        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 3, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
}

