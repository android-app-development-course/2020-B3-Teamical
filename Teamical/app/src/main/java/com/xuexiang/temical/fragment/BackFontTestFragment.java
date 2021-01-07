package com.xuexiang.temical.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.entity.Notification;
import com.xuexiang.temical.adapter.entity.TeamCreate;
import com.xuexiang.temical.adapter.entity.Teammate;
import com.xuexiang.temical.adapter.entity.User;
import com.xuexiang.temical.core.BaseFragment;
import com.xuexiang.temical.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

@Page(name = "BackTest")
public class BackFontTestFragment extends BaseFragment {
    @BindView(R.id.addAUser)
    TextView addAUserTextView;

    @BindView(R.id.dellAUser)
    TextView delAUserTextView;

    @BindView(R.id.updateAUser)
    TextView updateAUserTextView;

    @BindView(R.id.searchAUser)
    TextView searchAUserTextView;

    @BindView(R.id.addATeam)
    TextView addATeamView;

    @BindView(R.id.searchTeam)
    TextView searchTeamView;

    @BindView(R.id.addTeammate)
    TextView addTeammateView;

    @BindView(R.id.addAMessage)
    TextView addAMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_backfont_test;
    }

    @Override
    protected void initViews() {

    }


    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        titleBar.setTitle("后端测试");
        titleBar.setHeight(200);

//        titleBar.setLeftImageDrawable(ResUtils.getVectorDrawable(getContext(), R.drawable.ic_close));
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_ok) {
            @Override
            public void performAction(View view) {
                XToastUtils.toast("点击了: ok");
            }
        });
        return titleBar;
    }

    @Override
    protected void initListeners() {
        addAUserTextView.setOnClickListener(view -> {
            createUser();
//                Log.d("warning", "view" + view);
        });
        delAUserTextView.setOnClickListener(view -> {
            delUser();
        });
        updateAUserTextView.setOnClickListener(view -> {
            updateAUser();
        });
        searchAUserTextView.setOnClickListener(view -> {
            searchAUser();
        });
        addATeamView.setOnClickListener(view -> {
            addATeam();
        });
        searchTeamView.setOnClickListener(view -> {
            searchTeam();
        });
        addTeammateView.setOnClickListener(view -> {
            addTeammate();
        });
        addAMessage.setOnClickListener(view -> {
            addAMessage();
        });
    }

    private void createUser() {
        User u1 = new User();
        u1.setUserName("Kobe");
        u1.setPhoneNum("13954869545");
        u1.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    XToastUtils.toast("数据添加成功，返回obejectId为:" + objectId);
                } else {
                    Log.d("warning", "创建数据失败: " + e.getMessage());
                    XToastUtils.toast("创建数据失败: " + e.getMessage());
                }
            }
        });
    }

    private void delUser() {
        User u1 = new User();
        u1.setObjectId("3da36c2715");
        u1.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // u1.getUpdatedAt()返回null
                    XToastUtils.toast("删除成功:" + u1.getUpdatedAt());
                } else {
                    XToastUtils.toast("删除失败：" + e.getMessage());
                }
            }
        });
    }

    private void updateAUser() {
        User u1 = new User();
        u1.setPhoneNum("1111111111");
        u1.update("bc5f060f0c", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // u1.getUpdatedAt()返回修改后时间
                    XToastUtils.toast("更新成功:" + u1.getUpdatedAt());
                } else {
                    XToastUtils.toast("更新失败：" + e.getMessage());
                }
            }
        });
    }

    private void searchAUser(){
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject("8f6db54d8f", new QueryListener<User>() {
            @Override
            public void done(User object,BmobException e) {
                if(e==null){
                    XToastUtils.toast("查询成功");
                }else{
                    XToastUtils.toast("查询失败：" + e.getMessage());
                }
            }
        });
    }

    private void addATeam(){
        TeamCreate t1 = new TeamCreate();
        t1.setTeamName("华师心计研究所");
        t1.setManagerPN("15854699569");

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

    private void searchTeam(){
        BmobQuery<TeamCreate> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("ManagerPN", "15854699569");
        categoryBmobQuery.findObjects(new FindListener<TeamCreate>() {
            @Override
            public void done(List<TeamCreate> object, BmobException e) {
                if (e == null) {
                    XToastUtils.toast("查询成功：" + object.get(0).getManagerPN());
                } else {
                    Log.e("BMOB", e.toString());
                    XToastUtils.toast(e.getMessage());
                }
            }
        });
    }

    private void addTeammate(){
        Teammate tm = new Teammate();
        tm.setManagerPN("15854699569");
        tm.setTeamName("华师心计研究所");
        tm.setMateName("Lisa");
        tm.setMatePN("45646456456");
        tm.save(new SaveListener<String>() {
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

    private void addAMessage(){
        Notification nf = new Notification();
        nf.setTeamName("华师心计研究所");
        nf.setUserName("Lisa");
        nf.setApplicantPN("45646456456");
        nf.setCheckerPN("15854699569");
        nf.setStatus("待审核");
        nf.save(new SaveListener<String>() {
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
}
