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

import android.graphics.Color;
import android.view.View;

import com.xuexiang.temical.R;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.TokenUtils;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.XUtil;

/**
 * @author xuexiang
 * @since 2019-10-15 22:38
 */
@Page(name = "新建日程")
public class NewEventFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.create_event;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        //titleBar.setBackgroundColor(Color.TRANSPARENT);
        titleBar.setTitle("新建日程");
        titleBar.setHeight(200);

        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_close));
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_ok) {
            @Override
            public void performAction(View view) {

            }
        });
        return titleBar;
    }


    @SingleClick
    @Override
    public void onClick(SuperTextView superTextView) {
        switch (superTextView.getId()) {
            case R.id.menu_common:
            case R.id.menu_privacy:
            case R.id.menu_push:
            case R.id.menu_helper:
                XToastUtils.toast(superTextView.getLeftString());
                break;
            case R.id.menu_change_account:
                XToastUtils.toast(superTextView.getCenterString());
                break;
            case R.id.menu_logout:
                DialogLoader.getInstance().showConfirmDialog(
                        getContext(),
                        getString(R.string.lab_logout_confirm),
                        getString(R.string.lab_yes),
                        (dialog, which) -> {
                            dialog.dismiss();
                            XUtil.getActivityLifecycleHelper().exit();
                            TokenUtils.handleLogoutSuccess();
                        },
                        getString(R.string.lab_no),
                        (dialog, which) -> dialog.dismiss()
                );
                break;
            default:
                break;
        }
    }
}
