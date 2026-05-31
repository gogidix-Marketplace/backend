package com.gogidix.analytics.data.domain.port.out;

import com.gogidix.analytics.data.domain.model.DataExport;

import java.util.Map;

/**
 * Output port: Gateway for storing and retrieving exported data.
 */
public interface DataStorageGateway {

    String storeExportFile(DataExport export, byte[] data);

    byte[] retrieveExportFile(String filePath);

    String getDownloadUrl(String filePath, int expiresInSeconds);

    void deleteExportFile(String filePath);

    Map<String, Object> getExportMetadata(String filePath);
}
