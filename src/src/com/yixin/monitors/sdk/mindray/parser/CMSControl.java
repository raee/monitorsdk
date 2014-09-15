package com.yixin.monitors.sdk.mindray.parser;

import com.yixin.monitors.sdk.api.IBluetoothSendable;
import com.yixin.monitors.sdk.api.IDataParser;
import com.yixin.monitors.sdk.mindray.ModelConveter;
import com.yixin.monitors.sdk.model.PackageModel;
import com.yx.model.FinishPackageData;

public class CMSControl implements IDataParser {

	// public interface CMSControlHandler {
	// public void onDeviceBaseInfo(CMSPacketStream packetStream,
	// int nDeviceType);
	//
	// public void onDeviceResult(CMSPacketStream packetStream);
	// }

	// private CMSControlHandler m_handler;

	// 当前发送的序列号
	protected int m_nSeqenceNumber;

	// 之前未处理的数据流
	protected byte[] dataLast;

	// 数据包流包含的数据包集合
	CMSPacketStream packetStreamLast;

	private IBluetoothSendable mBluetoothSendable;

	public void setBluetoothSendInterface(IBluetoothSendable sendable) {
		this.mBluetoothSendable = sendable;
	}

	public CMSControl(IBluetoothSendable sendable) {
		packetStreamLast = null;
		// m_handler = null;
		m_nSeqenceNumber = 0;
		setBluetoothSendInterface(sendable);
	}

	// public void SetCMSControlHandler(CMSControlHandler h) {
	// m_handler = h;
	// }

	public int getSeqenceNumber() {
		return m_nSeqenceNumber++;
	}

	/**
	 * 从字节流中读取数据包内容 dataBuffer：字节流的指针 nBufferLen：字节流的长度 返回值：已经读取的长度
	 * 
	 * @author crj
	 * 
	 */
	@Override
	public PackageModel parse(byte[] dataIn) {
		FinishPackageData result = null;
		// 是否为全新的数据包
		int start = 0;
		boolean bNewData = true;
		// 首先将新来的数据流和之前未处理的数据流合并
		byte[] data;
		int nAllLen;
		if (this.dataLast != null) {
			if (this.dataLast.length > 0) {
				bNewData = false;
			}
			nAllLen = this.dataLast.length + dataIn.length - start;
			data = new byte[nAllLen];
			System.arraycopy(this.dataLast, 0, data, 0, this.dataLast.length);
			System.arraycopy(dataIn, start, data, this.dataLast.length,
					dataIn.length - start);
			start = 0;
		} else {
			data = dataIn;
			nAllLen = data.length;
		}

		// 从数据流中读取数据包

		int nLenRead = 0; // 已经成功去读的长度

		while (true) {
			CMSPacket packet = new CMSPacket();
			int nLen = packet.fromBuffer(data, start + nLenRead);
			if (nLen > 0) {
				// 数据包读取成功
				if (packet.isPkgCheckOk()) {
					// 判断数据包是否为序列包
					if (this.isSeqencePacket(packet)) {
						// 如果是序列包
						// 判断之前是否存在处理中的序列包,如果存在,说明原来的序列包已经传输完毕，则处理到序列包命令
						if (packetStreamLast != null) {
							// 处理该数据包流
							result = processPacketStream(packetStreamLast);
							// 清空数据包流
							packetStreamLast = null;
						}
						if (packetStreamLast == null) {
							// 创建已经新的序列包处理流程
							packetStreamLast = new CMSPacketStream(packet);
						}
					} else {
						// 如果不是序列包，则看看是否存在处理中的数据包流，存在则加入，否则放弃该数据包
						if (packetStreamLast != null) {
							packetStreamLast.addPacket(packet);
							if (packetStreamLast.isPacketStreamFull()) {
								// 处理该数据包流
								result = processPacketStream(packetStreamLast);
								// 清空数据包流
								packetStreamLast = null;
							}
						}
					}
				}
				// 移动指针，准备下一个读取
				nLenRead += nLen;
				// 判断数据包流是否结束**待续**
			} else {
				// 数据包读取失败
				if (bNewData) {
					if (packet.getVersion() != 1) {
						break;
					}
				}
				if (packet.isCMSPacketFull()) {
					nLenRead += packet.getPkgLength();
				}
				// 保存未处理的数据流
				int nLastLen = data.length - start - nLenRead;
				if (nLastLen > 0) {
					this.dataLast = new byte[nLastLen];
					System.arraycopy(data, start + nLenRead, this.dataLast, 0,
							nLastLen);
				} else {
					this.dataLast = null;
				}
				break;
			}
		}

		return ModelConveter.conver(result);
	}

