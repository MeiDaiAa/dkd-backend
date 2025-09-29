package com.dkd.manage.domain.dto;

import lombok.Data;

@Data
public class ChannelDTO {
    // 售货机软编号
    private String innerCode;
    // 货道编号
    private String channelCode;
    // 商品Id
    private Long skuId;
}
