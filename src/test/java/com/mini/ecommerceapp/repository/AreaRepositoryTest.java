package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.utils.MockData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AreaRepositoryTest {

    @Autowired
    private AreaRepository areaRepository;

    @Test
    @DisplayName("Find Area using name")
    void getByName() {
        Area area = saveDummyData();

        Optional<Area> value = areaRepository.getByName(area.getName());

        assertTrue(value.isPresent());
        assertEquals(area, value.get());
    }

    @Test
    @DisplayName("Dont find Area using name")
    void dontGetByName() {
        Optional<Area> value = areaRepository.getByName(RandomStringUtils.random(20));

        assertFalse(value.isPresent());
    }

    @Test
    @DisplayName("Find Area using name match")
    void findByName() {
        Area area = saveDummyData();

        List<Area> areaList = areaRepository.findByNameContainingIgnoreCase(area.getName());

        assertEquals(1, areaList.size());
        assertEquals(area, areaList.get(0));
    }

    @Test
    @DisplayName("Dont find Area using name match")
    void dontFindByName() {
        List<Area> areaList = areaRepository.findByNameContainingIgnoreCase(RandomStringUtils.random(20));

        assertEquals(0, areaList.size());
    }

    private Area saveDummyData() {
        Area area = MockData.buildArea();
        return areaRepository.save(area);
    }

}