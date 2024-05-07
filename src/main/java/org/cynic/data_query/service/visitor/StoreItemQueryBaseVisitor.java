package org.cynic.data_query.service.visitor;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.cynic.data_query.Constants;
import org.cynic.data_query.antlr.query.QueryBaseVisitor;
import org.cynic.data_query.antlr.query.QueryParser.ComparisonOperationContext;
import org.cynic.data_query.antlr.query.QueryParser.OperationContext;
import org.cynic.data_query.domain.ApplicationException;
import org.cynic.data_query.domain.entity.StoreItem;


public class StoreItemQueryBaseVisitor extends QueryBaseVisitor<Predicate> {

    private final CriteriaBuilder criteriaBuilder;
    private final Root<StoreItem> root;

    public StoreItemQueryBaseVisitor(CriteriaBuilder criteriaBuilder, Root<StoreItem> root) {
        this.criteriaBuilder = criteriaBuilder;
        this.root = root;
    }

    @Override
    public Predicate visitOperation(OperationContext ctx) {
        return Stream.<Entry<Function<OperationContext, Boolean>, Function<OperationContext, Predicate>>>of(
                Map.entry(it -> Objects.nonNull(it.comparisonOperation()), this::comparison),
                Map.entry(it -> Objects.nonNull(it.negationOperator()), this::negation),
                Map.entry(it -> Objects.nonNull(it.unionOperator()), this::list)
            )
            .filter(it -> it.getKey().apply(ctx))
            .findAny()
            .orElseThrow(() ->
                new ApplicationException(
                    "error.query.visitor.operation-unknown",
                    Map.entry("text", ctx.getText())
                )
            )
            .getValue()
            .apply(ctx);
    }

    @Override
    public Predicate visitComparisonOperation(ComparisonOperationContext ctx) {
        String operation = ctx.getChild(0).getText();
        String field = ctx.getChild(2).getText();

        String value = Optional.of(ctx)
            .map(ComparisonOperationContext::STRING)
            .map(ParseTree::getText)
            .map(it -> StringUtils.removeStart(it, Constants.QUOTE))
            .map(it -> StringUtils.removeEnd(it, Constants.QUOTE))
            .orElseGet(() -> ctx.NUMBER().getText());

        return switch (operation) {
            case "EQUAL" -> criteriaBuilder.equal(root.get(field), value);
            case "GREATER_THAN" -> criteriaBuilder.gt(root.get(field), Long.parseLong(value));
            case "LESS_THAN" -> criteriaBuilder.lt(root.get(field), Long.parseLong(value));
            default -> throw new ApplicationException("error.query.visitor.comparison-unknown",
                Map.entry("operation", operation),
                Map.entry("field", field),
                Map.entry("value", value)
            );
        };
    }

    private Predicate comparison(OperationContext ctx) {
        return visitComparisonOperation(ctx.comparisonOperation());
    }

    private Predicate negation(OperationContext ctx) {
        return criteriaBuilder.not(visitOperation(ctx.operation()));
    }

    private Predicate list(OperationContext ctx) {
        Supplier<Predicate[]> predicates = () -> ctx.operatorList()
            .operation()
            .stream()
            .map(this::visitOperation)
            .toArray(Predicate[]::new);

        String union = ctx.unionOperator().getChild(0).getText();

        return switch (union) {
            case "AND" -> criteriaBuilder.and(predicates.get());
            case "OR" -> criteriaBuilder.or(predicates.get());
            default -> throw new ApplicationException(
                "error.query.visitor.list.operation-unknown",
                Map.entry("union", union)
            );
        };
    }
}
