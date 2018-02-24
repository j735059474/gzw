package nc.uap.wfm.participantdefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.util.LfwClassUtil;
import nc.uap.wfm.model.HumAct;
import nc.uap.wfm.model.Participant;
import nc.uap.wfm.participant.IParticipantAdapter;
import nc.uap.wfm.participant.IParticipantService;
import nc.uap.wfm.participant.ParticipantContext;
import nc.uap.wfm.participant.ParticipantTypeFactory;
import uap.web.bd.pub.user.CpUserManager;

public class ParticipantServiceImpl implements IParticipantService{

	@Override
	public List<String> getUsers(ParticipantContext participantContext) {
				List<Participant> participants=participantContext.getParticipants();
		if(participants==null||participants.size()==0)
			return null;
		participantContext.setPk_curUser(participantContext.getPk_curUser());
		List<String> allUserIds= new ArrayList<String>();
		IParticipantAdapter participantAdapter=null;

		List<String> currUserIds=null;
		for(int i=0; i<participants.size();i++){
			Participant participant=participants.get(i);
			participantAdapter=this.getParticipantAdapter(participant);
			List<Participant> curParticipants=new ArrayList<Participant>();
			curParticipants.add(participant);
			participantContext.setParticipants(curParticipants);
			//根据默认实现查找
			currUserIds=participantAdapter.findUsers(participantContext);
			if(currUserIds==null||currUserIds.size()==0)
				continue;
			allUserIds.addAll(currUserIds);
		}
		return allUserIds;
	}

	public boolean isContainCurUser(ParticipantContext participantContext) {
		List<Participant> participants = participantContext.getParticipants();
		if (participants == null || participants.size() == 0)
			return false;
		IParticipantAdapter participantAdapter = null;

		boolean isContainCurUser = false;
		for (int i = 0; i < participants.size(); i++) {
			Participant participant = participants.get(i);
			participantAdapter = this.getParticipantAdapter(participant);
			List<Participant> curParticipants = new ArrayList<Participant>();
			curParticipants.add(participant);
			participantContext.setParticipants(curParticipants);

			isContainCurUser = participantAdapter
					.isContainCurUser(participantContext);
			if (isContainCurUser){
//				isContainCurUser=true;
				return true;
			}
		}
		return isContainCurUser;

	}
	/**
	 * 是否可串行排序
	 */
	public boolean isOrder(ParticipantContext participantContext){
		boolean isOrder=true;
		List<Participant> participants=participantContext.getParticipants();
		if(participants==null||participants.size()==0)return false;
		for (Participant participant : participants) {
			if(!ParticipantTypeFactory.getInstance().getType(participant.getTypeCode()).isSerialAble()){
				isOrder=false;
				break;
			}
		}
		return isOrder;
	}
	//得到逐级审批点以供下一步使用
	@Override
	public String getPk_dynamicOnwer(ParticipantContext participantContext){
		if(this.getSelfCircleParticipant(participantContext)==null) return null;
		return this.getSelfCircleParticipant(participantContext).getPk_dynamicOnwer(participantContext);
	}
	//得到逐级审批类型以供下一步使用
	@Override
	public String getDynamicType(ParticipantContext participantContext){
		if(this.getSelfCircleParticipant(participantContext)==null) return null;
		return this.getSelfCircleParticipant(participantContext).getDynamicType(participantContext);
	}
	/**
	 * 以键值对的形式返回参与者的名称key为主键value为显示名称
	 * @param participant
	 * @return
	 */
	public Map<String,String> getShowValues(Participant participant){
		return this.getParticipantAdapter(participant).getShowValue(participant);
	}
	private IParticipantAdapter getParticipantAdapter(Participant participant){
		IParticipantAdapter participantAdapter=null;
		String implClassName=ParticipantTypeFactory.getInstance().getImpl(participant.getTypeCode());
		participantAdapter=(IParticipantAdapter) LfwClassUtil.newInstance(implClassName);
		return  participantAdapter;
	}
	
	private IParticipantAdapter getSelfCircleParticipant(ParticipantContext participantContext){
		HumAct curHumAct=participantContext.getHumAct();
		if(!HumAct.ActionType_SelfCircle.equalsIgnoreCase(curHumAct.getActionType()))
			return null;
		participantContext.setPk_curUser(participantContext.getPk_curUser()==null?CpUserManager.getPk_user():participantContext.getPk_curUser());
		List<Participant> participants=participantContext.getParticipants();
		if(participants==null||participants.size()==0)
			return null;
		Participant participant=participants.get(0);
		return this.getParticipantAdapter(participant);
	}
}
