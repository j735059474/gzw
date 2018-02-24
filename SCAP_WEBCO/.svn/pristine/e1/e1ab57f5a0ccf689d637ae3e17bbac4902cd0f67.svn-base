package nc.scap.pub.sms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.scap.pub.vos.ScapSmsVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.lang.UFDateTime;

public class SmsManageService {

    public static ScapSmsTaskVO addTask(ApplicationContext ctx, String content, String numbers) {
		String mdName = getMdName(ctx);
		String userName = getUserName(ctx);
		ScapSmsTaskVO task = createScapSmsTaskVO(content, numbers, mdName, userName);
		saveTask(task);
	    return task;
    }

    public static ScapSmsTaskVO addTask(ApplicationContext ctx, String content, List<String> numbers) {
		String mdName = getMdName(ctx);
		String userName = getUserName(ctx);
		ScapSmsTaskVO task = createScapSmsTaskVO(content, numbers, mdName, userName);
		saveTask(task);
		return task;
    }

    public static ScapSmsTaskVO addTask(String content, String numbers, String mdName, String userName) {
		ScapSmsTaskVO task = createScapSmsTaskVO(content, numbers, mdName, userName);
		saveTask(task);
		return task;
    }

    public static ScapSmsTaskVO addTask(String content, List<String> numbers, String mdName, String userName) {
		ScapSmsTaskVO task = createScapSmsTaskVO(content, numbers, mdName, userName);
		saveTask(task);
		return task;
    }

	public static void addTask(ScapSmsTaskVO task) {
		if (task.getSmses() == null || task.getSmses().length == 0) {
			List<String> numbers = splitNumbers(task.getAddress());
			String numberStr = joinNumbers(numbers);
			ScapSmsVO[] smses = createScapSmsVOs(numbers);
			task.setSmses(smses);
			task.setAddress(numberStr);
		}
		saveTask(task);
	}

    public static ScapSmsTaskVO[] findTasks(String[] taskStatusEnums, String[] smsStatusEnums) {
		
		ScapSmsTaskVO[] tasks = new ScapSmsTaskVO[0];
		
		final String taskCondition, smsCondition;
		SQLParameter tmpParam = new SQLParameter();
		StringBuilder tmpStr = new StringBuilder();
		
		if (taskStatusEnums == null || taskStatusEnums.length == 0) {
			taskCondition = ScapSmsTaskVO.PK_TASK + " is not null";
		} else {
			tmpStr.append(ScapSmsTaskVO.SENDSTATUS).append("in(");
			for (String status : taskStatusEnums) {
	            tmpStr.append("?,");
	            tmpParam.addParam(status);
            }
			tmpStr.setLength(tmpStr.length() - 1);
			tmpStr.append(")");
			taskCondition = tmpStr.toString();
		}
		
		try {
	        tasks = (ScapSmsTaskVO[]) ScapDAO.getBaseDAO().retrieveByClause(ScapSmsTaskVO.class, taskCondition, tmpParam).toArray(tasks);
	        
	        if (tasks != null) {
		        tmpParam.clearParams();
		        tmpStr.setLength(0);
		        
		        tmpParam.addParam("");
		        if (smsStatusEnums == null || smsStatusEnums.length == 0) {
		        	smsCondition = ScapSmsVO.PK_TASK + "=?";
		        } else {
		        	tmpStr.append(ScapSmsVO.PK_TASK).append("=? and ").append(ScapSmsVO.SENDSTATUS).append("in(");
		        	for (String status : smsStatusEnums) {
		        		tmpStr.append("?,");
		        		tmpParam.addParam(status);
		        	}
		        	tmpStr.setLength(tmpStr.length() - 1);
		        	tmpStr.append(")");
		        	smsCondition = tmpStr.toString();
		        }
	        	
	        	for (ScapSmsTaskVO task : tasks) {
		        	ScapSmsVO[] smses = new ScapSmsVO[0];
	                tmpParam.replace(1, task.getPk_task());
	                smses = (ScapSmsVO[]) ScapDAO.getBaseDAO().retrieveByClause(ScapSmsVO.class, smsCondition, tmpParam).toArray(smses);
	                task.setSmses(smses);
                }
	        }
        } catch (DAOException e) {
        	ScapLogger.error(e);
        }
		
		
		return tasks;
    }

    public static ScapSmsTaskVO findTask(String pkTask, String... smsStatusEnums) {
		
		ScapSmsTaskVO task = (ScapSmsTaskVO) ScapDAO.retrieveByPK(ScapSmsTaskVO.class, pkTask);
		if (task != null) {
			SQLParameter params = new SQLParameter();
			StringBuilder condition = new StringBuilder(ScapSmsVO.PK_TASK + "=?");
			
			params.addParam(pkTask);
			if (smsStatusEnums != null && smsStatusEnums.length != 0) {
				condition.append(" and ").append(ScapSmsVO.SENDSTATUS).append(" in(");
				for (String status : smsStatusEnums) {
					condition.append("?,");
					params.addParam(status);
				}
				condition.setLength(condition.length() - 1);
				condition.append(")");
			}
			
			try {
	            Collection smsCollection = ScapDAO.getBaseDAO().retrieveByClause(ScapSmsVO.class, condition.toString(), params);
	            if (smsCollection != null && !smsCollection.isEmpty()) {
	            	Object[] objs = smsCollection.toArray();
	            	ScapSmsVO[] smses = new ScapSmsVO[objs.length];
	            	for (int i = 0; i < objs.length; i++) {
	                    smses[i] = (ScapSmsVO) objs[i];
                    }
	            	task.setSmses(smses);
	            }
            } catch (DAOException e) {
            	ScapLogger.error(e);
            }
		}
		
	    return task;
    }

