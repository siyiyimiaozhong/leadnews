package com.siyi.migration.service;

import com.siyi.model.article.pojos.ApAuthor;

import java.util.List;

public interface ApAuthorService {

    List<ApAuthor> queryByIds(List<Integer> ids);

    ApAuthor getById(Long id);
}
