package com.flexpag.paymentscheduler.controllers;

import com.flexpag.paymentscheduler.entities.PaymentScheduleEntity;
import com.flexpag.paymentscheduler.services.PaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/payments")
public class PaymentScheduleController {

    @Autowired
    private PaymentScheduleService paymentScheduleService;

    @GetMapping
    public ResponseEntity<List<PaymentScheduleEntity>> findAll() {
        List<PaymentScheduleEntity> paymentsList = paymentScheduleService.findAll();
        for (PaymentScheduleEntity ps :
                paymentsList) {
            if (ps.getDate().equals(Instant.now()) || ps.getDate().isBefore(Instant.now())) {
                ps.setPaymentStatus("paid");
                paymentScheduleService.save(ps);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(paymentScheduleService.findAll());
    }

    @GetMapping(value = "/{id}")
    public Object findById(@PathVariable Long id) {
        Optional<PaymentScheduleEntity> ps = paymentScheduleService.findById(id);
        if (ps.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule with this id does not exist.");
        } else if (ps.get().getDate().equals(Instant.now()) || ps.get().getDate().isBefore(Instant.now())) {
            ps.get().setPaymentStatus("paid");
            paymentScheduleService.save(ps.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(ps.get());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody PaymentScheduleEntity paymentSchedule) {
        if (paymentSchedule.getDate().equals(Instant.now()) || paymentSchedule.getDate().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Schedule date has to be after the current date: " + Instant.now());
        }
        paymentSchedule.setPaymentStatus("pending");
        return ResponseEntity.status(HttpStatus.OK).body(paymentScheduleService.save(paymentSchedule));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody PaymentScheduleEntity paymentSchedule) {
        Optional<PaymentScheduleEntity> ps = paymentScheduleService.findById(id);

        if (ps.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule with this id does not exist.");
        } else if (ps.get().getDate().equals(Instant.now()) || ps.get().getDate().isBefore(Instant.now())) {
            ps.get().setPaymentStatus("paid");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This payment has already been made.");
        } else if (paymentSchedule.getDate().equals(Instant.now()) || paymentSchedule.getDate().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Schedule date has to be after the current date: " + Instant.now());
        }

        PaymentScheduleEntity updated = new PaymentScheduleEntity();
        updated.setId(ps.get().getId());
        updated.setDate(ps.get().getDate());
        updated.setPaymentStatus(ps.get().getPaymentStatus());

        return ResponseEntity.status(HttpStatus.OK).body(paymentScheduleService.save(updated));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<PaymentScheduleEntity> ps = paymentScheduleService.findById(id);

        if (ps.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule with this id does not exist.");
        } else if (ps.get().getDate().isAfter(Instant.now())) {
            paymentScheduleService.delete(ps.get());
            return ResponseEntity.status(HttpStatus.OK).body("Schedule deleted successfully.");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This payment has already been made.");
    }
}
