package com.pokaboo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pokaboo.common.helper.MenuHelper;
import com.pokaboo.common.result.ResultCodeEnum;
import com.pokaboo.model.system.SysMenu;
import com.pokaboo.system.exception.GlobalException;
import com.pokaboo.system.mapper.SysMenuMapper;
import com.pokaboo.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    SysMenuMapper sysMenuMapper;

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

}
