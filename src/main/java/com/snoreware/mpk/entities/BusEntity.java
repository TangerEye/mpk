package com.snoreware.mpk.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "buses")
public class BusEntity extends VehicleEntity {
    public BusEntity(Long vehicleNumber, boolean lowFloor, boolean articulated) {
        super(vehicleNumber, lowFloor);
        this.articulated = articulated;
    }

    @Column(name = "articulated", nullable = false)
    private boolean articulated;

    @OneToMany(mappedBy = "bus")
    private List<BusCourseEntity> busCourses;

    @Override
    public String toString() {
        return String.format(
                "Autobus[id = %d, przegubowy = %b]", getVehicleNumber(), this.articulated);
    }
}