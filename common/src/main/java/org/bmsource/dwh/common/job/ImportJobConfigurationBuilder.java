package org.bmsource.dwh.common.job;

import java.util.function.Function;

public final class ImportJobConfigurationBuilder<RawFact, Fact> {
    private RawFact baseEntity;
    private Fact mappedEntity;
    private Function<RawFact, Fact> mapper;
    private Function<ImportContext, Object> cleanUpHandler;

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

    /**
     * Callback called before starting the import.
     * Cleaning logic from previous state should be placed here
     * @param cleanUpHandler Handler to handle the cleanup
     * @return should return 0 when everything else is treated as error
     */
    public ImportJobConfigurationBuilder<RawFact, Fact> onCleanUp(Function<ImportContext, Object> cleanUpHandler) {
        this.cleanUpHandler = cleanUpHandler;
        return this;
    }

    public ImportJobConfiguration<RawFact, Fact> build() {
        ImportJobConfiguration<RawFact, Fact> importJobConfiguration = new ImportJobConfiguration<>();
        importJobConfiguration.setBaseEntity(baseEntity);
        importJobConfiguration.setMappedEntity(mappedEntity);
        importJobConfiguration.setMapper(mapper);
        importJobConfiguration.setCleanUpHandler(cleanUpHandler);
        return importJobConfiguration;
    }

}
