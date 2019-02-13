package com.lambdaschool.restdogs;

import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/dogs/{breed}")
    public Resources<Resource<Dog>> findSpecificBreed(@PathVariable String breed) {
        List<Resource<Dog>> dogsChosenByBreed = dogRepo.findAll()
                .stream()
                .filter(dog -> dog.getBreed() == breed)
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogsChosenByBreed, linkTo(methodOn(DogController.class).findSpecificBreed(breed)).withSelfRel());
    }
}
