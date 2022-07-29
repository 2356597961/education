package com.xiaoliu.aclservice.controller;


import com.xiaoliu.aclservice.entity.Permission;
import com.xiaoliu.aclservice.service.PermissionService;
import com.xiaoliu.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 菜单管理
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/permission")
//@CrossOrigin
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    //获取全部菜单递归实现
    @ApiOperation(value = "查询所有菜单")
    @GetMapping
    public R indexAllPermission() {
        List<Permission> list =  permissionService.queryAllMenuXiaoLiu();
        return R.ok().data("children",list);
    }

    @ApiOperation(value = "递归删除菜单")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        permissionService.removeChildByIdXiaoLiu(id);
        return R.ok();
    }

    //这里就是一个角色可能有多个菜单,添加到角色菜单表中
    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public R doAssign(String roleId,String[] permissionId) {
        permissionService.saveRolePermissionRealtionShipXiaoLiu(roleId,permissionId);
        return R.ok();
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public R toAssign(@PathVariable String roleId) {
        List<Permission> list = permissionService.selectAllMenu(roleId);
        return R.ok().data("children", list);
    }



    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public R save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return R.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public R updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return R.ok();
    }

}

