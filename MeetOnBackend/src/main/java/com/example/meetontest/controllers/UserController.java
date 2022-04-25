package com.example.meetontest.controllers;

import com.example.meetontest.converters.TagGroupConverter;
import com.example.meetontest.dto.MessageResponse;
import com.example.meetontest.dto.TagGroupDTO;
import com.example.meetontest.dto.UserSettingDTO;
import com.example.meetontest.entities.ImageModel;
import com.example.meetontest.entities.User;
import com.example.meetontest.services.ImageModelService;
import com.example.meetontest.services.TagGroupService;
import com.example.meetontest.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TagGroupService tagGroupService;
    private final TagGroupConverter tagGroupConverter;
    private final ImageModelService imageModelService;

    @GetMapping
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{username}/by-username")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.getUserByName(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User newUser) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, newUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted!");
    }

    @PutMapping("/changeSettings/{id}")
    public ResponseEntity<?> updateUserSettings(@PathVariable Long id, @RequestBody UserSettingDTO userSettingDTO) {
        return ResponseEntity.ok(userService.updateUserSettings(id, userSettingDTO));
    }

    @GetMapping("/{id}/tagGroups")
    public List<TagGroupDTO> getUserTagGroups(@PathVariable Long id) {
        return tagGroupService.getByUser(userService.getUserById(id)).stream().map(tagGroupConverter::convertBack).collect(Collectors.toList());
    }

    @PostMapping("/{id}/tagGroups")
    public TagGroupDTO createTagGroup(@PathVariable Long id, @RequestBody TagGroupDTO tagGroup) throws ParseException {
        return tagGroupConverter.convertBack(tagGroupService.createTagGroup(tagGroupConverter.convert(tagGroup), userService.getUserById(id)));
    }

    @PutMapping("/{userId}/tagGroups/{id}")
    public TagGroupDTO setNotifiable(@PathVariable Long userId, @PathVariable Long id, @RequestParam Boolean isNotifiable) {
        return tagGroupConverter.convertBack(tagGroupService.setNotifiable(id, isNotifiable));
    }

    @DeleteMapping("/{userId}/tagGroups/{id}")
    public void deleteTagGroup(@PathVariable Long userId, @PathVariable Long id) {
        tagGroupService.deleteByGroupId(id);
    }
    @PostMapping(value = "/{userId}/avatar")
    public ResponseEntity<?> createOrUpdateUserAvatar(@PathVariable Long userId, @RequestParam("imageFile") MultipartFile avatarFile) throws IOException {
        try {
            imageModelService.updateOrCreateUserAvatar(userId, new ImageModel(avatarFile.getOriginalFilename(), avatarFile.getContentType(), avatarFile.getBytes()));
            return ResponseEntity.ok("Uploaded");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Error while uploading image!"));
        }

    }
    @GetMapping(value = "/{username}/avatar")
    public ResponseEntity<?> getUserAvatar(@PathVariable String username) {
        try {
            ImageModel userAvatar = imageModelService.getUserAvatar(username);
            return ResponseEntity.ok().cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES)).contentType(MediaType.valueOf(userAvatar.getType())).contentLength(userAvatar.getPic().length).body(userAvatar.getPic());
        }
        catch (Exception e) {
            System.out.println("Cannot get avatar for user " + username);
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
