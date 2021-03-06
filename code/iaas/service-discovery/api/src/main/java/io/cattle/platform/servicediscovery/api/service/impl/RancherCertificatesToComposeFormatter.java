package io.cattle.platform.servicediscovery.api.service.impl;

import io.cattle.platform.core.model.Certificate;
import io.cattle.platform.db.jooq.dao.impl.AbstractJooqDao;
import io.cattle.platform.object.ObjectManager;
import io.cattle.platform.servicediscovery.api.resource.ServiceDiscoveryConfigItem;
import io.cattle.platform.servicediscovery.api.service.RancherConfigToComposeFormatter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

@Named
public class RancherCertificatesToComposeFormatter extends AbstractJooqDao
        implements RancherConfigToComposeFormatter {

    @Inject
    ObjectManager objManager;

    @Override
    @SuppressWarnings("unchecked")
    public Object format(ServiceDiscoveryConfigItem item, Object valueToTransform) {
        if (item.getDockerName().equalsIgnoreCase(ServiceDiscoveryConfigItem.CERTIFICATES.getDockerName())) {
            List<Number> certificateIds = (List<Number>) valueToTransform;
            List<String> certificateNames = new ArrayList<>();
            for (Number certificateId : certificateIds) {
                String certName = getCertName(certificateId);
                if (StringUtils.isNotBlank(certName)) {
                    certificateNames.add(certName);
                }
            }
            return certificateNames;
        } else if (item.getDockerName().equals(ServiceDiscoveryConfigItem.DEFAULT_CERTIFICATE.getDockerName())) {
            Integer defaultCertId = (Integer) valueToTransform;
            return getCertName(defaultCertId);
        } else {
            return null;
        }
    }

    private String getCertName(Number certId) {
        if (certId == null) {
            return null;
        }
        Certificate cert = objManager.loadResource(Certificate.class, certId.longValue());
        return cert == null ? null : cert.getName();
    }
}
