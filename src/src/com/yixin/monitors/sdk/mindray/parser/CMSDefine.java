package com.yixin.monitors.sdk.mindray.parser;

public class CMSDefine
{
	public final static int	VERSION						= 1;
	public final static int	VERSION_EXT					= 0;
	
	public final static int	CMD_ID_DEVICE_BASE_INFO		= 1;
	public final static int	CMD_ID_SYSTEM_TIME			= 6;
	public final static int	CMD_ID_WAVE_DATA			= 157;
	public final static int	CMD_ID_WAVE_NAME			= 159;
	public final static int	CMD_ID_MEASURE_PARA_LIST	= 204;
	public final static int	CMD_ID_ECG_GENERAL_SET		= 251;
	public final static int	CMD_ID_ECG_GAIN				= 253;
	public final static int	CMD_ID_ECG_FILTER_TYPE		= 256;
	public final static int	CMD_ID_NIBP_DATA			= 503;
	public final static int	CMD_ID_USER_MARK_INFO		= 2000;
	public final static int	CMD_ID_SEQUENCE				= 2001;
	public final static int	OPERATION_CODE_NOTICE		= 1;
	public final static int	OPERATION_CODE_SREQ			= 2;
	public final static int	OPERATION_CODE_CONFIRM		= 3;
	public final static int	STREAM_TYPE_DEVICE			= 1;
	public final static int	STREAM_TYPE_SYSTEMDATE		= 2;
	public final static int	STREAM_TYPE_RESULT			= 3;
	
	public final static int	COMMAND_TYPE_RESPONSE		= 2;
	public final static int	COMMAND_TYPE_NOTICE			= 5;
	
	public final static int	DATA_TYPE_BYTE				= 1;
	public final static int	DATA_TYPE_CHAR				= 2;
	public final static int	DATA_TYPE_BOOL				= 3;
	public final static int	DATA_TYPE_USHORT			= 4;
	public final static int	DATA_TYPE_SHORT				= 5;
	public final static int	DATA_TYPE_UINT				= 6;
	public final static int	DATA_TYPE_INT				= 7;
	public final static int	DATA_TYPE_CHAR_ARRAY		= 8;
	public final static int	DATA_TYPE_BINARY			= 9;
	public final static int	DATA_TYPE_FLOAT				= 10;
	public final static int	DATA_TYPE_DOUBLE			= 11;
	public final static int	FIELD_ID_DEVICE_MODEL		= 1;
	
	public final static int	FIELD_ID_SYSTEM_TIME		= 81;
	
	public final static int	FIELD_ID_PARA_DATA_SIZE		= 585;
	public final static int	FIELD_ID_PARA_DATA			= 586;
	
	public final static int	FIELD_ID_ECG_LEAD_TYPE		= 689;
	
	public final static int	FIELD_ID_ECG_GAIN_SIZE		= 725;
	public final static int	FIELD_ID_ECG_GAIN			= 726;
	public final static int	FIELD_ID_ECG_GAIN_FACTOR	= 727;
	
	public final static int	FIELD_ID_ECG_FILTER_SIZE	= 745;
	public final static int	FIELD_ID_ECG_FILTER_TYPE	= 746;
	
	public final static int	FIELD_ID_WAVE_DATA			= 1355;
	
	public final static int	FIELD_ID_WAVE_NAME_SIZE		= 1375;
	public final static int	FIELD_ID_WAVE_NAME			= 1376;
	
	public final static int	FIELD_ID_NIBP_DATA_SIZE		= 1120;
	public final static int	FIELD_ID_NIBP_DATA			= 1121;
	
	public final static int	FIELD_ID_USER_MARK			= 7000;
	public final static int	FIELD_ID_MEASURE_TIME		= 7001;
	public final static int	FIELD_ID_MEASURE_DATA_MARK	= 7002;
	
	public final static int	FIELD_ID_SEQUENCE_NUM		= 7010;
	public final static int	FIELD_ID_OPERATE_CODE		= 7011;
	public final static int	FIELD_ID_DATA_STREAM_TYPE	= 7012;
	public final static int	FIELD_ID_DATA_STREAM		= 7013;
	//参数类型定义
	public final static int	PARAMETER_ECG_HR			= 1;
	public final static int	PARAMETER_SPO2				= 18;
	public final static int	PARAMETER_NIBP_S			= 20;
	public final static int	PARAMETER_NIBP_M			= 21;
	public final static int	PARAMETER_NIBP_D			= 22;
	public final static int	PARAMETER_TEMP_T1			= 35;
	
	//导联类型
	public final static int	ECG_LEADTYPE_3				= 1;
	public final static int	ECG_LEADTYPE_5				= 2;
	public final static int	ECG_LEADTYPE_12				= 3;
	
}
