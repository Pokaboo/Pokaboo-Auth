package com.pokaboo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pokaboo.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> selectMenuListByUserId(@Param("userId") Long userId);
}
