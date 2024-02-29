package ru.job4j.rest.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.rest.model.User;
import ru.job4j.rest.service.UserService;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> get(@PathVariable("userId")
                                    @NotNull
                                    @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                    Long userId) {
        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        userService.save(user);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(user);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody User user) {
        if (userService.update(user)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void change(@RequestBody User user) {
        userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeById(@PathVariable long userId) {
        if (userService.deleteById(userId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        return ResponseEntity.notFound().build();
    }
}
