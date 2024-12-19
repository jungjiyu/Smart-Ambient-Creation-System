package com.raspberrypi.iot.raspberrypi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SensorDataLevel {
    LOW("low", 1),
    MEDIUM("medium", 2),
    HIGH("high", 3);

    private final String description;
    private final int level;

    @JsonCreator
    public static SensorDataLevel fromJson(@JsonProperty("description") String description,
                                           @JsonProperty("level") int level) {
        for (SensorDataLevel sensorDataLevel : SensorDataLevel.values()) {
            if (sensorDataLevel.description.equalsIgnoreCase(description) && sensorDataLevel.level == level) {
                return sensorDataLevel;
            }
        }
        throw new IllegalArgumentException("Invalid SensorDataLevel: description=" + description + ", level=" + level);
    }
}
