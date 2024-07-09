package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper

public interface DishFlavorMapper {
    void insert(DishFlavor flavor);

    void insertBatch(List<DishFlavor> list);

    /**
     * 通过菜品Id删除对应口味信息
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    @Select("select *from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> selectByDishId(Long id);
}
