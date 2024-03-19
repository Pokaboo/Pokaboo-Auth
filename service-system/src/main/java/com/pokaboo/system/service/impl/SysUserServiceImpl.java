package com.pokaboo.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pokaboo.model.system.SysRole;
import com.pokaboo.model.system.SysUser;
import com.pokaboo.model.system.SysUserRole;
import com.pokaboo.model.vo.SysUserQueryVo;
import com.pokaboo.system.mapper.SysRoleMapper;
import com.pokaboo.system.mapper.SysUserMapper;
import com.pokaboo.system.mapper.SysUserRoleMapper;
import com.pokaboo.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 分页查询
     *
     * @param pageParam
     * @param userQueryVo
     * @return
     */
    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo userQueryVo) {
        return sysUserMapper.selectPage(pageParam, userQueryVo);
    }

    /**
     * 修改用户状态
     *
     * @param id
     * @param status
     */
    @Override
    public void switchStatus(Long id, Integer status) {
        SysUser sysUser = sysUserMapper.selectById(id);
        sysUser.setStatus(status);
        sysUserMapper.updateById(sysUser);
    }


}
