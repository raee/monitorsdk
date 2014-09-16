package com.yixin.device.parser.mindray;

import java.util.ArrayList;
import java.util.List;


public class CMSPacketStream {

	//序列号
	protected int	seqenceNum;
	protected int	operateCode;
	protected int	packetStreamType;
	protected int[] arrCmdID;
	
	//数据包流包含的数据包集合
	protected List<CMSPacket> packets;		
	
	public List<CMSPacket> getPackets() {
		return packets;
	}

	public CMSPacketStream(){
		packets = new ArrayList<CMSPacket>();
	}
	
	public CMSPacketStream(CMSPacket packet){
		
		for (CMSPacket.Field field : packet.fileds) {
			if(field.id==CMSDefine.FIELD_ID_SEQUENCE_NUM){
				this.seqenceNum = CMSPacket.bufferToInt(field.value,0);			
			} else if(field.id==CMSDefine.FIELD_ID_OPERATE_CODE){
				this.operateCode = CMSPacket.bufferToByte(field.value,0); 
			} else if(field.id==CMSDefine.FIELD_ID_DATA_STREAM_TYPE){
				this.packetStreamType = CMSPacket.bufferToShort(field.value,0);
			} else if(field.id==CMSDefine.FIELD_ID_DATA_STREAM){
				int nLen = field.length / 2;
				this.arrCmdID = new int[nLen];
				for(int i=0;i<nLen;i++){
					arrCmdID[i] = CMSPacket.bufferToShort(field.value,i*2);
				}
			}
		}
		packets = new ArrayList<CMSPacket>();
	}

	public int getSeqenceNum(){
		return this.seqenceNum;
	}
	
	public void setSeqenceNum(int nVal){
		this.seqenceNum = nVal;
	}	
	
	public int getOperateCode(){
		return this.operateCode;
	}
	
	public void setOperateCode(int nVal){
		this.operateCode = nVal;
	}	
	
	public int getPacketStreamType(){
		return this.packetStreamType;
	}
	
	public void setPacketStreamType(int nVal){
		this.packetStreamType = nVal;
	}		
	
	
	/**
	 * 添加数据包
	 * 
	 * @param data
	 * @param start
	 */	
	public void addPacket(CMSPacket packet){
		packets.add(packet);
	}
	
	/**
	 * 判断当前数据包流是否已经完整,即序列包描述的数据包数量与附加的数据包数组数量一致
	 * 
	 * @param data
	 * @param start
	 */		
	public boolean isPacketStreamFull(){
		if(this.arrCmdID.length==packets.size()){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断当前数据包流是否正确，每个数据包校验正确并且命令字与序列包描述一致
	 * 
	 * @param data
	 * @param start
	 */		
	public boolean isPacketStreamCorrect(){
		if(!this.isPacketStreamFull()){
			return false;
		}
		for (int i=0;i<this.packets.size();i++) {
			CMSPacket packet = packets.get(i);
			int nCmdID = this.arrCmdID[i];
			if(!packet.isPkgCheckOk()){
				return false;
			}
			if(packet.commandID!=nCmdID){
				return false;
			}
		}
		return true;
	}	
	
	public byte[] toBuffer() {
		arrCmdID = null;
		int nLen = 0;
		int nSize = packets.size(); 
		if(nSize > 0){
			arrCmdID = new int[nSize];
			for(int i=0;i<nSize;i++){
				CMSPacket packet = packets.get(i);
				packet.toBuffer();
				arrCmdID[i] = packet.getCommandID();
				nLen += packet.getPkgLength();
			}
		}
		CMSPacket packet = new CMSPacket();
		packet.makeSeqPack(this.seqenceNum, this.operateCode, this.packetStreamType, this.arrCmdID);
		byte[] buf = packet.toBuffer();
		nLen += packet.getPkgLength();
		
		int start = 0;
		byte[] data = new byte[nLen];
		System.arraycopy(buf, 0, data, start, buf.length);
		start += buf.length;
		
		if(nSize > 0){
			for(int i=0;i<nSize;i++){
				packet = packets.get(i);
				buf = packet.toBuffer();
				System.arraycopy(buf, 0, data, start, buf.length);
				start += buf.length;
			}
		}
		return data;
	}	
	
	public CMSPacket findPacket(int nCmdID){
		if(this.packets==null){
			return null;
		}
		int nSize = this.packets.size();
		CMSPacket packet = null;
		for(int i=0;i<nSize;i++){
			packet = packets.get(i);
			if(packet.getCommandID()==nCmdID){
				return packet;
			}
		}
		return null;
	}
	
	public CMSPacket.Field findField(int nCmdID,int nFID){
		CMSPacket packet = this.findPacket(nCmdID);
		if(packet!=null){
			return packet.findField(nFID);
		}
		return null;
	}	
	
}
