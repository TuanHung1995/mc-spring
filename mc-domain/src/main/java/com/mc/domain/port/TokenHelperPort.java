package com.mc.domain.port;

import java.util.Date;

public interface TokenHelperPort {

    Date getExpirationDateFromToken(String token);
    String getUsernameFromToken(String token);

}
