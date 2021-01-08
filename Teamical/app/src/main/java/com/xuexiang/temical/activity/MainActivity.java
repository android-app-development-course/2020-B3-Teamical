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

package com.xuexiang.temical.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.entity.CurrentUser;
import com.xuexiang.temical.adapter.entity.User;
import com.xuexiang.temical.core.BaseActivity;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.fragment.AboutFragment;
import com.xuexiang.temical.fragment.BackFontTestFragment;
import com.xuexiang.temical.fragment.ComplexCalendarFragment;
import com.xuexiang.temical.fragment.LoginByPasswordFragment;
import com.xuexiang.temical.fragment.NewEventFragment;
import com.xuexiang.temical.fragment.NotificationFragment;
import com.xuexiang.temical.fragment.StatisticsFragment;
import com.xuexiang.temical.fragment.TeamApplyMessageListFragment;
import com.xuexiang.temical.fragment.TeamManagerFragment;
import com.xuexiang.temical.fragment.profile.ProfileFragment;
import com.xuexiang.temical.fragment.TeamFragment;
import com.xuexiang.temical.utils.Utils;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.guidview.GuideCaseQueue;
import com.xuexiang.xui.widget.guidview.GuideCaseView;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.display.Colors;

import butterknife.BindView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener, ClickUtils.OnClick2ExitListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    /**
     * 底部导航栏
     */
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    /**
     * 侧边栏
     */
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private String[] mTitles;
    static final int REQUEST_CODE = 111;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 数据库服务
        Bmob.initialize(this, "4a6d691cfdb623ee87f8c99605ea55ad");

        initViews();
        initListeners();
    }

    Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
//        XToastUtils.toast("点击了:" + item.getTitle());
        switch (item.getItemId()) {
            case R.id.action_scan:
                //点击设置
                //XToastUtils.toast("点击了: 扫描");
//                openNewPage(BackFontTestFragment.class);
                XQRCode.startScan(this, REQUEST_CODE);
                break;
            case R.id.action_notifications:
                openNewPage(NotificationFragment.class);
//                XToastUtils.toast("点击了: 通知");
            default:
                break;
        }
        return false;
    };
    @Permission(CAMERA)
    @IOThread(ThreadType.Single)
    private void startScan() {

        XQRCode.startScan(this, REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //处理扫描结果（在界面上显示）
            handleScanResult(data);
        }
    }

    private void handleScanResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_SUCCESS) {
                    String result = bundle.getString(XQRCode.RESULT_DATA);
                    XToastUtils.toast("解析结果:" + result, Toast.LENGTH_LONG);
                } else if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_FAILED) {
                    XToastUtils.toast("解析二维码失败", Toast.LENGTH_LONG);
                }
            }
        }
    }
//    private void doSearchATeam() {
//        if (CurrentUser.getUserName().length() > 0) {
//            new MaterialDialog.Builder(this)
//                    .iconRes(R.drawable.ic_team)
//                    .title("申请加入加入团队")
//                    .content("请依次输入您要加入的团队名称,团队负责人手机号,中间用-分开")
//                    //                    .inputType(
//                    //                            InputType.TYPE_CLASS_TEXT
//                    //                                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
//                    //                                    | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
//                    .input(
//                            "团队名称-团队负责人手机号",
//                            "",
//                            false,
//                            ((dialog, input) -> Log.d("add a team", "申请消息"))
//                    )
//                    .positiveText("确认")
//                    .negativeText("取消")
//                    .onPositive((dialog, which) -> {
//                        //                        XToastUtils.toast("你输入了:" + dialog.getInputEditText().getText().toString());
//                        //                        itemList.add(new TeamCreate(dialog.getInputEditText().getText().toString()));
//                        //                        mAdapter.refresh(itemList);
//                        String input = dialog.getInputEditText().getText().toString();
//                        doSubmitApply(input);
//                    })
//                    .cancelable(false)
//                    .show();
//        } else {
//            XToastUtils.toast("请您先登录");
//            openNewPage(LoginByPasswordFragment.class);
//        }
//    }

