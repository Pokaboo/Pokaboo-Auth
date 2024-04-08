package com.pokaboo.system.controller;

import com.pokaboo.common.helper.JwtHelper;
import com.pokaboo.common.result.Result;
import com.pokaboo.common.result.ResultCodeEnum;
import com.pokaboo.common.util.MD5;
import com.pokaboo.model.system.SysUser;
import com.pokaboo.model.vo.LoginVo;
import com.pokaboo.system.exception.GlobalException;
import com.pokaboo.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登陆")
    public Result login(@RequestBody LoginVo loginVo) {
        SysUser sysUser = sysUserService.getByUsername(loginVo.getUsername());
        if (null == sysUser) {
            throw new GlobalException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        if (!MD5.encrypt(loginVo.getPassword()).equals(sysUser.getPassword())) {
            throw new GlobalException(ResultCodeEnum.PASSWORD_ERROR);
        }
        if (sysUser.getStatus().intValue() == 0) {
            throw new GlobalException(ResultCodeEnum.ACCOUNT_STOP);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", JwtHelper.createToken(sysUser.getId(), sysUser.getUsername()));
        return Result.ok(map);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    public Result info(HttpServletRequest request) {
        Long userId = JwtHelper.getUserId(request.getHeader("token"));
        Map<String, Object> userInfo = sysUserService.getUserInfoByUserId(userId);
        return Result.ok(userInfo);
    }


    @PostMapping("/logout")
    @ApiOperation(value = "用户退出")
    public Result loginout() {
        return Result.ok();
    }
}
