package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.dictionary.DictionaryEntry;
import eco.ywhc.xr.common.dictionary.DictionaryLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据字典接口
 **/
@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryLoader dictionaryLoader;

    /**
     * 获取数据字典全部的条目
     */
    @GetMapping("/dictionary/entries")
    public List<DictionaryEntry> getAllEntries() {
        return dictionaryLoader.getEntries();
    }

    /**
     * 获取数据字典指定的条目
     *
     * @param entry 条目值
     */
    @GetMapping("/dictionary/entries/{entry}")
    public List<DictionaryEntry> getEntries(@PathVariable String entry) {
        return dictionaryLoader.getEntries(entry);
    }

}
