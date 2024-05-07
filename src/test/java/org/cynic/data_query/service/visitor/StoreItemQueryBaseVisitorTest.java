package org.cynic.data_query.service.visitor;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.cynic.data_query.antlr.query.QueryParser.ComparisonOperationContext;
import org.cynic.data_query.antlr.query.QueryParser.NegationOperatorContext;
import org.cynic.data_query.antlr.query.QueryParser.OperationContext;
import org.cynic.data_query.antlr.query.QueryParser.OperatorListContext;
import org.cynic.data_query.antlr.query.QueryParser.UnionOperatorContext;
import org.cynic.data_query.domain.ApplicationException;
import org.cynic.data_query.domain.entity.StoreItem;
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
class StoreItemQueryBaseVisitorTest {

    @InjectMocks
    private StoreItemQueryBaseVisitor storeItemQueryBaseVisitor;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Root<StoreItem> root;


    @SuppressWarnings("unchecked")
    @Test
    void visitComparisonOperationEqualWhenOk() {
        ComparisonOperationContext ctx = Mockito.mock(ComparisonOperationContext.class);
        ParseTree operationChild = Mockito.mock(ParseTree.class);
        TerminalNode valueChild = Mockito.mock(TerminalNode.class);
        ParseTree fieldChild = Mockito.mock(ParseTree.class);
        Path<String> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        String operation = "EQUAL";
        String value = Instancio.create(String.class);
        String field = Instancio.create(String.class);

        Mockito.when(ctx.getChild(0)).thenReturn(operationChild);
        Mockito.when(ctx.getChild(2)).thenReturn(fieldChild);
        Mockito.when(operationChild.getText()).thenReturn(operation);
        Mockito.when(fieldChild.getText()).thenReturn(field);
        Mockito.when(ctx.STRING()).thenReturn(valueChild);
        Mockito.when(valueChild.getText()).thenReturn("\"" + value + "\"");
        Mockito.when(root.<String>get(field)).thenReturn(path);
        Mockito.when(criteriaBuilder.equal(path, value)).thenReturn(predicate);

        Assertions.assertThat(storeItemQueryBaseVisitor.visitComparisonOperation(ctx))
            .isEqualTo(predicate);

        Mockito.verify(ctx).getChild(0);
        Mockito.verify(ctx).getChild(2);
        Mockito.verify(operationChild).getText();
        Mockito.verify(fieldChild).getText();
        Mockito.verify(ctx).STRING();
        Mockito.verify(valueChild).getText();
        Mockito.verify(root).<String>get(field);
        Mockito.verify(criteriaBuilder).equal(path, value);

        Mockito.verifyNoMoreInteractions(criteriaBuilder, root, operationChild, fieldChild, valueChild, path, predicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void visitComparisonOperationGreaterThanWhenOk() {
        ComparisonOperationContext ctx = Mockito.mock(ComparisonOperationContext.class);
        ParseTree operationChild = Mockito.mock(ParseTree.class);
        TerminalNode valueChild = Mockito.mock(TerminalNode.class);
        ParseTree fieldChild = Mockito.mock(ParseTree.class);
        Path<Long> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        String operation = "GREATER_THAN";
        Long value = Instancio.create(Long.class);
        String field = Instancio.create(String.class);

        Mockito.when(ctx.getChild(0)).thenReturn(operationChild);
        Mockito.when(ctx.getChild(2)).thenReturn(fieldChild);
        Mockito.when(operationChild.getText()).thenReturn(operation);
        Mockito.when(fieldChild.getText()).thenReturn(field);
        Mockito.when(ctx.NUMBER()).thenReturn(valueChild);
        Mockito.when(valueChild.getText()).thenReturn(value.toString());
        Mockito.when(root.<Long>get(field)).thenReturn(path);
        Mockito.when(criteriaBuilder.gt(path, value)).thenReturn(predicate);

        Assertions.assertThat(storeItemQueryBaseVisitor.visitComparisonOperation(ctx))
            .isEqualTo(predicate);

        Mockito.verify(ctx).getChild(0);
        Mockito.verify(ctx).getChild(2);
        Mockito.verify(operationChild).getText();
        Mockito.verify(fieldChild).getText();
        Mockito.verify(ctx).STRING();
        Mockito.verify(ctx).NUMBER();
        Mockito.verify(valueChild).getText();
        Mockito.verify(root).<Long>get(field);
        Mockito.verify(criteriaBuilder).gt(path, value);

        Mockito.verifyNoMoreInteractions(criteriaBuilder, root, operationChild, fieldChild, valueChild, path, predicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void visitComparisonOperationLessThanWhenOk() {
        ComparisonOperationContext ctx = Mockito.mock(ComparisonOperationContext.class);
        ParseTree operationChild = Mockito.mock(ParseTree.class);
        TerminalNode valueChild = Mockito.mock(TerminalNode.class);
        ParseTree fieldChild = Mockito.mock(ParseTree.class);
        Path<Long> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        String operation = "LESS_THAN";
        Long value = Instancio.create(Long.class);
        String field = Instancio.create(String.class);

        Mockito.when(ctx.getChild(0)).thenReturn(operationChild);
        Mockito.when(ctx.getChild(2)).thenReturn(fieldChild);
        Mockito.when(operationChild.getText()).thenReturn(operation);
        Mockito.when(fieldChild.getText()).thenReturn(field);
        Mockito.when(ctx.NUMBER()).thenReturn(valueChild);
        Mockito.when(valueChild.getText()).thenReturn(value.toString());
        Mockito.when(root.<Long>get(field)).thenReturn(path);
        Mockito.when(criteriaBuilder.lt(path, value)).thenReturn(predicate);

        Assertions.assertThat(storeItemQueryBaseVisitor.visitComparisonOperation(ctx))
            .isEqualTo(predicate);

        Mockito.verify(ctx).getChild(0);
        Mockito.verify(ctx).getChild(2);
        Mockito.verify(operationChild).getText();
        Mockito.verify(fieldChild).getText();
        Mockito.verify(ctx).STRING();
        Mockito.verify(ctx).NUMBER();
        Mockito.verify(valueChild).getText();
        Mockito.verify(root).<Long>get(field);
        Mockito.verify(criteriaBuilder).lt(path, value);

        Mockito.verifyNoMoreInteractions(criteriaBuilder, root, operationChild, fieldChild, valueChild, path, predicate);
    }

    @SuppressWarnings("unchecked")
    @Test
    void visitComparisonOperationWhenError() {
        ComparisonOperationContext ctx = Mockito.mock(ComparisonOperationContext.class);
        ParseTree operationChild = Mockito.mock(ParseTree.class);
        TerminalNode valueChild = Mockito.mock(TerminalNode.class);
        ParseTree fieldChild = Mockito.mock(ParseTree.class);
        Path<Long> path = Mockito.mock(Path.class);
        Predicate predicate = Mockito.mock(Predicate.class);

        String operation = Instancio.create(String.class);
        Long value = Instancio.create(Long.class);
        String field = Instancio.create(String.class);

        Mockito.when(ctx.getChild(0)).thenReturn(operationChild);
        Mockito.when(ctx.getChild(2)).thenReturn(fieldChild);
        Mockito.when(operationChild.getText()).thenReturn(operation);
        Mockito.when(fieldChild.getText()).thenReturn(field);
        Mockito.when(ctx.NUMBER()).thenReturn(valueChild);
        Mockito.when(valueChild.getText()).thenReturn(value.toString());

        Assertions.assertThatThrownBy(() -> storeItemQueryBaseVisitor.visitComparisonOperation(ctx))
            .asInstanceOf(InstanceOfAssertFactories.throwable(ApplicationException.class))
            .matches(it -> StringUtils.equals(it.getCode(), "error.query.visitor.comparison-unknown"))
            .extracting(ApplicationException::getValues)
            .asInstanceOf(InstanceOfAssertFactories.MAP)
            .containsOnly(
                Map.entry("operation", operation),
                Map.entry("field", field),
                Map.entry("value", value.toString())
            );

        Mockito.verify(ctx).getChild(0);
        Mockito.verify(ctx).getChild(2);
        Mockito.verify(operationChild).getText();
        Mockito.verify(fieldChild).getText();
        Mockito.verify(ctx).STRING();
        Mockito.verify(ctx).NUMBER();
        Mockito.verify(valueChild).getText();

        Mockito.verifyNoMoreInteractions(criteriaBuilder, root, operationChild, fieldChild, valueChild, path, predicate);
    }


    @Test
    void visitOperationWhenComparisonOperationOk() {
        Predicate predicate = Mockito.mock(Predicate.class);

        StoreItemQueryBaseVisitor spy = Mockito.spy(storeItemQueryBaseVisitor);
        OperationContext ctx = Mockito.mock(OperationContext.class);
        ComparisonOperationContext comparisonOperationContext = Mockito.mock(ComparisonOperationContext.class);

        Mockito.when(ctx.comparisonOperation()).thenReturn(comparisonOperationContext);

        Mockito.doReturn(predicate)
            .when(spy).visitComparisonOperation(comparisonOperationContext);

        Assertions.assertThat(spy.visitOperation(ctx))
            .isEqualTo(predicate);

        Mockito.verify(ctx, Mockito.times(2)).comparisonOperation();
        Mockito.verify(spy).visitComparisonOperation(comparisonOperationContext);
        Mockito.verifyNoMoreInteractions(criteriaBuilder, root, predicate, ctx, comparisonOperationContext, predicate);
    }

    @Test
    void visitOperationWhenNegationOperationOk() {
        Predicate predicate = Mockito.mock(Predicate.class);
        Predicate notPredicate = Mockito.mock(Predicate.class);

        StoreItemQueryBaseVisitor spy = Mockito.spy(storeItemQueryBaseVisitor);

        OperationContext ctx = Mockito.mock(OperationContext.class);
        NegationOperatorContext negationOperatorContext = Mockito.mock(NegationOperatorContext.class);
        OperationContext operationContext = Mockito.mock(OperationContext.class);
        ComparisonOperationContext comparisonContext = Mockito.mock(ComparisonOperationContext.class);

        Mockito.when(ctx.negationOperator()).thenReturn(negationOperatorContext);
        Mockito.when(ctx.operation()).thenReturn(operationContext);
        Mockito.when(operationContext.comparisonOperation()).thenReturn(comparisonContext);

        Mockito.doReturn(predicate)
            .when(spy).visitComparisonOperation(comparisonContext);
        Mockito.when(criteriaBuilder.not(predicate)).thenReturn(notPredicate);

        Assertions.assertThat(spy.visitOperation(ctx))
            .isEqualTo(notPredicate);

        Mockito.verify(ctx).comparisonOperation();
        Mockito.verify(ctx).operation();
        Mockito.verify(operationContext, Mockito.times(2)).comparisonOperation();

        Mockito.verify(spy).visitComparisonOperation(comparisonContext);
        Mockito.verify(criteriaBuilder).not(predicate);

        Mockito.verifyNoMoreInteractions(criteriaBuilder, root, predicate, ctx, negationOperatorContext, operationContext, comparisonContext, predicate,
            notPredicate);
    }

    @Test
    void visitOperationWhenUnionOperationAndOk() {
        Predicate predicate = Mockito.mock(Predicate.class);
        Predicate andPredicate = Mockito.mock(Predicate.class);

        StoreItemQueryBaseVisitor spy = Mockito.spy(storeItemQueryBaseVisitor);

        OperationContext ctx = Mockito.mock(OperationContext.class);
        OperationContext operationContext = Mockito.mock(OperationContext.class);

        ComparisonOperationContext comparisonContext = Mockito.mock(ComparisonOperationContext.class);
        OperatorListContext operationList = Mockito.mock(OperatorListContext.class);
        UnionOperatorContext unionContext = Mockito.mock(UnionOperatorContext.class);
        ParseTree unionChild = Mockito.mock(ParseTree.class);

        Mockito.when(ctx.operatorList()).thenReturn(operationList);
        Mockito.when(operationList.operation()).thenReturn(List.of(operationContext));
        Mockito.when(ctx.unionOperator()).thenReturn(unionContext);
        Mockito.when(unionContext.getChild(0)).thenReturn(unionChild);
        Mockito.when(unionChild.getText()).thenReturn("AND");
        Mockito.when(operationContext.comparisonOperation()).thenReturn(comparisonContext);
        Mockito.when(criteriaBuilder.and(predicate)).thenReturn(andPredicate);

        Mockito.doReturn(predicate)
            .when(spy).visitComparisonOperation(comparisonContext);

        Assertions.assertThat(spy.visitOperation(ctx))
            .isEqualTo(andPredicate);

        Mockito.verify(ctx).operatorList();
        Mockito.verify(ctx).comparisonOperation();
        Mockito.verify(ctx).negationOperator();
        Mockito.verify(operationList).operation();
        Mockito.verify(ctx, Mockito.times(2)).unionOperator();
        Mockito.verify(unionContext).getChild(0);
        Mockito.verify(unionChild).getText();
        Mockito.verify(operationContext, Mockito.times(2)).comparisonOperation();
        Mockito.verify(spy).visitComparisonOperation(comparisonContext);
        Mockito.verify(criteriaBuilder).and(predicate);

        Mockito.verifyNoMoreInteractions(criteriaBuilder, root, predicate, ctx, operationContext, comparisonContext, operationList, unionContext, unionChild,
            andPredicate);
    }

    @Test
    void visitOperationWhenUnionOperationOrdOk() {
        Predicate predicate = Mockito.mock(Predicate.class);
        Predicate orPredicate = Mockito.mock(Predicate.class);

        StoreItemQueryBaseVisitor spy = Mockito.spy(storeItemQueryBaseVisitor);

        OperationContext ctx = Mockito.mock(OperationContext.class);
        OperationContext operationContext = Mockito.mock(OperationContext.class);

        ComparisonOperationContext comparisonContext = Mockito.mock(ComparisonOperationContext.class);
        OperatorListContext operationList = Mockito.mock(OperatorListContext.class);
        UnionOperatorContext unionContext = Mockito.mock(UnionOperatorContext.class);
        ParseTree unionChild = Mockito.mock(ParseTree.class);

        Mockito.when(ctx.operatorList()).thenReturn(operationList);
        Mockito.when(operationList.operation()).thenReturn(List.of(operationContext));
        Mockito.when(ctx.unionOperator()).thenReturn(unionContext);
        Mockito.when(unionContext.getChild(0)).thenReturn(unionChild);
        Mockito.when(unionChild.getText()).thenReturn("OR");
        Mockito.when(operationContext.comparisonOperation()).thenReturn(comparisonContext);
        Mockito.when(criteriaBuilder.or(predicate)).thenReturn(orPredicate);

        Mockito.doReturn(predicate)
            .when(spy).visitComparisonOperation(comparisonContext);

        Assertions.assertThat(spy.visitOperation(ctx))
            .isEqualTo(orPredicate);

        Mockito.verify(ctx).operatorList();
        Mockito.verify(ctx).comparisonOperation();
        Mockito.verify(ctx).negationOperator();
        Mockito.verify(operationList).operation();
        Mockito.verify(ctx, Mockito.times(2)).unionOperator();
        Mockito.verify(unionContext).getChild(0);
        Mockito.verify(unionChild).getText();
        Mockito.verify(operationContext, Mockito.times(2)).comparisonOperation();
        Mockito.verify(spy).visitComparisonOperation(comparisonContext);
        Mockito.verify(criteriaBuilder).or(predicate);

        Mockito.verifyNoMoreInteractions(criteriaBuilder, root, predicate, ctx, operationContext, comparisonContext, operationList, unionContext, unionChild,
            orPredicate);
    }


    @Test
    void visitOperationWhenUnionOperationUnknownError() {
        OperationContext ctx = Mockito.mock(OperationContext.class);
        UnionOperatorContext unionOperationContext = Mockito.mock(UnionOperatorContext.class);
        ParseTree unionChild = Mockito.mock(ParseTree.class);
        String union = Instancio.create(String.class);

        Mockito.when(ctx.unionOperator()).thenReturn(unionOperationContext);
        Mockito.when(unionOperationContext.getChild(0)).thenReturn(unionChild);
        Mockito.when(unionChild.getText()).thenReturn(union);

        Assertions.assertThatThrownBy(() -> storeItemQueryBaseVisitor.visitOperation(ctx))
            .asInstanceOf(InstanceOfAssertFactories.throwable(ApplicationException.class))
            .matches(it -> StringUtils.equals(it.getCode(), "error.query.visitor.list.operation-unknown"))
            .extracting(ApplicationException::getValues)
            .asInstanceOf(InstanceOfAssertFactories.MAP)
            .containsOnly(
                Map.entry("union", union)
            );

        Mockito.verify(ctx).comparisonOperation();
        Mockito.verify(ctx).negationOperator();
        Mockito.verify(ctx, Mockito.times(2)).unionOperator();
        Mockito.verify(unionOperationContext).getChild(0);
        Mockito.verify(unionChild).getText();

        Mockito.verifyNoMoreInteractions(unionOperationContext, unionChild, ctx, root, criteriaBuilder);
    }

    @Test
    void visitOperationWhenOperationUnknownError() {
        OperationContext ctx = Mockito.mock(OperationContext.class);
        String text = Instancio.create(String.class);

        Mockito.when(ctx.getText()).thenReturn(text);

        Assertions.assertThatThrownBy(() -> storeItemQueryBaseVisitor.visitOperation(ctx))
            .asInstanceOf(InstanceOfAssertFactories.throwable(ApplicationException.class))
            .matches(it -> StringUtils.equals(it.getCode(), "error.query.visitor.operation-unknown"))
            .extracting(ApplicationException::getValues)
            .asInstanceOf(InstanceOfAssertFactories.MAP)
            .containsOnly(
                Map.entry("text", text)
            );

        Mockito.verify(ctx).comparisonOperation();
        Mockito.verify(ctx).negationOperator();
        Mockito.verify(ctx).unionOperator();
        Mockito.verify(ctx).getText();

        Mockito.verifyNoMoreInteractions(ctx, root, criteriaBuilder);
    }
}