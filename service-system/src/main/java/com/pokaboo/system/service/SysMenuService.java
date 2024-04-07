package com.pokaboo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pokaboo.model.system.SysMenu;
import com.pokaboo.model.vo.AssginMenuVo;

import java.util.List;


public interface SysMenuService extends IService<SysMenu> {

    /**
     * 菜单树形数据
     *
     */
    List<SysMenu> findNodes();

    /**
     * 删除菜单
     * @param id
     *
     */
    boolean removeMenuById(Long id);


    /**
     * 根据角色获取授权权限数据
     * @return
     */
    List<SysMenu> findSysMenuByRoleId(Long roleId);

    /**
     * 保存角色权限
     * @param  assginMenuVo
     */
    void doAssign(AssginMenuVo assginMenuVo);
}
