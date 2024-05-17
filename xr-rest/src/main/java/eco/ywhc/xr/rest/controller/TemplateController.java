package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.constant.FileOwnerType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sugar.commons.exception.InvalidInputException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文档模板接口
 */
@RestController
public class TemplateController {

    private static final String projectInfoStatisticsTemplate = "项目基本情况统计表模板.docx";

    private static final String projectFundingTemplate = "项目收资表模板.xlsx";

    private static final String visitRecordTemplate = "拜访记录模板.xlsx";

    private static final String meetingMinutesTemplate = "会议纪要模板.docx";

    /**
     * 获取文档模板
     *
     * @param templateType 文档类型
     */
    @GetMapping(value = "/documentTemplates", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> documentTemplates(@RequestParam FileOwnerType templateType) {
        String fileName = switch (templateType) {
            case PROJECT_INFO_STATISTICS -> projectInfoStatisticsTemplate;
            case PROJECT_FUNDING -> projectFundingTemplate;
            case VISIT_RECORD -> visitRecordTemplate;
            case MEETING_MINUTES -> meetingMinutesTemplate;
            default -> throw new InvalidInputException("templateType参数错误");
        };
        Resource resource = loadResource(fileName);
        HttpHeaders headers = generateContentDispositionHeader(fileName);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private static Resource loadResource(String fileName) {
        return new ClassPathResource("/static/documentTemplates/" + fileName);
    }

    private static HttpHeaders generateContentDispositionHeader(String attachmentName) {
        HttpHeaders headers = new HttpHeaders();
        String headerValue = generateContentDisposition(attachmentName);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        return headers;
    }

    private static String generateContentDisposition(String attachmentName) {
        if (attachmentName == null || attachmentName.isEmpty()) {
            return null;
        }
        return String.format("attachment;filename*=UTF-8''%s",
                URLEncoder.encode(attachmentName, StandardCharsets.UTF_8).replaceAll("\\+", "%20"));
    }

}
