package pl.mw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.mw.model.Material;
import pl.mw.repositories.MaterialRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialRestController {

    private MaterialRepository materialRepository;

    @Autowired
    public MaterialRestController(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Material>> getAll(){
        List<Material> list = materialRepository.findAll();
        return ResponseEntity.ok(list);

    }

    @GetMapping(path="/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Material> getOne(@PathVariable Long id){
        Material mat = materialRepository.getOne(id);
        if(mat==null){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(mat);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>saveMaterial(@RequestBody Material material){
        Material savedMat = materialRepository.save(material);
        if(material.getId()==null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id")
                    .buildAndExpand(savedMat.getId())
                    .toUri();
            return ResponseEntity.created(location).body(savedMat);
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }




}
