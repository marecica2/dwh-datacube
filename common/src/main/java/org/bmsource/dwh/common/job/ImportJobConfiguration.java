package org.bmsource.dwh.common.job;

import java.util.function.Function;

public class ImportJobConfiguration<RawFact, Fact> {

    private RawFact baseEntity;

    private Fact mappedEntity;

    private Function<RawFact, Fact> mapper;

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

    public Function<RawFact, Fact> getMapper() {
        return mapper;
    }

    public void setMapper(Function<RawFact, Fact> mapper) {
        this.mapper = mapper;
    }
}
