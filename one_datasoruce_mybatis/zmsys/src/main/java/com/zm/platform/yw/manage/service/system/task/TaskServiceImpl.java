package com.zm.platform.yw.manage.service.system.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zm.platform.common.exception.runtime.CurrentException;
import com.zm.platform.common.exception.runtime.ParamIsNullException;
import com.zm.platform.quartz.JobStatus;
import com.zm.platform.quartz.service.QuartzService;
import com.zm.platform.quartz.util.MethodCheck;
import com.zm.platform.common.util.EmptyUtil;
import com.zm.platform.yw.manage.dao.system.task.TaskDao;
import com.zm.platform.yw.manage.entity.system.task.Task;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private QuartzService quartzService;

    @Override
    public PageInfo<Task> getObjectListByPage(Map<String, Object> params) {
        if(params == null){
            throw new ParamIsNullException("参数为空");
        }
        Object pageNum = params.get("pageNum");
        Object pageSize = params.get("pageSize");
        if(EmptyUtil.isNotEmpty_All(pageNum, pageSize)){
            PageHelper.startPage((Integer)pageNum, (Integer)pageSize);
        }
        List<Task> list = taskDao.getObjectListByPage(params);
        PageInfo<Task> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public Task getObjectById(Long id) {
        if (EmptyUtil.isNullOrEmpty(id)) {
            throw new ParamIsNullException("参数为空");
        }
        return taskDao.getObjectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveObject(Task task) {
        if (task == null) {
            throw new ParamIsNullException("参数为空");
        }
        if(!CronExpression.isValidExpression(task.getCronExpression())){
            throw new CurrentException("cron表达式错误");
        }
        if(!MethodCheck.checkHasMethod(task.getInvokeName())){
            throw new CurrentException("未查询到此方法");
        }
        int status = task.getStatus();
        if (JobStatus.START_ONCE == status) {
            //执行一次存数据库为不执行。
            task.setStatus(JobStatus.STOP);
        }
        if (taskDao.saveObject(task)) {
            //存完后恢复原值，开始任务会判断这个状态
            task.setStatus(status);
            //不执行
            if(task.getStatus() != JobStatus.STOP){
                try {
                    quartzService.addJob(task);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                    throw new CurrentException("添加任务失败！");
                }
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateObject(Task task) {
        if (EmptyUtil.isNullOrEmpty(task) || EmptyUtil.isNullOrEmpty(task.getId())) {
            throw new ParamIsNullException("参数为空");
        }
        if(!CronExpression.isValidExpression(task.getCronExpression())){
            throw new CurrentException("cron表达式错误");
        }
        if(!MethodCheck.checkHasMethod(task.getInvokeName())){
            throw new CurrentException("未查询到此方法");
        }
        int status = task.getStatus();
        if (JobStatus.START_ONCE == status) {
            //执行一次存数据库为不执行。
            task.setStatus(JobStatus.STOP);
        }
        if (taskDao.updateObject(task)) {
            //存完后恢复原值，开始任务会判断这个状态
            task.setStatus(status);
            try {
                //删除任务
                quartzService.pause(task);
                if (task.getStatus() != JobStatus.STOP) {
                    //执行任务
                    quartzService.addJob(task);
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
                throw new CurrentException("修改任务状态失败！");
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteObjectByIds(String ids) {
        if(EmptyUtil.isNullOrEmpty(ids)){
            throw new ParamIsNullException("参数为空");
        }
        String[] s = ids.split(",");
        for (String id: s) {
            Task task = taskDao.getObjectById(Long.valueOf(id));
            if(!taskDao.deleteObjectByIds(id.split(","))){
                return false;
            }
            try {
                //删除任务
                quartzService.pause(task);
            } catch (SchedulerException e) {
                e.printStackTrace();
                throw new CurrentException("删除任务失败！");
            }
        }
        return true;
    }

    /**
     * 暂停、继续任务
     * @param job
     * @return
     */
    public boolean startOrPause(Task job) {
        String msg = null;
        try {
            if (job.getStatus() == JobStatus.START_NOW){
                msg = "启动任务失败！";
                quartzService.addJob(job);
            }else if (job.getStatus() == JobStatus.STOP){
                msg = "暂停任务失败！";
                quartzService.pause(job);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new CurrentException(msg);
        }
        return true;
    }

    /**
     * 执行一次任务
     * @param job
     * @return
     */
    public boolean startOne(Task job) {
        try {
            job.setStatus(JobStatus.START_ONCE);
            quartzService.addJob(job);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new CurrentException("启动任务失败");
        }
        return true;
    }

    @Override
    public void startAllJob() throws SchedulerException {

    }

    @Override
    public void pauseAllJob() throws SchedulerException {

    }
}
