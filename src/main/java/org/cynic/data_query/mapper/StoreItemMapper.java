package org.cynic.data_query.mapper;

import java.util.Optional;
import org.cynic.data_query.domain.entity.StoreItem;
import org.cynic.data_query.domain.http.post.CreateStoreItemHttp;
import org.cynic.data_query.domain.http.post.StoreItemHttp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class StoreItemMapper {


    protected abstract StoreItemHttp internal(StoreItem entity);

    protected abstract StoreItem internal(StoreItemHttp http);

    protected abstract StoreItem internal(CreateStoreItemHttp http);

    @Mapping(target = "id", ignore = true)
    protected abstract StoreItem internal(@MappingTarget StoreItem entity, CreateStoreItemHttp http);

    public Optional<StoreItemHttp> to(StoreItem entity) {
        return Optional.ofNullable(internal(entity));
    }

    public Optional<StoreItem> merge(StoreItem entity, CreateStoreItemHttp http) {
        return Optional.ofNullable(internal(entity, http));
    }

    public Optional<StoreItem> to(StoreItemHttp http) {
        return Optional.ofNullable(internal(http));
    }

    public Optional<StoreItem> to(CreateStoreItemHttp http) {
        return Optional.ofNullable(internal(http));
    }

}
