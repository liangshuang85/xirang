package eco.ywhc.xr.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * OAuth2 Token对象
 */
@ToString
@AllArgsConstructor(staticName = "of")
public class OAuth2Token {

    /**
     * X-Auth-Token
     */
    private String xAuthToken;

    @JsonProperty("xAuthToken")
    public String getXAuthToken() {
        return xAuthToken;
    }

}
