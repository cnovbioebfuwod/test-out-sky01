package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper

public interface DishFlavorMapper {
    void insert(DishFlavor flavor);

    void insertBatch(List<DishFlavor> list);
}
