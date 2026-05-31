package com.gogidix.sysadmin.userprovisioning.interfaces.rest;
import com.gogidix.sysadmin.userprovisioning.application.service.UserAccountService;
import com.gogidix.sysadmin.userprovisioning.domain.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/user-provisioning")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService service;
    @PostMapping
    public ResponseEntity<UserAccount> create(@RequestBody UserAccount entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<UserAccount>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<UserAccount> getById(@PathVariable String id) { UserAccount result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
