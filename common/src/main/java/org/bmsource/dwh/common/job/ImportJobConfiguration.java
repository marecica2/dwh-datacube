package org.bmsource.dwh.common.job;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ImportJobConfiguration<RawFact, Fact> {

    private RawFact baseEntity;

    private Fact mappedEntity;

    private BiFunction<ImportContext, RawFact, Fact> mapper;

    private Function<ImportContext, Object> cleanUpHandler;

    public RawFact getBaseEntity() {
        return baseEntity;
    }

    public void setBaseEntity(RawFact baseEntity) {
        this.baseEntity = baseEntity;
    }

    public Fact getMappedEntity() {
        return mappedEntity;
    }

    public void setMappedEntity(Fact mappedEntity) {
        this.mappedEntity = mappedEntity;
    }

    public BiFunction<ImportContext, RawFact, Fact> getMapper() {
        return mapper;
    }

    public void setMapper(BiFunction<ImportContext, RawFact, Fact> mapper) {
        this.mapper = mapper;
    }

    public void setCleanUpHandler(Function<ImportContext, Object> cleanUpHandler) {
        this.cleanUpHandler = cleanUpHandler;
    }

    public Object performCleanUp(ImportContext ctx) {
        return cleanUpHandler.apply(ctx);
    }
}
