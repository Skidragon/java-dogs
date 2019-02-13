package com.lambdaschool.restdogs;

import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
@RestController
public class DogController {
    private final DogRepository dogRepo;
    private final DogResourceAssembler assembler;

    public DogController(DogRepository dogRepo, DogResourceAssembler assembler) {
        this.dogRepo = dogRepo;
        this.assembler = assembler;
    }

    @GetMapping("/dogs")
    public Resources<Resource<Dog>> all()
    {
        List<Resource<Dog>> dogs = dogRepo.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/{id}")
    public Resource<Dog> findOne(@PathVariable Long id) {
        Dog foundDog = dogRepo.findById(id)
                .orElseThrow(() -> new DogNotFoundException(id));

        return assembler.toResource(foundDog);
    }

    @GetMapping("/dogs/breeds")
    public Resources<Resource<Dog>> allBreedsSorted()
    {
        List<Resource<Dog>> dogs = dogRepo.findAll(new Sort(Sort.Direction.ASC, "breed"))
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).allBreedsSorted()).withSelfRel());
    }
    @GetMapping("/dogs/weight")
    public Resources<Resource<Dog>> allWeightsSorted()
    {
        List<Resource<Dog>> dogs = dogRepo.findAll(new Sort(Sort.Direction.ASC, "avgWeight"))
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(dogs, linkTo(methodOn(DogController.class).allWeightsSorted()).withSelfRel());
    }
    @GetMapping("/dogs/breeds/{breed}")
    public Resources<Resource<Dog>> findSpecificBreed(@PathVariable String breed) {
        List<Resource<Dog>> dogsChosenByBreed = dogRepo.findAll()
                .stream()
                .filter(dog -> dog.getBreed().toLowerCase().equals(breed))
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogsChosenByBreed, linkTo(methodOn(DogController.class).findSpecificBreed(breed)).withSelfRel());
    }
    @GetMapping("/dogs/apartment")
    public Resources<Resource<Dog>> findApartmentDogs() {
        List<Resource<Dog>> dogs = dogRepo.findAll()
                .stream()
                .filter(dog -> dog.isForApartment() == true)
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).findApartmentDogs()).withSelfRel());
    }
    @PutMapping("/dogs/{id}")
    public ResponseEntity<?> updateDog(@RequestBody Dog dogUpdates, @PathVariable Long id) throws URISyntaxException {
        Dog updatedDog = dogRepo.findById(id)
                .map(dog -> {
                    dog.setAvgWeight(dog.getAvgWeight());
                    dog.setBreed(dogUpdates.getBreed());
                    dog.setForApartment(dogUpdates.isForApartment());
                    return dogRepo.save(dog);
                })
                .orElseGet(() -> {
                    dogUpdates.setId(id);
                    return dogRepo.save(dogUpdates);
                });
        Resource<Dog> resource = assembler.toResource(updatedDog);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    @PostMapping("/dogs")
    public Dog createDog(@RequestBody Dog newDog)  {
        return dogRepo.save(newDog);
    }

    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable Long id)
    {
        dogRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
