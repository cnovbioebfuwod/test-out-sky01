package com.sky.service.impl;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper

public interface SetmealDishMapper {
    public List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
