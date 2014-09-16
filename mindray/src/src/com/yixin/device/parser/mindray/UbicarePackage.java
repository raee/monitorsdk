package com.yixin.device.parser.mindray;

/**
 * ubicare 数据包类
 * @author Administrator
 *
 */
public class UbicarePackage {
	
	private byte 	version;			//协议版本
	private byte 	commandType;		//命令字类型
	private short 	commandID;			//命令字ID
	private int		pkgLength;			//包总长度
	private int		pkgTypeID;			//包类型ID
	private int		relationPkgTypeID;	//关联包类型ID
	private short	pkgCheckSum;		//包校验和
	private short	signBit;			//标志位
	private byte	responseCode;		//应答码
	private byte	saveCode;			//保留字段
	private short	optionLength;		//option项长度
	private byte[]	option;				//Option项
	@SuppressWarnings("unused")
	private byte[]  dataPkg;			//数据包体
	
	public UbicarePackage() {
		this.version = 0;
		this.commandType = 0;
		this.commandID = 0;
		this.pkgLength = 0;
		this.pkgTypeID = 0;
		this.relationPkgTypeID = 0;
		this.pkgCheckSum = 0;
		this.signBit = 0;
		this.responseCode = 0;
		this.saveCode = 0;
		this.optionLength = 0;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getCommandType() {
		return commandType;
	}

	public void setCommandType(byte commandType) {
		this.commandType = commandType;
	}

	public short getCommandID() {
		return commandID;
	}

	public void setCommandID(short commandID) {
		this.commandID = commandID;
	}

	public int getPkgLength() {
		return pkgLength;
	}

	public void setPkgLength(int pkgLength) {
		this.pkgLength = pkgLength;
	}

	public int getPkgTypeID() {
		return pkgTypeID;
	}

	public void setPkgTypeID(int pkgTypeID) {
		this.pkgTypeID = pkgTypeID;
	}

	public int getRelationPkgTypeID() {
		return relationPkgTypeID;
	}

	public void setRelationPkgTypeID(int relationPkgTypeID) {
		this.relationPkgTypeID = relationPkgTypeID;
	}

	public short getPkgCheckSum() {
		return pkgCheckSum;
	}

	public void setPkgCheckSum(short pkgCheckSum) {
		this.pkgCheckSum = pkgCheckSum;
	}

	public short getSignBit() {
		return signBit;
	}

	public void setSignBit(short signBit) {
		this.signBit = signBit;
	}

	public byte getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(byte responseCode) {
		this.responseCode = responseCode;
	}

	public byte getSaveCode() {
		return saveCode;
	}

	public void setSaveCode(byte saveCode) {
		this.saveCode = saveCode;
	}

	public short getOptionLength() {
		return optionLength;
	}

	public void setOptionLength(short optionLength) {
		this.optionLength = optionLength;
	}

	public byte[] getOption() {
		return option;
	}

	public void setOption(byte[] option) {
		this.option = option;
	}
}