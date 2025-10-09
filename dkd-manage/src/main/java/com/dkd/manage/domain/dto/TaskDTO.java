package com.dkd.manage.domain.dto;

import com.dkd.manage.domain.Task;
import com.dkd.manage.domain.TaskType;
import lombok.Data;

@Data
public class TaskDTO extends Task {
    // 工单类型
    private TaskType taskType;
}
