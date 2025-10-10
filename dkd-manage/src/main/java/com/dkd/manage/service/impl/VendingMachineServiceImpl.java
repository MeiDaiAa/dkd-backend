package com.dkd.manage.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.dkd.common.utils.DateUtils;
import com.dkd.common.utils.uuid.UUIDUtils;
import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.VmType;
import com.dkd.manage.service.INodeService;
import com.dkd.manage.service.IVmTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.VendingMachineMapper;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备管理Service业务层处理
 * 
 * @author meidaia
 * @date 2025-09-15
 */
@Service
public class VendingMachineServiceImpl implements IVendingMachineService 
{
    @Autowired
    private VendingMachineMapper vendingMachineMapper;
    @Autowired
    private IVmTypeService vmTypeService;
    @Autowired
    private INodeService nodeService;
    @Autowired
    private ChannelServiceImpl channelService;
    /**
     * 查询设备管理
     * 
     * @param id 设备管理主键
     * @return 设备管理
     */
    @Override
    public VendingMachine selectVendingMachineById(Long id)
    {
        return vendingMachineMapper.selectVendingMachineById(id);
    }

    /**
     * 查询设备管理列表
     * 
     * @param vendingMachine 设备管理
     * @return 设备管理
     */
    @Override
    public List<VendingMachine> selectVendingMachineList(VendingMachine vendingMachine)
    {
        return vendingMachineMapper.selectVendingMachineList(vendingMachine);
    }

    /**
     * 新增设备管理
     * 
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertVendingMachine(VendingMachine vendingMachine)
    {
        //设备编号
        vendingMachine.setInnerCode(UUIDUtils.getUUID());
        VmType vmType = vmTypeService.selectVmTypeById(vendingMachine.getVmTypeId());
        //渠道最大容量
        vendingMachine.setChannelMaxCapacity(vmType.getChannelMaxCapacity());

        Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
        //节点信息
        if(node != null) {
            BeanUtils.copyProperties(node, vendingMachine, "id");
            vendingMachine.setAddr(node.getAddress());
        }
        //设备状态
        vendingMachine.setVmStatus(0L);
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        vendingMachine.setCreateTime(DateUtils.getNowDate());

        int result = vendingMachineMapper.insertVendingMachine(vendingMachine);

        //新增设备时更新货道信息
        Long row = vmType.getVmRow(), col = vmType.getVmCol();
        List<Channel> channels = new ArrayList<>();
        for (Long i = 1L; i <= row; i++) {
            for (Long l = 1L; l <= col; l++) {
                Channel channel = new Channel();
                //货道编号
                channel.setChannelCode(i + "-" + l);
                //货道商品ID
                channel.setSkuId(0L);
                //售货机货道
                channel.setVmId(vendingMachine.getId());
                //售货机编号
                channel.setInnerCode(vendingMachine.getInnerCode());
                //货道最大容量
                channel.setMaxCapacity(vmType.getChannelMaxCapacity());
                //货道当前容量
                channel.setCurrentCapacity(0L);
                channel.setCreateTime(DateUtils.getNowDate());
                channel.setUpdateTime(DateUtils.getNowDate());

                channels.add(channel);
            }
        }
        channelService.insertChannelList(channels);

        return result;
    }

    /**
     * 修改设备管理
     * 
     * @param vendingMachine 设备管理
     * @return 结果
     */
    @Override
    public int updateVendingMachine(VendingMachine vendingMachine)
    {
        Node node = nodeService.selectNodeById(vendingMachine.getNodeId());
        //节点信息
        if(node != null) {
            BeanUtils.copyProperties(node, vendingMachine, "id");
            vendingMachine.setAddr(node.getAddress());
        }
        vendingMachine.setUpdateTime(DateUtils.getNowDate());
        return vendingMachineMapper.updateVendingMachine(vendingMachine);
    }

    /**
     * 批量删除设备管理
     * 
     * @param ids 需要删除的设备管理主键
     * @return 结果
     */
    @Override
    public int deleteVendingMachineByIds(Long[] ids)
    {
        return vendingMachineMapper.deleteVendingMachineByIds(ids);
    }

    /**
     * 删除设备管理信息
     * 
     * @param id 设备管理主键
     * @return 结果
     */
    @Override
    public int deleteVendingMachineById(Long id)
    {
        return vendingMachineMapper.deleteVendingMachineById(id);
    }

    /**
     * 根据售货机编号查询售货机信息
     *
     * @param vendingId 售货机编号
     * @return 售货机信息
     */
    @Override
    public VendingMachine selectVendingMachineByInnerCode(String vendingId) {
        return vendingMachineMapper.selectVendingMachineByInnerCode(vendingId);
    }
}
