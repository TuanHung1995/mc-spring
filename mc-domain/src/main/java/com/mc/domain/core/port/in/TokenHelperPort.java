package com.mc.domain.core.port.in;

import java.util.Date;

public interface TokenHelperPort {

    Date getExpirationDateFromToken(String token);
    String getUsernameFromToken(String token);

}
