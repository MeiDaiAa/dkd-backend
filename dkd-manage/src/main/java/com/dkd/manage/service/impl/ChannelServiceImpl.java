package com.dkd.manage.service.impl;

import java.util.*;

import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.dto.ChannelDTO;
import com.dkd.manage.domain.dto.ChannelListDTO;
import com.dkd.manage.domain.vo.ChannelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.ChannelMapper;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.service.IChannelService;

/**
 * 售货机货道Service业务层处理
 * 
 * @author meidaia
 * @date 2025-09-15
 */
@Service
public class ChannelServiceImpl implements IChannelService 
{
    @Autowired
    private ChannelMapper channelMapper;

    /**
     * 查询售货机货道
     * 
     * @param id 售货机货道主键
     * @return 售货机货道
     */
    @Override
    public Channel selectChannelById(Long id)
    {
        return channelMapper.selectChannelById(id);
    }

    /**
     * 查询售货机货道列表
     * 
     * @param channel 售货机货道
     * @return 售货机货道
     */
    @Override
    public List<Channel> selectChannelList(Channel channel)
    {
        return channelMapper.selectChannelList(channel);
    }

    /**
     * 新增售货机货道
     * 
     * @param channel 售货机货道
     * @return 结果
     */
    @Override
    public int insertChannel(Channel channel)
    {
        channel.setCreateTime(DateUtils.getNowDate());
        return channelMapper.insertChannel(channel);
    }

    /**
     * 修改售货机货道
     * 
     * @param channel 售货机货道
     * @return 结果
     */
    @Override
    public int updateChannel(Channel channel)
    {
        channel.setUpdateTime(DateUtils.getNowDate());
        return channelMapper.updateChannel(channel);
    }

    /**
     * 批量删除售货机货道
     * 
     * @param ids 需要删除的售货机货道主键
     * @return 结果
     */
    @Override
    public int deleteChannelByIds(Long[] ids)
    {
        return channelMapper.deleteChannelByIds(ids);
    }

    /**
     * 删除售货机货道信息
     * 
     * @param id 售货机货道主键
     * @return 结果
     */
    @Override
    public int deleteChannelById(Long id)
    {
        return channelMapper.deleteChannelById(id);
    }

    /**
     * 批量新增售货机货道
     *
     * @param channels 售货机货道
     * @return 结果
     */
    @Override
    public int insertChannelList(List<Channel> channels)
    {
        return channelMapper.insertBatchChannel(channels);
    }

    /**
     * 根据商品Id查询货道数量
     *
     * @param skuIds 商品Id
     * @return 货道数量
     */
    @Override
    public int selectCountBySkuId(Long[] skuIds) {
        return channelMapper.selectCountBySkuIds(skuIds);
    }


    /**
     * 根据售货机软编号查询售货机信息
     *
     * @param innerCode 售货机软编号
     * @return 货道信息VO
     */
    @Override
    public List<ChannelVO> getByInnerCode(String innerCode) {
        return channelMapper.getByInnerCode(innerCode);
    }

    /**
     * 批量修改货道信息
     *
     * @param channelListDTO 货道列表DTO
     * @return 结果
     */
    @Override
    public int batchUpdateChannel(ChannelListDTO channelListDTO) {
        List<ChannelDTO> channelList = channelListDTO.getChannelList();
        String innerCode = channelListDTO.getInnerCode();
//        List<Channel> channels = new ArrayList<>();
//        channelList.forEach(channelDTO -> {
//            //通过货道编号和售货机软编号查询货道信息
//            Channel channel = channelMapper.getByInnerCodeAndchannelCode(innerCode, channelDTO.getChannelCode());
//            if (channel != null) {
//                channel.setSkuId(channelDTO.getSkuId());
//                channel.setUpdateTime(DateUtils.getNowDate());
//                channels.add(channel);
//            }
//        });

        // 创建一个Map，用于存储货道编号和商品Id
        Map<String, Long> map = new HashMap<>();
        channelList.forEach(channelDTO -> {
            map.put(channelDTO.getChannelCode(), channelDTO.getSkuId());
        });

        // 先查询当前售货机的说有货道
        Channel channel = new Channel();
        channel.setInnerCode(innerCode);
        List<Channel> channels = channelMapper.selectChannelList(channel);
        // 遍历货道列表，判断当前货道编号是否在map中，如果在，就是需要修改货道，更新信息
        List<Channel> needUpdateChannels = new ArrayList<>();
        channels.forEach(c -> {
            if(map.containsKey(c.getChannelCode())){
                c.setSkuId(map.get(c.getChannelCode()));
                c.setUpdateTime(DateUtils.getNowDate());
                needUpdateChannels.add(c);
            }
        });

        return channelMapper.batchUpdateChannel(needUpdateChannels);
    }
}
