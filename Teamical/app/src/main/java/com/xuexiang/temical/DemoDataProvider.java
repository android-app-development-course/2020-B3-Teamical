package com.xuexiang.temical;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.viewpager.widget.ViewPager;

import com.google.gson.reflect.TypeToken;
import com.xuexiang.temical.adapter.entity.TeammateInfo;
import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.ExpandableItem;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.banner.transform.DepthTransformer;
import com.xuexiang.xui.widget.banner.transform.FadeSlideTransformer;
import com.xuexiang.xui.widget.banner.transform.FlowTransformer;
import com.xuexiang.xui.widget.banner.transform.RotateDownTransformer;
import com.xuexiang.xui.widget.banner.transform.RotateUpTransformer;
import com.xuexiang.xui.widget.banner.transform.ZoomOutSlideTransformer;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.imageview.nine.NineGridImageView;
import com.xuexiang.temical.adapter.entity.NewInfo;
import com.xuexiang.xutil.net.JsonUtil;
import com.xuexiang.xutil.resource.ResourceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DemoDataProvider {


    /**
     * 拆分集合
     *
     * @param <T>
     * @param resList 要拆分的集合
     * @param count   每个集合的元素个数
     * @return 返回拆分后的各个集合
     */
    public static <T> List<List<T>> split(List<T> resList, int count) {
        if (resList == null || count < 1) {
            return null;
        }
        List<List<T>> ret = new ArrayList<>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }


    @MemoryCache
    public static Collection<String> getDemoData() {
        return Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    @MemoryCache
    public static Collection<String> getDemoData1() {
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18");
    }

    /**
     * 用于占位的空信息
     *
     * @return
     */
    @MemoryCache
    public static List<NewInfo> getEmptyNewInfo() {
        List<NewInfo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new NewInfo());
        }
        return list;
    }


    /**
     * 用于占位的空信息
     *
     * @return
     */
    @MemoryCache
    public static List<NewInfo> getDemoNewInfos() {
        List<NewInfo> list = new ArrayList<>();
        list.add(new NewInfo("个人日程", "光明公司宣讲会")
                .setSummary("会议需要穿着正装，并且需要提前进场")
                .setPraise("9:40 p.m. 15")
                .setComment("华信路礼堂"));

        list.add(new NewInfo("团队日程", "公司团建活动")
                .setSummary("会议需要穿着正装，并且需要提前进场")
                .setPraise("19:00 p.m. 15")
                .setComment("公司活动中心"));

        list.add(new NewInfo("个人日程", "光明公司管理层会议")
                .setSummary("会议需要穿着正装，并且需要提前进场")
                .setPraise("9:00 p.m. 16, Nov")
                .setComment("会议508"));

        list.add(new NewInfo("个人日程", "日程管理软件项目投标会")
                .setSummary("会议需要穿着正装，并且需要提前进场")
                .setPraise("14:30 p.m. 16, Nov")
                .setComment("市中心会议大厅"));
        return list;
    }

    /**
     * 用于模拟团队成员的数据
     * @return
     */
    @MemoryCache
    public static List<TeammateInfo> getDemoTeammateInfos() {
        List<TeammateInfo> list = new ArrayList<>();
        list.add(new TeammateInfo("Andy", "null"));
        list.add(new TeammateInfo("Lisa", "null"));
        list.add(new TeammateInfo("James", "null"));
        list.add(new TeammateInfo("Lucy", "null"));
        return list;
    }

    /**
     * 用于占位的空信息
     *
     * @return
     */

}
