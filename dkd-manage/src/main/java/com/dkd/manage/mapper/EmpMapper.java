package com.dkd.manage.mapper;

import java.util.List;
import com.dkd.manage.domain.Emp;
import org.apache.ibatis.annotations.Select;

/**
 * 人员列表Mapper接口
 * 
 * @author meidaia
 * @date 2025-09-11
 */
public interface EmpMapper 
{
    /**
     * 查询人员列表
     * 
     * @param id 人员列表主键
     * @return 人员列表
     */
    public Emp selectEmpById(Long id);

    /**
     * 查询人员列表列表
     * 
     * @param emp 人员列表
     * @return 人员列表集合
     */
    public List<Emp> selectEmpList(Emp emp);

    /**
     * 新增人员列表
     * 
     * @param emp 人员列表
     * @return 结果
     */
    public int insertEmp(Emp emp);

    /**
     * 修改人员列表
     * 
     * @param emp 人员列表
     * @return 结果
     */
    public int updateEmp(Emp emp);

    /**
     * 删除人员列表
     * 
     * @param id 人员列表主键
     * @return 结果
     */
    public int deleteEmpById(Long id);

    /**
     * 批量删除人员列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmpByIds(Long[] ids);

    /**
     * 根据区域id查询人员列表
     *
     * @param regionId
     * @return
     */
    @Select("select * from tb_emp where region_id = #{regionId}")
    List<Emp> selectEmpListByRegionId(Long regionId);
}
