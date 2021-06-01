package com.siyi.model.mappers.admin;

import com.siyi.model.admin.pojos.AdUser;

public interface AdUserMapper {

    AdUser selectByName(String name);
}