package com.tanhua.fmmall.vo;


import com.tanhua.fmmall.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * 表名：ordersVO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersVO {

    private String orderId;
    private String userId;
    private String untitled;
    private String receiverName;
    private String receiverMobile;
    private String receiverAddress;
    private BigDecimal totalAmount;
    private Integer actualAmount;
    private Integer payType;
    private String orderRemark;
    private String status;
    private String deliveryType;
    private String deliveryFlowId;
    private BigDecimal orderFreight;
    @Column(name = "delete_status")
    private Integer deleteStatus;
    private Date createTime;
    private Date updateTime;
    private Date payTime;
    private Date deliveryTime;
    private Date flishTime;
    private Date cancelTime;
    private Integer closeType;
    private List<OrderItem> orderItems;

}