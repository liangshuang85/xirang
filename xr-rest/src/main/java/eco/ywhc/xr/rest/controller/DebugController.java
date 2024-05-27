package eco.ywhc.xr.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调试
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DebugController {

    @PostMapping("/public/debug")
    public String publicDebug(@RequestBody String body) {
        return body;
    }

}
