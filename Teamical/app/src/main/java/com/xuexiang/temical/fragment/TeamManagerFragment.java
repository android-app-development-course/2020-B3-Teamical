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

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
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
import com.xuexiang.temical.adapter.ContactAdapter;
import com.xuexiang.temical.adapter.NewsCardViewListAdapter;
import com.xuexiang.temical.adapter.TeammateViewListAdapter;
import com.xuexiang.temical.adapter.entity.Contact;
import com.xuexiang.temical.adapter.entity.CurrentUser;
import com.xuexiang.temical.adapter.entity.NewInfo;
import com.xuexiang.temical.adapter.entity.TeamCreate;
import com.xuexiang.temical.adapter.entity.Teammate;
import com.xuexiang.temical.adapter.entity.TeammateInfo;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.HanziToPinyin;
import com.xuexiang.temical.utils.SettingUtils;
import com.xuexiang.temical.utils.Utils;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.temical.widget.SideBar;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xqrcode.util.QRCodeProduceUtils;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutQuad;


/**
 * 统计页面
 */
@Page(name = "团队成员管理")
public class TeamManagerFragment extends BaseFragment implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {
//    @BindView(R.id.toolbar_teammate_manage_view)
//    Toolbar toolbar;
    @BindView(R.id.school_friend_sidrbar)
    SideBar mSideBar;
    @BindView(R.id.school_friend_dialog)
    TextView mDialog;
    @BindView(R.id.school_friend_member_search_input)
    EditText mSearchInput;
    @BindView(R.id.school_friend_member)
    ListView mListView;
    @BindView(R.id.iv_qrcode)
    AppCompatImageView qrcode;

    private TextView mFooterView;
    private int choose = -1;
    private List<Teammate> itemList = new ArrayList<>();
    private ContactAdapter mAdapter;
    private ArrayList<Contact> datas = new ArrayList<>();
    // 用来装组员的名字
    ArrayList<String> texts = new ArrayList<>();
    //当前访问的团队名字
    String teamName;
    // 当前访问的团队负责人的手机号
    String managerPN;

    @Override
    protected TitleBar initTitle() {
        Bundle arguments = getArguments().getBundle("key");
        teamName = arguments.getString("TeamName");
        managerPN = arguments.getString("ManagerPN");

        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        titleBar.setTitle(teamName);
        titleBar.setHeight(200);
        if (managerPN.equals(CurrentUser.getPhoneNum())) {
            titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_dustbin) {
                @Override
                public void performAction(View view) {
                    XToastUtils.toast("解散团队");
                }
            });
        }
        return titleBar;
//        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_team_manager;
    }

    private char lastFirstAlpha;

    @Override
    protected void initViews() {
        Bundle arguments = getArguments().getBundle("key");
        teamName = arguments.getString("TeamName");
        managerPN = arguments.getString("ManagerPN");
//        XToastUtils.toast("传来的: " + teamName + " " + managerPN);

        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);
        mFooterView = (TextView) View.inflate(this.getContext(), R.layout.item_list_contact_count, null);
        mListView.addFooterView(mFooterView);

