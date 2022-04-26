package com.meeton.core.controllers;

import com.meeton.core.converters.PlatformConverter;
import com.meeton.core.dto.PlatformDTO;
import com.meeton.core.services.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "meeton-core/v1/platforms")
@RequiredArgsConstructor
public class PlatformController {
    private final PlatformService platformService;
    private final PlatformConverter platformConverter;

    @GetMapping
    List<PlatformDTO> getPlatforms() {
        return platformService.getAll().stream().map(platformConverter::convertBack).collect(Collectors.toList());
    }

    @GetMapping("/by")
    PlatformDTO getPlatforms(@RequestParam String name) {
        return platformConverter.convertBack(platformService.getByName(name).get());
    }
}
