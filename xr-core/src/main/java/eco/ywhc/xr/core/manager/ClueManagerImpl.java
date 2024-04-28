package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.core.mapper.ClueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.Year;

/**
 * 基础线索信息(eco.ywhc.xr.common.model.entity.BClue)表服务实现类
 *
 * @author makejava
 * @since 2024-04-18 11:51:02
 */
@Service
@RequiredArgsConstructor
public class ClueManagerImpl implements ClueManager {

    private final ClueMapper clueMapper;

    @Override
    public Clue findEntityById(@NonNull Long id) {
        return clueMapper.findEntityById(id);
    }

    @Override
    public String generateUniqueId() {
        QueryWrapper<Clue> qw = new QueryWrapper<>();
        qw.select("id", "clue_code").orderByDesc("id").last("LIMIT 1");
        var clue = clueMapper.selectOne(qw);
        int num = 1;
        if (clue != null) {
            String numString = clue.getClueCode().substring(6);
            num = Integer.parseInt(numString) + 1;
        }
        String formattedNum = String.format("%03d", num);
        return "XS" + Year.now() + formattedNum;
    }

}
