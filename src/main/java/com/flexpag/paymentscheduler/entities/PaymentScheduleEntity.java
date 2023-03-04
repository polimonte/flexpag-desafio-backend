package com.flexpag.paymentscheduler.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tb_paymentschedule")
public class PaymentScheduleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant date;

    private String paymentStatus;

    public PaymentScheduleEntity (){
    }

    public PaymentScheduleEntity(Instant date){
        this.date = date;
        paymentStatus = "pending";
    }
}
