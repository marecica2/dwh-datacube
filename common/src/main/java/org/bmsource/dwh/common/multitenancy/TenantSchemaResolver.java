package org.bmsource.dwh.common.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

    @Override
	public String resolveCurrentTenantIdentifier() {
		String t =  TenantContext.getTenantSchema();
		if(t!=null){
			return t;
		} else {
			return Constants.DEFAULT_TENANT;
		}
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
