package org.cynic.data_query.repository;

import jakarta.persistence.criteria.Predicate;
import org.antlr.v4.runtime.tree.ParseTree;
import org.cynic.data_query.antlr.query.QueryBaseVisitor;
import org.cynic.data_query.domain.entity.StoreItem;
import org.cynic.data_query.service.visitor.StoreItemQueryBaseVisitor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreItemRepository extends CrudRepository<StoreItem, String>, JpaSpecificationExecutor<StoreItem> {

    static Specification<StoreItem> all() {
        return (root, query, criteriaBuilder) -> null;
    }

    static Specification<StoreItem> filter(ParseTree tree) {
        return (root, query, criteriaBuilder) -> {
            QueryBaseVisitor<Predicate> visitor = new StoreItemQueryBaseVisitor(criteriaBuilder, root);

            return visitor.visit(tree);
        };
    }
}
