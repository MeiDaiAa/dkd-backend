package com.dkd.manage.service.impl;

import java.util.List;

import com.dkd.common.constant.DkdContants;
import com.dkd.common.exception.ServiceException;
import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.Region;
import com.dkd.manage.domain.Role;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.mapper.RegionMapper;
import com.dkd.manage.mapper.RoleMapper;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.EmpMapper;
import com.dkd.manage.domain.Emp;
import com.dkd.manage.service.IEmpService;

/**
 * 人员列表Service业务层处理
 * 
 * @author meidaia
 * @date 2025-09-11
 */
@Service
public class EmpServiceImpl implements IEmpService 
{
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IVendingMachineService vendingMachineService;

    /**
     * 查询人员列表
     * 
     * @param id 人员列表主键
     * @return 人员列表
     */
    @Override
    public Emp selectEmpById(Long id)
    {
        return empMapper.selectEmpById(id);
    }

    /**
     * 查询人员列表列表
     * 
     * @param emp 人员列表
     * @return 人员列表
     */
    @Override
    public List<Emp> selectEmpList(Emp emp)
    {
        return empMapper.selectEmpList(emp);
    }

    /**
     * 新增人员列表
     * 
     * @param emp 人员列表
     * @return 结果
     */
    @Override
    public int insertEmp(Emp emp)
    {
        /*人员列表的冗余字段填充*/
        Region region = regionMapper.selectRegionById(emp.getRegionId());
        Role role = roleMapper.selectRoleByRoleId(emp.getRoleId());

        emp.setRoleId(role.getRoleId());
        emp.setRoleCode(role.getRoleCode());
        emp.setRegionName(region.getRegionName());
        emp.setRoleName(role.getRoleName());

        emp.setCreateTime(DateUtils.getNowDate());
        return empMapper.insertEmp(emp);
    }

    /**
     * 修改人员列表
     * 
     * @param emp 人员列表
     * @return 结果
     */
    @Override
    public int updateEmp(Emp emp)
    {
        /*人员列表的冗余字段同步修改*/
        Region region = regionMapper.selectRegionById(emp.getRegionId());
        Role role = roleMapper.selectRoleByRoleId(emp.getRoleId());

        emp.setRoleId(role.getRoleId());
        emp.setRoleCode(role.getRoleCode());
        emp.setRegionName(region.getRegionName());
        emp.setRoleName(role.getRoleName());

        emp.setUpdateTime(DateUtils.getNowDate());
        return empMapper.updateEmp(emp);
    }

    /**
     * 批量删除人员列表
     * 
     * @param ids 需要删除的人员列表主键
     * @return 结果
     */
    @Override
    public int deleteEmpByIds(Long[] ids)
    {
        return empMapper.deleteEmpByIds(ids);
    }

    /**
     * 删除人员列表信息
     * 
     * @param id 人员列表主键
     * @return 结果
     */
    @Override
    public int deleteEmpById(Long id)
    {
        return empMapper.deleteEmpById(id);

    }

    /**
     * 根据售货机获取工作人员列表
     *
     * @param vendingId 售货机id
     * @param roleCode
     * @return
     */
    @Override
    public List<Emp> getEmpListByVendingIdAndRoleCode(String vendingId, String roleCode) {
        // 1. 先通过售货机id查询售货机
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setInnerCode(vendingId);
        List<VendingMachine> vendingMachines = vendingMachineService.selectVendingMachineList(vendingMachine);

        if (vendingMachines == null) {
            throw new ServiceException("未查询到售货机信息");
        }
        // 2. 获取售货机信息
        vendingMachine = vendingMachines.get(0);

        // 3. 通过售货机对象中的区域id查询维修人员列表
        Long regionId = vendingMachine.getRegionId();
        List<Emp> emps = empMapper.selectEmpListByRegionId(regionId);
        // 4. 过滤掉非指定人员和禁用员工
        return emps.stream()
                .filter(emp -> roleCode.equals(emp.getRoleCode()))
                .filter(emp -> DkdContants.EMP_STATUS_NORMAL.equals(emp.getStatus()))
                .toList();
    }
}
