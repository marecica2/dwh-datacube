package org.bmsource.dwh.common.job.step.normalizer;

import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.job.ImportContext;
import org.bmsource.dwh.common.job.ImportJobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.function.Function;

@Configuration
public class NormalizerStepConfiguration<RawFact extends BaseFact, Fact extends BaseFact> {

    private Logger logger = LoggerFactory.getLogger(NormalizerStepConfiguration.class);

    private static final int BATCH_SIZE = 5000;

    private RawFact rawFact;

    private Fact fact;

    private ItemProcessor<RawFact, Fact> normalizerProcessor;

    @Autowired
    public void setImportJobConfiguration(ImportJobConfiguration<RawFact, Fact> importJobConfiguration) {
        // TODO FIX ME!!!!
        ImportContext ctx = new ImportContext();
        ctx.setProjectId("1");
        this.rawFact = importJobConfiguration.getBaseEntity();
        this.fact = importJobConfiguration.getMappedEntity();
        this.normalizerProcessor = item -> importJobConfiguration.getMapper().apply(ctx, item);
    }

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }


    public JdbcBatchItemWriter<Fact> jdbcWriter() {
        JdbcBatchItemWriter<Fact> jdbcWriter = new JdbcBatchItemWriterBuilder<Fact>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql(writeSql())
            .dataSource(dataSource)
            .build();
        jdbcWriter.afterPropertiesSet();
        return jdbcWriter;
    }

    ItemReader<RawFact> jdbcReader() {
        JdbcCursorItemReader<RawFact> databaseReader = new JdbcCursorItemReader<>();
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(readSql());
        databaseReader.setRowMapper(new BeanPropertyRowMapper<>((Class<RawFact>) rawFact.getClass()));
        return databaseReader;
    }

    @Bean(name = "normalizerStep")
    public Step normalizerStep() {
        SimpleStepBuilder<RawFact, Fact> step = stepBuilderFactory.get("normalizerStep")
            .<RawFact, Fact>chunk(BATCH_SIZE)
            .reader(jdbcReader())
            .processor(normalizerProcessor)
            .writer(jdbcWriter());
        return step
            .build();
    }

    private String readSql() {
        String sql = null;
        try {
            sql = rawFact.getClass().newInstance().selectSQL();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return sql;
    }

    private String writeSql() {
        String sql = null;
        try {
            sql = fact.getClass().newInstance().insertSQL();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return sql;
    }
}
