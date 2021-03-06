package com.snoreware.mpk.controllers;

import com.snoreware.mpk.MpkApplication;
import com.snoreware.mpk.entities.BusCourseEntity;
import com.snoreware.mpk.entities.BusEntity;
import com.snoreware.mpk.entities.DriverEntity;
import com.snoreware.mpk.entities.RouteEntity;
import com.snoreware.mpk.model.input.CourseDTO;
import com.snoreware.mpk.model.input.DriverDTO;
import com.snoreware.mpk.model.output.OutCourseDTO;
import com.snoreware.mpk.repos.BusCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/busCourse")
@RestController
public class BusCourseController {

    private BusCourseRepository busCourseRepository;
    private static final Logger log = LoggerFactory.getLogger(MpkApplication.class);

    public BusCourseController(BusCourseRepository busCourseRepository) {
        this.busCourseRepository = busCourseRepository;
    }

    @GetMapping("/all")
    private ResponseEntity<List<OutCourseDTO>> getAllBusCourses() {
        List<BusCourseEntity> busCourses = busCourseRepository.findAllByOrderByCourseIdDesc();
        List<OutCourseDTO> result = new ArrayList<>();

        result = busCourses.stream()
                .map(OutCourseDTO::dtoFromBusCourse)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/add")
    private ResponseEntity addBusCourse(@RequestBody CourseDTO courseDTO) {
        BusCourseEntity busCourse = new BusCourseEntity(
                courseDTO.getLowFloorNeeded(),
                courseDTO.getRouteNumber(),
                courseDTO.getArticulatedNeeded());

        busCourse.setRoute(new RouteEntity(courseDTO.getRouteNumber()));
        busCourse.setDriver(new DriverEntity(courseDTO.getDriverId()));
        busCourse.setBus(new BusEntity(courseDTO.getVehicleNumber()));

        busCourseRepository.save(busCourse);

        log.info(String.format("Added new bus course on route %d", courseDTO.getRouteNumber()));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity removeBusCourse(@PathVariable UUID id) {
        busCourseRepository.deleteById(id);

        log.info(String.format("Removed course with UUID %s", id));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/driver")
    private ResponseEntity assignBusDriver(@RequestBody DriverDTO driverDTO, @PathVariable UUID courseId) {
        DriverEntity assignedDriver = new DriverEntity(driverDTO.getDriverId());
        BusCourseEntity courseToUpdate = busCourseRepository.findByCourseId(courseId);

        courseToUpdate.setDriver(assignedDriver);
        busCourseRepository.save(courseToUpdate);
        log.info(String.format("Assigned driver with id %s to course %s",
                assignedDriver.getDriverId(), courseId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/route")
    private ResponseEntity assignRoute(@RequestParam Long routeNumber, @PathVariable UUID courseId) {
        RouteEntity route = new RouteEntity(routeNumber);
        BusCourseEntity courseToUpdate = busCourseRepository.findByCourseId(courseId);

        courseToUpdate.setRoute(route);
        busCourseRepository.save(courseToUpdate);
        log.info(String.format("Assigned route with number %d to course %s",
                routeNumber, courseId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/bus")
    private ResponseEntity changeTram(@RequestParam Long vehicleNumber, @PathVariable UUID courseId) {
        BusEntity tram = new BusEntity(vehicleNumber);
        BusCourseEntity courseToUpdate = busCourseRepository.findByCourseId(courseId);

        courseToUpdate.setBus(tram);
        busCourseRepository.save(courseToUpdate);
        log.info(String.format("Assigned bus with number %d to course %s",
                vehicleNumber, courseId));
        return ResponseEntity.ok().build();
    }
}
