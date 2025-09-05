package com.dkd.manage.mapper;

import java.util.List;
import com.dkd.manage.domain.Region;
import com.dkd.manage.domain.vo.RegionVO;

/**
 * 区域Mapper接口
 * 
 * @author meidaia
 * @date 2025-09-05
 */
public interface RegionMapper 
{
    /**
     * 查询区域
     * 
     * @param id 区域主键
     * @return 区域
     */
    public Region selectRegionById(Long id);

    /**
     * 查询区域列表
     * 
     * @param region 区域
     * @return 区域集合
     */
    public List<Region> selectRegionList(Region region);

    /**
     * 新增区域
     * 
     * @param region 区域
     * @return 结果
     */
    public int insertRegion(Region region);

    /**
     * 修改区域
     * 
     * @param region 区域
     * @return 结果
     */
    public int updateRegion(Region region);

    /**
     * 删除区域
     * 
     * @param id 区域主键
     * @return 结果
     */
    public int deleteRegionById(Long id);

    /**
     * 批量删除区域
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRegionByIds(Long[] ids);

    /**
     * 查询区域列表
     *
     * @param region 区域
     * @return 区域VO集合
     */
    public List<RegionVO> selectRegionVoList(Region region);
}
