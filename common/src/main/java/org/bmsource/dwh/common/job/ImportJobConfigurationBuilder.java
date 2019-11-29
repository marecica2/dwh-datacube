package org.bmsource.dwh.common.job;

import java.util.function.Function;

public final class ImportJobConfigurationBuilder<RawFact, Fact> {
    private RawFact baseEntity;
    private Fact mappedEntity;
    private Function<RawFact, Fact> mapper;

    private ImportJobConfigurationBuilder() {
    }

    public static <RawFact, Fact> ImportJobConfigurationBuilder<RawFact, Fact> get() {
        return new ImportJobConfigurationBuilder<>();
    }

    public ImportJobConfigurationBuilder<RawFact, Fact> withBaseEntity(RawFact baseEntity) {
        this.baseEntity = baseEntity;
        return this;
    }

    public ImportJobConfigurationBuilder<RawFact, Fact> withMappedEntity(Fact mappedEntity) {
        this.mappedEntity = mappedEntity;
        return this;
    }

    public ImportJobConfigurationBuilder<RawFact, Fact> withMapper(Function<RawFact, Fact> mapper) {
        this.mapper = mapper;
        return this;
    }

    public ImportJobConfiguration<RawFact, Fact> build() {
        ImportJobConfiguration<RawFact, Fact> importJobConfiguration = new ImportJobConfiguration<>();
        importJobConfiguration.setBaseEntity(baseEntity);
        importJobConfiguration.setMappedEntity(mappedEntity);
        importJobConfiguration.setMapper(mapper);
        return importJobConfiguration;
    }
}
