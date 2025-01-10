package com.hsbc.financial.domain.common.factory;

import com.hsbc.financial.domain.common.utils.ApplicationContextUtil;

/**
 * DomainFactory
 *
 * @author zhaoyongping
 * @date 2023/11/8 17:32
 * @className DomainFactory
 **/
public class EntityFactory {
    public static <T> T create(Class<T> entity){
        return ApplicationContextUtil.getBean(entity);
    }
}
