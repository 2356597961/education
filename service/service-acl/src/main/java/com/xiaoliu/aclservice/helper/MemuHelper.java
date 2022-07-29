package com.xiaoliu.aclservice.helper;

import com.alibaba.fastjson.JSONObject;
import com.xiaoliu.aclservice.entity.Permission;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 根据权限数据构建登录用户左侧菜单数据,就是获取出来
 * </p>
 *
 * @author qy
 * @since 2019-11-11
 */
public class MemuHelper {

    /**
     * 构建菜单
     * @param treeNodes
     * @return
     */
    public static List<JSONObject> bulid(List<Permission> treeNodes) {  //用于前端展示
        List<JSONObject> meuns = new ArrayList<>();
        if(treeNodes.size() == 1) {  //不等于一的话就循环呗
            Permission topNode = treeNodes.get(0);  //这里直接拿第一个，因为实际就只有一个最顶级的父级
            //左侧一级菜单
            List<Permission> oneMeunList = topNode.getChildren();
            for(Permission one :oneMeunList) {
                JSONObject oneMeun = new JSONObject();
                oneMeun.put("path", one.getPath());
                oneMeun.put("component", one.getComponent());
                oneMeun.put("redirect", "noredirect");
                oneMeun.put("name", "name_"+one.getId());
                oneMeun.put("hidden", false);

                JSONObject oneMeta = new JSONObject();
                oneMeta.put("title", one.getName());
                oneMeta.put("icon", one.getIcon());
                oneMeun.put("meta", oneMeta);

                List<JSONObject> children = new ArrayList<>();
                //二级
                List<Permission> twoMeunList = one.getChildren();
                for(Permission two :twoMeunList) {
                    JSONObject twoMeun = new JSONObject();
                    twoMeun.put("path", two.getPath());
                    twoMeun.put("component", two.getComponent());
                    twoMeun.put("name", "name_"+two.getId());
                    twoMeun.put("hidden", false);

                    JSONObject twoMeta = new JSONObject();
                    twoMeta.put("title", two.getName());
                    twoMeun.put("meta", twoMeta);

                    children.add(twoMeun);

                    List<Permission> threeMeunList = two.getChildren();
                    for(Permission three :threeMeunList) {
                        if(StringUtils.isEmpty(three.getPath())) continue;

                        JSONObject threeMeun = new JSONObject();
                        threeMeun.put("path", three.getPath());
                        threeMeun.put("component", three.getComponent());
                        threeMeun.put("name", "name_"+three.getId());
                        threeMeun.put("hidden", true);

                        JSONObject threeMeta = new JSONObject();
                        threeMeta.put("title", three.getName());
                        threeMeun.put("meta", threeMeta);

                        children.add(threeMeun);  //和二级放在一起就是为了减少层数，为了方便操作
                    }
                }
                oneMeun.put("children", children);  //一级meun有children,children里面有二，三级
                meuns.add(oneMeun);  //meuns有很多一级
            }
        }
        return meuns;
    }
}
