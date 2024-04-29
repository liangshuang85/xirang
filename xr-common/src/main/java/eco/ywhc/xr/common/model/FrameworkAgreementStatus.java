package eco.ywhc.xr.common.model;

import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FrameworkAgreementStatus {

    public static final Map<FrameworkAgreementType, List<FrameworkAgreementType>> map = new HashMap<>();

    @PostConstruct
    public void initFrameworkAgreementStatus() {
        map.put(FrameworkAgreementType.PRE_PROJECT, List.of(FrameworkAgreementType.PROJECT_PROPOSAL_DRAFTING));
        map.put(FrameworkAgreementType.PROJECT_PROPOSAL_DRAFTING, List.of(FrameworkAgreementType.PROJECT_PROPOSAL_INTERNAL_REVIEW));
        map.put(FrameworkAgreementType.PROJECT_PROPOSAL_INTERNAL_REVIEW, List.of(FrameworkAgreementType.GOVERNMENT_APPROVAL));
        map.put(FrameworkAgreementType.GOVERNMENT_APPROVAL, List.of(FrameworkAgreementType.FRAMEWORK_AGREEMENT_DRAFTING, FrameworkAgreementType.GOVERNMENT_APPROVAL_REFUSED));
        map.put(FrameworkAgreementType.FRAMEWORK_AGREEMENT_DRAFTING, List.of(FrameworkAgreementType.PENDING_PROJECT_MEETING));
        map.put(FrameworkAgreementType.PENDING_PROJECT_MEETING, List.of(FrameworkAgreementType.FRAMEWORK_AGREEMENT_INTERNAL_REVIEW));
        map.put(FrameworkAgreementType.FRAMEWORK_AGREEMENT_INTERNAL_REVIEW, List.of(FrameworkAgreementType.FRAMEWORK_AGREEMENT_SIGNING));
        map.put(FrameworkAgreementType.GOVERNMENT_APPROVAL_REFUSED, List.of());
        map.put(FrameworkAgreementType.FRAMEWORK_AGREEMENT_SIGNING, List.of());
    }

    public static Map<FrameworkAgreementType, List<FrameworkAgreementType>> getMap() {
        return map;
    }

}