//    private void doSubmitApply(String input) {
//        try {
//            String[] temp = input.split("-");
//            String teamName = temp[0];
//            String managerPN = temp[1];
//            XToastUtils.toast("a: " + teamName + "*" + managerPN);
//            if (teamName.length() <= 0 || managerPN.length() <= 0) {
////                XToastUtils.toast("请您输入正确的格式.");
//                return;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
////            XToastUtils.toast("请您输入正确的格式");
//        }
//    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    private void initViews() {
        mTitles = ResUtils.getStringArray(R.array.home_titles);
        toolbar.setTitle(mTitles[0]);
        toolbar.inflateMenu(R.menu.menu_main);
//        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        initHeader();


        //主页内容填充
        BaseFragment[] fragments = new BaseFragment[]{
                new ComplexCalendarFragment(),
                new TeamFragment(),
                new ProfileFragment()
        };
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(mTitles.length - 1);
        viewPager.setAdapter(adapter);
    }

    private void initHeader() {
        navView.setItemIconTintList(null);
        View headerView = navView.getHeaderView(0);
        LinearLayout navHeader = headerView.findViewById(R.id.nav_header);
        RadiusImageView ivAvatar = headerView.findViewById(R.id.iv_avatar);
        TextView tvAvatar = headerView.findViewById(R.id.tv_avatar);
        if (Utils.isColorDark(ThemeUtils.resolveColor(this, R.attr.colorAccent))) {
            tvAvatar.setTextColor(Colors.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_white));
            }
        } else {
            tvAvatar.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_title_text));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_gray_3));
            }
        }

        // TODO: 2019-10-09 初始化数据
        ivAvatar.setImageResource(R.drawable.ic_default_head);
        tvAvatar.setText("未登录");
        navHeader.setOnClickListener(this);
    }

    protected void initListeners() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //侧边栏点击事件
        navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.isCheckable()) {
                drawerLayout.closeDrawers();
                return handleNavigationItemSelected(menuItem);
            } else {
                switch (menuItem.getItemId()) {
                    case R.id.nav_about:
                        openNewPage(AboutFragment.class);
                        break;
                    case R.id.nav_statistics:
                        openNewPage(StatisticsFragment.class);
                        break;
                    case R.id.nav_notifications:
                        // Todo: 拿来试验试验,到时记得删掉
                        openNewPage(TeamManagerFragment.class);
                        break;
                    default:
                        XToastUtils.toast("点击了:" + menuItem.getTitle());
                        break;
                }
            }
            return true;
        });

        //主页事件监听
        viewPager.addOnPageChangeListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * 处理侧边栏点击事件
     *
     * @param menuItem
     * @return
     */
    private boolean handleNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            toolbar.setTitle(menuItem.getTitle());
            viewPager.setCurrentItem(index, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            default:
                break;
        }
        return false;
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                ActivityUtils.startActivity(LoginActivity.class);
                break;
            default:
                break;
        }
    }

    //=============ViewPager===================//

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        MenuItem item = bottomNavigation.getMenu().getItem(position);
        toolbar.setTitle(item.getTitle());
        item.setChecked(true);

        updateSideNavStatus(item);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    //================Navigation================//

    /**
     * 底部导航栏点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            toolbar.setTitle(menuItem.getTitle());
            viewPager.setCurrentItem(index, false);

            updateSideNavStatus(menuItem);
            return true;
        }
        return false;
    }

    /**
     * 更新侧边栏菜单选中状态
     *
     * @param menuItem
     */
    private void updateSideNavStatus(MenuItem menuItem) {
        MenuItem side = navView.getMenu().findItem(menuItem.getItemId());
        if (side != null) {
            side.setChecked(true);
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this);
        }
        return true;
    }

    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    @Override
    public void onExit() {
        XUtil.exitApp();
    }


}
