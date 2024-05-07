package org.cynic.data_query.service;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.cynic.data_query.antlr.query.QueryParser;
import org.cynic.data_query.domain.ApplicationException;
import org.cynic.data_query.domain.entity.StoreItem;
import org.cynic.data_query.domain.http.IdHttp;
import org.cynic.data_query.domain.http.post.CreateStoreItemHttp;
import org.cynic.data_query.domain.http.post.StoreItemHttp;
import org.cynic.data_query.mapper.StoreItemMapper;
import org.cynic.data_query.repository.StoreItemRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StoreItemService {

    private final StoreItemRepository storeItemRepository;
    private final StoreItemMapper storeItemMapper;
    private final Function<String, QueryParser> queryParserCreator;

    public StoreItemService(StoreItemRepository storeItemRepository, StoreItemMapper storeItemMapper, Function<String, QueryParser> queryParserCreator) {
        this.storeItemRepository = storeItemRepository;
        this.storeItemMapper = storeItemMapper;
        this.queryParserCreator = queryParserCreator;
    }


    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public IdHttp<String> create(CreateStoreItemHttp http) {
        return storeItemRepository.findById(http.id())
            .map(it -> storeItemMapper.merge(it, http))
            .orElseGet(() -> storeItemMapper.to(http))
            .map(storeItemRepository::save)
            .map(StoreItem::getId)
            .map(IdHttp::new)
            .orElseThrow(
                () -> new ApplicationException("error.store-item.create.not-mappable",
                    Map.entry("body", http)
                )
            );
    }

    public List<StoreItemHttp> find(Optional<String> query) {
        Specification<StoreItem> specification = query
            .filter(StringUtils::isNotBlank)
            .map(queryParserCreator)
            .map(QueryParser::operation)
            .map(StoreItemRepository::filter)
            .orElseGet(StoreItemRepository::all);

        return storeItemRepository.findAll(specification)
            .stream()
            .map(storeItemMapper::to)
            .flatMap(Optional::stream)
            .toList();
    }
}
