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

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.NotificationAdapter;
import com.xuexiang.temical.adapter.entity.CurrentUser;
import com.xuexiang.temical.adapter.entity.Notification;
import com.xuexiang.temical.adapter.entity.TeamCreate;
import com.xuexiang.temical.adapter.entity.Teammate;
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
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
        XToastUtils.toast("CurrentUser: " + CurrentUser.getUserName());
        initNotificationRecyclerView();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        initNotificationListeners();
    }

    private void initNotificationRecyclerView() {

    }

    private void initNotificationListeners() {
        notificationRecyclerView.setLayoutManager(new XLinearLayoutManager(notificationRecyclerView.getContext()));
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationRecyclerView.setAdapter(nAdapter = new NotificationAdapter());

        // 生成一些demo数据
//        getDemoTeams();
        getMessageFromServer();


        // 监听点击事件
        nAdapter.setOnItemClickListener((itemView, item, position) -> {
//            XToastUtils.toast(itemList.get(position).getTeamName() + item.getUserName());
            if (item.getStatus().equals("待审核")) {
                showSingleChoiceDialog(item);
            }
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

                            switch (which) {
                                case 0: // 同意
                                    doAgree(item);
                                    break;
                                case 1: // 同意
                                    doReject(item);
                                    break;
                                case 2: // 同意
                                    doIgnore(item);
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
                    deleteAllMessage();
                })
                .show();
    }

    private void getDemoTeams() {
        itemList.add(new Notification("周莹", "华师研发", "待审核"));
        itemList.add(new Notification("枫亭", "柏木工程", "待审核"));
        itemList.add(new Notification("孔明", "华师研发", "待审核"));
        itemList.add(new Notification("心琳", "华师研发", "待审核"));
    }

    private void getMessageFromServer() {
        itemList.clear();
        BmobQuery<Notification> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("CheckerPN", CurrentUser.getPhoneNum());
        categoryBmobQuery.findObjects(new FindListener<Notification>() {
            @Override
            public void done(List<Notification> objectLt, BmobException e) {
                if (e == null) {
//                    XToastUtils.toast("查询成功：" + objectLt.get(0).getCheckerPN());
                    itemList.addAll(objectLt);
                    nAdapter.refresh(itemList);
                } else {
                    Log.e("BMOB", e.toString());
                    XToastUtils.toast("暂时没有消息");
                }
            }
        });
    }

    private void doAgree(Notification item){
        // 更新界面
        item.setStatus("已同意");
        item.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // u1.getUpdatedAt()返回修改后时间
                    XToastUtils.toast("更新成功:" + item.getUpdatedAt());
                    // 将此人添加到团队中
                    createATeammate(item);
                } else {
                    XToastUtils.toast("更新失败：" + e.getMessage());
                }
            }
        });
    }

    private void doReject(Notification item){
        // 更新界面
        item.setStatus("已拒绝");
        item.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // u1.getUpdatedAt()返回修改后时间
                    XToastUtils.toast("更新成功:" + item.getUpdatedAt());
                } else {
                    XToastUtils.toast("更新失败：" + e.getMessage());
                }
            }
        });
    }

    private void doIgnore(Notification item){
        // 更新界面
        item.setStatus("已忽略");
        item.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // u1.getUpdatedAt()返回修改后时间
                    XToastUtils.toast("更新成功:" + item.getUpdatedAt());
                } else {
                    XToastUtils.toast("更新失败：" + e.getMessage());
                }
            }
        });
    }

    private void createATeammate(Notification item){
        Teammate tm = new Teammate();
        tm.setMateName(item.getUserName());
        tm.setManagerPN(CurrentUser.getPhoneNum());
//        tm.setManagerPN(item.getCheckerPN());
        tm.setTeamName(item.getTeamName());
        tm.setMatePN(item.getApplicantPN());
        tm.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
//                    XToastUtils.toast("数据添加成功，返回obejectId为:" + objectId);
                    XToastUtils.toast("该成员通过审核加入团队");
                } else {
                    Log.d("BMOB", "创建数据失败: " + e.getMessage());
                    XToastUtils.toast("创建数据失败: " + e.getMessage());
                }
            }
        });
    }

    private void deleteAllMessage(){
        List<BmobObject> delMessageLt = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++){
            Notification nf = new Notification();
            nf.setObjectId(itemList.get(i).getObjectId());
            delMessageLt.add(nf);
        }

        // 批量删除
        new BmobBatch().deleteBatch(delMessageLt).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> results, BmobException e) {
                if (e == null) {
//                    for (int i = 0; i < results.size(); i++) {
//                        BatchResult result = results.get(i);
//                        BmobException ex = result.getError();
//                        if (ex == null) {
//                           XToastUtils.toast("第" + i + "个数据批量删除成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
//                        } else {
//                            XToastUtils.toast("第" + i + "个数据批量删除失败：" + ex.getMessage() + "," + ex.getErrorCode());
//                        }
//                    }
                    itemList.clear();
                    nAdapter.refresh(itemList);
                } else {
                    XToastUtils.toast("失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
