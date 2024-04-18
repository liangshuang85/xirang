package eco.ywhc.xr.common.model;

import eco.ywhc.xr.common.constant.ProjectType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProjectStatus {

    public static final Map<ProjectType, List<ProjectType>> map = new HashMap<>();

    @PostConstruct
    public void initProjectStatus() {
        map.put(ProjectType.PENDING_PROJECT_MEETING, List.of(ProjectType.INVESTMENT_AGREEMENT_DRAFTING, ProjectType.PENDING_DECISION));
        map.put(ProjectType.INVESTMENT_AGREEMENT_DRAFTING, List.of(ProjectType.INVESTMENT_AGREEMENT_INTERNAL_REVIEW));
        map.put(ProjectType.INVESTMENT_AGREEMENT_INTERNAL_REVIEW, List.of(ProjectType.PENDING_DECISION));
        map.put(ProjectType.PENDING_DECISION, List.of(ProjectType.PENDING_PROJECT_MEETING, ProjectType.INVESTMENT_AGREEMENT_Final_Review));
        map.put(ProjectType.INVESTMENT_AGREEMENT_Final_Review, List.of(ProjectType.INVESTMENT_AGREEMENT_SIGNING));
        map.put(ProjectType.INVESTMENT_AGREEMENT_SIGNING, List.of(ProjectType.ENTERPRISE_FILING));
        map.put(ProjectType.ENTERPRISE_FILING, List.of(ProjectType.FILING_COMPLETED));
    }

    public static Map<ProjectType, List<ProjectType>> getMap() {
        return map;
    }

}
