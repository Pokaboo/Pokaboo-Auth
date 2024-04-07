package com.pokaboo.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pokaboo.common.helper.MenuHelper;
import com.pokaboo.common.helper.RouterHelper;
import com.pokaboo.model.system.SysMenu;
import com.pokaboo.model.system.SysRole;
import com.pokaboo.model.system.SysUser;
import com.pokaboo.model.system.SysUserRole;
import com.pokaboo.model.vo.RouterVo;
import com.pokaboo.model.vo.SysUserQueryVo;
import com.pokaboo.system.mapper.SysMenuMapper;
import com.pokaboo.system.mapper.SysRoleMapper;
import com.pokaboo.system.mapper.SysUserMapper;
import com.pokaboo.system.mapper.SysUserRoleMapper;
import com.pokaboo.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

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

    /**
     * 根据用户账号查询用户信息
     *
     * @param username
     * @return
     */
    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    /**
     * 根据用户id获取用户登录信息
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getUserInfoByUserId(Long userId) {
        //查询用户信息
        SysUser sysUser = sysUserMapper.selectById(userId);
        //调用根据用户id获取权限菜单的方法
        List<SysMenu> userMenuList = getUserMenusByUserId(userId);
        //将userMenuList转换为菜单树
        List<SysMenu> userMenuTree = MenuHelper.buildTree(userMenuList);
        //将菜单树转换为路由
        List<RouterVo> routerVoList = RouterHelper.buildRouters(userMenuTree);
        //获取用户的按钮权限标识符
        List<String> buttonPermissons = getUserBtnPermsByUserId(userId);
        //创建一个Map
        Map<String, Object> map = new HashMap<>();
        //设置前端需要的数据，{"code":200,"data":{"roles":["admin"],"introduction":"I am a super administrator","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
        map.put("name", sysUser.getName());
        //当前权限控制使用不到，我们暂时忽略
        map.put("roles", new HashSet<>());
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("routers", routerVoList);
        map.put("buttons", buttonPermissons);
        return map;
    }

    /**
     * 根据用户id获取用户按钮权限标识符
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserBtnPermsByUserId(Long userId) {
        //调用根据用户id获取权限菜单的方法
        List<SysMenu> userMenuList = getUserMenusByUserId(userId);
        //创建一个保存用户按钮权限标识的List
        List<String> buttonPermissons = new ArrayList<>();
        //遍历权限菜单
        for (SysMenu sysMenu : userMenuList) {
            //将SysMenu的type值为2的perms的值放到buttonPermissons中
            if (sysMenu.getType() == 2) {
                buttonPermissons.add(sysMenu.getPerms());
            }
        }
        return buttonPermissons;
    }

    /**
     * 根据用户id查询用户的权限菜单
     * @param userId
     * @return
     */
    public List<SysMenu> getUserMenusByUserId(Long userId) {
        List<SysMenu> userMenuList = null;
        //判断该用户是否是系统管理员
        if (userId == 1L) {
            //证明是系统管理员
            userMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("status", 1).orderByAsc("sort_value"));
        } else {
            //根据用户id查询用户的权限菜单
            userMenuList = sysMenuMapper.selectMenuListByUserId(userId);
        }
        return userMenuList;
    }

}
