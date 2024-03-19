package com.pokaboo.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pokaboo.common.result.Result;
import com.pokaboo.model.system.SysRole;
import com.pokaboo.model.vo.AssginRoleVo;
import com.pokaboo.model.vo.SysRoleQueryVo;
import com.pokaboo.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation(value = "获取全部角色列表")
    @GetMapping("/findAll")
    public Result<List<SysRole>> findAll() {
        List<SysRole> roleList = sysRoleService.list();
        return Result.ok(roleList);
    }

    @ApiOperation(value = "角色分页查询")
    @GetMapping("/{pageNum}/{pageSize}")
    public Result<IPage<SysRole>> index(
            @ApiParam(name = "pageNum", value = "当前页码", required = true)
            @PathVariable long pageNum,
            @ApiParam(name = "pageSize", value = "每页记录数", required = true)
            @PathVariable long pageSize,
            @ApiParam(name = "roleQueryVo", value = "查询条件对象", required = false)
            SysRoleQueryVo roleQueryVo
    ) {
        Page<SysRole> pageParam = new Page<>(pageNum, pageSize);
        IPage<SysRole> pageModel = sysRoleService.selectPage(pageParam, roleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("get/{id}")
    public Result<SysRole> getSysRoleById(@PathVariable long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated SysRole role) {
        boolean saveFlag = sysRoleService.save(role);
        return saveFlag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "修改角色")
    @PostMapping("/update")
    public Result update(@RequestBody SysRole role) {
        role.setUpdateTime(null);
        boolean updateFlag = sysRoleService.updateById(role);
        return updateFlag ? Result.ok() : Result.fail();

    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable long id) {
        boolean remove = sysRoleService.removeById(id);
        return remove ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "批量删除角色")
    @DeleteMapping("/removeBath")
    public Result removeBath(
            @ApiParam(name = "idList", value = "批量删除id列表i", required = true)
            @RequestBody List<String> idList
    ) {
        boolean removeBath = sysRoleService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation(value = "获取所有权限以及当前用户拥有的权限")
    @GetMapping("/toAssign/{id}")
    public Result toAssign(
            @ApiParam(name = "id", value = "用户id", required = true)
            @PathVariable Long id) {
        Map<String, Object> roleMap = sysRoleService.getRolesByUserId(id);
        return Result.ok(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }
}
