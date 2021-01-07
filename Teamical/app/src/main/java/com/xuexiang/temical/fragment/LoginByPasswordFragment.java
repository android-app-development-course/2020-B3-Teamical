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

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xuexiang.rxutil2.rxjava.RxJavaUtils;
import com.xuexiang.temical.R;
import com.xuexiang.temical.activity.MainActivity;
import com.xuexiang.temical.adapter.entity.CurrentUser;
import com.xuexiang.temical.adapter.entity.TeamCreate;
import com.xuexiang.temical.adapter.entity.User;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.RandomUtils;
import com.xuexiang.temical.utils.SettingUtils;
import com.xuexiang.temical.utils.TokenUtils;
import com.xuexiang.temical.utils.Utils;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.LoadingDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.app.ActivityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(anim = CoreAnim.none)
public class LoginByPasswordFragment extends BaseFragment {

    @BindView(R.id.et_phone_number)
    MaterialEditText etPhoneNumber;
    @BindView(R.id.et_password)
    MaterialEditText etPassword;

    Context mcontext=this.getContext();
    // 弹出进度框
    LoadingDialog mLoadingDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private CountDownButtonHelper mCountDownHelper;

   @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_password;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle()
                .setImmersive(true);
        titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("");

        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_login_close));
        titleBar.setActionTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
//        titleBar.addAction(new TitleBar.TextAction(R.string.title_jump_login) {
//            @Override
//            public void performAction(View view) {
//                onLoginSuccess();
//            }
//        });
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

        // 对话框
        mLoadingDialog = WidgetUtils.getLoadingDialog(getContext())
                .setIconScale(0.4F)
                .setLoadingSpeed(8);
    }

    @SingleClick
    @OnClick({R.id.btn_login_password, R.id.tv_other_login, R.id.tv_register, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_password:
                doLoginJudge();
                break;
            case R.id.tv_other_login:
                XToastUtils.info("其他登录方式");
                openNewPage(LoginFragment.class);
                break;
            case R.id.tv_register:
                openNewPage(RegisterFragment.class);
//                XToastUtils.info("注册");
                break;
            case R.id.tv_user_protocol:
                XToastUtils.info("用户协议");
                break;
            case R.id.tv_privacy_protocol:
                XToastUtils.info("隐私政策");
                break;
            default:
                break;
        }
    }

    // 登录判断
    private void doLoginJudge(){
        String phoneNumber = etPhoneNumber.getEditValue();
        String password = etPassword.getEditValue();
        if (phoneNumber.isEmpty()) {
            XToastUtils.toast("请输入手机号");
        } else if (password.isEmpty()) {
            XToastUtils.toast("请输入密码");
        } else {
//            XToastUtils.toast(phoneNumber + " " +  password);
            BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
            categoryBmobQuery.addWhereEqualTo("PhoneNum", phoneNumber);
            categoryBmobQuery.addWhereEqualTo("Password", password);
            categoryBmobQuery.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> object, BmobException e) {
                    if (e == null) {
                        if (object.size() > 0) {
                            saveLoginState(object.get(0));
                            LoginSuccess();
                        } else {
                            XToastUtils.toast("登录失败");
                        }
                    } else {
                        Log.e("BMOB", e.toString());
                        XToastUtils.toast("登录失败");
                    }
                }
            });
        }
    }

    // 保存用户名和手机号
    private void saveLoginState(User u1){
       CurrentUser.setUserName(u1.getUserName());
       CurrentUser.setPhoneNum(u1.getPhoneNum());
       CurrentUser.setPassword(u1.getPassword());
    }

    /**
     * 登录成功的处理
     */
    private void LoginSuccess() {
        String token = RandomUtils.getRandomNumbersAndLetters(16);
        if (TokenUtils.handleLoginSuccess(token)) {
            popToBack();
            ActivityUtils.startActivity(MainActivity.class);
        }
    }

    @Override
    public void onDestroyView() {
        if (mCountDownHelper != null) {
            mCountDownHelper.recycle();
        }
        super.onDestroyView();
    }
}

