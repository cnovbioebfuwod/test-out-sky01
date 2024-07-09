package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜品相关业务接口
 * @param
 */

@Service
public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */


    void save(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO pageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    DishVO geiByIdWithFlavors(Long id);

    void update(DishDTO dishDTO);
}