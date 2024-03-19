package com.pokaboo.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pokaboo.model.system.SysRole;
import com.pokaboo.model.vo.AssginRoleVo;
import com.pokaboo.model.vo.SysRoleQueryVo;

import java.util.Map;

public interface SysRoleService  extends IService<SysRole> {

    /**
     * 角色分页查询
     * @param pageParam
     * @param roleQueryVo
     * @return
     */
    IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo roleQueryVo);

    /**
     * 用户角色查询
     * @param id
     * @return
     */
    Map<String, Object> getRolesByUserId(Long id);

    /**
     * 分配角色
     * @param assginRoleVo
     */
    void doAssign(AssginRoleVo assginRoleVo);
}
