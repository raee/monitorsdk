package com.yixin.device.parser.mindray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CMSPacket {

	public static class CMSDate {
		public int year;
		public int month;
		public int day;
		public int hour;
		public int minute;
		public int second;

		public CMSDate() {
			year = Calendar.getInstance().get(Calendar.YEAR);
			month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			hour = Calendar.getInstance().get(Calendar.HOUR);
			if (Calendar.getInstance().get(Calendar.AM_PM) == Calendar.PM) {
				hour += 12;
			}
			minute = Calendar.getInstance().get(Calendar.MINUTE);
			second = Calendar.getInstance().get(Calendar.SECOND);
		}

		public byte[] toBuffer() {
			byte[] data = new byte[7];
			CMSPacket.shortToBuffer(data, 0, year);
			CMSPacket.byteToBuffer(data, 2, month);
			CMSPacket.byteToBuffer(data, 3, day);
			CMSPacket.byteToBuffer(data, 4, hour);
			CMSPacket.byteToBuffer(data, 5, minute);
			CMSPacket.byteToBuffer(data, 6, second);
			return data;
		}

		public boolean setToBuffer(byte[] data) {
			if (data.length < 7) {
				return false;
			}
			CMSPacket.shortToBuffer(data, 0, year);
			CMSPacket.byteToBuffer(data, 2, month);
			CMSPacket.byteToBuffer(data, 3, day);
			CMSPacket.byteToBuffer(data, 4, hour);
			CMSPacket.byteToBuffer(data, 5, minute);
			CMSPacket.byteToBuffer(data, 6, second);
			return true;
		}

		public String fromBuffer(byte[] data) {
			if (data.length < 7) {
				return "";
			}
			year = CMSPacket.bufferToShort(data, 0);
			month = CMSPacket.bufferToByte(data, 2);
			day = CMSPacket.bufferToByte(data, 3);
			hour = CMSPacket.bufferToByte(data, 4);
			minute = CMSPacket.bufferToByte(data, 5);
			second = CMSPacket.bufferToByte(data, 6);

			return year + "-" + month + "-" + day + " " + hour + ":" + minute
					+ ":" + second;
		}
	}

	/**
	 * 字段内容
	 * 
	 * @author Administrator
	 * 
	 */
	public static class Field {
		public int id;
		public int type;
		public int length;
		public byte[] value;

		public Field() {

		}

		public Field(int length) {
			this.length = length;
			value = new byte[length];
		}
	}

	// 版本
	protected int version;
	// 版本扩展
	protected int versionExt;
	// 命令类型
	protected int commandType;
	// 命令ID
	protected int commandID;
	// 包长度
	protected int pkgLength;
	// 包类型
	protected int pkgTypeID;
	// 关联包类型ID
	protected int relationPkgTypeID;
	// 包检验和
	protected int pkgCheckSum;
	// 标志位
	protected int signBit;
	// 应答码
	protected int responseCode;
	// 保留长度
	protected int saveCode;
	// 选择项长度
	protected int optionLength;

	// 选择项
	protected byte[] option;
	protected List<Field> fileds;

	// 数据包是否完整
	protected boolean pkgFull;

	// 数据包是否传输无误
	protected boolean pkgCheckOk;

	public CMSPacket() {
		this.version = CMSDefine.VERSION;
		this.versionExt = CMSDefine.VERSION_EXT;
		this.commandType = CMSDefine.FIELD_ID_SYSTEM_TIME;
		this.commandID = 0;
		this.pkgLength = 0;
		this.pkgTypeID = 0;
		this.relationPkgTypeID = 0;
		this.pkgCheckSum = 0;
		this.signBit = 0;
		this.responseCode = 0;
		this.saveCode = 0;
		this.optionLength = 0;
		this.option = null;
		this.fileds = null;
		this.pkgCheckOk = false;
		this.pkgFull = false;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getCommandType() {
		return commandType;
	}

	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}

	public int getCommandID() {
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

	public int getPkgCheckSum() {
		return pkgCheckSum;
	}

	public void setPkgCheckSum(short pkgCheckSum) {
		this.pkgCheckSum = pkgCheckSum;
	}

	public int getSignBit() {
		return signBit;
	}

	public void setSignBit(short signBit) {
		this.signBit = signBit;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(byte responseCode) {
		this.responseCode = responseCode;
	}

	public int getSaveCode() {
		return saveCode;
	}

	public void setSaveCode(byte saveCode) {
		this.saveCode = saveCode;
	}

	public int getOptionLength() {
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

	public boolean isCMSPacketFull() {

		return false;
	}

	public boolean isPkgCheckOk() {
		return pkgCheckOk;
	}

	public CMSPacket.Field findField(int nFID) {
		CMSPacket.Field field = null;
		for (int i = 0; i < this.fileds.size(); i++) {
			field = this.fileds.get(i);
			if (field.id == nFID) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 检验码
	 * 
	 * @param data
	 * @param start
	 * @param length
	 * @return
	 */
	public int getCheckSum(byte[] data, int start, int length) {
		if (start + length > data.length)
			return 0;
		int cksum = 0;
		int i = start;
		int size = length;
		while (size > 1) {
			size -= 2;
			cksum += (((data[i] & 0xff) << 8) | (data[i + 1] & 0xff));
			i += 2;
		}
		if (size == 1) {
			cksum += data[i] & 0xff;
		}

		cksum = (cksum >> 16) + (cksum & 0xffff);
		cksum += (cksum >> 16);
		cksum = (~cksum) & 0xffff;
		return cksum;
	}

	public boolean checkSum(byte[] data, int checkSum, int start, int length) {

		if (length < 0) {
			return false;
		}
		if (start + length > data.length)
			return false;

		int nCheckSum = getCheckSum(data, start, length);
		if (nCheckSum == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 从字节流中读取数据包内容 dataBuffer：字节流的指针 nBufferLen：字节流的长度 返回值：已经读取的长度
	 * 
	 * @author crj
	 * 
	 */
	public int fromBuffer(byte[] data, int start) {

		this.pkgCheckOk = false;
		this.pkgFull = false;

		// 首先判断字节流是否足够一个包头的大小
		if (data.length < start + 24) {
			return 0;
		}
		System.out.println("data length:" + String.valueOf(data.length));

		// 开始读取包头
		int nPos = start;
		this.version = CMSPacket.bufferToByte(data, nPos) & 0xf;
		;
		this.versionExt = (CMSPacket.bufferToByte(data, nPos) >> 4) & 0xf;
		nPos += 1;
		this.commandType = CMSPacket.bufferToByte(data, nPos);
		nPos += 1;
		this.commandID = CMSPacket.bufferToShort(data, nPos);
		nPos += 2;

		this.pkgLength = CMSPacket.bufferToInt(data, nPos);
		nPos += 4;

		this.pkgTypeID = CMSPacket.bufferToInt(data, nPos);
		nPos += 4;

		this.relationPkgTypeID = CMSPacket.bufferToInt(data, nPos);
		nPos += 4;

		this.pkgCheckSum = CMSPacket.bufferToShort(data, nPos);
		nPos += 2;

		this.signBit = CMSPacket.bufferToShort(data, nPos);
		nPos += 2;

		this.responseCode = CMSPacket.bufferToByte(data, nPos);
		nPos += 1;
		this.saveCode = CMSPacket.bufferToByte(data, nPos);
		nPos += 1;
		this.optionLength = (data[start + 22] << 8) | data[start + 23];
		/*
		 * this.version = data[start + 0] & 0xf; this.versionExt = (data[start +
		 * 0] >> 4) & 0xf; this.commandType = data[start + 1]; this.commandID =
		 * (data[start + 2] & 0xff) << 8 | (data[start + 3] & 0xff);
		 * this.pkgLength = (data[start + 4] & 0xff << 24) | (data[start + 5] &
		 * 0xff << 16) | (data[start + 6] & 0xff << 8) | (data[start + 7] &
		 * 0xff);
		 * 
		 * this.pkgTypeID = (data[start + 8] << 24) | (data[start + 9] << 16) |
		 * (data[start + 10] << 8) | (data[start + 11]);
		 * 
		 * this.relationPkgTypeID = ((data[start + 12] << 24) & 0xff) |
		 * ((data[start + 13] << 16) & 0xff) | ((data[start + 14] << 8) & 0xff)
		 * | ((data[start + 15]) & 0xff);
		 * 
		 * this.pkgCheckSum = ((data[start + 16] & 0xff) << 8) | (data[start +
		 * 17] & 0xff);
		 * 
		 * this.signBit = (data[start + 18] & 0xff << 8) | data[19];
		 * this.responseCode = data[20]; this.saveCode = data[start + 21];
		 * this.optionLength = (data[start + 22] << 8) | data[start + 23];
		 */
		if (data.length < start + 24) {
			return 0;
		}

		// 再判断字节流是否足够一个完整包的大小
		if (data.length < start + this.pkgLength || this.pkgLength < 0) {
			return 0;
		}

		this.pkgFull = true;

		// 判断校验和是否准确
		if (!checkSum(data, this.pkgCheckSum, start, this.pkgLength)) {
			// 数据包完整但校验错误
			this.pkgCheckOk = false;
			return this.pkgLength;
		}
		// 校验正确
		this.pkgCheckOk = true;

		// 读取option内容
		int nOpLen = this.optionLength * 4;
		if (nOpLen > 0) {
			this.option = new byte[nOpLen];
			System.arraycopy(data, start + 24, this.option, 0, nOpLen);
		}
		// 读取包体内容
		this.parsePackConcent(data, start + 24 + nOpLen, this.pkgLength - 24
				- nOpLen);
		return this.pkgLength;
	}

	/**
	 * 把数据包内容转换成从字节流 返回值：字节流
	 * 
	 * @author crj
	 * 
	 */
	public byte[] toBuffer() {

		// 包头基本长度
		int baseLength = 24;

		// option长度
		baseLength += this.optionLength;

		for (Field field : this.fileds) {

			baseLength += field.length;
			baseLength += 7; // ID, TYPE LENGTH;

		}

		byte[] buffer = new byte[baseLength];

		buffer[0] = (byte) (((this.versionExt << 4) | this.version) & 0xff);
		buffer[1] = (byte) this.commandType;
		buffer[2] = (byte) ((this.commandID >> 8) & 0xff);
		buffer[3] = (byte) ((this.commandID) & 0xff);

		buffer[4] = (byte) ((baseLength >> 24) & 0xff);
		buffer[5] = (byte) ((baseLength >> 16) & 0xff);
		buffer[6] = (byte) ((baseLength >> 8) & 0xff);
		buffer[7] = (byte) (baseLength & 0xff);

		buffer[8] = (byte) ((this.pkgTypeID >> 24) & 0xff);
		buffer[9] = (byte) ((this.pkgTypeID >> 16) & 0xff);
		buffer[10] = (byte) ((this.pkgTypeID >> 8) & 0xff);
		buffer[11] = (byte) (this.pkgTypeID & 0xff);

		buffer[12] = (byte) ((this.relationPkgTypeID >> 24) & 0xff);
		buffer[13] = (byte) ((this.relationPkgTypeID >> 16) & 0xff);
		buffer[14] = (byte) ((this.relationPkgTypeID >> 8) & 0xff);
		buffer[15] = (byte) ((this.relationPkgTypeID) & 0xff);

		buffer[16] = 0;
		buffer[17] = 0;

		buffer[18] = (byte) ((this.signBit >> 8) & 0xff);
		buffer[19] = (byte) ((this.signBit) & 0xff);

		buffer[20] = (byte) (this.responseCode & 0xff);
		buffer[21] = (byte) (this.saveCode & 0xff);

		buffer[22] = (byte) ((optionLength >> 8) & 0xff);
		buffer[23] = (byte) ((optionLength) & 0xff);

		// 写入option内容
		int nOpLen = this.optionLength * 4;
		if (nOpLen > 0 && this.option.length == nOpLen) {
			System.arraycopy(this.option, 0, buffer, 24, nOpLen);
		}

		// 写入包体内容
		int pos = 24 + nOpLen;
		for (Field field : fileds) {

			buffer[pos] = (byte) ((field.id >> 8) & 0xff);
			buffer[pos + 1] = (byte) ((field.id) & 0xff);
			buffer[pos + 2] = (byte) (field.type);

			buffer[pos + 3] = (byte) ((field.length >> 24) & 0xff);
			buffer[pos + 4] = (byte) ((field.length >> 16) & 0xff);
			buffer[pos + 5] = (byte) ((field.length >> 8) & 0xff);
			buffer[pos + 6] = (byte) ((field.length) & 0xff);

			if (field.length > 0 && field.value.length == field.length) {
				System.arraycopy(field.value, 0, buffer, pos + 7, field.length);
			}

			pos += field.length + 7;
		}

		int checkSum = getCheckSum(buffer, 0, buffer.length);

		buffer[16] = (byte) ((checkSum >> 8) & 0xff);
		buffer[17] = (byte) (checkSum & 0xff);

		this.pkgLength = buffer.length;

		return buffer;
	}

	/**
	 * 包体内容的解析
	 * 
	 * @param data
	 * @param start
	 */
	protected void parsePackConcent(byte[] data, int start, int nPkgBodyLen) {
		int post = 0;
		this.fileds = new ArrayList<CMSPacket.Field>();
		while (post < nPkgBodyLen) {

			Field filed = new Field();

			int id_ = ((data[start + post] & 0xff) << 8)
					| (data[start + post + 1] & 0xff);
			filed.id = id_;
			filed.type = data[start + post + 2];
			filed.length = ((data[start + post + 3] & 0xff) << 24)
					| ((data[start + post + 4] & 0xff) << 18)
					| ((data[start + post + 5] & 0xff) << 8)
					| ((data[start + post + 6] & 0xff));

			filed.value = new byte[filed.length];
			System.arraycopy(data, start + post + 7, filed.value, 0,
					filed.length);

			post += 7 + filed.length;

			fileds.add(filed);
		}
	}

	public void makeDatePack(CMSPacket.CMSDate dt) {
		if (dt == null) {
			dt = new CMSPacket.CMSDate();
		}
		this.fileds = new ArrayList<CMSPacket.Field>();
		/*
		 * int year = Calendar.getInstance().get(Calendar.YEAR); int month =
		 * Calendar.getInstance().get(Calendar.MONTH) + 1; int day =
		 * Calendar.getInstance().get(Calendar.DAY_OF_MONTH); int hour =
		 * Calendar.getInstance().get(Calendar.HOUR);
		 * if(Calendar.getInstance().get(Calendar.AM_PM)==Calendar.PM){
		 * hour+=12; } int minute = Calendar.getInstance().get(Calendar.MINUTE);
		 * int second = Calendar.getInstance().get(Calendar.SECOND);
		 */
		this.commandID = CMSDefine.CMD_ID_SYSTEM_TIME;
		this.commandType = CMSDefine.COMMAND_TYPE_NOTICE;

		Field field = new Field(7);
		field.id = CMSDefine.FIELD_ID_SYSTEM_TIME;
		field.type = CMSDefine.DATA_TYPE_BINARY;
		dt.setToBuffer(field.value);
		/*
		 * field.value[0] = (byte) ((year >> 8) & 0xff); field.value[1] = (byte)
		 * ((year) & 0xff);
		 * 
		 * field.value[2] = (byte) ((month) & 0xff); field.value[3] = (byte)
		 * ((day) & 0xff); field.value[4] = (byte) ((hour) & 0xff);
		 * field.value[5] = (byte) ((minute) & 0xff); field.value[6] = (byte)
		 * ((second) & 0xff);
		 */
		this.fileds.add(field);
	}

	public void makeSeqPack(int seqenceNum, int operateCode,
			int packetStreamType, int[] arrCmdID) {

		this.fileds = new ArrayList<CMSPacket.Field>();

		this.commandID = CMSDefine.CMD_ID_SEQUENCE;
		this.commandType = CMSDefine.COMMAND_TYPE_NOTICE;

		Field field = new Field(4);
		field.id = CMSDefine.FIELD_ID_SEQUENCE_NUM;
		field.type = CMSDefine.DATA_TYPE_UINT;
		CMSPacket.intToBuffer(field.value, 0, seqenceNum);
		this.fileds.add(field);

		field = new Field(1);
		field.id = CMSDefine.FIELD_ID_OPERATE_CODE;
		field.type = CMSDefine.DATA_TYPE_BYTE;
		CMSPacket.byteToBuffer(field.value, 0, operateCode);
		this.fileds.add(field);

		field = new Field(2);
		field.id = CMSDefine.FIELD_ID_DATA_STREAM_TYPE;
		field.type = CMSDefine.DATA_TYPE_USHORT;
		CMSPacket.shortToBuffer(field.value, 0, packetStreamType);
		this.fileds.add(field);

		if (arrCmdID != null) {
			int nLen = arrCmdID.length;
			if (nLen > 0) {
				field = new Field(nLen * 2);
				field.id = CMSDefine.FIELD_ID_DATA_STREAM;
				field.type = CMSDefine.DATA_TYPE_BINARY;
				for (int i = 0; i < nLen; i++) {
					CMSPacket.shortToBuffer(field.value, i * 2, arrCmdID[i]);
				}
				this.fileds.add(field);
			}
		}
	}

	/**
	 * 将给定字节流的指定位置开始的4个连续的字节转成int值
	 * 
	 * @param data
	 * @param start
	 */
	public static int bufferToInt(byte[] data, int start) {
		int nValue = 0;
		if (data.length >= start + 4) {
			nValue = ((data[start + 0] & 0xff) << 24)
					| ((data[start + 1] & 0xff) << 16)
					| ((data[start + 2] & 0xff) << 8)
					| ((data[start + 3] & 0xff));
		}
		return nValue;
	}

	public static void intToBuffer(byte[] data, int start, int nValue) {

		if (data.length >= start + 4) {
			data[start + 0] = (byte) ((nValue >> 24) & 0xff);
			data[start + 1] = (byte) ((nValue >> 16) & 0xff);
			data[start + 2] = (byte) ((nValue >> 8) & 0xff);
			data[start + 3] = (byte) ((nValue) & 0xff);
		}
	}

	/**
	 * 将给定字节流的指定位置开始的2个连续的字节转成short值
	 * 
	 * @param data
	 * @param start
	 */
	public static Short bufferToShort(byte[] data, int start) {
		Short nValue = 0;
		if (data.length >= start + 2) {
			nValue = (short) (((data[start + 0] & 0xff) << 8) | (data[start + 1] & 0xff));
		}

		return nValue;
	}

	/**
	 * 将给定字节流的指定位置开始的4个连续的字节转成Float值
	 * 
	 * @param data
	 * @param start
	 */
	public static Float bufferToFloat(byte[] data, int start) {
		int nValue = 0;
		if (data.length >= start + 4) {
			nValue = ((data[start + 3] << 24) & 0xff)
					| ((data[start + 2] << 16) & 0xff)
					| ((data[start + 1] << 8) & 0xff)
					| ((data[start + 0]) & 0xff);
		}

		return Float.intBitsToFloat(nValue);
	}

	public static void shortToBuffer(byte[] data, int start, int nValue) {
		if (data.length >= start + 2) {
			data[start + 0] = (byte) ((nValue >> 8) & 0xff);
			data[start + 1] = (byte) ((nValue) & 0xff);
		}
	}

	/**
	 * 将给定字节流的指定位置开始的1个连续的字节转成byte值
	 * 
	 * @param data
	 * @param start
	 */
	public static int bufferToByte(byte[] data, int start) {
		int nValue = 0;
		if (data.length >= start + 1) {
			nValue = data[start + 0];
		}

		return nValue;
	}

	/**
	 * 将给定字节流的指定位置开始的1个连续的字节转成bool值
	 * 
	 * @param data
	 * @param start
	 */
	public static int bufferTobool(byte[] data, int start) {
		int nValue = 0;
		if (data.length >= start + 1) {
			nValue = data[start + 0];
		}
		return nValue;
	}
	
	/**
	 * 将byte转换成int类型
	 * 
	 * @param b
	 * @return
	 */
	public static float buffertoInt(byte[] b, int index) {
		// 4 bytes
		int accum = 0;
		for (int shiftBy = 0; shiftBy < 4; shiftBy++) {
			accum |= (b[shiftBy + index] & 0xff) << shiftBy * 8;
		}
		return accum;
	}
	
	/**
	 * 将byte转换成char类型
	 * 
	 * @param b
	 * @return
	 */
	public static char buffertoChar(byte[] b, int index) {
		// 4 bytes
		char accum = 0;
		for (int shiftBy = 0; shiftBy < 4; shiftBy++) {
			accum |= (b[shiftBy + index] & 0xff) << shiftBy * 8;
		}
		return accum;
	}
	

	/**
	 * 将byte转换成Double类型
	 * 
	 * @param b
	 * @return
	 */
	public static Double buffertoDouble(byte[] b, int index) {
		// 4 bytes
		long accum = 0;
		for (int shiftBy = 0; shiftBy < 4; shiftBy++) {
			accum |= (b[shiftBy + index] & 0xff) << shiftBy * 8;
		}
		return Double.longBitsToDouble(accum);
	}
	
	public static void byteToBuffer(byte[] data, int start, int nValue) {
		if (data.length >= start + 1) {
			data[start + 0] = (byte) ((nValue) & 0xff);
		}
	}

	/**
	 * 将给定字节流的指定位置开始的7个连续的字节转成Date值
	 * 
	 * @param data
	 * @param start
	 */
	public static Date bufferToDate(byte[] data, int start) {
		Date dt = new Date();

		return dt;
	}
}
