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
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.xuexiang.temical.adapter.entity.NewInfo;
import com.xuexiang.temical.adapter.entity.TeammateInfo;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.HanziToPinyin;
import com.xuexiang.temical.utils.SettingUtils;
import com.xuexiang.temical.utils.Utils;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.temical.widget.SideBar;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutQuad;


/**
 * 统计页面
 */
@Page(name = "团队成员管理")
public class TeamManagerFragment extends BaseFragment implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {
    @BindView(R.id.toolbar_teammate_manage_view)
    Toolbar toolbar;
    @BindView(R.id.school_friend_sidrbar)
    SideBar mSideBar;
    @BindView(R.id.school_friend_dialog)
    TextView mDialog;
    @BindView(R.id.school_friend_member_search_input)
    EditText mSearchInput;
    @BindView(R.id.school_friend_member)
    ListView mListView;
    private TextView mFooterView;
    private int choose = -1;
    private List<TeammateInfo> itemList = new ArrayList<>();
    private ContactAdapter mAdapter;
    private ArrayList<Contact> datas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_team_manager;
    }

    private char lastFirstAlpha;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initViews() {
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);
        mFooterView = (TextView) View.inflate(this.getContext(), R.layout.item_list_contact_count, null);
        mListView.addFooterView(mFooterView);
        ArrayList<String> texts = new ArrayList<>();
        texts.add("____00");
        texts.add("张晓明aaa");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("bbb");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ccc");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");
        texts.add("ddd");

        parser(texts);

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
               showSimpleBottomSheetGrid();
            }
        });

    }

    private void showSimpleBottomSheetGrid() {
        final int SET_MANAGER = 0;
        final int REMOVE_MEMBER = 1;
        BottomSheet.BottomGridSheetBuilder builder = new BottomSheet.BottomGridSheetBuilder(getActivity());
        builder
                .addItem(R.drawable.ic_manager, "设为管理员", SET_MANAGER, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.ic_disable, "移出团队", REMOVE_MEMBER, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener((dialog, itemView) -> {
                    dialog.dismiss();
                    int tag = (int) itemView.getTag();
                    XToastUtils.toast("tag:" + tag + ", content:" + itemView.toString());
                }).build().show();


    }

    private void parser(ArrayList<String> texts) {
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
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }


    @Override
    protected void initListeners() {
        toolbar.setNavigationOnClickListener(v -> popToBack());
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
}

