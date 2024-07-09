package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service

public class DishServiceImpl implements DishService{

    @Autowired
    private DishMapper dishMapper;

    @Resource
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    @Override
    @Transactional
    public void save(DishDTO dishDTO) {
//        转成dish
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
//        设置状态为停售
        dish.setStatus(StatusConstant.DISABLE);
//        添加dish到数据表中Dish
        dishMapper.insert(dish);
        Long dishId=dish.getId();
        List<DishFlavor> flavors=dishDTO.getFlavors();
//        判断是否有口味，有则需要添加口味表
        if(!CollectionUtils.isEmpty(flavors)){
//            for(DishFlavor flavor :flavors){
//                flavor.setDishId(dishId);
//                dishFlavorMapper.insert(flavor);
//            }
//            使用批量插入
            flavors.forEach(f->f.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }
//        操作两张表，要加上事务处理

    }
}
