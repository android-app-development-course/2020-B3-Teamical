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

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.NotificationAdapter;
import com.xuexiang.temical.adapter.TeamCreateAdapter;
import com.xuexiang.temical.adapter.entity.Notification;
import com.xuexiang.temical.adapter.entity.TeamCreate;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
//import com.yuyakaido.android.cardstackview.CardStackListener;
//import com.yuyakaido.android.cardstackview.CardStackView;
//import com.yuyakaido.android.cardstackview.Direction;
//import com.yuyakaido.android.cardstackview.StackFrom;
//import com.yuyakaido.android.cardstackview.SwipeableMethod;

/**
 * @author xuexiang
 * @since 2019-10-30 00:19
 */
@Page(anim = CoreAnim.none)
public class NotificationFragment extends BaseFragment {

    @BindView(R.id.notification_recyclerView)
    RecyclerView notificationRecyclerView;

    private List<Notification> itemList = new ArrayList<>();
    private NotificationAdapter nAdapter;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        titleBar.setTitle("我的消息");
        titleBar.setHeight(200);
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_dustbin) {
            @Override
            public void performAction(View view) {
                showSimpleConfirmDialog();
//                XToastUtils.toast("清空消息");
            }
        });
        return titleBar;
//        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notifications;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initNotificationRecyclerView();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        initNotificationListeners();
    }

    private void initNotificationRecyclerView() {

    }

    private void initNotificationListeners(){
        notificationRecyclerView.setLayoutManager(new XLinearLayoutManager(notificationRecyclerView.getContext()));
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationRecyclerView.setAdapter(nAdapter = new NotificationAdapter());

        // 生成一些demo数据
        getDemoTeams();
        nAdapter.refresh(itemList);

        // 监听点击事件
        nAdapter.setOnItemClickListener((itemView, item, position) -> {
//            XToastUtils.toast(itemList.get(position).getTeamName() + item.getUserName());;
            showSingleChoiceDialog(item);
        });
    }

    private void showSingleChoiceDialog(Notification item) {
        new MaterialDialog.Builder(getContext())
                .title("操作")
                .items(R.array.router_choice_entry)
                .itemsCallbackSingleChoice(
                        0,
                        (dialog, itemView, which, text) -> {
//                            XToastUtils.toast(which + ": " + text);
                            // which是下标，从0开始，text是选项内容
                            switch (which){
                                case 0: // 同意
                                    item.setStatus("已同意");
                                    break;
                                case 1: // 同意
                                    item.setStatus("已拒绝");
                                    break;
                                case 2: // 同意
                                    item.setStatus("已忽略");
                                    break;
                                default:
                                    break;
                            }
                            nAdapter.refresh(itemList);
                            return true;
                        })
                .positiveText("确认")
                .negativeText("取消")
                .show();
    }

    private void showSimpleConfirmDialog() {
        new MaterialDialog.Builder(getContext())
                .content("是否确认清空所有消息?")
                .positiveText(R.string.lab_yes)
                .negativeText(R.string.lab_no)
                .onPositive((dialog, which) -> {
                    XToastUtils.success("清空成功");
                    itemList.clear();
                    nAdapter.refresh(itemList);
                })
                .show();
    }

    private void getDemoTeams(){
        itemList.add(new Notification("周莹", "华师研发", "待审核"));
        itemList.add(new Notification("枫亭", "柏木工程", "待审核"));
        itemList.add(new Notification("孔明", "华师研发", "待审核"));
        itemList.add(new Notification("心琳", "华师研发", "待审核"));
    }
}
