package com.dkd.manage.service;

import java.util.List;
import com.dkd.manage.domain.Partner;
import com.dkd.manage.domain.vo.PartnerVO;

/**
 * 合作商Service接口
 * 
 * @author meidaia
 * @date 2025-09-05
 */
public interface IPartnerService 
{
    /**
     * 查询合作商
     * 
     * @param id 合作商主键
     * @return 合作商
     */
    public Partner selectPartnerById(Long id);

    /**
     * 查询合作商列表
     * 
     * @param partner 合作商
     * @return 合作商集合
     */
    public List<Partner> selectPartnerList(Partner partner);

    /**
     * 新增合作商
     * 
     * @param partner 合作商
     * @return 结果
     */
    public int insertPartner(Partner partner);

    /**
     * 修改合作商
     * 
     * @param partner 合作商
     * @return 结果
     */
    public int updatePartner(Partner partner);

    /**
     * 批量删除合作商
     * 
     * @param ids 需要删除的合作商主键集合
     * @return 结果
     */
    public int deletePartnerByIds(Long[] ids);

    /**
     * 删除合作商信息
     * 
     * @param id 合作商主键
     * @return 结果
     */
    public int deletePartnerById(Long id);

    /**
     * 查询合作商列表
     *
     * @param partner 合作商
     * @return 合作商VO集合
     */
    List<PartnerVO> selectPartnerVoList(Partner partner);

    /**
     * 重置合作商密码
     *
     * @param id 合作商主键
     * @return 结果
     */
    int resetPwd(Long id);
}
