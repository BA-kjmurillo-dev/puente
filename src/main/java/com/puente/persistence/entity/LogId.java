package com.puente.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogId implements Serializable {
    private String remesa;
    private LocalDate fechaLog;
    private LocalTime horaLog;
    private String canal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogId logId)) return false;
        return Objects.equals(remesa, logId.remesa) && Objects.equals(fechaLog, logId.fechaLog) && Objects.equals(horaLog, logId.horaLog) && Objects.equals(canal, logId.canal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remesa, fechaLog, horaLog, canal);
    }
}
