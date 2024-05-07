package org.cynic.data_query.controller;

import java.util.List;
import java.util.Optional;
import org.cynic.data_query.domain.http.IdHttp;
import org.cynic.data_query.domain.http.post.CreateStoreItemHttp;
import org.cynic.data_query.domain.http.post.StoreItemHttp;
import org.cynic.data_query.service.StoreItemService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreItemController {

    private final StoreItemService storeItemService;

    public StoreItemController(StoreItemService storeItemService) {
        this.storeItemService = storeItemService;
    }

    @GetMapping
    public List<StoreItemHttp> list(@RequestParam Optional<String> query) {
        return storeItemService.find(query);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdHttp<String> create(@Validated @RequestBody CreateStoreItemHttp post) {
        return storeItemService.create(post);
    }
}
