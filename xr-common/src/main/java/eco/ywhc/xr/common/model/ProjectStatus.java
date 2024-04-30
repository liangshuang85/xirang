package eco.ywhc.xr.common.model;

import eco.ywhc.xr.common.constant.ProjectStatusType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProjectStatus {

    public static final Map<ProjectStatusType, List<ProjectStatusType>> map = new HashMap<>();

    @PostConstruct
    public void initProjectStatus() {
        map.put(ProjectStatusType.NEW, List.of(ProjectStatusType.INVESTMENT_AGREEMENT_DRAFTING));
        map.put(ProjectStatusType.INVESTMENT_AGREEMENT_DRAFTING, List.of(ProjectStatusType.INVESTMENT_AGREEMENT_INTERNAL_REVIEW));
        map.put(ProjectStatusType.INVESTMENT_AGREEMENT_INTERNAL_REVIEW, List.of(ProjectStatusType.PENDING_DECISION));
        map.put(ProjectStatusType.PENDING_DECISION, List.of(ProjectStatusType.INVESTMENT_AGREEMENT_DRAFTING, ProjectStatusType.INVESTMENT_AGREEMENT_FINAL_REVIEW));
        map.put(ProjectStatusType.INVESTMENT_AGREEMENT_FINAL_REVIEW, List.of(ProjectStatusType.INVESTMENT_AGREEMENT_SIGNING));
        map.put(ProjectStatusType.INVESTMENT_AGREEMENT_SIGNING, List.of(ProjectStatusType.ENTERPRISE_FILING));
        map.put(ProjectStatusType.ENTERPRISE_FILING, List.of(ProjectStatusType.FILING_COMPLETED));
        map.put(ProjectStatusType.FILING_COMPLETED, List.of());
    }

    public static Map<ProjectStatusType, List<ProjectStatusType>> getMap() {
        return map;
    }

}
