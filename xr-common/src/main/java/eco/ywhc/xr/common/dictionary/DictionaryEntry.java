package eco.ywhc.xr.common.dictionary;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 字典条目
 */
@Getter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class DictionaryEntry {

    /**
     * 标签
     */
    private final String label;

    /**
     * 值
     */
    private final String value;

    /**
     * 子条目
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<DictionaryEntry> entries;

}
