package com.mc.domain.iam.port;

import java.util.Date;

public interface TokenHelperPort {

    Date getExpirationDateFromToken(String token);
    String getUsernameFromToken(String token);

}
