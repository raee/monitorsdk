package com.yixin.monitors.sdk.mindray.parser;

/**
 * 序列包
 * @author wgj
 *
 */
public class UbicareSequencePkg {

	private int 	sequenceNum;	//序列号
	private byte 	operateCode;	//操作码
	private short	pkgStreamType;	//数据包流类型
	private byte[]	pkgStream;		//数据包流组成
	
	public UbicareSequencePkg(){
		
		this.sequenceNum = 0;
		this.operateCode = 0;
		this.pkgStreamType = 0;
	}

	public int getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	public byte getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(byte operateCode) {
		this.operateCode = operateCode;
	}

	public short getPkgStreamType() {
		return pkgStreamType;
	}

	public void setPkgStreamType(short pkgStreamType) {
		this.pkgStreamType = pkgStreamType;
	}

	public byte[] getPkgStream() {
		return pkgStream;
	}

	public void setPkgStream(byte[] pkgStream) {
		this.pkgStream = pkgStream;
	}
}
