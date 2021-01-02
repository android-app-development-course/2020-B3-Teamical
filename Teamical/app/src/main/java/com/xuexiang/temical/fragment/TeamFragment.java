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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.temical.DemoDataProvider;
import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.CardStackAdapter;
import com.xuexiang.temical.adapter.NewsCardViewListAdapter;
import com.xuexiang.temical.adapter.TeamCreateAdapter;
import com.xuexiang.temical.adapter.TeamJoinAdapter;
import com.xuexiang.temical.adapter.entity.NewInfo;
import com.xuexiang.temical.adapter.entity.Spot;
import com.xuexiang.temical.adapter.entity.TeamCreate;
import com.xuexiang.temical.adapter.entity.TeamJoin;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.shadow.ShadowButton;
//import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
//import com.yuyakaido.android.cardstackview.CardStackListener;
//import com.yuyakaido.android.cardstackview.CardStackView;
//import com.yuyakaido.android.cardstackview.Direction;
//import com.yuyakaido.android.cardstackview.StackFrom;
//import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-10-30 00:19
 */
@Page(anim = CoreAnim.none)
public class TeamFragment extends BaseFragment {

    @BindView(R.id.team_create_recyclerView)
    RecyclerView teamCreateRecyclerView;

    @BindView(R.id.team_join_recyclerView)
    RecyclerView teamJoinRecyclerView;

    @BindView(R.id.new_a_team)
    CardView cardview;

    private List<TeamCreate> itemList = new ArrayList<>();
    private TeamCreateAdapter mAdapter;

    private List<TeamJoin> joinList = new ArrayList<>();
    private TeamJoinAdapter  joinAdapter;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_team;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initTeamCreateRecyclerView();
        initTeamJoinRecyclerView();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        initTeamCreateListeners();
        initTeamJoinListeners();
        cardview.setOnClickListener((itemView)->{
            itemList.add(new TeamCreate("ipad研发"));
            mAdapter.refresh(itemList);
            XToastUtils.toast("你又想新建团队?别太累了");
        });
    }

    private void initTeamCreateRecyclerView() {

    }

    private void initTeamJoinRecyclerView() {

    }

    private void initTeamCreateListeners(){
        teamCreateRecyclerView.setLayoutManager(new XLinearLayoutManager(teamCreateRecyclerView.getContext()));
        teamCreateRecyclerView.setItemAnimator(new DefaultItemAnimator());
        teamCreateRecyclerView.setAdapter(mAdapter = new TeamCreateAdapter());

        // 生成一些demo数据
        getDemoTeams();
        mAdapter.refresh(itemList);

        // 监听点击事件
        mAdapter.setOnItemClickListener((itemView, item, position) -> {
            XToastUtils.toast(itemList.get(position).getTeamName());;
        });
    }

    private void initTeamJoinListeners() {
        teamJoinRecyclerView.setLayoutManager(new XLinearLayoutManager(teamJoinRecyclerView.getContext()));
        teamJoinRecyclerView.setItemAnimator(new DefaultItemAnimator());
        teamJoinRecyclerView.setAdapter(joinAdapter = new TeamJoinAdapter());

        // 生成一些demo数据
        getDemoJoinTeams();
        joinAdapter.refresh(joinList);

        // 监听点击事件
        joinAdapter.setOnItemClickListener((itemView, item, position) -> {
            XToastUtils.toast(joinList.get(position).getTeamName());;
        });
    }

    private void getDemoTeams(){
        itemList.add(new TeamCreate("敏捷开发"));
        itemList.add(new TeamCreate("甘兰开发"));
        itemList.add(new TeamCreate("瀑布开发"));
        itemList.add(new TeamCreate("手机研发"));
        itemList.add(new TeamCreate("平板开发"));
    }

    private void getDemoJoinTeams(){
        joinList.add(new TeamJoin("石桥工程"));
        joinList.add(new TeamJoin("校园建设工程"));
        joinList.add(new TeamJoin("冰箱嵌入式开发"));
        joinList.add(new TeamJoin("手机研发"));
    }
}
