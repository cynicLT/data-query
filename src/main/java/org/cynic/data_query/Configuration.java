package org.cynic.data_query;

import java.time.Clock;
import java.util.function.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.cynic.data_query.antlr.query.QueryLexer;
import org.cynic.data_query.antlr.query.QueryParser;
import org.cynic.data_query.framework.antlr.CustomAntlrErrorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration(proxyBeanMethods = false)
@ImportAutoConfiguration({
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    TransactionAutoConfiguration.class,
    JpaRepositoriesAutoConfiguration.class,
    LiquibaseAutoConfiguration.class,

    JacksonAutoConfiguration.class,

    HttpMessageConvertersAutoConfiguration.class,

    ServletWebServerFactoryAutoConfiguration.class,
    DispatcherServletAutoConfiguration.class,
    WebMvcAutoConfiguration.class
})
@ComponentScan(excludeFilters = {
    @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {TypeExcludeFilter.class}),
    @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {AutoConfigurationExcludeFilter.class})
})
@EnableJpaRepositories
@EntityScan("org.cynic.data_query.domain.entity")
public class Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    public Configuration() {
        Package pkg = getClass().getPackage();

        LOGGER.info(Constants.AUDIT_MARKER,
            "[{}-{}] STARTED",
            pkg.getImplementationTitle(),
            pkg.getImplementationVersion()
        );
    }

    @Bean
    public Clock clock() {
        return Clock.system(Constants.SYSTEM_ZONE_ID);
    }

    @Bean
    public Function<String, QueryParser> queryParser() {
        CustomAntlrErrorListener errorListener = new CustomAntlrErrorListener();

        return query -> {
            QueryLexer lexer = new QueryLexer(CharStreams.fromString(query));
            lexer.removeErrorListeners();
            lexer.addErrorListener(errorListener);

            QueryParser parser = new QueryParser(new CommonTokenStream(lexer));
            parser.removeParseListeners();
            parser.addErrorListener(errorListener);

            return parser;
        };
    }
}