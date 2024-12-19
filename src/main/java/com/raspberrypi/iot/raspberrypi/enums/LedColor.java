package com.raspberrypi.iot.raspberrypi.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LedColor {
    RED("red"), BLUE("blue"), OFF("off");
    private final String color;
}
