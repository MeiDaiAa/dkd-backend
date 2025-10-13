package com.dkd.manage.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dkd.common.constant.DkdContants;
import com.dkd.common.exception.ServiceException;
import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.*;
import com.dkd.manage.domain.dto.TaskDTO;
import com.dkd.manage.domain.dto.TaskDetailsDto;
import com.dkd.manage.service.IEmpService;
import com.dkd.manage.service.ITaskDetailsService;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.TaskMapper;
import com.dkd.manage.service.ITaskService;

/**
 * 工单Service业务层处理
 * 
 * @author meidaia
 * @date 2025-10-09
 */
@Service
public class TaskServiceImpl implements ITaskService 
{
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private IVendingMachineService vendingMachineService;

    @Autowired
    private IEmpService empService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ITaskDetailsService taskDetailsService;

    /**
     * 查询工单
     * 
     * @param taskId 工单主键
     * @return 工单
     */
    @Override
    public Task selectTaskByTaskId(Long taskId)
    {
        return taskMapper.selectTaskByTaskId(taskId);
    }

    /**
     * 查询工单列表
     * 
     * @param task 工单
     * @return 工单
     */
    @Override
    public List<Task> selectTaskList(Task task)
    {
        return taskMapper.selectTaskList(task);
    }

    /**
     * 新增工单
     *
     * @param taskDTO 工单
     * @return 结果
     */
    @Override
    public int insertTask(TaskDTO taskDTO)
    {
        // 检验售货机
        VendingMachine vm = vendingMachineService.selectVendingMachineByInnerCode(taskDTO.getInnerCode());
        if (vm == null) {
           throw new ServiceException("未查询到售货机信息");
        }
        // 检验工单类型是否与售货机状态相符
        checkCreateTask(vm.getVmStatus(), taskDTO.getProductTypeId());
        // 校验指派员工是否符合
        Emp emp = empService.selectEmpById(taskDTO.getUserId());
        checkAssignor(emp, taskDTO.getProductTypeId(), vm.getRegionId());
        // 检验是否有重复工单
        hasTask(taskDTO);


        //将dto转为po并补充属性，保存工单
        // 属性复制
        Task task = BeanUtil.copyProperties(taskDTO, Task.class);
        // 创建工单
        task.setTaskStatus(DkdContants.TASK_STATUS_CREATE);
        // 执行人名称
        task.setUserName(emp.getUserName());
        // 所属区域id
        task.setRegionId(vm.getRegionId());
        // 地址
        task.setAddr(vm.getAddr());
        // 创建时间
        task.setCreateTime(DateUtils.getNowDate());
        // 工单编号
        task.setTaskCode(generateTaskCode());
        //保存工单
        int taskResult = taskMapper.insertTask(task);

        //7. 判断是否为补货工单
        if (taskDTO.getProductTypeId().equals(DkdContants.TASK_TYPE_SUPPLY)) {
            // 8.保存工单详情
            List<TaskDetailsDto> details = taskDTO.getDetails();
            if (CollUtil.isEmpty(details)) {
                throw new ServiceException("补货工单详情不能为空");
            }
            // 将dto转为po补充属性
            List<TaskDetails> taskDetailsList = details.stream().map(dto -> {
                TaskDetails taskDetails = BeanUtil.copyProperties(dto, TaskDetails.class);
                taskDetails.setTaskId(task.getTaskId());
                return taskDetails;
            }).collect(Collectors.toList());
            // 批量新增
            taskDetailsService.batchInsertTaskDetails(taskDetailsList);
        }


        return taskResult;
    }

    /**
     * 生成并获取当天任务代码的唯一标识。
     * 该方法首先尝试从Redis中获取当天的任务代码计数，如果不存在，则初始化为1并返回"日期0001"格式的字符串。
     * 如果存在，则对计数加1并返回更新后的任务代码。
     *
     * @return 返回当天任务代码的唯一标识，格式为"日期XXXX"，其中XXXX是四位数字的计数。
     */
    public String generateTaskCode() {
        // 获取当前日期并格式化为"yyyyMMdd"
        String dateStr = DateUtils.getDate().replaceAll("-", "");
        // 根据日期生成redis的键
        String key = "dkd.task.code." + dateStr;
        // 判断key是否存在
        if (!redisTemplate.hasKey(key)) {
            // 如果key不存在，设置初始值为1，并指定过期时间为1天
            redisTemplate.opsForValue().set(key, 1, Duration.ofDays(1));
            // 返回工单编号（日期+0001）
            return dateStr + "0001";
        }
        // 如果key存在，计数器+1（0002），确保字符串长度为4位
        return dateStr+ StrUtil.padPre(redisTemplate.opsForValue().increment(key).toString(),4,'0');
    }

