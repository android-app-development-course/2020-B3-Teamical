package com.xuexiang.temical.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.xuexiang.temical.R;
import com.xuexiang.temical.adapter.entity.TeamCreate;
import com.xuexiang.temical.adapter.entity.TeamJoin;
import com.xuexiang.temical.fragment.components.refresh.diffutil.DiffUtilCallback;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.common.logger.Logger;

import java.util.List;

public class TeamJoinAdapter extends BaseRecyclerAdapter<TeamJoin> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_team_join_list_item;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, TeamJoin model) {
        if (model != null) {
//            holder.text(R.id.tv_user_name, model.getUserName());
//            holder.text(R.id.tv_tag, model.getUsername());
            holder.text(R.id.team_join_name, model.getTeamName());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (CollectionUtils.isEmpty(payloads)) {
            Logger.e("正在进行全量刷新:" + position);
            onBindViewHolder(holder, position);
            return;
        }
        // payloads为非空的情况，进行局部刷新

        //取出我们在getChangePayload（）方法返回的bundle
        Bundle payload = WidgetUtils.getChangePayload(payloads);
        if (payload == null) {
            return;
        }

        Logger.e("正在进行增量刷新:" + position);
        TeamJoin teamJoin = getItem(position);
        for (String key : payload.keySet()) {
            switch (key) {
                case DiffUtilCallback.PAYLOAD_USER_NAME:
                    //这里可以用payload里的数据，不过newInfo也是新的 也可以用
                    holder.text(R.id.team_join_name, teamJoin.getTeamName());
                    break;
                case DiffUtilCallback.PAYLOAD_READ_NUMBER:
//                    holder.text(R.id.tv_read, "阅读量 " + payload.getInt(DiffUtilCallback.PAYLOAD_READ_NUMBER));
                    break;
                default:
                    break;
            }
        }
    }
}
