package com.xiaoliu.aclservice.helper;

import com.xiaoliu.aclservice.entity.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 根据权限数据构建菜单数据
 * </p>
 *
 * @author qy
 * @since 2019-11-11
 */
public class PermissionHelper {

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    public static List<Permission> bulid(List<Permission> treeNodes) { //根据用户ID查出所有的Permission
        List<Permission> trees = new ArrayList<>();
        for (Permission treeNode : treeNodes) {
            if ("0".equals(treeNode.getPid())) { //最顶级的也可能有多个
                treeNode.setLevel(1);  //为一级菜单，也是最顶级的
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点 在不知道有
     * @param treeNodes
     * @return
     */
    public static Permission findChildren(Permission treeNode,List<Permission> treeNodes) {
        treeNode.setChildren(new ArrayList<Permission>());

        for (Permission it : treeNodes) {
            if(treeNode.getId().equals(it.getPid())) {  //判断父ID（传过来的）等于当前循环项的父ID积极，退出递归的关键
                int level = treeNode.getLevel() + 1;  //等级加一
                it.setLevel(level);
                if (treeNode.getChildren() == null) {  //再次判断是否为空
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));  //这个一定会执行的
            }
        }
        return treeNode;
    }
}
