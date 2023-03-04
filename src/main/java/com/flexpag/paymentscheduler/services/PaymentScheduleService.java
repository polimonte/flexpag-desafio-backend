package com.flexpag.paymentscheduler.services;

import com.flexpag.paymentscheduler.entities.PaymentScheduleEntity;
import com.flexpag.paymentscheduler.repositories.PaymentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentScheduleService {

    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;

    public List<PaymentScheduleEntity> findAll(){
        return paymentScheduleRepository.findAll();
    }

    public Optional<PaymentScheduleEntity> findById(Long id){
        return paymentScheduleRepository.findById(id);
    }

    public Long save(PaymentScheduleEntity paymentSchedule){
        paymentScheduleRepository.save(paymentSchedule);
        return paymentSchedule.getId();
    }

    public void delete(PaymentScheduleEntity paymentSchedule){
        paymentScheduleRepository.delete(paymentSchedule);
    }
}
