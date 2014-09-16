package com.signove.health.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.util.Log;

import com.yx.model.DefiniteData;
import com.yx.model.FinishPackageData;
import com.yx.model.FinishPackageData.litPackDataType;
import com.yx.model.PackageDatatype;

/**
 * 欧姆龙蓝牙数据解析
 * 
 * @author ChenRui
 */
public class OrmonXmlParser
{
	
	private DefiniteData		mCurrentData	= null;							// 当前解析的数据
	private List<DefiniteData>	mListData		= new ArrayList<DefiniteData>();	// 当前解析的数据列表
	private FinishPackageData	mPackageData	= null;							// 解析后的数据
																					
	public FinishPackageData parser(String xml)
	{
		return parser(xml, null);
	}
	
	public FinishPackageData parser(InputStream inputStream)
	{
		return parser(null, inputStream);
	}
	
	public FinishPackageData parser(String xml, InputStream inputStream)
	{
		mPackageData = new FinishPackageData();
		
		try
		{
			// 开始解析
			XmlPullParser p = XmlPullParserFactory.newInstance().newPullParser();
			if (inputStream != null)
			{
				p.setInput(inputStream, "UTF-8");
			}
			else
			{
				StringReader reader = new StringReader(xml);
				p.setInput(reader);
			}
			
			int type = p.getEventType();
			String name = ""; // 标签名称
			String attrVal = "";// 属性值
			while (type != XmlPullParser.END_DOCUMENT)
			{
				switch (type)
				{
					case XmlPullParser.START_TAG:
						name = p.getName();
						attrVal = p.getAttributeValue(null, "name");
						// <meta name="metric-id">18949</meta>
						if ("meta".equals(name) && "metric-id".equals(attrVal))
						{
							mCurrentData = new DefiniteData(); // 实例化当前解析数据对象
						}
						break;
					case XmlPullParser.TEXT:
						String text = p.getText().trim();
						if (mCurrentData == null || text.length() <= 0)
						{
							break;
						}
						else if ("18949".equals(text))
						{
							mCurrentData.setItemTypeName("收缩压"); // 高压
							mCurrentData.setDataItemUnit("mmHg");
						}
						else if ("18950".equals(text))
						{
							mCurrentData.setItemTypeName("舒张压"); // 低压
							mCurrentData.setDataItemUnit("mmHg");
						}
						else if ("18474".equals(text))
						{
							mCurrentData.setItemTypeName("脉搏");
							mCurrentData.setDataItemUnit("博/分");
						}
						else if ("value".equals(name))
						{
							int val = (int) Float.parseFloat(text);
							mCurrentData.setDataItemValue(String.valueOf(val)); // 设置体征值
							mListData.add(mCurrentData);
							mCurrentData = null;
						}
						else if ("18951".equals(text) || "61458".equals(text))
						{
							mCurrentData = null;
						}
						
						break;
					default:
						break;
				}
				
				type = p.next();
			}
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			setPagckType();
		}
		return mPackageData;
	}
	
	/**
	 * 设置设备信息
	 */
	private void setPagckType()
	{
		litPackDataType listType = mPackageData.new litPackDataType();
		PackageDatatype dataType = new PackageDatatype();
		dataType.setDataTypeName("NIBP");
		dataType.setIsEcgdata(0);
		dataType.setReceiveDate(getCurrentDateTime());
		dataType.setDeviceName("ORMON");
		listType.setPackDataType(dataType); // 设置包信息
		listType.setLitDefiniteData(mListData);
		ArrayList<litPackDataType> litPackDataType = new ArrayList<FinishPackageData.litPackDataType>();
		litPackDataType.add(listType);
		mPackageData.setLitPackDataType(litPackDataType);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}
	
	void show(Object msg)
	{
		msg = msg == null ? "" : msg;
		Log.i("xml", msg.toString());
	}
	
}
