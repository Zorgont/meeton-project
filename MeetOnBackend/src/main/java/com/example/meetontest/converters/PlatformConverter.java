package com.example.meetontest.converters;

import com.example.meetontest.dto.PlatformDTO;
import com.example.meetontest.entities.Platform;
import com.example.meetontest.entities.PlatformType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@RequiredArgsConstructor
@Component
public class PlatformConverter implements Converter<Platform, PlatformDTO> {

    @Override
    public Platform convert(PlatformDTO entity) throws ParseException {
        Platform platform = new Platform();
        platform.setName(entity.getName());
        platform.setInfo(entity.getInfo());

        if (entity.getType() == null)
            platform.setType(PlatformType.ONLINE);
        else
            platform.setType(PlatformType.valueOf(entity.getType().toUpperCase()));

        return platform;
    }

    @Override
    public PlatformDTO convertBack(Platform entity) {
        return new PlatformDTO(entity.getId(), entity.getName(), entity.getInfo(), entity.getType().toString());
    }
}
