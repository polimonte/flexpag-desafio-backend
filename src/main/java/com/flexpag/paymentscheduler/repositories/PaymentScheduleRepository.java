package com.flexpag.paymentscheduler.repositories;

import com.flexpag.paymentscheduler.entities.PaymentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentScheduleRepository extends JpaRepository<PaymentScheduleEntity, Long> {
}
