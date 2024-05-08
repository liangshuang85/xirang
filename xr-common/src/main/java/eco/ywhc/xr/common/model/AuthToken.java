package eco.ywhc.xr.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 认证Token
 */
@ToString
@AllArgsConstructor(staticName = "of")
public class AuthToken {

    /**
     * X-Auth-Token
     */
    private String xAuthToken;

    @JsonProperty("xAuthToken")
    public String getXAuthToken() {
        return xAuthToken;
    }

}
