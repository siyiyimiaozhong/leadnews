package com.siyi.model.mappers.app;

import com.siyi.model.article.pojos.ApHotWords;

import java.util.List;

public interface ApHotWordsMapper {

    /**
     * 查询今日热词
     *
     * @param hotDate
     * @return
     */
    List<ApHotWords> queryByHotDate(String hotDate);
}