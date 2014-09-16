package com.yixin.device.parser.mindray;

import com.yx.model.FinishPackageData;

public interface PackageParseIntf {
public FinishPackageData parsePacket(CMSPacketStream stream);
}
