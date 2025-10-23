package com.altioratech.pettiway.shared.util;

import org.springframework.data.domain.Page;

import java.util.function.Function;

public class PageMapperUtil {

    public static <E, D> Page<D> mapPage(Page<E> entities, Function<E, D> mapper) {
        return entities.map(mapper);
    }
}