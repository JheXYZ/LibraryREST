package com.JheX.Library.library.PublishingHouse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/publishing-houses")
public class PublishingHouseController {
    private final PublishingHouseService PHService;

    public PublishingHouseController(PublishingHouseService publishingHouseService) { //here using Dependency Injection
        this.PHService = publishingHouseService;
    }

    @GetMapping
    public ResponseEntity<List<PublishingHouse>> getAllPublishingHouses(){
        return PHService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublishingHouse> findById(@PathVariable Long id){
        return  PHService.findById(id);
    }

    @PostMapping
    public ResponseEntity<PublishingHouse> createPublishingHouse(@RequestBody @Valid PublishingHouse publishingHouse){
        return PHService.createPublishingHouse(publishingHouse);
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<Void> putPublishingHouse(@PathVariable Long requestedId, @RequestBody @Valid PublishingHouse publishingHouseUpdate){
        return PHService.putPublishingHouse(requestedId, publishingHouseUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublishingHouse(@PathVariable Long id){
        return PHService.deletePublishingHouse(id);
    }

}
