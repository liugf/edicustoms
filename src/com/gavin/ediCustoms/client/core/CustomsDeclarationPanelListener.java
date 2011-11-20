package com.gavin.ediCustoms.client.core;

import com.gavin.ediCustoms.entity.edi.PermitedEnterprise;
import com.gavin.ediCustoms.entity.edi.core.CustomsDeclarationHead;

public interface CustomsDeclarationPanelListener {
	void changePermitedEnterprise(PermitedEnterprise permitedEnterprise);
	void changeCustomsDeclarationHead(CustomsDeclarationHead selectedCustomsDeclarationHead);
}
