package com.pokaboo.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pokaboo.model.system.SysRole;
import com.pokaboo.model.system.SysUser;
import com.pokaboo.model.vo.AssginRoleVo;
import com.pokaboo.model.vo.SysRoleQueryVo;
import com.pokaboo.model.vo.SysUserQueryVo;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    /**
     * 用户分页查询
     * @param pageParam
     * @param userQueryVo
     * @return
     */
    IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo userQueryVo);

    /**
     * 修改用户状态
     * @param id
     * @param status
     */
    void switchStatus(Long id , Integer status);


}