//        getDemoData();
        getTeammateFromServer(teamName, managerPN);
        if (managerPN.equals(CurrentUser.getPhoneNum())) {
            // 只有队伍里至少有一人才需要监听
            if (itemList.size() > 0) {
                initListViewListener();
            }
        }

        QRCodeProduceUtils.Builder builder = XQRCode.newQRCodeBuilder(teamName+"|"+managerPN);
        qrcode.setImageBitmap(builder.build());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListViewListener() {
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        mDialog.setVisibility(View.INVISIBLE);
                        return false;
                    }
                    default: {
                        Contact contact = datas.get(mListView.getFirstVisiblePosition());

                        if (lastFirstAlpha != contact.getPinyin().charAt(0)) {
                            mDialog.setText(String.valueOf(contact.getPinyin().charAt(0)).toUpperCase());
                            mDialog.setVisibility(View.VISIBLE);
                            lastFirstAlpha = contact.getPinyin().charAt(0);
                        }
                        return false;
                    }

                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String opTeammateName = datas.get(i).getName();
                XToastUtils.toast("item: " + opTeammateName);

                showSimpleBottomSheetGrid(opTeammateName);
            }
        });
    }

    private void showSimpleBottomSheetGrid(String opTeammateName) {
        final int SET_MANAGER = 0;
        final int REMOVE_MEMBER = 1;
        BottomSheet.BottomGridSheetBuilder builder = new BottomSheet.BottomGridSheetBuilder(getActivity());
        builder
                .addItem(R.drawable.ic_manager, "设为管理员", SET_MANAGER, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.ic_disable, "移出团队", REMOVE_MEMBER, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener((dialog, itemView) -> {
                    dialog.dismiss();
                    int tag = (int) itemView.getTag();
                    switch (tag) {
                        case 1:
                            // 通过寻找itemList中等于opTeammateName名字的进行删除
                            deleteByName(opTeammateName);
                            break;
                        default:
                            XToastUtils.toast("暂时不支持多个管理员，请期待下一个版本");
//                            XToastUtils.toast("tag:" + tag + ", content:" + itemView.toString());
                            break;
                    }
                }).build().show();
    }

    private void parserTeammateName() {
        texts.clear();
        for (int i = 0; i < itemList.size(); i++) {
            texts.add(itemList.get(i).getMateName());
        }
        parser(texts);
    }

    private void parser(ArrayList<String> texts) {
        datas.clear();
        for (int i = 0; i < texts.size(); i++) {
            Contact data = new Contact();
            data.setName(texts.get(i));
            data.setUrl("test");
            data.setId(i);
            data.setPinyin(HanziToPinyin.getPinYin(data.getName()));
            datas.add(data);
        }
        mFooterView.setText(datas.size() + "位团队成员");
        mAdapter = new ContactAdapter(mListView, datas);
        mListView.setAdapter(mAdapter);
        mAdapter.refresh(datas);
    }

    @Override
    protected void initListeners() {
//        toolbar.setNavigationOnClickListener(v -> popToBack());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        } else if (s.contains("#")) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ArrayList<Contact> temp = new ArrayList<>(datas);
        for (Contact data : datas) {
            if (data.getName().contains(s) || data.getPinyin().contains(s)) {
            } else {
                temp.remove(data);
            }
        }
        if (mAdapter != null) {
            mAdapter.refresh(temp);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void getDemoData() {
        texts.add("Lisa");
        texts.add("张晓明");
        texts.add("bbb");
    }

    private void getTeammateFromServer(String teamName, String managerPN) {
        BmobQuery<Teammate> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("TeamName", teamName);
        categoryBmobQuery.addWhereEqualTo("ManagerPN", managerPN);
        categoryBmobQuery.findObjects(new FindListener<Teammate>() {
            @Override
            public void done(List<Teammate> objectLt, BmobException e) {
                if (e == null) {
//                    XToastUtils.toast("查询team成功：" + objectLt.get(0).getManagerPN());
                    itemList.clear();
                    // 将所有object装进去
                    itemList.addAll(objectLt);
                    // 进行后续操作
                    parserTeammateName();
                } else {
                    Log.e("BMOB, 查询数据失败", e.toString());
                    XToastUtils.toast("暂时没有成员");
                }
            }
        });
    }

    private void deleteByName(String delName) {
        for (int i = 0; i < itemList.size(); i++) {
            Teammate tm = itemList.get(i);
            if (tm.getMateName().equals(delName)) {
                tm.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            // u1.getUpdatedAt()返回null
                            XToastUtils.toast("删除成功");
                        } else {
                            XToastUtils.toast("删除失败：" + e.getMessage());
                        }
                    }
                });
                getTeammateFromServer(teamName, managerPN);
                break;
            }
        }
    }
}

