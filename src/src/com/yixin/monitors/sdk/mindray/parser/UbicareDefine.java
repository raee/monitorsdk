package com.yixin.monitors.sdk.mindray.parser;


/**
 * Ubicare鍗忚鍩烘湰瀹氫箟
 * @author wgj
 *
 */

public class UbicareDefine {

	//鍛戒护瀛桰D
	public final static int CMD_ID_DEVICE_BASE_INFO 	= 1;	//璁惧鍩烘湰淇℃伅
	public final static int CMD_ID_SYSTEM_TIME			= 6;	//绯荤粺鏃堕棿
	public final static int CMD_ID_WAVE_DATA			= 157;	//娉㈠舰鏁版嵁
	public final static int CMD_ID_WAVE_NAME			= 159;	//娉㈠舰鍚嶅瓧
	public final static int CMD_ID_MEASURE_PARA_LIST	= 204;	//娴嬮噺鍙傛暟鍒楄〃
	public final static int CMD_ID_ECG_GENERAL_SET		= 251;	//ECG妯″潡鍩烘湰璁剧疆
	public final static int CMD_ID_ECG_GAIN				= 253;	//ECG娉㈠舰澧炵泭
	public final static int CMD_ID_ECG_FILTER_TYPE		= 256;	//ECG娉㈠舰婊ゆ尝鏂瑰紡
	public final static int CMD_ID_NIBP_DATA			= 503;	//NIBP娴嬮噺鏁版嵁
	public final static int CMD_ID_USER_MARK_INFO		= 2000;	//鐢ㄦ埛鏍囪瘑淇℃伅
	public final static int CMD_ID_SEQUENCE 			= 2001;	//搴忓垪鍖�
	
	
	
	//瀛楁ID
	public final static int FIELD_ID_DEVICE_MODEL		= 1;	//璁惧鍨嬪彿
	
	public final static int FIELD_ID_SYSTEM_TIME		= 81;	//绯荤粺鏃堕棿
	
	public final static int FIELD_ID_PARA_DATA_SIZE		= 585;	//鍙傛暟鏁版嵁缁撴瀯澶у皬
	public final static int FIELD_ID_PARA_DATA			= 586;	//鍙傛暟鏁版嵁
	
	public final static int FIELD_ID_ECG_LEAD_TYPE		= 689;	//ECG瀵艰仈绫诲瀷
	
	public final static int FIELD_ID_ECG_GAIN_SIZE		= 725;	//ECG娉㈠舰澧炵泭缁撴瀯澶у皬
	public final static int FIELD_ID_ECG_GAIN			= 726;	//ECG娉㈠舰澧炵泭
	public final static int FIELD_ID_ECG_GAIN_FACTOR	= 727;	//ECG娉㈠舰澧炵泭绯绘暟
	
	public final static int FIELD_ID_ECG_FILTER_SIZE	= 745;	//ECG娉㈠舰婊ゆ尝鏂瑰紡缁撴瀯澶у皬
	public final static int FIELD_ID_ECG_FILTER_TYPE	= 746;	//ECG娉㈠舰婊ゆ尝鏂瑰紡
	
	public final static int FIELD_ID_WAVE_DATA			= 1335;	//娉㈠舰鏁版嵁
	
	public final static int FIELD_ID_WAVE_NAME_SIZE		= 1375;	//娉㈠舰鍚嶇О缁撴瀯澶у皬
	public final static int FIELD_ID_WAVE_NAME			= 1376;	//娉㈠舰鍚嶇О
	
	public final static int FIELD_ID_NIBP_DATA_SIZE		= 1120;	//NIBP娴嬮噺鏁版嵁缁撴瀯澶у皬
	public final static int FIELD_ID_NIBP_DATA			= 1121;	//NIBP娴嬮噺鏁版嵁
	
	public final static int FIELD_ID_USER_MARK			= 7000;	//鐢ㄦ埛鏍囪瘑
	public final static int FIELD_ID_MEASURE_TIME		= 7001;	//娴嬮噺鏃堕棿
	public final static int FIELD_ID_MEASURE_DATA_MARK	= 7002;	//娴嬮噺鏁版嵁鏍囪瘑
	
	public final static int FIELD_ID_SEQUENCE_NUM		= 7010;	//搴忓垪鍙�
	public final static int FIELD_ID_OPERATE_CODE		= 7011;	//鎿嶄綔鐮�
	public final static int FIELD_ID_DATA_STREAM_TYPE	= 7012;	//鏁版嵁鍖呮祦绫诲瀷
	public final static int FIELD_ID_DATA_STREAM		= 7013;	//鏁版嵁鍖呮祦缁勬垚
	
	//参数类型定义
	public final static int PARAMETER_TYPE_UNKNOWN    	= 0;//未知
	public final static int PARAMETER_TYPE_ECG_HR 		= 1;//脉搏
	public final static int PARAMETER_TYPE_SPO2_SPO2 	= 18;//血氧饱和度
	public final static int PARAMETER_TYPE_NIBP_NIBP_S 	= 20;//血压
	public final static int PARAMETER_TYPE_NIBP_NIBP_M 	= 21;
	public final static int PARAMETER_TYPE_NIBP_NIBP_D 	= 22;
	public final static int PARAMETER_TYPE_TEMP_T1 		= 35; //体温
}
