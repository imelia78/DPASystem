package ge.project.dpasystem.controller;


import ge.project.dpasystem.dto.ClientDto;
import ge.project.dpasystem.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    private List<ClientDto> getAllClients(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNumber") Integer pageNumber
    ){
        var filter = new RequestFilter(pageSize, pageNumber);
    return ResponseEntity.ok(clientService.findAllClientsByPages(filter)).getBody();
    }




}
