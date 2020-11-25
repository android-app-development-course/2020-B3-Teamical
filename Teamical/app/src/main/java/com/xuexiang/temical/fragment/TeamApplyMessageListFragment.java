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

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.temical.DemoDataProvider;
import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.TeamApplyMessageListAdapter;
import com.xuexiang.temical.adapter.entity.TeammateInfo;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.SettingUtils;
import com.xuexiang.temical.utils.Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 统计页面
 */
@Page(name = "团队申请记录")
public class TeamApplyMessageListFragment extends BaseFragment{
    @BindView(R.id.toolbar_teammate_view)
    Toolbar toolbar;

    @BindView(R.id.recycler_teammate_view)
    RecyclerView recyclerView;

    private List<TeammateInfo> applyItemList = new ArrayList<>();
    private TeamApplyMessageListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_team_apply_manager;
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
        recyclerView.setAdapter(mAdapter = new TeamApplyMessageListAdapter());
        // 可以用这个来获取到成员列表
//        itemList = DemoDataProvider.getDemoTeammateInfos();
        applyItemList = getDemoApplyMessageData();
        mAdapter.refresh(applyItemList);
        // 监听点击事件
//        mAdapter.setOnItemClickListener((itemView, item, position) -> {});
    }

    protected List<TeammateInfo> getDemoApplyMessageData(){
        List<TeammateInfo> list = new ArrayList<>();
        list.add(new TeammateInfo("Mike", "null"));
        list.add(new TeammateInfo("John", "null"));
        list.add(new TeammateInfo("Bob", "null"));
        list.add(new TeammateInfo("Curry", "null"));
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

