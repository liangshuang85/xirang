package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.oapi.Client;
import eco.ywhc.xr.common.converter.ClueConverter;
import eco.ywhc.xr.common.model.entity.ChannelEntry;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.core.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    private final ApprovalMapper approvalMapper;

    private final FundingMapper fundingMapper;

    private final VisitMapper visitMapper;

    private final ChannelEntryMapper channelEntryMapper;

    private final ClueConverter clueConverter;

    private final Client client;

    @Override
    public Clue findEntityById(@NonNull Long id) {
        return clueMapper.findEntityById(id);
    }

    @Override
    public ChannelEntry getChannelEntryByClueId(long id) {
        QueryWrapper<ChannelEntry> qw = new QueryWrapper<>();
        qw.lambda().eq(ChannelEntry::getDeleted, false)
                .eq(ChannelEntry::getClueId, id);

        return channelEntryMapper.selectOne(qw);
    }

    @Override
    public Serializable getFundingIdByClueId(long id) {
        return getChannelEntryByClueId(id).getId();
    }

    @Override
    public String generateUniqueId() {
        QueryWrapper<Clue> qw = new QueryWrapper<>();
        qw.select("MAX(`id`) AS `id`)");
        var clue = clueMapper.selectOne(qw);
        int num = 1;
        if (clue != null) {
            String numString = clue.getClueCode().substring(6);
            num = Integer.parseInt(numString);
        }
        return "XS" + Year.now() + num;
    }

}
