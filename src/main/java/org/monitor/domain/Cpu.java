package org.monitor.domain;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import lombok.Data;

@Data
public class Cpu {

    private Double user;

    private Double sys;

    private Double idle;

    private Double total;

}
