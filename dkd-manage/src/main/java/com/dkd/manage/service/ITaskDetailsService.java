package com.dkd.manage.service;

import java.util.List;
import com.dkd.manage.domain.TaskDetails;

/**
 * 工单详情，只有补货工单才有Service接口
 * 
 * @author meidaia
 * @date 2025-10-09
 */
public interface ITaskDetailsService 
{
    /**
     * 查询工单详情，只有补货工单才有
     * 
     * @param detailsId 工单详情，只有补货工单才有主键
     * @return 工单详情，只有补货工单才有
     */
    public TaskDetails selectTaskDetailsByDetailsId(Long detailsId);

    /**
     * 查询工单详情，只有补货工单才有列表
     * 
     * @param taskDetails 工单详情，只有补货工单才有
     * @return 工单详情，只有补货工单才有集合
     */
    public List<TaskDetails> selectTaskDetailsList(TaskDetails taskDetails);

    /**
     * 新增工单详情，只有补货工单才有
     * 
     * @param taskDetails 工单详情，只有补货工单才有
     * @return 结果
     */
    public int insertTaskDetails(TaskDetails taskDetails);

    /**
     * 修改工单详情，只有补货工单才有
     * 
     * @param taskDetails 工单详情，只有补货工单才有
     * @return 结果
     */
    public int updateTaskDetails(TaskDetails taskDetails);

    /**
     * 批量删除工单详情，只有补货工单才有
     * 
     * @param detailsIds 需要删除的工单详情，只有补货工单才有主键集合
     * @return 结果
     */
    public int deleteTaskDetailsByDetailsIds(Long[] detailsIds);

    /**
     * 删除工单详情，只有补货工单才有信息
     * 
     * @param detailsId 工单详情，只有补货工单才有主键
     * @return 结果
     */
    public int deleteTaskDetailsByDetailsId(Long detailsId);
}
