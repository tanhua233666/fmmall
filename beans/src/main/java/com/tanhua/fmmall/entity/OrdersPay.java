package com.tanhua.fmmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersPay {

    @Id
    @Column(name = "order_pay_id")
    private String orderPayId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "pay_url")
    private String payUrl;

    @Column(name = "pay_total")
    private Double payTotal;

    @Column(name = "pay_status")
    private String payStatus;
}
