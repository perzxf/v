package com.ls.service.spider;

import java.util.List;

public interface SpiderMonitorService {
    void spiderMonitor(Long siteTypeId, String siteUrl, List<String> keyList);
}
