package eco.ywhc.xr.common.model;

import eco.ywhc.xr.common.constant.ClueStatusType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClueStatus {

    public static final Map<ClueStatusType, List<ClueStatusType>> map = new HashMap<>();

    @PostConstruct
    public void initClueStatus() {
        map.put(ClueStatusType.CLUE_DISCOVERY, List.of(ClueStatusType.CLUE_ENTRY));
        map.put(ClueStatusType.CLUE_ENTRY, List.of(ClueStatusType.CLUE_STORAGE_EVALUATION));
        map.put(ClueStatusType.CLUE_STORAGE_EVALUATION, List.of(ClueStatusType.CLUE_EVALUATION));
        map.put(ClueStatusType.CLUE_EVALUATION, List.of(ClueStatusType.CLUE_FOLLOW, ClueStatusType.CLUE_CLOSE));
        map.put(ClueStatusType.CLUE_FOLLOW, List.of(ClueStatusType.CLUE_DERIVABLE, ClueStatusType.CLUE_PROPOSABLE, ClueStatusType.CLUE_CLOSE));
        map.put(ClueStatusType.CLUE_PROPOSABLE, List.of(ClueStatusType.CLUE_APPROVE));
        map.put(ClueStatusType.CLUE_APPROVE, List.of());
        map.put(ClueStatusType.CLUE_CLOSE, List.of());
    }

    public static Map<ClueStatusType, List<ClueStatusType>> getMap() {
        return map;
    }

}
