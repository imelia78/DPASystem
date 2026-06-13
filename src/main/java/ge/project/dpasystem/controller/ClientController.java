package ge.project.dpasystem.controller;


import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.dto.UpdateEmailDto;
import ge.project.dpasystem.dto.UpdatePhoneDto;
import ge.project.dpasystem.service.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "client_methods")
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;


    @PreAuthorize("hasAuthority('dpasystem.ADMIN')")
    @GetMapping()
    public ResponseEntity<List<ClientDto>> getAllClients(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ) {
        var filter = new RequestFilter(pageSize, pageNumber);
        return ResponseEntity.ok(clientService.findAllClientsByPages(filter));
    }

    @PreAuthorize("hasAuthority('dpasystem.ADMIN')")
    @GetMapping(params = "email")
    public ResponseEntity<ClientDto> getClientByEmail(@RequestParam String email) {
        var client = clientService.findClientByEmail(email);
        return ResponseEntity.ok(client);

    }

    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable UUID id) {
        var client = clientService.findClientById(id);
        return ResponseEntity.ok(client);
    }

    /*@PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        var newClient = clientService.createClient(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }*/

    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.CLIENT')")
    @PutMapping
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) {
        var updatedClient = clientService.updateClient(clientDto);
        return ResponseEntity.ok(updatedClient);
    }

    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.CLIENT')")
    @PatchMapping("/{id}/phone")
    public ResponseEntity<ClientDto> updateClientPhoneNumber(@PathVariable UUID id, @RequestBody UpdatePhoneDto phoneDto) {

        var changedClient = clientService.updatePhoneNumber(id, phoneDto.phoneNumber());

        return ResponseEntity.ok(changedClient);
    }


    @PreAuthorize("hasAnyAuthority('dpasystem.ADMIN','dpasystem.CLIENT')")
    @PatchMapping("/{id}/email")
    public ResponseEntity<ClientDto> updateClientEmail(@PathVariable UUID id, @RequestBody UpdateEmailDto emailDto) {

        var changedClient = clientService.updateEmail(id, emailDto.email());

        return ResponseEntity.ok(changedClient);
    }

    @PreAuthorize("hasAuthority('dpasystem.ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClientById(@PathVariable UUID id) {
        clientService.deleteClientById(id);
        return ResponseEntity.ok("Client has been deleted successfully");
    }


}
