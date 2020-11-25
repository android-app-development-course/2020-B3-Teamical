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

import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.CardStackAdapter;
import com.xuexiang.temical.adapter.entity.Spot;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.shadow.ShadowButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @since 2019-10-30 00:19
 */
@Page(anim = CoreAnim.none)
public class TeamFragment extends BaseFragment implements CardStackListener {
    private List<Spot> createSpots() {
        List<Spot> spots = new ArrayList<>();
        spots.add(new Spot("测试团队1", "团队人数:3", "https://source.unsplash.com/Xq1ntWruZQI/600x800"));
        spots.add(new Spot("测试团队2", "团队人数:4", "https://source.unsplash.com/NYyCqdBOKwc/600x800"));
        spots.add(new Spot("测试团队3", "团队人数:10", "https://source.unsplash.com/buF62ewDLcQ/600x800"));
        return spots;
    }


    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    @BindView(R.id.card_stack_view)
    CardStackView cardStackView;
    @BindView(R.id.manage_member)
    ShadowButton manage_member;
    @BindView(R.id.leave_team)
    ShadowButton leave_team;
    @BindView(R.id.handel_application)
    ShadowButton handle_application;


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
        manager = new CardStackLayoutManager(this.getActivity(), this);
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.VERTICAL);
        manager.setCanScrollHorizontal(false);
        manager.setCanScrollVertical(true);
        adapter = new CardStackAdapter(this.getActivity(), createSpots());
        cardStackView = findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        manage_member.setOnClickListener(view -> openNewPage(TeamManagerFragment.class));
        handle_application.setOnClickListener(view -> openNewPage(TeamApplyMessageListFragment.class));
        leave_team.setOnClickListener(view -> {
            DialogLoader.getInstance().showConfirmDialog(
                    getContext(),
                    "确认退出？",
                    getString(R.string.lab_yes),
                    (dialog, which) -> {
                        XToastUtils.toast("退出成功");
                        dialog.dismiss();
                    },
                    getString(R.string.lab_no),
                    (dialog, which) -> {
                        XToastUtils.toast("取消退出");
                        dialog.dismiss();
                    }
            );
        });
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
