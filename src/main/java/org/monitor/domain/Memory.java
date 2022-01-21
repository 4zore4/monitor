package org.monitor.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Memory {
    private Double used;

    private Double unused;

    private Double total;
}
