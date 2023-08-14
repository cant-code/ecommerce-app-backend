package com.mini.ecommerceapp.utils;

import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.models.Status;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QRUtilTest {

    @Test
    void createQR() {
        Order order = new Order();
        order.setId(RandomUtils.nextLong());
        order.setUserId(RandomStringUtils.random(20));
        order.setTotalCost(RandomUtils.nextDouble());
        order.setFinalCharge(RandomUtils.nextDouble());
        order.setExtraCharges(RandomUtils.nextDouble());
        order.setDateCreated(LocalDateTime.now());
        order.setStart(LocalDateTime.now());
        order.setExpiry(LocalDateTime.now().plusHours(2));
        order.setStatus(Status.CONFIRMED);

        String QR = QRUtil.generateQR(order);
        assertNotNull(QR);
    }

}