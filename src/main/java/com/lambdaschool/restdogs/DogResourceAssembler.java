package com.lambdaschool.restdogs;


import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DogResourceAssembler implements ResourceAssembler <Dog, Resource<Dog>>
{
    @Override
    public Resource<Dog> toResource(Dog dog)
    {
        return new Resource<Dog>(dog,
                linkTo(methodOn(DogController.class).findOne(dog.getId())).withSelfRel(),
                linkTo(methodOn(DogController.class).all()).withRel("dogs"),
                linkTo(methodOn(DogController.class).allBreedsSorted()).withRel("dogs"),
                linkTo(methodOn(DogController.class).allWeightsSorted()).withRel("dogs"),
                linkTo(methodOn(DogController.class).findSpecificBreed(dog.getBreed())).withRel("dogs"),
                linkTo(methodOn(DogController.class).findApartmentDogs()).withRel("dogs"));
    }
}