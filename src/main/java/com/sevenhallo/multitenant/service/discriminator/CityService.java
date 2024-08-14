package com.sevenhallo.multitenant.service.discriminator;

import com.sevenhallo.multitenant.entity.City;
import com.sevenhallo.multitenant.repository.CityRepository;

import jakarta.persistence.EntityManager;
import java.sql.SQLException;
import java.util.List;

//@Service
public class CityService {

    //@Autowired
    private CityRepository cityRepository;

    //@PersistenceContext
    public EntityManager entityManager;

    public void save(City city){
        cityRepository.save(city);
    }

    public List<City> getAll() throws SQLException {
        return cityRepository.findAll();
    }

    public City get(Long id){
        return cityRepository.findById(id).get();
    }

    public City getByName(String name){
        return cityRepository.findByName(name);
    }

    public void delete(String name){
        cityRepository.deleteByName(name);
    }
}
