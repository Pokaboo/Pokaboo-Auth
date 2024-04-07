package com.pokaboo.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pokaboo.common.result.Result;
import com.pokaboo.common.util.MD5;
import com.pokaboo.model.system.SysRole;
import com.pokaboo.model.system.SysUser;
import com.pokaboo.model.vo.SysRoleQueryVo;
import com.pokaboo.model.vo.SysUserQueryVo;
import com.pokaboo.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    @ApiOperation(value = "用户分页查询")
    @GetMapping("/{pageNum}/{pageSize}")
    public Result<IPage<SysUser>> index(
            @ApiParam(name = "pageNum", value = "当前页码", required = true)
            @PathVariable long pageNum,
            @ApiParam(name = "pageSize", value = "每页记录数", required = true)
            @PathVariable long pageSize,
            @ApiParam(name = "roleQueryVo", value = "查询条件对象", required = false)
            SysUserQueryVo userQueryVo
    ) {
        Page<SysUser> pageParam = new Page<>(pageNum, pageSize);
        IPage<SysUser> pageModel = sysUserService.selectPage(pageParam, userQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated SysUser sysUser) {
        sysUser.setPassword(MD5.encrypt(sysUser.getPassword()));
        boolean saveFlag = sysUserService.save(sysUser);
        return saveFlag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "更新用户")
    @PostMapping("/update")
    public Result updateById(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "修改用户状态")
    @PostMapping("/switchStatus/{id}/{status}")
    public Result switchStatus(
            @ApiParam(name = "id", value = "用户id", required = true)
            @PathVariable Long id,
            @ApiParam(name = "status", value = "用户状态", required = true)
            @PathVariable Integer status) {
        sysUserService.switchStatus(id, status);
        return Result.ok();
    }



}
