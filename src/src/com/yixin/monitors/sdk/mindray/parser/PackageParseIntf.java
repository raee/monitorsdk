package com.yixin.monitors.sdk.mindray.parser;

import com.yx.model.FinishPackageData;

public interface PackageParseIntf {
public FinishPackageData parsePacket(CMSPacketStream stream);
}
