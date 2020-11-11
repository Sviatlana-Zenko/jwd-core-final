package com.epam.jwd.core_final.factory;

import com.epam.jwd.core_final.domain.BaseEntity;
import java.util.List;

public interface EntityFactory<T extends BaseEntity> {

    T create(List<String> infoFromFile);
}
