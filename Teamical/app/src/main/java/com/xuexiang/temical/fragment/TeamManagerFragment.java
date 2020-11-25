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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

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
import com.xuexiang.temical.DemoDataProvider;
import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.NewsCardViewListAdapter;
import com.xuexiang.temical.adapter.TeammateViewListAdapter;
import com.xuexiang.temical.adapter.entity.NewInfo;
import com.xuexiang.temical.adapter.entity.TeammateInfo;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.SettingUtils;
import com.xuexiang.temical.utils.Utils;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutQuad;


/**
 * 统计页面
 */
@Page(name = "团队成员管理")
public class TeamManagerFragment extends BaseFragment{
    @BindView(R.id.toolbar_teammate_view)
    Toolbar toolbar;

    @BindView(R.id.recycler_teammate_view)
    RecyclerView recyclerView;

    private List<TeammateInfo> itemList = new ArrayList<>();
    private TeammateViewListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_team_manager;
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

        initRecyclerView();
    }

    @Override
    protected void initListeners() {
        // 返回
        toolbar.setNavigationOnClickListener(v -> popToBack());
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new XLinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        recyclerView.setAdapter(mAdapter = new TeammateViewListAdapter());
        // 生成一些demo数据
        itemList = DemoDataProvider.getDemoTeammateInfos();
        mAdapter.refresh(itemList);
        // 监听点击事件
        mAdapter.setOnItemClickListener((itemView, item, position) -> {
            showSimpleConfirmDialog(position);
        });
    }

    /**
     * 简单的确认对话框
     */
    private void showSimpleConfirmDialog(int position) {
        new MaterialDialog.Builder(getContext())
                .content("是否确认删除" + itemList.get(position).getUsername() + "?")
                .positiveText(R.string.lab_yes)
                .negativeText(R.string.lab_no)
                .onPositive((dialog, which) -> {
                    XToastUtils.success("删除" + itemList.get(position).getUsername() + "成功");
                    itemList.remove(position);
                    mAdapter.refresh(itemList);
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