	/**
	 * 判断给定的数据包是否为序列包
	 * 
	 * @author crj
	 * 
	 */
	public boolean isSeqencePacket(CMSPacket packet) {
		if (packet.commandID == CMSDefine.CMD_ID_SEQUENCE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 核心，处理数据包流
	 * 
	 * @author crj
	 * 
	 */
	public FinishPackageData processPacketStream(CMSPacketStream packetStream) {
		FinishPackageData result = null;
		// 如果数据包流传输不正确，则不处理该数据包流
		if (!packetStream.isPacketStreamCorrect()) {
			return result;
		}

		// 是否为通知类型
		if (packetStream.operateCode == CMSDefine.OPERATION_CODE_NOTICE) {
			// 是否为设备信息
			if (packetStream.packetStreamType == CMSDefine.STREAM_TYPE_DEVICE) {
				// 当前设备类型
//				int nDeviceType = 0;
//				if (packetStream.packets.size() > 0) {
//					CMSPacket packet = packetStream.packets.get(0);
//					if (packet.getCommandID() == CMSDefine.CMD_ID_DEVICE_BASE_INFO) {
//						if (packet.fileds.size() > 0) {
//							CMSPacket.Field field = packet.fileds.get(0);
//							nDeviceType = CMSPacket
//									.bufferToByte(field.value, 0);
//						}
//					}
//				}

				CMSPacketStream pksReturn = getSysdatePackets(null);
				if (pksReturn != null) {
					byte[] buf = pksReturn.toBuffer();
					mBluetoothSendable.send(buf); // 发送数据
				}

				// if (m_handler != null) {
				// m_handler.onDeviceBaseInfo(packetStream, nDeviceType);
				// }
			}

		}
		// 是否为请求类型
		else if (packetStream.operateCode == CMSDefine.OPERATION_CODE_SREQ) {
			// 是否为设备信息
			if (packetStream.packetStreamType == CMSDefine.STREAM_TYPE_RESULT) {

				CMSPacketStream pksReturn = getConfirmPackets(packetStream);
				if (pksReturn != null) {
					byte[] buf = pksReturn.toBuffer();
					mBluetoothSendable.send(buf); // 发送数据
				}
				PackageParseIntf parsePacket = new ParseCMSPacket();
				result = parsePacket.parsePacket(packetStream);
				// if (m_handler != null) {
				// m_handler.onDeviceResult(packetStream);
				// }
			}
		}
		// 是否为确认类型
		else if (packetStream.operateCode == CMSDefine.OPERATION_CODE_CONFIRM) {
		}

		return result;
	}

	public CMSPacketStream getSysdatePackets(CMSPacket.CMSDate dt) {
		CMSPacketStream pksSysdate = new CMSPacketStream();
		pksSysdate.setSeqenceNum(this.getSeqenceNumber());
		pksSysdate.setOperateCode(CMSDefine.OPERATION_CODE_NOTICE);
		pksSysdate.setPacketStreamType(CMSDefine.STREAM_TYPE_SYSTEMDATE);
		CMSPacket packet = new CMSPacket();
		packet.makeDatePack(null);
		pksSysdate.addPacket(packet);
		return pksSysdate;
	}

	public CMSPacketStream getConfirmPackets(CMSPacketStream pks) {
		CMSPacketStream pksConfirm = new CMSPacketStream();
		pksConfirm.setSeqenceNum(pks.getSeqenceNum());
		pksConfirm.setOperateCode(CMSDefine.OPERATION_CODE_CONFIRM);
		pksConfirm.setPacketStreamType(pks.getPacketStreamType());
		return pksConfirm;
	}
}
