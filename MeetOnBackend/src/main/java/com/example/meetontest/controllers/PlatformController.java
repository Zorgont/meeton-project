package com.example.meetontest.controllers;

import com.example.meetontest.converters.PlatformConverter;
import com.example.meetontest.dto.PlatformDTO;
import com.example.meetontest.services.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/platforms")
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
