package org.cynic.data_query.controller;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.cynic.data_query.domain.http.IdHttp;
import org.cynic.data_query.domain.http.post.CreateStoreItemHttp;
import org.cynic.data_query.domain.http.post.StoreItemHttp;
import org.cynic.data_query.service.StoreItemService;
import org.instancio.Instancio;
import org.instancio.TypeToken;
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
class StoreItemControllerTest {

    @InjectMocks
    private StoreItemController storeItemController;

    @Mock
    private StoreItemService storeItemService;

    @Test
    void listWhenOk() {
        List<StoreItemHttp> response = Instancio.createList(StoreItemHttp.class);
        String query = Instancio.create(String.class);

        Mockito.when(storeItemService.find(Optional.of(query))).thenReturn(response);

        Assertions.assertThat(storeItemController.list(Optional.of(query)))
            .isEqualTo(response);

        Mockito.verify(storeItemService).find(Optional.of(query));

        Mockito.verifyNoMoreInteractions(storeItemService);
    }

    @Test
    void createWhenOk() {
        CreateStoreItemHttp request = Instancio.create(CreateStoreItemHttp.class);
        IdHttp<String> response = Instancio.create(new TypeToken<>() {
        });

        Mockito.when(storeItemService.create(request)).thenReturn(response);

        Assertions.assertThat(storeItemController.create(request))
            .isEqualTo(response);

        Mockito.verify(storeItemService).create(request);

        Mockito.verifyNoMoreInteractions(storeItemService);
    }
}