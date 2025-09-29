package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.Sku;
import lombok.Data;

/**
 * 货道信息VO
 *
 * @author meidaia
 * @date 2025-09-29
 */
@Data
public class ChannelVO extends Channel {
    // 商品
    private Sku sku;
}
