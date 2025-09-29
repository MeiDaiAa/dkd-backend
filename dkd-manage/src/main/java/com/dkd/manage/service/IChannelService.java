package com.dkd.manage.service;

import java.util.List;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.dto.ChannelListDTO;
import com.dkd.manage.domain.vo.ChannelVO;

/**
 * 售货机货道Service接口
 * 
 * @author meidaia
 * @date 2025-09-15
 */
public interface IChannelService {
    /**
     * 查询售货机货道
     *
     * @param id 售货机货道主键
     * @return 售货机货道
     */
    public Channel selectChannelById(Long id);

    /**
     * 查询售货机货道列表
     *
     * @param channel 售货机货道
     * @return 售货机货道集合
     */
    public List<Channel> selectChannelList(Channel channel);

    /**
     * 新增售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    public int insertChannel(Channel channel);

    /**
     * 修改售货机货道
     *
     * @param channel 售货机货道
     * @return 结果
     */
    public int updateChannel(Channel channel);

    /**
     * 批量删除售货机货道
     *
     * @param ids 需要删除的售货机货道主键集合
     * @return 结果
     */
    public int deleteChannelByIds(Long[] ids);

    /**
     * 删除售货机货道信息
     *
     * @param id 售货机货道主键
     * @return 结果
     */
    public int deleteChannelById(Long id);

    /**
     * 批量新增售货机货道
     *
     * @param channels 售货机货道集合
     * @return 结果
     */
    public int insertChannelList(List<Channel> channels);

    /**
     * 根据商品Id查询货道数量
     *
     * @param skuIds 商品Id
     * @return 货道数量
     */
    int selectCountBySkuId(Long[] skuIds);


    /**
     * 根据售货机软编号查询售货机信息
     *
     * @param innerCode 售货机软编号
     * @return 货道信息VO
     */
    List<ChannelVO> getByInnerCode(String innerCode);

    /**
     * 批量修改货道信息
     *
     * @param channelListDTO 货道列表DTO
     * @return 结果
     */
    int batchUpdateChannel(ChannelListDTO channelListDTO);
}
