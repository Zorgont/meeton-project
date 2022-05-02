package com.meeton.core.services.client;

public interface ServiceClient<R, DTO> {
    R execute(DTO dto);
}