    /**
     * 检验是否有重复工单
     *
     * @param taskDTO 工单
     */
    private void hasTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setInnerCode(taskDTO.getInnerCode());
        task.setProductTypeId(taskDTO.getProductTypeId());
        List<Task> tasks = taskMapper.selectTaskList(task);
        // 检查是否存在与当前工单类型一致的未完成工单
        boolean b = tasks.stream().anyMatch(t ->
                DkdContants.TASK_STATUS_CREATE.equals(t.getTaskStatus())
                || DkdContants.TASK_STATUS_PROGRESS.equals(t.getTaskStatus()));
        if(b) {
            throw new ServiceException("已存在与当前工单类型一致的未完成工单");
        }
    }


    /**
     * 检验指派员工是否符合
     *
     * @param emp 员工
     * @param productTypeId 工单类型
     * @param regionId 区域id
     */
    private void checkAssignor(Emp emp, Long productTypeId, Long regionId) {
        if (emp == null) {
            throw new ServiceException("员工不存在");
        }
        // 员工负责区域是否符合
        if(!emp.getRegionId().equals(regionId)) {
            throw new ServiceException("员工负责区域不符合");
        }

        //如果为补货工单，员工角色必须为运营员
        if(DkdContants.TASK_TYPE_SUPPLY.equals(productTypeId)) {
            if(!DkdContants.ROLE_CODE_BUSINESS.equals(emp.getRoleCode())) {
                throw new ServiceException("员工角色不符合");
            }
        } else {
            // 如果为其他工单，员工角色必须为维修员
            if(!DkdContants.ROLE_CODE_OPERATOR.equals(emp.getRoleCode())) {
                throw new ServiceException("员工角色不符合");
            }
        }
    }

    /**
     * 检验工单类型是否与售货机状态相符
     *
     * @param vmStatus 售货机状态
     * @param productTypeId 工单类型
     */
    private void checkCreateTask(Long vmStatus, Long productTypeId) {
        // 如果是投放工单，售货机状态必须为未投放
        if(DkdContants.TASK_TYPE_DEPLOY.equals(productTypeId) && !DkdContants.VM_STATUS_NODEPLOY.equals(vmStatus)) {
            throw new ServiceException("售货机已投放");
        }
        // 如果是补货工单，售货机状态必须为运营
        if(DkdContants.TASK_TYPE_SUPPLY.equals(productTypeId) && !DkdContants.VM_STATUS_RUNNING.equals(vmStatus)) {
            throw new ServiceException("售货机未运行");
        }
        // 如果是维修工单，售货机状态必须为运营
        if(DkdContants.TASK_TYPE_REPAIR.equals(productTypeId) && !DkdContants.VM_STATUS_RUNNING.equals(vmStatus)) {
            throw new ServiceException("售货机未运行");
        }
        // 如果是撤机工单，售货机状态必须为运营
        if(DkdContants.TASK_TYPE_REVOKE.equals(productTypeId) && !DkdContants.VM_STATUS_RUNNING.equals(vmStatus)) {
            throw new ServiceException("售货机未运行");
        }
    }

    /**
     * 修改工单
     * 
     * @param task 工单
     * @return 结果
     */
    @Override
    public int updateTask(Task task)
    {
        task.setUpdateTime(DateUtils.getNowDate());
        return taskMapper.updateTask(task);
    }

    /**
     * 批量删除工单
     * 
     * @param taskIds 需要删除的工单主键
     * @return 结果
     */
    @Override
    public int deleteTaskByTaskIds(Long[] taskIds)
    {
        return taskMapper.deleteTaskByTaskIds(taskIds);
    }

    /**
     * 删除工单信息
     * 
     * @param taskId 工单主键
     * @return 结果
     */
    @Override
    public int deleteTaskByTaskId(Long taskId)
    {
        return taskMapper.deleteTaskByTaskId(taskId);
    }

    /**
     * 查询工单列表
     *
     * @param task 工单
     * @return 工单集合
     */
    @Override
    public List<TaskDTO> selectTaskDtoList(TaskDTO task) {
        return taskMapper.selectTaskDtoList(task);
    }
}
