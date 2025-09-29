package com.dkd.manage.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChannelListDTO {
    private String innerCode;
    private List<ChannelDTO> channelList;
}
