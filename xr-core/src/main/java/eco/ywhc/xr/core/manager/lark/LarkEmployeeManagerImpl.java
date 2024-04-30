package eco.ywhc.xr.core.manager.lark;

import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.service.contact.v3.enums.GetUserDepartmentIdTypeEnum;
import com.lark.oapi.service.contact.v3.enums.GetUserUserIdTypeEnum;
import com.lark.oapi.service.contact.v3.model.GetUserReq;
import com.lark.oapi.service.contact.v3.model.GetUserResp;
import com.lark.oapi.service.ehr.v1.enums.ListEmployeeUserIdTypeEnum;
import com.lark.oapi.service.ehr.v1.enums.ListEmployeeViewEnum;
import com.lark.oapi.service.ehr.v1.model.ListEmployeeReq;
import com.lark.oapi.service.ehr.v1.model.ListEmployeeRespBody;
import eco.ywhc.xr.common.model.lark.LarkAvatarInfo;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LarkEmployeeManagerImpl implements LarkEmployeeManager {

    private static final int BATCH_SIZE = 50;

    private static final Logger log = LoggerFactory.getLogger(LarkEmployeeManagerImpl.class);

    private final Client larkClient;

    private final CacheProperties cacheProperties;

    private final StringRedisTemplate redisTemplate;

    /**
     * 获取员工花名册信息
     *
     * @param pageToken 分页标记
     */
    public ListEmployeeRespBody listEmployees(String pageToken) {
        log.debug("开始获取员工花名册信息，BATCH_SIZE={}", BATCH_SIZE);
        if (StringUtils.isBlank(pageToken)) {
            pageToken = null;
        }
        ListEmployeeReq req = ListEmployeeReq.newBuilder()
                .view(ListEmployeeViewEnum.BASIC)
                .userIdType(ListEmployeeUserIdTypeEnum.OPEN_ID)
                .pageToken(pageToken)
                .pageSize(BATCH_SIZE)
                .build();
        try {
            return larkClient.ehr().employee().list(req).getData();
        } catch (Exception e) {
            log.error("获取员工花名册信息出错: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户OpenId
     */
    @Cacheable(cacheNames = "larkEmployees", key = "#p0", unless = "#result == null")
    public LarkEmployee retrieveLarkEmployee(String userId) {
        log.debug("获取用户信息，用户OpenId：{}", userId);
        GetUserReq req = GetUserReq.newBuilder()
                .userId(userId)
                .userIdType(GetUserUserIdTypeEnum.OPEN_ID)
                .departmentIdType(GetUserDepartmentIdTypeEnum.OPEN_DEPARTMENT_ID)
                .build();
        try {
            GetUserResp resp = larkClient.contact().user().get(req, RequestOptions.newBuilder().build());
            if (!resp.success()) {
                log.error("获取用户信息失败");
                return null;
            }
            var user = resp.getData().getUser();
            return LarkEmployee.builder()
                    .unionId(user.getUnionId())
                    .userId(user.getUserId())
                    .openId(user.getOpenId())
                    .name(user.getName())
                    .mobile(user.getMobile())
                    .departmentIds(user.getDepartmentIds())
                    .employeeNo(user.getEmployeeNo())
                    .avatarInfo(LarkAvatarInfo.of(user.getAvatar().getAvatar72(), user.getAvatar().getAvatar240(), user.getAvatar().getAvatar640(), user.getAvatar().getAvatarOrigin()))
                    .build();
        } catch (Exception e) {
            log.error("获取用户信息失败：{}", e.getMessage());
            return null;
        }
    }

    @Override
    public void appendLarkEmployeeUserId(String... userId) {
        redisTemplate.opsForSet().add(getLarkAllEmployeesKey(), userId);
    }

    @Override
    public Set<String> getAllLarkEmployeeUserIds() {
        return redisTemplate.opsForSet().members(getLarkAllEmployeesKey());
    }

    private String getLarkAllEmployeesKey() {
        String keyPrefix = cacheProperties.getRedis().getKeyPrefix();
        if (StringUtils.isBlank(keyPrefix)) {
            keyPrefix = "";
        }
        return keyPrefix + "larkAllEmployees";
    }

}
