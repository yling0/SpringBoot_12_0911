package me.ling.springboot_12_0911.repositories;

import me.ling.springboot_12_0911.models.Actor;
import org.springframework.data.repository.CrudRepository;

public interface ActorRepo extends CrudRepository<Actor, Long>{
}
