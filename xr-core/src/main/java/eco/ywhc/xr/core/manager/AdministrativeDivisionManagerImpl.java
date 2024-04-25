package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import eco.ywhc.xr.common.converter.AdministrativeDivisionConverter;
import eco.ywhc.xr.common.model.dto.res.AdministrativeDivisionRes;
import eco.ywhc.xr.common.model.entity.AdministrativeDivision;
import eco.ywhc.xr.common.model.query.AdministrativeDivisionQuery;
import eco.ywhc.xr.core.mapper.AdministrativeDivisionMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdministrativeDivisionManagerImpl implements AdministrativeDivisionManager {

    private static final ConcurrentHashMap<Long, AdministrativeDivision> ADMINISTRATIVE_DIVISION_MAP1 = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Long, AdministrativeDivisionRes> ADMINISTRATIVE_DIVISION_MAP2 = new ConcurrentHashMap<>();

    private final CacheManager cacheManager;

    private final AdministrativeDivisionConverter administrativeDivisionConverter;

    private final AdministrativeDivisionMapper administrativeDivisionMapper;

    @PostConstruct
    private void initialize() {
        if (ADMINISTRATIVE_DIVISION_MAP1.isEmpty()) {
            findAllEntities().forEach(i -> {
                ADMINISTRATIVE_DIVISION_MAP1.put(i.getAdcode(), i);
            });
        }
        if (ADMINISTRATIVE_DIVISION_MAP2.isEmpty()) {
            findAll().forEach(i -> {
                ADMINISTRATIVE_DIVISION_MAP2.put(i.getAdcode(), i);
            });
        }
    }

    @Override
    public List<AdministrativeDivision> findAllEntitiesByAdcodes(Collection<Long> adcodes) {
        return administrativeDivisionMapper.findAllByIds(adcodes);
    }

    @Override
    public List<AdministrativeDivision> findAllEntities() {
        Cache administrativeDivisionCache = cacheManager.getCache("administrativeDivisions");
        if (administrativeDivisionCache == null) {
            log.error("缓存操作异常");
            throw new InternalErrorException("获取行政区划列表失败");
        }
        try {
            return administrativeDivisionCache.retrieve("divisions", this::findAllEntitiesAsync).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new InternalErrorException("获取行政区划列表失败");
        }
    }

    @Override
    public List<AdministrativeDivisionRes> findAll() {
        return findAllEntities().stream()
                .map(administrativeDivisionConverter::toResponse)
                .toList();
    }

    @Override
    public Map<Long, AdministrativeDivision> findAllEntitiesAsMap() {
        return ADMINISTRATIVE_DIVISION_MAP1;
    }

    @Override
    public Map<Long, AdministrativeDivisionRes> findAllAsMap() {
        return ADMINISTRATIVE_DIVISION_MAP2;
    }

    @Override
    public List<AdministrativeDivisionRes> findMany(AdministrativeDivisionQuery query) {
        QueryWrapper<AdministrativeDivision> qw = Wrappers.query();
        qw.lambda().eq(AdministrativeDivision::getDeleted, 0)
                .eq(AdministrativeDivision::getParent, query.getParent())
                .orderByAsc(AdministrativeDivision::getAdcode);
        List<AdministrativeDivisionRes> administrativeDivisions = administrativeDivisionMapper.selectList(qw).stream()
                .map(administrativeDivisionConverter::toResponse)
                .toList();

        List<AdministrativeDivisionRes> current = new ArrayList<>(administrativeDivisions);
        Map<Long, List<AdministrativeDivisionRes>> parentChildrenMap = new HashMap<>();
        int depth = query.getDepth();
        if (depth > 0) {
            parentChildrenMap = groupByParent();
        }
        while (depth > 0) {
            for (AdministrativeDivisionRes i : current) {
                var children = parentChildrenMap.getOrDefault(i.getAdcode(), new ArrayList<>());
                i.setChildren(children);
            }
            current = current.stream().map(AdministrativeDivisionRes::getChildren)
                    .flatMap(Collection::stream)
                    .toList();
            depth--;
        }

        return administrativeDivisions;
    }

    @Override
    public AdministrativeDivisionRes findByAdcode(long adcode) {
        AdministrativeDivision entity = administrativeDivisionMapper.findEntityById(adcode);
        if (entity == null) {
            throw new ResourceNotFoundException("该行政区不存在");
        }
        return administrativeDivisionConverter.toResponse(entity);
    }

    @Override
    public AdministrativeDivisionRes findByAdcodeSurely(long adcode) {
        AdministrativeDivision entity = administrativeDivisionMapper.findEntityById(adcode);
        return administrativeDivisionConverter.toResponse(entity);
    }

    @Override
    public List<AdministrativeDivisionRes> findAllByAdcodesSurely(Collection<Long> adcodes) {
        return findAllEntitiesByAdcodes(adcodes).stream()
                .map(administrativeDivisionConverter::toResponse)
                .toList();
    }

    @Override
    public Map<Long, AdministrativeDivisionRes> findAllAsMapByAdcodesSurely(Collection<Long> adcodes) {
        return findAllByAdcodesSurely(adcodes).stream()
                .collect(Collectors.toMap(AdministrativeDivisionRes::getAdcode, Function.identity()));
    }

    @Override
    public Map<Short, AdministrativeDivision> analyzeByAdcode(long adcode) {
        AdministrativeDivision administrativeDivision = ADMINISTRATIVE_DIVISION_MAP1.get(adcode);
        if (administrativeDivision == null) {
            return Collections.emptyMap();
        }
        Map<Short, AdministrativeDivision> result = new HashMap<>();
        short level = administrativeDivision.getLevel();
        do {
            result.put(level, administrativeDivision);
            administrativeDivision = ADMINISTRATIVE_DIVISION_MAP1.get(administrativeDivision.getParent());
            if (administrativeDivision == null) {
                break;
            }
            level = administrativeDivision.getLevel();
        } while (level >= 0);
        return result;
    }

    private CompletableFuture<List<AdministrativeDivision>> findAllEntitiesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            QueryWrapper<AdministrativeDivision> qw = Wrappers.query();
            qw.lambda().eq(AdministrativeDivision::getDeleted, false)
                    .orderByAsc(AdministrativeDivision::getAdcode);
            return administrativeDivisionMapper.selectList(qw);
        });
    }

    private Map<Long, List<AdministrativeDivisionRes>> groupByParent(Collection<AdministrativeDivisionRes> divisions) {
        if (CollectionUtils.isEmpty(divisions)) {
            return Collections.emptyMap();
        }
        return divisions.stream()
                .filter(i -> i.getParent() != null)
                .collect(Collectors.groupingBy(AdministrativeDivisionRes::getParent));
    }

    private Map<Long, List<AdministrativeDivisionRes>> groupByParent() {
        return groupByParent(findAll());
    }

}
