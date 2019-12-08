package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.common.utils.StringUtils;
import org.bmsource.dwh.masterdata.model.RateCard;
import org.bmsource.dwh.masterdata.model.ServiceTypeMapping;
import org.bmsource.dwh.masterdata.model.Taxonomy;

import java.util.function.Function;

public class MasterDataNormalizer {
    public static Function<Taxonomy, Taxonomy> normalizeTaxonomy = item -> {
        if(item == null)
            return null;
        item.setExpedite(item.getExpedite());
        item.setName(StringUtils.normalize(item.getName()));
        item.setStandardServiceTypeGroup(StringUtils.normalize(item.getStandardServiceTypeGroup()));
        return item;
    };

    public static Function<ServiceTypeMapping, ServiceTypeMapping> normalizeServiceTypeMapping = item -> {
        if(item == null)
            return null;
        item.setStandardServiceTypeLetter(StringUtils.normalize(item.getStandardServiceTypeLetter()));
        item.setStandardServiceTypeParcel(StringUtils.normalize(item.getStandardServiceTypeParcel()));
        item.setSupplierName(StringUtils.normalize(item.getSupplierName()));
        item.setSupplierServiceType(StringUtils.normalize(item.getSupplierServiceType()));
        return item;
    };

    public static Function<RateCard, RateCard> normalizeRateCards = item -> {
        if(item == null)
            return null;
        item.setSupplierName(StringUtils.normalize(item.getSupplierName()));
        item.setSupplierServiceType(StringUtils.normalize(item.getSupplierServiceType()));
        return item;
    };
}
