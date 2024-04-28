package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import eco.ywhc.xr.common.model.dto.res.ProvinceResourceCountRes;
import eco.ywhc.xr.common.model.entity.AdministrativeDivision;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.core.manager.AdministrativeDivisionManager;
import eco.ywhc.xr.core.mapper.ClueMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementMapper;
import eco.ywhc.xr.core.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final AdministrativeDivisionManager administrativeDivisionManager;

    private final ClueMapper clueMapper;

    private final ProjectMapper projectMapper;

    private final FrameworkAgreementMapper frameworkAgreementMapper;

    @Override
    public List<ProvinceResourceCountRes> summarizeProvinceResource() {
        List<AdministrativeDivision> provinces = administrativeDivisionManager.findAllEntities().stream()
                .filter(i -> i.getLevel() == (short) 1)
                .sorted(Comparator.comparing(AdministrativeDivision::getAdcode))
                .toList();
        Map<Long, Long> provinceClueMap = groupingAndCountClue();
        Map<Long, Long> provinceFrameworkAgreementMap = groupingAndCountFrameworkAgreement();
        Map<Long, Long> provinceProjectMap = groupingAndCountProject();
        return provinces.stream()
                .map(province -> {
                    Long clueCount = provinceClueMap.getOrDefault(province.getAdcode(), 0L);
                    Long frameworkAgreementCount = provinceFrameworkAgreementMap.getOrDefault(province.getAdcode(), 0L);
                    Long projectCount = provinceProjectMap.getOrDefault(province.getAdcode(), 0L);
                    return ProvinceResourceCountRes.of(province.getAdcode(), province.getName(), clueCount, frameworkAgreementCount, projectCount);
                })
                .toList();
    }

    private Map<Long, Long> groupingAndCountClue() {
        QueryWrapper<Clue> qw = Wrappers.query();
        qw.lambda().eq(Clue::getDeleted, false);
        List<Long> adcodes = clueMapper.selectList(qw).stream().map(Clue::getAdcode).toList();
        return countProvinces(adcodes);
    }

    private Map<Long, Long> groupingAndCountFrameworkAgreement() {
        QueryWrapper<FrameworkAgreement> qw = Wrappers.query();
        qw.lambda().eq(FrameworkAgreement::getDeleted, false);
        List<Long> adcodes = frameworkAgreementMapper.selectList(qw).stream().map(FrameworkAgreement::getAdcode).toList();
        return countProvinces(adcodes);
    }

    private Map<Long, Long> groupingAndCountProject() {
        QueryWrapper<Project> qw = Wrappers.query();
        qw.lambda().eq(Project::getDeleted, false);
        List<Long> adcodes = projectMapper.selectList(qw).stream().map(Project::getAdcode).toList();
        return countProvinces(adcodes);
    }

    /**
     * 计算行政区划代码列表中各个省份的数量
     *
     * @param adcodes 行政区划代码列表
     * @return Key为省份行政区划代码，Value为省份数量
     */
    private Map<Long, Long> countProvinces(Collection<Long> adcodes) {
        return adcodes.stream()
                .map(adcode -> {
                    Map<Short, AdministrativeDivision> administrativeDivision = administrativeDivisionManager.analyzeByAdcode(adcode);
                    AdministrativeDivision province = administrativeDivision.get((short) 1);
                    return Optional.ofNullable(province).map(AdministrativeDivision::getAdcode).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

}
