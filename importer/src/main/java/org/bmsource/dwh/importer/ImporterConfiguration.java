package org.bmsource.dwh.importer;

import org.bmsource.dwh.common.job.EnableImportJob;
import org.bmsource.dwh.common.job.ImportJobConfiguration;
import org.bmsource.dwh.common.job.ImportJobConfigurationBuilder;
import org.bmsource.dwh.common.utils.StringUtils;
import org.bmsource.dwh.model.Fact;
import org.bmsource.dwh.model.RawFact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableImportJob
@Configuration
public class ImporterConfiguration {

    @Bean
    ImportJobConfiguration<RawFact, Fact> importJobConfiguration() {
        return ImportJobConfigurationBuilder.<RawFact, Fact>get()
            .withBaseEntity(new RawFact())
            .withMappedEntity(new Fact())
            .withMapper(item -> {
                Fact fact = new Fact();
                fact.setTransactionId(item.getTransactionId());
                fact.setBusinessUnit(item.getBusinessUnit());
                fact.setServiceType(StringUtils.normalize(item.getServiceType()));

                fact.setOriginCity(item.getOriginCity());
                fact.setOriginState(item.getOriginState());
                fact.setOriginZip(item.getOriginZip());
                fact.setOriginCountry(item.getOriginCountry());

                fact.setDestinationCity(item.getDestinationCity());
                fact.setDestinationState(item.getDestinationState());
                fact.setDestinationCountry(item.getDestinationCountry());
                fact.setDestinationZip(item.getDestinationZip());

                fact.setDeliveryDate(item.getDeliveryDate());
                fact.setShipmentDate(item.getShipmentDate());
                fact.setCost(item.getCost());
                fact.setBillableWeight(item.getBillableWeight());
                fact.setActualWeight(item.getActualWeight());
                fact.setLength(item.getLength());
                fact.setWidth(item.getWidth());
                fact.setHeight(item.getHeight());

                fact.setAccessorialService1(item.getAccessorialService1());
                fact.setAccessorialCharge1(item.getAccessorialCharge1());
                fact.setAccessorialService2(item.getAccessorialService2());
                fact.setAccessorialCharge2(item.getAccessorialCharge2());
                return fact;
            })
            .build();
    }
}
