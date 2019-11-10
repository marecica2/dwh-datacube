package org.bmsource.dwh.common.job.step.normalizer;

import org.bmsource.dwh.common.BaseFact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
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

@Configuration
public class NormalizerStepConfiguration<RawFact extends BaseFact, Fact extends BaseFact> {

    private Logger logger = LoggerFactory.getLogger(NormalizerStepConfiguration.class);

    private static final int BATCH_SIZE = 5000;

    private static final int MAX_CONCURRENT_FILES = 10;

    private RawFact rawFact;

    @Autowired
    @Qualifier("rawFact")
    public void setRawFact(RawFact rawFact) {
        this.rawFact = rawFact;
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
            sql = rawFact.getClass().newInstance().insertSQL();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return sql;
    }
}