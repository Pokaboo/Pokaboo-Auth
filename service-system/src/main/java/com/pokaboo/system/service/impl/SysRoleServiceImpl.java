package com.pokaboo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pokaboo.model.system.SysRole;
import com.pokaboo.model.system.SysUserRole;
import com.pokaboo.model.vo.AssginRoleVo;
import com.pokaboo.model.vo.SysRoleQueryVo;
import com.pokaboo.system.mapper.SysRoleMapper;
import com.pokaboo.system.mapper.SysUserRoleMapper;
import com.pokaboo.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    /**
     * 分页查询
     *
     * @param pageParam
     * @param roleQueryVo
     * @return
     */
    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo roleQueryVo) {
        return sysRoleMapper.selectPage(pageParam, roleQueryVo);
    }

    /**
     * 获取用户角色
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getRolesByUserId(Long id) {
        Map<String, Object> roleMap = new HashMap<>();
        List<Long> userRoleIds = new ArrayList<>();

        // 查询所有的角色
        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
        roleMap.put("allRoles", sysRoleList);

        // 根据id获取当前用户的角色
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(queryWrapper);
        if (sysUserRoleList != null && !sysUserRoleList.isEmpty()) {
            for (SysUserRole sysUserRole : sysUserRoleList) {
                userRoleIds.add(sysUserRole.getRoleId());
            }
        }
        roleMap.put("userRoleIds", userRoleIds);

        return roleMap;
    }

    /**
     * 分配角色
     *
     * @param assginRoleVo
     */
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //根据用户id删除原来分配的角色
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",assginRoleVo.getUserId());
        sysUserRoleMapper.delete(queryWrapper);
        //获取所有的角色id
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            if(roleId != null){
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assginRoleVo.getUserId());
                sysUserRole.setRoleId(roleId);
                //保存
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
    }

}
