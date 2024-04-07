package com.pokaboo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pokaboo.common.helper.MenuHelper;
import com.pokaboo.common.result.ResultCodeEnum;
import com.pokaboo.model.system.SysMenu;
import com.pokaboo.model.system.SysRoleMenu;
import com.pokaboo.model.vo.AssginMenuVo;
import com.pokaboo.system.exception.GlobalException;
import com.pokaboo.system.mapper.SysMenuMapper;
import com.pokaboo.system.mapper.SysRoleMenuMapper;
import com.pokaboo.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;


    /**
     * 查询所有菜单
     *
     * @return
     */
    @Override
    public List<SysMenu> findNodes() {
        //全部权限列表
        List<SysMenu> sysMenuList = this.list();
        if (CollectionUtils.isEmpty(sysMenuList)) return null;

        //构建树形数据
        List<SysMenu> result = MenuHelper.buildTree(sysMenuList);
        return result;
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeMenuById(Long id) {
        // 删除菜单前，需要考虑当前菜单下是否存在子菜单
        Integer menuCount = sysMenuMapper.selectCount(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if (menuCount > 0) {
            throw new GlobalException(ResultCodeEnum.NODE_ERROR);
        }
        sysMenuMapper.deleteById(id);
        return false;
    }

    /**
     * 根据角色获取授权权限数据
     *
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        //获取所有status为1的权限列表
        List<SysMenu> menuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("status", 1));
        //根据角色id获取角色权限
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        //获取该角色已分配的所有权限id
        List<Long> roleMenuIds = new ArrayList<>();
        for (SysRoleMenu roleMenu : roleMenus) {
            roleMenuIds.add(roleMenu.getMenuId());
        }
        //遍历所有权限列表
        for (SysMenu sysMenu : menuList) {
            if (roleMenuIds.contains(sysMenu.getId())) {
                //设置该权限已被分配
                sysMenu.setSelect(true);
            } else {
                sysMenu.setSelect(false);
            }
        }
        //将权限列表转换为权限树
        List<SysMenu> sysMenus = MenuHelper.buildTree(menuList);
        return sysMenus;
    }

    /**
     * 角色权限
     *
     * @param assginMenuVo
     */
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //删除已分配的权限
        sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("role_id", assginMenuVo.getRoleId()));
        //遍历所有已选择的权限id
        for (Long menuId : assginMenuVo.getMenuIdList()) {
            if (menuId != null) {
                //创建SysRoleMenu对象
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
                //添加新权限
                sysRoleMenuMapper.insert(sysRoleMenu);
            }
        }
    }

}
