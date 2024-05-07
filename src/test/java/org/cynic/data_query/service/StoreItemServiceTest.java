package org.cynic.data_query.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.cynic.data_query.antlr.query.QueryParser;
import org.cynic.data_query.antlr.query.QueryParser.OperationContext;
import org.cynic.data_query.domain.ApplicationException;
import org.cynic.data_query.domain.entity.StoreItem;
import org.cynic.data_query.domain.http.IdHttp;
import org.cynic.data_query.domain.http.post.CreateStoreItemHttp;
import org.cynic.data_query.domain.http.post.StoreItemHttp;
import org.cynic.data_query.mapper.StoreItemMapper;
import org.cynic.data_query.repository.StoreItemRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({
    MockitoExtension.class,
    InstancioExtension.class
})
@Tag("unit")
public class StoreItemServiceTest {


    @InjectMocks
    private StoreItemService storeItemService;

    @Mock
    private StoreItemRepository storeItemRepository;

    @Mock
    private StoreItemMapper storeItemMapper;

    @Mock
    private Function<String, QueryParser> queryParserCreator;

    @Test
    void createWhenUpdateOK() {
        CreateStoreItemHttp http = Instancio.create(CreateStoreItemHttp.class);
        StoreItem entity = Instancio.create(StoreItem.class);
        StoreItem updated = Instancio.create(StoreItem.class);
        IdHttp<String> response = new IdHttp<>(updated.getId());

        Mockito.when(storeItemRepository.findById(http.id())).thenReturn(Optional.of(entity));
        Mockito.when(storeItemMapper.merge(entity, http)).thenReturn(Optional.of(updated));
        Mockito.when(storeItemRepository.save(updated)).thenReturn(updated);

        Assertions.assertThat(storeItemService.create(http))
            .isEqualTo(response);

        Mockito.verify(storeItemRepository).findById(http.id());
        Mockito.verify(storeItemMapper).merge(entity, http);
        Mockito.verify(storeItemRepository).save(updated);

        Mockito.verifyNoMoreInteractions(storeItemRepository, storeItemMapper);
    }

    @Test
    void createWhenCreateOK() {
        CreateStoreItemHttp http = Instancio.create(CreateStoreItemHttp.class);
        StoreItem entity = Instancio.create(StoreItem.class);
        IdHttp<String> response = new IdHttp<>(entity.getId());

        Mockito.when(storeItemRepository.findById(http.id())).thenReturn(Optional.empty());
        Mockito.when(storeItemMapper.to(http)).thenReturn(Optional.of(entity));
        Mockito.when(storeItemRepository.save(entity)).thenReturn(entity);

        Assertions.assertThat(storeItemService.create(http))
            .isEqualTo(response);

        Mockito.verify(storeItemRepository).findById(http.id());
        Mockito.verify(storeItemMapper).to(http);
        Mockito.verify(storeItemRepository).save(entity);

        Mockito.verifyNoMoreInteractions(storeItemRepository, storeItemMapper);
    }

    @Test
    void createWhenError() {
        CreateStoreItemHttp http = Instancio.create(CreateStoreItemHttp.class);

        Mockito.when(storeItemRepository.findById(http.id())).thenReturn(Optional.empty());
        Mockito.when(storeItemMapper.to(http)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> storeItemService.create(http))
            .asInstanceOf(InstanceOfAssertFactories.throwable(ApplicationException.class))
            .matches(it -> StringUtils.equals(it.getCode(), "error.store-item.create.not-mappable"))
            .extracting(ApplicationException::getValues)
            .asInstanceOf(InstanceOfAssertFactories.MAP)
            .containsExactly(
                Map.entry("body", http)
            );

        Mockito.verify(storeItemRepository).findById(http.id());
        Mockito.verify(storeItemMapper).to(http);

        Mockito.verifyNoMoreInteractions(storeItemRepository, storeItemMapper);
    }

    @Test
    void findWhenNotEmptyOk() {
        String filter = Instancio.create(String.class);
        OperationContext operationContext = Instancio.create(OperationContext.class);
        StoreItem entity = Instancio.create(StoreItem.class);
        StoreItemHttp http = Instancio.create(StoreItemHttp.class);

        QueryParser queryParser = Mockito.mock(QueryParser.class);

        Mockito.when(queryParserCreator.apply(filter)).thenReturn(queryParser);
        Mockito.when(queryParser.operation()).thenReturn(operationContext);
        Mockito.when(storeItemRepository.findAll(Mockito.any())).thenReturn(List.of(entity));
        Mockito.when(storeItemMapper.to(entity)).thenReturn(Optional.of(http));

        Assertions.assertThat(storeItemService.find(Optional.of(filter)))
            .isEqualTo(List.of(http));

        Mockito.verify(queryParserCreator).apply(filter);
        Mockito.verify(queryParser).operation();
        Mockito.verify(storeItemRepository).findAll(Mockito.any());
        Mockito.verify(storeItemMapper).to(entity);

        Mockito.verifyNoMoreInteractions(queryParserCreator, queryParser, storeItemRepository, storeItemMapper);
    }

    @Test
    void findWhenEmptyOk() {
        StoreItem entity = Instancio.create(StoreItem.class);
        StoreItemHttp http = Instancio.create(StoreItemHttp.class);

        Mockito.when(storeItemRepository.findAll(Mockito.any())).thenReturn(List.of(entity));
        Mockito.when(storeItemMapper.to(entity)).thenReturn(Optional.of(http));

        Assertions.assertThat(storeItemService.find(Optional.empty()))
            .isEqualTo(List.of(http));

        Mockito.verify(storeItemRepository).findAll(Mockito.any());
        Mockito.verify(storeItemMapper).to(entity);

        Mockito.verifyNoMoreInteractions(queryParserCreator, storeItemRepository, storeItemMapper);
    }
}
