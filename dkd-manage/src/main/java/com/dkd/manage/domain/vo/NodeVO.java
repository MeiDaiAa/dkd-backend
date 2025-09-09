package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.Partner;
import com.dkd.manage.domain.Region;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NodeVO extends Node {
    /*区域*/
    private Region region;
    /*合作商*/
    private Partner partner;
    /*总数量*/
    private int vmCount;
}
