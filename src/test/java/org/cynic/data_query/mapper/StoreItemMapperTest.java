package org.cynic.data_query.mapper;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.cynic.data_query.domain.entity.StoreItem;
import org.cynic.data_query.domain.http.post.CreateStoreItemHttp;
import org.cynic.data_query.domain.http.post.StoreItemHttp;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith({
    MockitoExtension.class,
    InstancioExtension.class
})
@Tag("unit")
class StoreItemMapperTest {

    private StoreItemMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new StoreItemMapperImpl();
    }

    @Test
    void toHttpWhenOk() {
        StoreItem entity = Instancio.create(StoreItem.class);

        StoreItemHttp expected = new StoreItemHttp(
            entity.getId(),
            entity.getTitle(),
            entity.getContent(),
            entity.getViews(),
            entity.getTimestamp()
        );

        Assertions.<Optional<StoreItemHttp>>assertThat(mapper.to(entity))
            .extracting(Optional::get)
            .usingRecursiveComparison()
            .comparingOnlyFields(
                "id",
                "title",
                "content",
                "views",
                "timestamp"
            ).isEqualTo(expected);
    }

    @Test
    void toEntityWhenOk() {
        StoreItemHttp http = Instancio.create(StoreItemHttp.class);

        StoreItem expected = new StoreItem();
        expected.setId(http.id());
        expected.setTitle(http.title());
        expected.setContent(http.content());
        expected.setViews(http.views());
        expected.setTimestamp(http.timestamp());

        Assertions.<Optional<StoreItem>>assertThat(mapper.to(http))
            .extracting(Optional::get)
            .usingRecursiveComparison()
            .comparingOnlyFields(
                "id",
                "title",
                "content",
                "views",
                "timestamp"
            ).isEqualTo(expected);
    }

    @Test
    void toEntityCreateWhenOk() {
        CreateStoreItemHttp http = Instancio.create(CreateStoreItemHttp.class);

        StoreItem expected = new StoreItem();
        expected.setId(http.id());
        expected.setTitle(http.title());
        expected.setContent(http.content());
        expected.setViews(http.views());
        expected.setTimestamp(http.timestamp());

        Assertions.<Optional<StoreItem>>assertThat(mapper.to(http))
            .extracting(Optional::get)
            .usingRecursiveComparison()
            .comparingOnlyFields(
                "id",
                "title",
                "content",
                "views",
                "timestamp"
            ).isEqualTo(expected);
    }

    @Test
    void mergeWhenOk() {
        CreateStoreItemHttp http = Instancio.create(CreateStoreItemHttp.class);
        StoreItem entity = Instancio.create(StoreItem.class);

        StoreItem expected = new StoreItem();
        expected.setTitle(http.title());
        expected.setContent(http.content());
        expected.setViews(http.views());
        expected.setTimestamp(http.timestamp());

        Assertions.<Optional<StoreItem>>assertThat(mapper.merge(entity, http))
            .extracting(Optional::get)
            .usingRecursiveComparison()
            .comparingOnlyFields(
                "title",
                "content",
                "views",
                "timestamp"
            ).isEqualTo(expected);
    }
}