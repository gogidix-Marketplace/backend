package com.gogidix.ecommerce.communication.domain.port.in;

import com.gogidix.ecommerce.communication.application.dto.*;
import java.util.List;

public interface CommunicationUseCase {
    CommunicationResponse create(CreateCommunicationRequest request);
    CommunicationResponse update(String id, UpdateCommunicationRequest request);
    void delete(String id);
    CommunicationResponse getById(String id);
    List<CommunicationResponse> getAll();
}