	/**
	 * 构建一个ScapSmsTaskVO和相关ScapSmsVO
	 */
	public static ScapSmsTaskVO createScapSmsTaskVO(String content, String numbers, String mdName, String userName) {
		ScapSmsTaskVO task = new ScapSmsTaskVO();
		List<String> numberList = splitNumbers(numbers);
		numbers = joinNumbers(numberList);
		
		task.setContent(content);
		task.setAddress(numbers);
		task.setMdname(mdName);
		task.setUsername(userName);
		task.setCreatetime(new UFDateTime(new Date(System.currentTimeMillis())));
		task.setSendstatus(ScapSmsTaskStatusEnum.NOT_FINISHED);
		task.setSendtimes(0);

		ScapSmsVO[] smses = createScapSmsVOs(numberList);
		task.setSmses(smses);
		
		return task;
	}
	
	/**
	 * 构建一个ScapSmsTaskVO和相关ScapSmsVO
	 */
	public static ScapSmsTaskVO createScapSmsTaskVO(String content, List<String> numbers, String mdName, String userName) {
		ScapSmsTaskVO task = new ScapSmsTaskVO();
		String numberStr = joinNumbers(numbers);
		numbers = splitNumbers(numberStr);

		task.setContent(content);
		task.setAddress(numberStr);
		task.setMdname(mdName);
		task.setUsername(userName);
		task.setCreatetime(new UFDateTime(new Date(System.currentTimeMillis())));
		task.setSendstatus(ScapSmsTaskStatusEnum.NOT_FINISHED);
		task.setSendtimes(0);

		ScapSmsVO[] smses = createScapSmsVOs(numbers);
		task.setSmses(smses);
		
		return task;
	}
	
	/**
	 * 根据号码构建一组ScapSmsVO
	 */
	public static ScapSmsVO[] createScapSmsVOs(List<String> numbers) {
		List<ScapSmsVO> list = new ArrayList<ScapSmsVO>();
		UFDateTime sendtime = new UFDateTime(new Date(System.currentTimeMillis()));
		for (String number : numbers) {
			if (number == null || number.trim().isEmpty()) {
				continue;
			}
			ScapSmsVO vo = new ScapSmsVO();
			vo.setAddress(number);
			vo.setSendstatus(ScapSmsStatusEnum.NOT_SENT);
			vo.setSendtimes(0);
			vo.setSendtime(sendtime);
			list.add(vo);
		}
	    return list.toArray(new ScapSmsVO[0]);
    }

	/**
	 * 持久化task和task.getSmses()
	 */
	public static void saveTask(ScapSmsTaskVO task) {
		BaseDAO dao = new BaseDAO();
		try {
	        String pkTask = dao.insertVO(task);
	        for (ScapSmsVO sms : task.getSmses()) {
	        	sms.setPk_task(pkTask);
	        }
	        dao.insertVOArray(task.getSmses());
        } catch (DAOException e) {
        	ScapLogger.error("新增短信任务出错：" + e.getMessage(), e);
        }
	}
	
	/**
	 * 将号码字符串拆分成List
	 */
	public static List<String> splitNumbers(String numberStr) {
		List<String> result = new ArrayList<String>();
		HashSet<String> numberSet = new HashSet<String>();
		
		if (numberStr == null) {
			return result;
		}
		
		char[] cs = numberStr.toCharArray();
		StringBuilder temp = new StringBuilder();
		for (int i = 0, j = cs.length; i < j; i++) {
			char c = cs[i];
			
			if (c < '0' || c > '9') {    // 除数字外的其它字符当作分隔符
				if (temp.length() != 0) {
					numberSet.add(temp.toString());
					temp.setLength(0);
				}
			} else {    // 数字
				temp.append(c);
			}
		}
		
		if (temp.length() > 0) {
			numberSet.add(temp.toString());
		}
		
		result.addAll(numberSet);
		return result;
	}
	
	public static String joinNumbers(List<String> numbers) {
		StringBuilder sb = new StringBuilder();
		
		if (numbers == null || numbers.size() == 0) {
			return "";
		}
		
		for (String number : numbers) {
	        sb.append(number);
	        sb.append(',');
        }
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	
	public static String getMdName(ApplicationContext ctx) {
		String mdName = ctx.getApplication().getCaption();
		
		if (mdName == null || mdName.trim().length() == 0) {
			String pk_appsnode = ctx.getAppEnvironment().getPk_appsnode();
			CpAppsNodeVO appsNode = (CpAppsNodeVO) ScapDAO.retrieveByPK(CpAppsNodeVO.class, pk_appsnode);
			if (appsNode != null) {
				mdName = appsNode.getTitle();
			}
		}
		
		if (mdName == null || mdName.trim().length() == 0) {
			mdName = "未知模块";
		}
		
		return mdName;
	}
	
	public static String getUserName(ApplicationContext ctx) {
		CpUserVO user = (CpUserVO) ScapDAO.retrieveByPK(CpUserVO.class, ctx.getAppEnvironment().getLoginUser());
		if (user != null) {
			return user.getUser_name();
		}
		return null;
	}
	
}
