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

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.temical.DemoDataProvider;
import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.CardStackAdapter;
import com.xuexiang.temical.adapter.NewsCardViewListAdapter;
import com.xuexiang.temical.adapter.TeamCreateAdapter;
import com.xuexiang.temical.adapter.TeamJoinAdapter;
import com.xuexiang.temical.adapter.entity.CurrentUser;
import com.xuexiang.temical.adapter.entity.NewInfo;
import com.xuexiang.temical.adapter.entity.Spot;
import com.xuexiang.temical.adapter.entity.TeamCreate;
import com.xuexiang.temical.adapter.entity.TeamJoin;
import com.xuexiang.temical.adapter.entity.Teammate;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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

    @BindView(R.id.team_my_manager)
    TextView teamMyManagerView;

    @BindView(R.id.team_my_join)
    TextView teamMyJoinView;

    private List<TeamCreate> itemList = new ArrayList<>();
    private TeamCreateAdapter mAdapter;

    private List<Teammate> joinList = new ArrayList<>();
    private TeamJoinAdapter joinAdapter;

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
        initCardViewListerner();
        teamMyManagerView.setOnClickListener(view -> {
            getTeamFromServer();
        });
        teamMyJoinView.setOnClickListener(view -> {
            getJoinTeamFromServer();
        });
    }

    private void initCardViewListerner() {
        if (CurrentUser.getUserName().length() > 0) {
            cardview.setOnClickListener((itemView) -> {
                new MaterialDialog.Builder(getContext())
                        .iconRes(R.drawable.ic_team)
                        .title("新建团队")
                        .content("请输入您要创建的团队名称")
                        //                    .inputType(
                        //                            InputType.TYPE_CLASS_TEXT
                        //                                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        //                                    | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .input(
                                "请输入您要创建的团队名称",
                                "",
                                false,
                                ((dialog, input) -> XToastUtils.toast("团队创建成功"))
                        )
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> {
                            //                        XToastUtils.toast("你输入了:" + dialog.getInputEditText().getText().toString());
                            //                        itemList.add(new TeamCreate(dialog.getInputEditText().getText().toString()));
                            //                        mAdapter.refresh(itemList);
                            String teamName = dialog.getInputEditText().getText().toString();
                            // 新建一个团队
                            newATeam(teamName, CurrentUser.getPhoneNum());
                            getTeamFromServer();
                        })
                        .cancelable(false)
                        .show();
            });
        } else {
            cardview.setOnClickListener((itemView) -> {
                XToastUtils.toast("请您先登录");
                openNewPage(LoginByPasswordFragment.class);
            });
        }
    }

    private void initTeamCreateRecyclerView() {
        getTeamFromServer();
        getJoinTeamFromServer();
//        for (int i = 0; i < itemList.size(); i++) {
//            System.out.println("!!!!!服务器获取team了");
//            System.out.println(itemList.get(i).getTeamName());
//        }
//        System.out.println("itemList.size(): " + itemList.size());
    }

    private void initTeamJoinRecyclerView() {

    }

    private void initTeamCreateListeners() {
        teamCreateRecyclerView.setLayoutManager(new XLinearLayoutManager(teamCreateRecyclerView.getContext()));
        teamCreateRecyclerView.setItemAnimator(new DefaultItemAnimator());
        teamCreateRecyclerView.setAdapter(mAdapter = new TeamCreateAdapter());

        // 生成一些demo数据
//        getDemoTeams();
//        mAdapter.refresh(itemList);


        // 监听点击事件
        mAdapter.setOnItemClickListener((itemView, item, position) -> {
            Bundle params = new Bundle();
            params.putString("TeamName", item.getTeamName());
            params.putString("ManagerPN", item.getManagerPN());
            openNewPage(TeamManagerFragment.class, "key", params);
//            XToastUtils.toast(itemList.get(position).getTeamName());
        });


    }

    private void initTeamJoinListeners() {
        teamJoinRecyclerView.setLayoutManager(new XLinearLayoutManager(teamJoinRecyclerView.getContext()));
        teamJoinRecyclerView.setItemAnimator(new DefaultItemAnimator());
        teamJoinRecyclerView.setAdapter(joinAdapter = new TeamJoinAdapter());

        // 生成一些demo数据
//        getDemoJoinTeams();
//        joinAdapter.refresh(joinList);

        // 监听点击事件
        joinAdapter.setOnItemClickListener((itemView, item, position) -> {
//            XToastUtils.toast(joinList.get(position).getTeamName());
            Bundle params = new Bundle();
            params.putString("TeamName", item.getTeamName());
            params.putString("ManagerPN", item.getManagerPN());
            openNewPage(TeamManagerFragment.class, "key", params);
        });
    }

    private void getDemoTeams() {
        itemList.add(new TeamCreate("敏捷开发"));
        itemList.add(new TeamCreate("手机研发"));
        itemList.add(new TeamCreate("平板开发"));
    }

    private void getDemoJoinTeams() {
        joinList.add(new Teammate("华师地科兴趣小组", "5984844649", "15854699569", "Andy"));
    }

    private void getTeamFromServer() {
        BmobQuery<TeamCreate> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("ManagerPN", CurrentUser.getPhoneNum());
        categoryBmobQuery.findObjects(new FindListener<TeamCreate>() {
            @Override
            public void done(List<TeamCreate> objectLt, BmobException e) {
                if (e == null) {
//                    XToastUtils.toast("查询team成功：" + objectLt.get(0).getManagerPN());
                    itemList.clear();
                    // 将所有object装进去
                    itemList.addAll(objectLt);
                    mAdapter.refresh(itemList);
                } else {
                    Log.e("BMOB, 查询数据失败", e.toString());
                    XToastUtils.toast(e.getMessage());
                }
            }
        });
    }

    private void newATeam(String teamName, String managerPN) {
        TeamCreate t1 = new TeamCreate();
        t1.setTeamName(teamName);
        t1.setManagerPN(managerPN);

        t1.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    XToastUtils.toast("数据添加成功，返回obejectId为:" + objectId);
                } else {
                    Log.d("BMOB", "创建数据失败: " + e.getMessage());
                    XToastUtils.toast("创建数据失败: " + e.getMessage());
                }
            }
        });
    }

    private void getJoinTeamFromServer() {
        BmobQuery<Teammate> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("MatePN", CurrentUser.getPhoneNum());
        categoryBmobQuery.findObjects(new FindListener<Teammate>() {
            @Override
            public void done(List<Teammate> objectLt, BmobException e) {
                if (e == null) {
//                    XToastUtils.toast("查询team成功：" + objectLt.get(0).getManagerPN());
                    joinList.clear();
                    // 将所有object装进去
                    joinList.addAll(objectLt);
                    joinAdapter.refresh(joinList);
                } else {
                    Log.e("BMOB, 查询数据失败", e.toString());
                    XToastUtils.toast(e.getMessage());
                }
            }
        });
    }
}