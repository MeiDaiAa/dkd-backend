package com.dkd.manage.domain.dto;

import com.dkd.manage.domain.Task;
import com.dkd.manage.domain.TaskType;
import lombok.Data;

import java.util.List;

@Data
public class TaskDTO extends Task {
    // 工单类型
    private TaskType taskType;
    // 创建类型
    private Long createType;
    // 关联设备编号
    private String innerCode;
    // 任务执行人Id
    private Long userId;
    // 用户创建人id
    private Long assignorId;
    // 工单类型
    private Long productTypeId;
    // 描述信息
    private String desc;
    // 工单详情(只有补货工单才涉及)
    private List<TaskDetailsDto> details;
}
